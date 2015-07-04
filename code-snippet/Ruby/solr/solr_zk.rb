#!/usr/bin/env ruby
# -*- coding : UTF-8 -*-

require 'rubygems'
require 'zookeeper'
require 'json'
require 'rest-client'
require 'optparse'
require 'uri'
require "curb"
require 'time_difference'
#require 'pp'

$VERSION=0.01

ZK_DEV='sjkf01:2181,sjkf02:2181,sjkf03:2181'
ZK_STAGING='rtzk01:2181,rtzk02:2181,rtzk03:2181'
#ZK_PROD='srzk34032:2181,srzk34050:2181,srzk34066:2181,srzk35032:2181,srzk35073:2181,srzk35080:2181,srzk35081:2181,srzk35088:2181,srzk35092:2181'
ZK_PROD='srzk01:2181,srzk02:2181,srzk03:2181'
#ZK_BACKUP='bkzk34058:2181,bkzk34047:2181,bkzk34063:2181'
ZK_BACKUP='bkzk34058:2181,bkzk34047:2181,bkzk34063:2181,dsadtt03:2181,dsadtt04:2181'
TEST_SOLR='solrzktst01:2181,solrzktst02:2181,solrzktst03:2181'

class CommandOption
  @@commands=[ :lives, :status, :aliases, :collection, :get ,:ls, :put, :push_key, :service, :query, :fetch , :shard]
  @@clusters={ :dev => ZK_DEV, :staging => ZK_STAGING, :prod => ZK_PROD, :backup => ZK_BACKUP , :testing => TEST_SOLR}
  @@options = {}

  def self.parse()

    usage = "Usage: #{__FILE__} -c cluster -m command  args"

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

      opts.on('-v','--version' , "show version") do 
        puts "#{__FILE__} - #{$VERSION}"
        exit
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

  def stat(collection)
    self.get "#{@solr_root}/collections/#{collection}/state.json" if self.collections.include? collection
  end

  def ls(path)
    @zookeeper.get_children(:path => path)[:children]
  end

  def get(path)
    @zookeeper.get(:path => path)[:data]
  end

  def put(dest,datafile)
    data = File.read(datafile)
    @zookeeper.set(:path => dest, :data => data)
  end

  def root
    @solr_root
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
  attr_accessor :path, :query ,:startDate,:endDate,:dataSrcs,:fq,:startRow,:rows,:sortBy,:sortDir,:fields, :facetType
  attr_accessor :facetField,:facetQuery,:start,:endd,:gap

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
    diff = TimeDifference.between(Time.at(startDate.to_i / 1000),Time.now)
    months = diff.in_months.to_i
    prefix=nil
    case months
    when 0
      if Time.at(startDate.to_i / 1000).month == Time.now.month  then
        prefix="current_month_alias"
      else
        prefix="last_30_days_alias"
      end
    when 1..3
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



  def solr_param

    qry=query
    sort_by = nil
    params = {} 
    if path == '/api/rest/v1/posts' then
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
                 :start => startRow==nil ? 0 : startRow,
                 :rows => rows == nil  ? 0 : rows,
                 :fl => [ :id , :createDate, :score ], 
                 :sort => sort_by,
                 :fq => fq == nil ? [] : fq.split('|') 
      }
      params[:fq] <<  "createDate:[ #{startDate.to_i / 1000} TO #{endDate.to_i / 1000} ]"

      date_filter="createDate:[ #{startDate.to_i / 1000} TO #{endDate.to_i / 1000} ]"
      params[:fq] << date_filter
    elsif path == '/api/rest/v1/post/perday' then

      params = { :q => qry,
                 :start => startRow,
                 :rows => 0,
                 :facet => 'on',
                 'facet.range' => 'createDate',
                 'f.createDate.facet.range.start' => "#{startDate.to_i / 1000}",
                 'f.createDate.facet.range.end'=> "#{endDate.to_i / 1000}",
                 'f.createDate.facet.range.gap' => '86400',
                 :fq => "createDate:[ #{startDate.to_i / 1000} TO #{endDate.to_i / 1000} ]"
      }
    elsif path == '/api/rest/v1/posts/facet' then
      params = { :q => qry,
                 :rows => 0,
                 :facet => 'on',
                 'facet.field' => facetField,
                 :fq => "createDate:[ #{startDate.to_i / 1000} TO #{endDate.to_i / 1000} ]"

      }
      if facetType != 'field' then
        params["facet.#{facetType}"] = facetField
        params["f.#{facetField}.#{facetType}.start"] = start
        params["f.#{facetField}.#{facetType}.end"] = endd
        params["f.#{facetField}.#{facetType}.gap"] = gap
      end
    end

    params
  end

  def self.from_query(path,query)
    ret = self.new
    ret.path = path
    CGI.parse(query).each do |k,v|
      if k == 'end' then
        ret.endd = v[0]
      else
        ret.send("#{k}=",v[0])
      end
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
    host_name, port_number, base_url = node.split(/[:_]/)
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
      return  "#{url}/#{cols[0]}/select?#{params}&collection=#{collection}&wt=json"
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
    host="#{self.url}/admin/collections"
    cmd['wt']='json' if not cmd.has_key?('wt')
    cmd['indent']='true' if not cmd.has_key?('indent')
    begin
      puts "#{host}/#{cmd.to_param}"
      response = RestClient.get(host, {:params => cmd })
      puts cmd
      puts response.code
      puts response
    rescue => e
      puts e
    end
  end

  def delete(collection,id)
    host="#{self.url}/#{collection}/update/json"
    header = {:content_type =>'json'}
    delete_json = "{'delete':{'id':#{id}}}"
    RestClient.post host,delete_json,:content_type => 'json'
  end

  def self.from_lives(lives)
    node = lives[rand(lives.size)]
    host,port,base = node.split(/[_:]/)
    SolrAdmin.new(host,port,base)
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
     if args.size == 0 then
       puts solr.collections
     else
       case args[0]
       when 'create'
         self.collection_create args[1..-1]
       when 'reload'
         self.collection_reload args[1..-1]
       when 'delete'
         self.collection_delete args[1..-1]
       else
         puts "wrong command please check command"
       end
     end
     
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

  def put(args)
    if args.size < 2 then
      puts "need dest path"
    else
      solr.put args[0], args[1]
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
    api=SysomosAPI.from_query(url.path,url.query)
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

  def shard(args)
    begin 
      status=solr.status
      status.keys.sort.each do |key|
        shards=status[key]['shards']
        sort_shard=shards.keys.sort_by { |s| [s[5..-1].to_i] }
        sort_shard.each do |k|
          puts "#{key} #{k} #{shards[k]['state']}"
        end
        puts '---'
      end

    rescue ArgumentError => e
      puts e
    end 

  end
  def collection_create(args)
    admin = SolrAdmin.from_lives(solr.lives)
    if args.size > 0 then
      cmd = { :action => 'CREATE', :name => 'default', 'numShards' => 1, 'replicationFactor' => 1, 'maxShardsPerNode' => 1}
      opts = JSON.parse(args[1]) if args.size > 1
      opts.each do |k,v|
        cmd[k]=v
      end
      cmd[:name]=args[0]
      p args[0]
      admin.collection(cmd)
    end
  end

  def collection_reload(args)
    admin = SolrAdmin.from_lives(solr.lives)

    if args.size == 0 then
      print "are you sure to reload all collections? [y/N]"
      case $stdin.gets.strip 
      when 'Y','y'
        solr.collections.each do |c|
          puts c
          admin.collection( :action => 'RELOAD', :name => c)
        end
      end
    else
      args.each do |c|
        puts c
        admin.collection( :action => 'RELOAD', :name => c)
      end
    end
  end

  def collection_delete(args)
    admin = SolrAdmin.from_lives(solr.lives)
    if args.size == 0 then
      print "are you sure to delete all collections? [y/N]"
      case $stdin.gets.strip 
      when 'Y','y'
        solr.collections.each do |c|
          puts c
          admin.collection( :action => 'DELETE', :name => c)
        end
      end
    else
      args.each do |c|
        puts c
        admin.collection( :action => 'DELETE', :name => c)
      end
    end
  end

  def delete(args)
    if args.size > 1 then
      collection = args[0] 
      if solr.collections.include? collection
         stat=JSON.parse(solr.stat(collection))
         node=stat[collection]['shards']['shard1']['replicas']['core_node1']['node_name']
         host,port,base = node.split(/[_:]/)
         admin = SolrAdmin.new(host,port,base)
         admin.delete collection, args[1]
      end
    end
  end
end

def main
  CommandOption.parse
  cmd = ExecuteCommand.new(CommandOption.zkhost)
  cmd.send(CommandOption.command,ARGV) if cmd.methods.include? CommandOption.command
end

main


# puts "#{nodes[0]}"
# puts collection
# actions= { :action => 'CREATEALIAS', :name => 'last_30_days_alias_NW', :collections =>  'post_NW_201411' }
# collection_api(nodes[0].sub!('_','/'),actions)



