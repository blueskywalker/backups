package org.blueskywalker.java.solr.facet;

import java.util.List;

/**
 * Created by kkim on 1/8/16.
 */
public class ResultList<T> {

    long totalFound;
    List<T> results;
    String errorMsg;

    public long getTotalFound() {
        return totalFound;
    }

    public void setTotalFound(long totalFound) {
        this.totalFound = totalFound;
    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
