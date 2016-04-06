package org.blueskywalker.java.solr.facet;


import org.blueskywalker.java.solr.facet.SolrQueryFacet.QueryType;
import org.blueskywalker.java.solr.facet.SolrQueryFacet.FacetType;

/**
 *
 * @author kkim
 */
public class SolrFacetParameter {
    QueryType queryType;
    FacetType facetType;
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
        queryType = QueryType.POST;
        facetType = FacetType.FIELD;
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

    public QueryType getQueryType() {
        return queryType;
    }

    public SolrFacetParameter setQueryType(QueryType queryType) {
        this.queryType = queryType;
        return this;
    }

    public FacetType getFacetType() {
        return facetType;
    }

    public SolrFacetParameter setFacetType(FacetType facetType) {
        this.facetType = facetType;
        return this;
    }


    public long getStartDate() {
        return startDate;
    }

    public SolrFacetParameter setStartDate(long startDate) {
        this.startDate = startDate;
        return this;
    }

    public long getEndDate() {
        return endDate;
    }

    public SolrFacetParameter setEndDate(long endDate) {
        this.endDate = endDate;
        return this;
    }

    public String getQuery() {
        return query;
    }

    public SolrFacetParameter setQuery(String query) {
        this.query = query;
        return this;
    }

    public String getFquery() {
        return fquery;
    }

    public SolrFacetParameter setFquery(String fquery) {
        this.fquery = fquery;
        return this;
    }

    public String getField() {
        return field;
    }

    public SolrFacetParameter setField(String field) {
        this.field = field;
        return this;
    }

    public String getStart() {
        return start;
    }

    public SolrFacetParameter setStart(String start) {
        this.start = start;
        return this;
    }

    public String getEnd() {
        return end;
    }

    public SolrFacetParameter setEnd(String end) {
        this.end = end;
        return this;
    }

    public String getGap() {
        return gap;
    }

    public SolrFacetParameter setGap(String gap) {
        this.gap = gap;
        return this;
    }

    public String getSort() {
        return sort;
    }

    public SolrFacetParameter setSort(String sort) {
        this.sort = sort;
        return this;
    }

    public String getLimit() {
        return limit;
    }

    public SolrFacetParameter setLimit(String limit) {
        this.limit = limit;
        return this;
    }

}
