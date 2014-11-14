package com.sysomos.jerry.test.solr;

import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Calendar;

/**
 * Created by kkim on 11/4/14.
 */
public class SolrIndexer {

    SolrServer server;

    public SolrIndexer(String host) {
        server = new HttpSolrServer(host);
    }


    public void index(File file,String timestamp) throws IOException, SolrServerException {

        String text = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField("id", file.getName());
        doc.addField("content", text);
        doc.addField("timestamp",timestamp);

        UpdateResponse response = server.add(doc);
        System.out.println(response.getStatus());


    }

    public void close() throws IOException, SolrServerException {
        server.commit();
        server.shutdown();
    }

    @Override
    protected void finalize() throws Throwable {
        server.shutdown();
        super.finalize();
    }

    public static void main(String[] args) {


        String rfcPath = "/Users/kkim/workspace/rfc";

        File dir = new File(rfcPath);
        FileFilter fileFilter = new WildcardFileFilter("rfc*.txt");

        SolrIndexer indexer = new SolrIndexer("http://localhost:8983/solr/collection1");


        DateTime joda = new DateTime(1970,1,1,0,1);
        DateTimeFormatter fmt =DateTimeFormat.forPattern("yyyy-MM-dd");

        try {

            for (File file : dir.listFiles(fileFilter)) {
                System.out.println(file.getName());
                indexer.index(file,fmt.print(joda));
                joda=joda.plusDays(1);
            }

            indexer.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SolrServerException e) {
            e.printStackTrace();
        }

    }
}
