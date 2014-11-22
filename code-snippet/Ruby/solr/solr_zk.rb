#!/usr/bin/env ruby
#

require 'rubygems'
require 'zookeeper'
require 'json'
require 'rest-client'
require 'optparse'


ZK_DEV='sjkf01:2181,sjkf02:2181,sjkf03:2181'
ZK_STAGING='rtzk01:2181,rtzk02:2181,rtzk03:2181'
ZK_PROD='srzk34032:2181,srzk34050:2181,srzk34066:2181,srzk35032:2181,srzk35073:2181,srzk35080:2181,srzk35081:2181,srzk35088:2181,srzk35092:2181'


class CommandOption
    @@clusters={ :dev => ZK_DEV, :staging => ZK_STAGING, :prod => ZK_PROD }
    @@options = {} 
    
    def self.parse()

        usage = "Usage: solr_zk.rb -c cluster -m command "

        optparse=OptionParser.new do |opts|
            opts.banner = usage
            opts.on('-c','--cluster cluster',[:dev,:staging,:prod],'cluster in (dev staging prod)') do |cluster|
                @@options[:cluster] = cluster
            end

            opts.on('-m','--command command',[:lives,:status,:collection,:get],
                    'command in (lives,status,collection,get') do |command|
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

    def status
        JSON.parse(@zookeeper.get(:path => "#{@solr_root}/clusterstate.json")[:data])
    end
end

def collection_api(node,command)
    host="http://#{node}/admin/collections"
    begin
        response = RestClient.get(host, {:params => command })
        puts response.code
        puts response
    rescue => e
        puts e.response
    end
end


#CommandOption.parse
#puts CommandOption.cluster


solr=SolrCloud.new(CommandOption.zkhost,'/solr')

case CommandOption.command
when :lives
    puts solr.lives
when :status
    puts solr.status 
end

# puts "#{nodes[0]}"
# puts collection
#actions= { :action => 'CREATEALIAS', :name => 'last_30_days_alias_NW', :collections =>  'post_NW_201411' }
#collection_api(nodes[0].sub!('_','/'),actions)



