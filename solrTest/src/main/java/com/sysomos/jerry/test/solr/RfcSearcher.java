package com.sysomos.jerry.test.solr;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;

import java.net.MalformedURLException;
import java.util.Iterator;

/**
 * Created by kkim on 10/31/14.
 */
public class RfcSearcher {
    static final String SOLR_ENSEMBLE="192.168.2.213:2181,192.168.2.214:2181,192.168.2.215:2181/solr";

    CloudSolrServer server;

    public RfcSearcher() {
        try {

            server = new CloudSolrServer(SOLR_ENSEMBLE);
            server.setDefaultCollection("rfc");
            server.setZkClientTimeout(10000);
            server.setZkConnectTimeout(10000);



        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void search() {

        try {
            SolrQuery query = new SolrQuery();

            query.set("q","http");

            QueryResponse response = server.query(query);

            SolrDocumentList doclist=response.getResults();

            Iterator<SolrDocument> iterator = doclist.iterator();

            while (iterator.hasNext()) {
                SolrDocument doc = iterator.next();
                System.out.println(doc.get("id"));
            }
        } catch (SolrServerException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        RfcSearcher searcher = new RfcSearcher();
        searcher.search();

    }
}
