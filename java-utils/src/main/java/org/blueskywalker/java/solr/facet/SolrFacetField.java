package org.blueskywalker.java.solr.facet;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;

/**
 *
 * @author kkim
 */
public class SolrFacetField extends SolrFacet {

    String field;

    SolrFacetField() {
        super();
        this.field = null;
    }

    public SolrFacetField(String field) {
        super();
        this.field = field;
    }

    protected SolrFacetField(FieldBuilder b) throws Exception {
        super(b);
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    @Override
    public SolrQuery createSolrQuery() throws Exception {
        SolrQuery solrQuery = super.createSolrQuery();
        solrQuery.setParam("facet.field", field);
        return solrQuery;
    }

    public static class FieldBuilder extends FacetBuilder {

        String field;

        public FieldBuilder(String field) {
            super();
            this.field = field;
        }

        public String getField() {
            return field;
        }

        public FieldBuilder setField(String field) {
            this.field = field;
            return this;
        }

        @Override
        public FieldBuilder setPrefix(String prefix) {
            this.prefix = prefix;
            return this;
        }

        @Override
        public FieldBuilder setSort(String sort) {
            this.sort = sort;
            return this;
        }

        @Override
        public FieldBuilder setLimit(String limit) {
            this.limit = limit;
            return this;
        }

        @Override
        public FieldBuilder setOffset(String offset) {
            this.offset = offset;
            return this;
        }

        @Override
        public FieldBuilder setMincount(String mincount) {
            this.mincount = mincount;
            return this;
        }

        @Override
        public FieldBuilder setMissing(String missing) {
            this.missing = missing;
            return this;
        }

        @Override
        public FieldBuilder setMethod(String method) {

            this.method = method;

            return this;
        }

        @Override
        public FieldBuilder setThreads(String threads) {
            this.threads = threads;
            return this;
        }

        @Override
        public SolrFacet build() throws Exception {
            return new SolrFacetField(this);
        }
    }

}
