#!/usr/bin/env ruby
# -*- coding : UTF-8 -*-

require 'rubygems'
require 'zookeeper'
require 'json'
require 'rest-client'
require 'optparse'
require 'uri'
require "curb"
#require 'pp'


ZK_DEV='sjkf01:2181,sjkf02:2181,sjkf03:2181'
ZK_STAGING='rtzk01:2181,rtzk02:2181,rtzk03:2181'
ZK_PROD='srzk34032:2181,srzk34050:2181,srzk34066:2181,srzk35032:2181,srzk35073:2181,srzk35080:2181,srzk35081:2181,srzk35088:2181,srzk35092:2181'

class CommandOption
    @@commands=[ :lives, :status, :aliases, :collection, :get ,:ls, :push_key, :service, :query, :fetch ]
    @@clusters={ :dev => ZK_DEV, :staging => ZK_STAGING, :prod => ZK_PROD }
    @@options = {} 
    
    def self.parse()

        usage = "Usage: solr_zk.rb -c cluster -m command  args"

        optparse=OptionParser.new do |opts|
            opts.banner = usage
            opts.on('-c','--cluster cluster',@@clusters.keys,
                    "cluster in #{@@clusters.keys.map &:to_s}") do |cluster|
                @@options[:cluster] = cluster
            end

            opts.on('-m','--command command',@@commands,
                    "command in #{@@commands.map &:to_s}" ) do |command|
                @@options[:command] = command
            end

        end

        begin
            optparse.parse!
        rescue OptionParser::ParseError => e
            puts e
        end

        if not @@options.has_key? :cluster or not @@options.has_key? :command then
            puts optparse 
            exit
        end
    end

    def self.cluster
        return @@options[:cluster] 
    end

    def self.zkhost
        return @@clusters[@@options[:cluster]]
    end

    def self.command
        return @@options[:command] 
    end
end

class SolrCloud
    def initialize(ensemble,solr)
        @zookeeper=Zookeeper.new(ensemble)
        @solr_root=solr
    end

    def lives
        @zookeeper.get_children(:path => "#{@solr_root}/live_nodes")[:children]
    end

    def collections
        @zookeeper.get_children(:path => "#{@solr_root}/collections")[:children]
    end

    def aliases
        JSON.parse(@zookeeper.get(:path => "#{@solr_root}/aliases.json")[:data])["collection"]
    end
    
    def status
        JSON.parse(@zookeeper.get(:path => "#{@solr_root}/clusterstate.json")[:data])
    end

    def ls(path)
        @zookeeper.get_children(:path => path)[:children]
    end

    def get(path)
        @zookeeper.get(:path => path)[:data]
    end
end


class SolrQuery
    attr_accessor :q, :start, :rows, :sort, :fl 

    def initialize
    end
end

class SysomosDataSource
    @@kind= { 
        :facebook => "FB", 
        :twitter => "TT", 
        :tumbler => "TB", 
        :news => "NW", 
        :blog => "BL", 
        :forum => "FR" 
    }
end

#
# Query Parameters
#
# query
# startDate
# endDate
# dataSrcs
# fq
# startRow
# rows
# sortBy
# sortDir
# fields
#
class SysomosAPI
    attr_accessor :query,:startDate,:endDate,:dataSrcs,:fq,:startRow,:rows,:sortBy,:sortDir,:fields

    def get_data_source        
        if self.dataSrcs == nil or self.dataSrcs.strip == "" then
            SysomosAPI.kind.keys
        else
            dataSrcs.split('|')
        end
    end

    def month_from(ts) 
        Time.at(ts).gmtime.strftime("%Y%m")
    end

    def aliases
        data = get_data_source

        # is same month
        if month_from(startDate.to_i / 1000) ==
            month_from(endDate.to_i / 1000) then
            return data.map do |item|
                #"post_#{item}_#{month_from(startDate.to_i/1000)}"
                "current_month_alias_#{item}"
            end
        else
            months = Time.now.gmtime.month - Time.at(startDate.to_i / 1000).gmtime.month
            prefix=nil
            case months
            when 1
                prefix="last_30_days_alias"
            when 2..3
                prefix="last_90_days_alias"
            when 4..6
                prefix="last_180_days_alias"
            when 7..12
                prefix="last_12_months_alias"
            else
                prefix="all_alias"
            end

            return data.map do |item|
                "#{prefix}_#{item}"
            end
        end
    end



    def solr_param

        qry=query
        sort_by = nil
        
        case sortBy
        when "lucence"
            sort_by=""
        when "random"
            sort_by="random_%d" % Time.now
        when "randominfluence"
            qry = "(#{q} AND NOT influenceScore:0"
        when "recenttop"
            qry = "{!boost b=recip(ms(NOW,manufacturedate_dt),3.16e-11,1,1)}#{q}"
        when "datainfluence1"
            qry = "{!dateinfluence1}#{q}"
        when "datainfluence2"
            qry = "{!dateinfluence2}#{q}"
        else
            sort_by = "createDate desc"    
        end            

        params = { :q => qry,
                :start => startRow,
                :rows => rows,
                :fl => [ :id , :createDate, :score ], 
                :fq => fq == nil ? [] : fq.split('|'), 
                :sort => sort_by
        }

        date_filter="createDate:[ #{startDate.to_i / 1000} TO #{endDate.to_i / 1000} ]"

        params[:fq] << date_filter

        params
    end

    def self.from_query(query)
        ret = self.new
        CGI.parse(query).each do |k,v|
            ret.send("#{k}=",v[0])
        end
        ret
    end

end

class Hash
    def to_param
        ret=[]
        self.each do |k,v|
            if v.kind_of? Array then
                v.each do |value|
                    ret << "#{k}=#{value}"
                end
            else
                ret << "#{k}=#{v}"
            end
        end
        ret.join('&')
    end
