#!/usr/bin/env ruby
# -*- coding : UTF-8 -*-
#

require 'rubygems'
require 'optparse'
require 'set'
require 'curl'
require 'time'
require 'json'
require 'uri'

END_POINTER={ :prod => '10.200.24.9', :staging => 'rtms01:8080'}

class GridRestAPI
  attr_accessor :endpoint, :version, :api, :query, :startDate, :endDate, :dataSrc, :fq, :startRow, :rows, :sortBy, :sortDir, :fields
  attr_accessor :facetType, :facetQuery, :facetField, :start, :end, :gap, :sort, :limit
  @@params_variables = [:query,
                        :startDate,
                        :endDate,
                        :fq,
                        :startRow,
                        :rows,
                        :sortBy,
                        :fields,
                        :facetType,
                        :facetQuery,
                        :facetField,
                        :start,
                        :end,
                        :gap,
                        :sort,
                        :limit ]

  def initialize
    @endpoint=END_POINTER[:staging]
    @version='v1'
    @api = :post
  end

  def api_path
    prefix="http://#{endpoint}/api/rest/#{version}"
    case api
    when :post
      return "#{prefix}/posts?"
    when :persona
      return "#{prefix}/actorpersona/search?"
    when :postfacet
      return "#{prefix}/posts/facet?"
    when :personafacet
    end
    "#{prefix}/posts?"
  end

  def nil_or_empty?(variable)
    variable.nil? || variable.empty?
  end

  def params
    ret=@@params_variables.select { |v| not nil_or_empty?(send(v)) }.map{ |p| "#{p}=#{send(p)}"}.join('&')
    case api
    when :post,:postfacet
      ret << "&dataSrcs=#{dataSrc}"
    when :persona, :personafacet
      ret << "&dataSrc=#{dataSrc}"
    when :persona
    end
    ret
  end

  def url
    api_path() + params()
  end
end

class QueryTester
  @@options={:endpoint => [:prod , :staging], :datasource => [:TT,:TB,:FB,:NW], :type => [:post,:persona]}
  @@opts= [:api, :type, :source, :start, :end, :file]
  @@setting={}

  def self.parse()
    usage = "Usage: #{__FILE__} -a [staging|prod] -t api_type -d dataSource -s start[yyyy-mm-dd] -e end[yyyy-mm-dd] -f queryFile"

    optparse=OptionParser.new do |opts|
      opts.banner = usage

      opts.on('-a','--api [staging|prod]',@@options[:endpoint],
              "endpoint in #{@@options[:endpoint].map &:to_s}") do |endpoint|
        @@setting[:api]= endpoint
      end
      opts.on('-d','--data-source source',@@options[:datasource],
             "data-source in #{@@options[:datasource].map &:to_s}") do |source|
        @@setting[:source]=source
      end
      opts.on('-t','--api-type type',@@options[:type],
             "type in #{@@options[:type].map &:to_s}") do |type|
        @@setting[:type]=type
      end
      opts.on('-s','--start-date yyyy-mm-dd',"start date") do |d_str|
        d_str+= " 00:00:00 UTC"
        @@setting[:start]=Time.parse(d_str).to_i
      end
      opts.on('-e','--end-date yyyy-mm-dd',"end date") do |d_str|
        d_str+= " 00:00:00 UTC"
        @@setting[:end]=Time.parse(d_str).to_i
      end
      opts.on('-f','--input file',"file that has queries") do |f|
        @@setting[:file]=f
      end
    end

    begin
      optparse.parse!
    rescue OptionParser::OptionParser => e
      puts e
    end

    if not @@setting.has_key?(:api) or not @@setting.has_key?(:source) then
      puts optparse
      exit
    end

    if @@setting.has_key?(:type) and @@setting["type"] == :post and  (not @@setting.has_key?(:start) or not @@setting.has_key?(:end)) then 
      puts optparse
      exit
    end
  end

  def self.info
    @@setting
  end

  def initialize
    @api = GridRestAPI.new
    @api.api = @@setting[:type] if @@setting.has_key?(:type)
    @api.endpoint=END_POINTER[@@setting[:api]]
    @api.startDate=(@@setting[:start]*1000).to_s if @@setting.has_key?(:start)
    @api.endDate=(@@setting[:end]*1000).to_s if @@setting.has_key?(:end)
    @api.dataSrc=@@setting[:source]
    @api.rows="1"
  end

  def request(query,url)
    puts url
    startTime=Time.now
    r = Curl.post URI.escape(URI.escape(url))
    endTime=Time.now
    response=JSON.parse(r.body_str)
    count = if response['recordFound'].nil? then "0" else response['recordFound'] end
#    puts response
    puts "#{query}\t#{response['statusCode']}\t#{count}\t#{endTime-startTime}"
  end 

  def run
    if @@setting.has_key? :file then
      File.open(@@setting[:file],'r').each_line do |line|
        @api.query=line.strip
        request @api.query,@api.url
      end
    else
      ARGV.each do |query|
        @api.query=query
        request @api.query, @api.url
      end
    end
  end
end

QueryTester.parse

tester = QueryTester.new
tester.run



