package org.blueskywalker.solr.facet;

/**
 * Created by kkim on 12/4/15.
 */




import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author kkim
 */
public class SolrFacet {

    String limit;
    String method;
    String mincount;
    String missing;
    String offset;
    String prefix;
    String sort;
    String threads;

    public SolrFacet() {
        this.prefix = null;
        this.sort = null;
        this.limit = null;
        this.offset = null;
        this.mincount = null;
        this.missing = null;
        this.method = null;
        this.threads = null;
    }

    public SolrFacet(FacetBuilder b) throws Exception {
        List<Field> fields = new ArrayList<Field>();
        Class tmpClass = getClass();
        while (tmpClass != null) {
            fields.addAll(Arrays.asList(tmpClass.getDeclaredFields()));
            tmpClass = tmpClass.getSuperclass();
        }

        for (Field f : fields) {
            Method getter = b.getClass().getMethod("get" + StringUtils.capitalize(f.getName()));
            Method setter = getClass().getMethod("set" + StringUtils.capitalize(f.getName()), f.getType());
            setter.invoke(this, getter.invoke(b));
        }
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public String getMincount() {
        return mincount;
    }

    public void setMincount(String mincount) {
        this.mincount = mincount;
    }

    public String getMissing() {
        return missing;
    }

    public void setMissing(String missing) {
        this.missing = missing;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getThreads() {
        return threads;
    }

    public void setThreads(String threads) {
        this.threads = threads;
    }

    /**
     *
     * @return SolrQuery
     */
    public SolrQuery createSolrQuery() throws Exception {
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setFacet(true);
        solrQuery.setRows(0);

        for (Field f : SolrFacet.class.getDeclaredFields()) {
            Method getter = getClass().getMethod("get" + StringUtils.capitalize(f.getName()));
            Object object = getter.invoke(this);
            if (object != null) {
                solrQuery.setParam(String.format("facet.%s", f.getName()), object.toString());
            }
        }
        return solrQuery;
    }

    public static class FacetBuilder {

        String prefix;
        String sort;
        String limit;
        String offset;
        String mincount;
        String missing;
        String method;
        String threads;

        public FacetBuilder() {
            this.prefix = null;
            this.sort = null;
            this.limit = null;
            this.offset = null;
            this.mincount = null;
            this.missing = null;
            this.method = null;
            this.threads = null;
        }

        public String getPrefix() {
            return prefix;
        }

        public FacetBuilder setPrefix(String prefix) {
            this.prefix = prefix;
            return this;
        }

        public String getSort() {
            return sort;
        }

        public FacetBuilder setSort(String sort) {
            this.sort = sort;
            return this;
        }

        public String getLimit() {
            return limit;
        }

        public FacetBuilder setLimit(String limit) {
            this.limit = limit;
            return this;
        }

        public String getOffset() {
            return offset;
        }

        public FacetBuilder setOffset(String offset) {
            this.offset = offset;
            return this;
        }

        public String getMincount() {
            return mincount;
        }

        public FacetBuilder setMincount(String mincount) {
            this.mincount = mincount;
            return this;
        }

        public String getMissing() {
            return missing;
        }

        public FacetBuilder setMissing(String missing) {
            this.missing = missing;
            return this;
        }

        public String getMethod() {
            return method;
        }

        public FacetBuilder setMethod(String method) {
            this.method = method;
            return this;
        }

        public String getThreads() {
            return threads;
        }

        public FacetBuilder setThreads(String threads) {
            this.threads = threads;
            return this;
        }

        public SolrFacet build() throws Exception {
            return new SolrFacet(this);
        }
    }
}
