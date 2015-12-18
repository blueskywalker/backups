package org.blueskywalker.solr.facet;

/**
 * Created by kkim on 12/4/15.
 */

/**
 *
 * @author kkim
 */
public class SolrFacetParameter {
    FacetQuery.QueryType queryType;
    FacetQuery.FacetType facetType;
    long startDate;
    long endDate;
    String query;
    String fquery;
    String field;
    String start;
    String end;
    String gap;
    String sort;
    String limit;

    public SolrFacetParameter() {
        queryType = FacetQuery.QueryType.POST;
        facetType = FacetQuery.FacetType.FIELD;
        startDate = 0;
        endDate = 0;
        query = null;
        fquery = null;
        field = null;
        start = null;
        end = null;
        gap = null;
        sort = null;
        limit = null;
    }

    public FacetQuery.QueryType getQueryType() {
        return queryType;
    }

    public void setQueryType(FacetQuery.QueryType queryType) {
        this.queryType = queryType;
    }

    public FacetQuery.FacetType getFacetType() {
        return facetType;
    }

    public void setFacetType(FacetQuery.FacetType facetType) {
        this.facetType = facetType;
    }


    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getFquery() {
        return fquery;
    }

    public void setFquery(String fquery) {
        this.fquery = fquery;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
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

}
