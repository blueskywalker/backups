package org.blueskywalker.java.utils.thread;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import org.blueskywalker.java.utils.YamlType;

import java.io.InputStream;

/**
 * Created by kkim on 12/23/15.
 */
public class MongoClientTest {

    public static void main(String[] args) {
        InputStream is = MongoClientTest.class.getClassLoader().getResourceAsStream("mongo.yaml");
        YamlType yaml = new YamlType(is);

        String  connectString = yaml.getYaml("mongodb").getString("connect.uri");
        MongoClientURI uri = new MongoClientURI(connectString);
        MongoClient client = new MongoClient(uri);

        MongoDatabase db = client.getDatabase("profileTest");


    }
}
