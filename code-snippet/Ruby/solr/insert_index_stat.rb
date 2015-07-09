#! /usr/bin/env ruby 
# -*- encoding : utf-8 -*-

require 'mysql2'
require 'json'

class CollectionDB

  def initialize
    @client = Mysql2::Client.new(:host => "gridmgmt", :username => "root", :password => "mysql", :database => "collections")
    @tables = getTables
  end

  def getTables
    results=@client.query("show tables;")
    table=[]
    results.each do |row|
      table <<  row.values[0]
    end
    table
  end

  def tableExists(name)
    @tables.include?(name)
  end

  def createTable(name)
    sql_create= %{
      create table IF NOT EXISTS #{name} (
        id int not null auto_increment,
        epoch int not null,
        numFound bigint not null,
        qtime int not null,
        primary key(id)
      );
    }
    sql_index = %{
      create index #{name}_index ON #{name} (epoch);
    }
    @client.query(sql_create)
    @client.query(sql_index)
    puts "create table #{name}"
  end

  def add(table,epoch,qtime,numFound)
    sql = %{
      insert into #{table} (epoch,numFound,qtime) values ( #{epoch}, #{numFound}, #{qtime})
    }
    @client.query(sql)
  end

  def setDB(db)
  	@client.select_db(db)
  end

  def query(sql)
	@client.query(sql)
  end

  def close
	@client.close
  end
end



records=[]
while STDIN.gets 
  record=$_.split('|').map { |s| s.strip}
  records << { 'table' => record[1], 'ts' => record[0], 'json' => record[2]}
end

db=CollectionDB.new

total_docs=0

records.each do |r|
  tn=r['table']
  if not db.tableExists(tn) then
    db.createTable(tn)
  end
  response=JSON.parse(r['json'])
  qtime=response['responseHeader']['QTime']
  numFound=response['response']['numFound']
  total_docs += numFound
  db.add(tn,r['ts'],qtime,numFound)
end

timestamp=Time.now.to_i
db.setDB('gridmon')
db.query("insert into total (epoch,total) values ( #{timestamp}, #{total_docs});")
db.close
