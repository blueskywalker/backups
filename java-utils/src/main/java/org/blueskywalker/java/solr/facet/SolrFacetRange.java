package org.blueskywalker.java.solr.facet;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;

/**
 *
 * @author kkim
 */
public class SolrFacetRange extends SolrFacet {

    String range;
    String start;
    String end;
    String gap;
    String hardend;
    String other;
    String include;

    SolrFacetRange() {
        super();
        this.range = null;
        this.start = null;
        this.end = null;
        this.gap = null;
        this.hardend = null;
        this.other = null;
        this.include = null;
    }

    public SolrFacetRange(String range) {
        this();
        this.range = range;
    }

    protected SolrFacetRange(RangeBuilder b) throws Exception {
        super(b);

    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getGap() {
        return gap;
    }

    public void setGap(String gap) {
        this.gap = gap;
    }

    public String getHardend() {
        return hardend;
    }

    public void setHardend(String hardend) {
        this.hardend = hardend;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public String getInclude() {
        return include;
    }

    public void setInclude(String include) {
        this.include = include;
    }

    protected SolrQuery getSolrQueryFromSolrFacet () throws Exception {
        return super.createSolrQuery();
    }

    @Override
    public SolrQuery createSolrQuery() throws Exception {
        SolrQuery solrQuery = super.createSolrQuery();
        Field[] fields = SolrFacetRange.class.getDeclaredFields();

        for (Field f : fields) {
            Method getter = getClass().getMethod("get" + StringUtils.capitalize(f.getName()));
            Object object = getter.invoke(this);
            if (object != null) {
                if (f.getName().equalsIgnoreCase("range")) {
                    solrQuery.setParam("facet.range", range);
                } else {
                    solrQuery.setParam(String.format("f.%s.facet.range.%s", range, f.getName()), object.toString());
                }
            }
        }
        return solrQuery;
    }

    public static class RangeBuilder extends FacetBuilder {

        String range;
        String start;
        String end;
        String gap;
        String hardend;
        String other;
        String include;

        public RangeBuilder(String range) {
            super();
            this.range = range;
        }

        public String getRange() {
            return range;
        }

        public RangeBuilder setRange(String range) {
            this.range = range;
            return this;
        }

        public String getStart() {
            return start;
        }

        public RangeBuilder setStart(String start) {
            this.start = start;
            return this;
        }

        public String getEnd() {
            return end;
        }

        public RangeBuilder setEnd(String end) {
            this.end = end;
            return this;
        }

        public String getGap() {
            return gap;
        }

        public RangeBuilder setGap(String gap) {
            this.gap = gap;
            return this;
        }

        public String getHardend() {
            return hardend;
        }

        public RangeBuilder setHardend(String hardend) {
            this.hardend = hardend;
            return this;
        }

        public String getOther() {
            return other;
        }

        public RangeBuilder setOther(String other) {
            this.other = other;
            return this;
        }

        public String getInclude() {
            return include;
        }

        public RangeBuilder setInclude(String include) {
            this.include = include;
            return this;
        }

        public SolrFacetRange build() throws Exception {
            return new SolrFacetRange(this);
        }

        @Override
        public RangeBuilder setThreads(String threads) {
            super.threads = threads;
            return this;
        }

        @Override
        public RangeBuilder setMethod(String method) {
            super.method = method;
            return this;
        }

        @Override
        public RangeBuilder setMissing(String missing) {
            super.missing = missing;
            return this;
        }

        @Override
        public RangeBuilder setMincount(String mincount) {
            super.mincount = mincount;
            return this;
        }

        @Override
        public RangeBuilder setOffset(String offset) {
            super.offset = offset;
            return this;
        }

        @Override
        public RangeBuilder setLimit(String limit) {
            super.limit = limit;
            return this;
        }

        @Override
        public RangeBuilder setSort(String sort) {
            super.sort = sort;
            return this;
        }

        @Override
        public RangeBuilder setPrefix(String prefix) {
            super.prefix = prefix;
            return this;
        }
    }

}
