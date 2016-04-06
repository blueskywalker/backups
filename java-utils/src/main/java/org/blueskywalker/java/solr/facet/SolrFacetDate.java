package org.blueskywalker.java.solr.facet;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;

/**
 *
 * @author kkim
 */
public class SolrFacetDate extends SolrFacetRange {


    public SolrFacetDate(String date) {
        super(date);
    }

    public SolrFacetDate(DateBuilder b) throws Exception {
        super(b);
    }

    public void setDate(String date) {
        setRange(date);
    }

    public String getDate() {
        return getRange();
    }

    @Override
    public SolrQuery createSolrQuery() throws Exception {
        SolrQuery solrQuery = getSolrQueryFromSolrFacet();
        Field[] fields = SolrFacetRange.class.getDeclaredFields();

        for (Field f : fields) {
            Method getter = getClass().getMethod("get" + StringUtils.capitalize(f.getName()));
            Object object = getter.invoke(this);
            if (object != null) {
                if (f.getName().equalsIgnoreCase("range")) {
                    solrQuery.setParam("facet.date", getDate());
                } else {
                    solrQuery.setParam(String.format("f.%s.facet.date.%s", getDate(), f.getName()), object.toString());
                }
            }
        }
        return solrQuery;
    }

    public static class DateBuilder extends RangeBuilder {

        public DateBuilder(String field) {
            super(field);
        }

        public String getDate() {
            return getRange();
        }

        public DateBuilder setDate(String date) {
            setRange(date);
            return this;
        }


        @Override
        public SolrFacetDate build() throws Exception {
            return new SolrFacetDate(this);
        }


        @Override
        public DateBuilder setStart(String start) {
            super.start = start;
            return this;
        }

        @Override
        public DateBuilder setEnd(String end) {
            super.end = end;
            return this;
        }

        @Override
        public DateBuilder setGap(String gap) {
            super.gap = gap;
            return this;
        }

        @Override
        public DateBuilder setHardend(String hardend) {
            super.hardend = hardend;
            return this;
        }

        /**
         *
         * @param other
         * @return
         */
        @Override
        public DateBuilder setOther(String other) {
            super.other = other;
            return this;
        }

        @Override
        public DateBuilder setInclude(String include) {
            super.include = include;
            return this;
        }

        @Override
        public DateBuilder setPrefix(String prefix) {
            super.prefix = prefix;
            return this;
        }

        @Override
        public DateBuilder setSort(String sort) {
            super.sort = sort;
            return this;
        }

        @Override
        public DateBuilder setLimit(String limit) {
            super.limit = limit;
            return this;
        }

        @Override
        public DateBuilder setOffset(String offset) {
            super.offset = offset;
            return this;
        }

        @Override
        public DateBuilder setMincount(String mincount) {
            super.mincount = mincount;
            return this;
        }

        @Override
        public DateBuilder setMissing(String missing) {
            super.missing = missing;
            return this;
        }

        @Override
        public DateBuilder setMethod(String method) {
            super.method = method;
            return this;
        }

        @Override
        public DateBuilder setThreads(String threads) {
            super.threads = threads;
            return this;
        }

    }

}
