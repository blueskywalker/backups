package org.blueskywalker.solr;

import org.apache.solr.core.CoreContainer;
import org.apache.solr.core.SolrResourceLoader;

/**
 * Created by kkim on 8/20/15.
 */
public class SolrCoreReader {

    public SolrCoreReader() {

    }

    public static void main(String[] args) {
        SolrResourceLoader loader = new SolrResourceLoader();
        CoreContainer container = new CoreContainer();
    }
}