end

class SolrNode
    attr_accessor :host,:port, :base, :collection

    def initialize(host,port=8983,base='solr')
        @host=host
        @port=port
        @base=base
    end

    def url
        "http://#{@host}:#{@port}/#{@base}"
    end 

    def self.from_zk(node)
        host_name, tmp =node.split(':')
        port_number,base_url = tmp.split('_')
        SolrNode.new(host_name,port_number,base_url)
    end

    def push_my_key
        `cat "$HOME/.ssh/id_rsa.pub" | ssh root@#{@host} "cat >> /root/.ssh/authorized_keys"`
    end

    def service(cmd) 
        %x(ssh root@#{@host} "service solr-server #{cmd}" )
    end

    def query(params)

        if collection == nil or collection.empty?
            puts "need a collection"
            return
        end

        cols = collection.split(',')
        base_url = "#{url}/"
        if cols.size > 1 then
            return  "#{url}/#{cols[-1]}/select?#{params}&collection=#{collection}&wt=json"
        else
            return  "#{url}/#{collection}/select?#{params}&wt=json"
        end
        
    end

    def to_s
        url
    end    
end

class SolrAdmin < SolrNode
    def collection(cmd) 
        host="http://#{node}:#{port}/admin/collections"
        begin
            response = RestClient.get(host, {:params => cmd })
            puts response.code
            puts response
        rescue => e
            puts e.response
        end
    end
end

def mprocess(nodes)
    if nodes.size == 0 then
        puts "there is no nodes"
        return
    end

    nodes.each do |node|
        index = node.index(':')
        host =  index > 0 ? node[0,index] : node 
        puts "process #{host}"
        Process.fork do 
            File.open("/tmp/#{host}.output.txt",'w')  do |f|
                ret=yield node
                f.write( "#{host} - #{ret}" )
            end
            sleep 1
            exit
        end
    end

    puts "waiting for all finishing"
    Process.waitall

    nodes.each do |node|
        index = node.index(':')
        host =  index > 0 ? node[0,index] : node 
        File.open("/tmp/#{host}.output.txt",'r') do |f|
            puts f.readlines
        end
    end
    sleep 3
end

class ExecuteCommand
    @@LIVE_NODE_STORE ="/tmp/solr_#{CommandOption.cluster}_lives.txt"

    attr_reader :solr

    def initialize(zkhost,base='/solr')
        @solr=SolrCloud.new(zkhost,base)
    end

    def lives(args)
        File.open(@@LIVE_NODE_STORE,'w') do |f|
            solr.lives.each do |node|
                puts node
                f.puts node
            end
        end
    end

    def status(args)
        puts solr.status.to_json
    end

    def collection(args)
        puts solr.collections
    end

    def aliases(args)
        solr.aliases.each do |k,v|
            puts "#{k} : #{v}"
        end
    end

    def ls(args)
        if args.size == 0 then
            puts "need apth"
        else
           puts solr.ls args[0]
        end
    end

    def get(args)
        if args.size == 0 then
            puts "need apth"
        else
           puts solr.get args[0]
        end
    end

    def service(args)
        if args.size == 0 then
            puts "need (status,start,stop,restart)"
        else
            if args[0] == "start" then
                mprocess(File.readlines(LIVE_NODE_STORE)) do |node|
                    SolrNode.from_zk(node).service args[0]
                end
            else
                mprocess(solr.lives) do |node| 
                    SolrNode.from_zk(node).service args[0]
                end
            end
        end
    end

    def push_key(args)
        solr.lives.each do |node|
            puts SolrNode.from_zk(node).push_my_key
        end
    end

    def get_querys(args)
        if args.size == 0 then
            raise ArgumentError, 'An argument is required'
        end

        url=URI(args[0])
        nodes=solr.lives
        random=Random.rand(nodes.size)
        api=SysomosAPI.from_query(url.query)
        params = api.solr_param.to_param            
        node = SolrNode.from_zk(nodes[random])

        alias2collection = solr.aliases

        ret = []

        api.aliases.each do |item|
            node.collection = alias2collection[item]
            if node.collection == nil then
                puts "warn:there is collection for #{item}"
                node.collection = item
            end
            ret << node.query(params)
        end

        ret
    end

    def query(args)
        begin 
            get_querys(args).each do |url|
                puts URI.escape(url)
            end
        rescue ArgumentError => e
            puts e
        end 
    end 

    def fetch(args)
        begin 
            get_querys(args).each do |url|
                #puts url
                http=Curl.get(URI.escape(url))
                puts http.body_str
            end
        rescue ArgumentError => e
            puts e
        end 
    end
end

def main
    CommandOption.parse

    #puts "[#{CommandOption.cluster}]"

    cmd = ExecuteCommand.new(CommandOption.zkhost)
    cmd.send(CommandOption.command,ARGV) if cmd.methods.include? CommandOption.command

end

main


#
#solr=SolrCloud.new(ZK_DEV,'/solr')
#solr.lives.each do |node|
#    puts SolrNode.from_zk(node)
#end

#[ 'sjdn01:8983_solr', 'sjdn02:8983_solr', 'sjdn03:8983_solr', 'sjdn04:8983_solr', 'sjdn05:8983_solr', 'sjdn06:8983_solr' ].each do |node|
#    SolrNode.from_zk(node).service 'start'
#end

# puts "#{nodes[0]}"
# puts collection
#actions= { :action => 'CREATEALIAS', :name => 'last_30_days_alias_NW', :collections =>  'post_NW_201411' }
#collection_api(nodes[0].sub!('_','/'),actions)



