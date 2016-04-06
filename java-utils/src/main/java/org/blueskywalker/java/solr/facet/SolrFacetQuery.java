package org.blueskywalker.java.solr.facet;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by kkim on 1/8/16.
 */
public class SolrFacetQuery extends SolrFacet {

    List<String> query = new ArrayList<String>();

    public SolrFacetQuery() {
    }

    private SolrFacetQuery(FacetQueryBuilder b) {
        super();
        setQuery(b.getQueries());
    }


    public List<String> getQuery() {
        return query;
    }

    public void setQuery(List<String> query) {
        this.query = query;
    }

    public void addQuery(String qry) {
        query.add(qry);
    }

    public static class FacetQueryBuilder extends FacetBuilder {
        List<String> queries = new ArrayList<String>();

        public FacetQueryBuilder() {
        }

        public FacetQueryBuilder setQueries(List<String> queries) {
            this.queries = queries;
            return this;
        }

        public List<String> getQueries() {
            return queries;
        }


        @Override
        public FacetQueryBuilder setPrefix(String prefix) {
            this.prefix = prefix;
            return this;
        }

        @Override
        public FacetQueryBuilder setSort(String sort) {
            this.sort = sort;
            return this;
        }

        @Override
        public FacetQueryBuilder setLimit(String limit) {
            this.limit = limit;
            return this;
        }

        @Override
        public FacetQueryBuilder setOffset(String offset) {
            this.offset = offset;
            return this;
        }

        @Override
        public FacetQueryBuilder setMincount(String mincount) {
            this.mincount = mincount;
            return this;
        }

        @Override
        public FacetQueryBuilder setMissing(String missing) {
            this.missing = missing;
            return this;
        }

        @Override
        public FacetQueryBuilder setMethod(String method) {
            this.method = method;
            return this;
        }

        @Override
        public FacetQueryBuilder setThreads(String threads) {
            this.threads = threads;
            return this;
        }

        public SolrFacetQuery build() {
            return new SolrFacetQuery(this);
        }

    }

}


