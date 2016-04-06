package org.blueskywalker.java.solr.facet;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kkim
 */

@JsonInclude()
public class FacetResult {
    private String alias;
    private String field;
    private List<Count> results = new ArrayList<Count>();

    static public class Count {
        String value;
        long count;

        public Count(){};

        public Count(String value, long count) {
            this.value = value;
            this.count = count;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public long getCount() {
            return count;
        }

        public void setCount(long count) {
            this.count = count;
        }

    }

    public List<Count> getResults() {
        return results;
    }

    public void setResults(List<Count> results) {
        this.results = results;
    }

    public void addResult(String value,long count) {
        results.add(new Count(value,count));
    }

    public FacetResult() {
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }


    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

}
