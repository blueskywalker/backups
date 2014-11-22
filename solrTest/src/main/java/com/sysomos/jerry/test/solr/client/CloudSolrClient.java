package com.sysomos.jerry.test.solr.client;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.util.Iterator;

/**
 * Created by kkim on 11/19/14.
 */
public class CloudSolrClient {

//    static final String SOLR_ENSEMBLE="192.168.2.213:2181,192.168.2.214:2181,192.168.2.215:2181/solr";
//    static final String SOLR_ENSEMBLE="srzk34050:2181,srzk34066:2181,srzk35032:2181,srzk35073:2181,srzk35080:2181,srzk35081:2181,srzk35088:2181,srzk35092:2181,srzk34032:2181/solr";
    static final String SOLR_ENSEMBLE="rtzk02:2181,rtzk03:2181,rtzk01:2181/solr";

//    static final String ALIAS="post_TT_201411";
    static final String ALIAS="last_90_days_alias_TT";
    static final int WAIT_TIME=100000;


    CloudSolrServer server;

    public CloudSolrClient() {
        try {

            server = new CloudSolrServer(SOLR_ENSEMBLE);
            server.setDefaultCollection(ALIAS);
            server.setZkClientTimeout(WAIT_TIME);
            server.setZkConnectTimeout(WAIT_TIME);



        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


    public void search(String qry,String fq) {

        try {
            SolrQuery query = new SolrQuery();

            query.setQuery(qry);
            if(fq != null) {
                query.setFilterQueries(fq);
            }

            QueryResponse response = server.query(query, SolrRequest.METHOD.POST);

            SolrDocumentList doclist=response.getResults();

            //Iterator<SolrDocument> iterator = doclist.iterator();

            System.out.println(doclist.getNumFound());
            System.out.println(doclist.size());

        } catch (SolrServerException e) {
            e.printStackTrace();
        }
    }

    public void search(String qry) {
        search(qry,null);
    }

    public static void main(String[] args) {
        CloudSolrClient client = new CloudSolrClient();

//        String query="surf+OR+surfer+OR+surfing+AND+createDate%3A%5B1416182400+TO+1416268800%5D&start=0&rows=25&fl=*%2Cscore&sort=createDate+desc";
        String query="surf+OR+surfer+OR+surfing";
        try {

            query=URLDecoder.decode(query,"UTF-8");

            System.out.println(query);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        client.search(query,"createDate:[1409529600 TO 1412121600 ]");
    }
}
