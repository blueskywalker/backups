package com.sysomos.jerry.test.solr;

import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by kkim on 10/30/14.
 */
public class RfcIndexer {

    static final String SOLR_ENSEMBLE="192.168.2.213:2181,192.168.2.214:2181,192.168.2.215:2181/solr";

    CloudSolrServer server;

    public RfcIndexer() {
        try {

            server = new CloudSolrServer(SOLR_ENSEMBLE);
            server.setDefaultCollection("rfc");
            server.setZkClientTimeout(10000);
            server.setZkConnectTimeout(10000);



        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    public void index(File file) {
        try {
            String text = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
            SolrInputDocument doc = new SolrInputDocument();
            doc.addField("id", file.getName());
            doc.addField("text",text);

            UpdateResponse response=server.add(doc);
            System.out.println(response.getStatus());



        } catch (IOException e) {
            e.printStackTrace();
        }
          catch (SolrServerException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        server.commit();
        server.shutdown();
        super.finalize();
    }

    public static void main(String[] args) {



        String rfcPath = "/Users/kkim/workspace/rfc";

        File dir = new File(rfcPath);
        FileFilter fileFilter = new WildcardFileFilter("rfc*.txt");

        RfcIndexer indexer = new RfcIndexer();

        for (File file : dir.listFiles(fileFilter)) {
            System.out.println(file.getName());
            indexer.index(file);
        }

    }
}
