package org.blueskywalker.solr.facet;

import java.util.List;
import java.util.Map;

/**
 * Created by kkim on 12/4/15.
 */
public class ResultList<T> {
    private long totalFound;
    private int totalReturned;
    private List<T> results;
    private Map<String, Object> properties;
    private long timeTaken;
    private String nextCursorMark;
    private String errorMsg;

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }


    /**
     * @return the totalFound
     */
    public long getTotalFound() {
        return totalFound;
    }

    /**
     * @param totalFound the totalFound to set
     */
    public void setTotalFound(long totalFound) {
        this.totalFound = totalFound;
    }

    /**
     * @return the totalReturned
     */
    public int getTotalReturned() {
        return totalReturned;
    }

    /**
     * @param totalReturned the totalReturned to set
     */
    public void setTotalReturned(int totalReturned) {
        this.totalReturned = totalReturned;
    }

    /**
     * @return the results
     */
    public List<T> getResults() {
        return results;
    }

    /**
     * @param results the results to set
     */
    public void setResults(List<T> results) {
        this.results = results;
    }

    /**
     * @return the properties
     */
    public Map<String, Object> getProperties() {
        return properties;
    }

    /**
     * @param properties the properties to set
     */
    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    /**
     * @return the timeTaken
     */
    public long getTimeTaken() {
        return timeTaken;
    }

    /**
     * @param timeTaken the timeTaken to set
     */
    public void setTimeTaken(long timeTaken) {
        this.timeTaken = timeTaken;
    }

    public void clear() {
        this.properties = null;
        this.results = null;
        this.timeTaken = 0;
        this.totalFound = 0;
        this.totalReturned = 0;
    }

    /**
     * @return the nextCursorMark
     */
    public String getNextCursorMark() {
        return nextCursorMark;
    }

    /**
     * @param nextCursorMark the nextCursorMark to set
     */
    public void setNextCursorMark(String nextCursorMark) {
        this.nextCursorMark = nextCursorMark;
    }
}
