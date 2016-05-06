package com.sysomos.os.kafka.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kkim on 4/28/16.
 */
public class KafkaDatabase {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaDatabase.class);
    private final KafkaJDBCFactory factory;
    private final Connection connection;

    public KafkaDatabase(KafkaJDBCFactory factory) throws ClassNotFoundException, SQLException {
        this.factory = factory;

        connection = DriverManager.getConnection(factory.getUrl(),"sa","");
    }

    public List<String> getSchemas() throws SQLException {
        Statement stat = connection.createStatement();
        ResultSet rs = stat.executeQuery("show schemas;");
        ArrayList<String> ret = new ArrayList<String>();

        while(rs.next()) {
            ret.add(rs.getString("SCHEMA_NAME"));
        }
        stat.close();
        return ret;
    }

    public void createSchema(String name) throws SQLException {
        Statement stat = connection.createStatement();
        stat.execute(String.format("CREATE SCHEMA IF NOT EXISTS %s;",name.toUpperCase()));
        stat.close();
    }

    public void dropSchema(String name) throws SQLException {
        Statement stat = connection.createStatement();
        stat.execute(String.format("DROP SCHEMA IF EXISTS %s;",name.toUpperCase()));
        stat.close();
    }


    public List<String> getTables(String schema) throws SQLException {
        Statement stat = connection.createStatement();
        ResultSet rs = stat.executeQuery(String.format("show tables from %s",schema.toUpperCase()));
        ArrayList<String> ret = new ArrayList<String>();

        while(rs.next()) {
            ret.add(rs.getString("TABLE_NAME"));
        }
        stat.close();
        return ret;
    }

    public void  createTable(String schema,String table) throws SQLException {
        Statement stat = connection.createStatement();
        stat.execute(String.format("CREATE TABLE IF NOT EXISTS \"%s\".\"%s\";",
                schema.toUpperCase(),table.toUpperCase()));
        stat.close();
    }


    public void close() throws SQLException {
        connection.close();
    }
}
