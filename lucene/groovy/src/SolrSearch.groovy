
/**
 * Created by kkim on 1/26/16.
 */

@Grab(group='org.apache.solr', module='solr-solrj', version='4.4.0')
@Grab(group='org.slf4j',module='slf4j-jdk14',version='1.6.5')

import org.apache.solr.client.solrj.SolrQuery
import org.apache.solr.client.solrj.impl.CloudSolrServer
import org.apache.solr.client.solrj.response.QueryResponse

class SolrSearch {

    static main(args) {

        CloudSolrServer server = new CloudSolrServer("stgzj001.grid.stg.yyz.corp.pvt:2181,stgzj002.grid.stg.yyz.corp.pvt:2181,stgzj003.grid.stg.yyz.corp.pvt:2181/solr");
        server.setDefaultCollection("post_TT_201512");

        SolrQuery query = new SolrQuery();
        query.setQuery("fashion");

        QueryResponse response = server.query(query);

        print(response)
    }
}
