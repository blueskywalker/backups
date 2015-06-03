package org.blueskywalker.solr.facet.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kkim on 6/3/15.
 */
public class SolrFacetQuery extends SolrFacet {

    List<String> query = new ArrayList<String>();

    public SolrFacetQuery() {
    }

    private SolrFacetQuery(QueryBuilder b) {
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

    public static class QueryBuilder extends FacetBuilder {
        List<String> queries = new ArrayList<String>();

        public QueryBuilder() {
        }

        public QueryBuilder setQueries(List<String> queries) {
            this.queries = queries;
            return this;
        }

        public List<String> getQueries() {
            return queries;
        }


        @Override
        public QueryBuilder setPrefix(String prefix) {
            this.prefix = prefix;
            return this;
        }

        @Override
        public QueryBuilder setSort(String sort) {
            this.sort = sort;
            return this;
        }

        @Override
        public QueryBuilder setLimit(String limit) {
            this.limit = limit;
            return this;
        }

        @Override
        public QueryBuilder setOffset(String offset) {
            this.offset = offset;
            return this;
        }

        @Override
        public QueryBuilder setMincount(String mincount) {
            this.mincount = mincount;
            return this;
        }

        @Override
        public QueryBuilder setMissing(String missing) {
            this.missing = missing;
            return this;
        }

        @Override
        public QueryBuilder setMethod(String method) {
            this.method = method;
            return this;
        }

        @Override
        public QueryBuilder setThreads(String threads) {
            this.threads = threads;
            return this;
        }

        public SolrFacetQuery build() {
            return new SolrFacetQuery(this);
        }

    }
}
