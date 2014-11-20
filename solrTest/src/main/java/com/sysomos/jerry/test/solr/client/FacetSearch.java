package com.sysomos.jerry.test.solr.client;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrResponse;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.RangeFacet;
import org.apache.solr.schema.DateField;
import org.apache.solr.update.SolrCmdDistributor;

import java.net.MalformedURLException;
import java.util.Date;

import static org.apache.solr.client.solrj.response.RangeFacet.*;

/**
 * Created by kkim on 11/18/14.
 */
public class FacetSearch {

    public static final String zk_hosts = "sjkf01:2181,sjkf02:2181,sjkf03:2181/solr";
    public static final String alias = "last_30_days_alias_NW";

    public static void main(String[] args) {

        String qry = "food";
        Date sDate = new Date(1414800000000L);
        Date eDate = new Date(1417392000000L);


        CloudSolrServer solr = null;
        try {
            solr = new CloudSolrServer(zk_hosts);
            solr.setDefaultCollection(alias);


            SolrQuery query = new SolrQuery();
            query.setQuery(qry);
            query.setFacet(true);
            query.setParam("rows", "0");
            query.setParam("facet.range", "createDate");
            query.setParam("f.createDate.facet.range.start", String.valueOf(sDate.getTime() / 1000));
            query.setParam("f.createDate.facet.range.end", String.valueOf(eDate.getTime() / 1000));
            query.setParam("f.createDate.facet.range.gap", "86400");

            System.out.println(query.toString());
            QueryResponse response = solr.query(query);

            System.out.println("OK");
            System.out.println(response.getResults().getNumFound());
            for (RangeFacet range : response.getFacetRanges()) {
                System.out.println(range.getName());
                for( Object count : range.getCounts()) {
                    RangeFacet.Count c = (RangeFacet.Count)count;
                    System.out.printf("%s:%d\n",c.getValue(),c.getCount());
                }
            }


            solr.shutdown();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (SolrServerException e) {
            e.printStackTrace();
        }


    }
}
