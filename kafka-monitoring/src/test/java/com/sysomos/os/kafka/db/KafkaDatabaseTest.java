package com.sysomos.os.kafka.db;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by kkim on 4/28/16.
 */
public class KafkaDatabaseTest {

    KafkaJDBCFactory factory;
    KafkaDatabase db;

    @Before
    public void setup() throws SQLException, ClassNotFoundException {
        factory = new KafkaJDBCFactory();
        factory.setUrl("jdbc:h2:file:./kafkadb.db");
        factory.setDriverClass("org.h2.Driver");
        db = new KafkaDatabase(factory);
    }

    @Test
    public void testListShema() throws SQLException {
        for (String s : db.getSchemas()) {
            System.out.println(s);
        }
    }

    @Test
    public void testCreateSchema() throws SQLException {

        String name = "cluster";
        db.createSchema(name);
        List<String> schemas = db.getSchemas();

        Assert.assertTrue(schemas.contains(name.toUpperCase()));
    }

    @Test
    public void testDropSchema() throws SQLException {
        String name = "cluster";
        db.dropSchema(name);
        List<String> schemas = db.getSchemas();

        Assert.assertFalse(schemas.contains(name.toUpperCase()));
    }

    @Test
    public void testCreateTable() throws SQLException {
        String schema="cluster";
        String table="cluster";

        db.createSchema(schema);
        db.createTable(schema,table);

        List<String> tables = db.getTables(schema);

        Assert.assertTrue(tables.contains(table.toUpperCase()));
    }
}