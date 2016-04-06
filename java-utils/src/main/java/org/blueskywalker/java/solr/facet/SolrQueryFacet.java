package org.blueskywalker.java.solr.facet;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.RangeFacet;



/**
 *
 * @author kkim
 */
public class SolrQueryFacet {

    static Logger logger = Logger.getLogger(SolrQueryFacet.class);

    public enum FacetType {

        QUERY,FIELD, RANGE, DATE
    }

    public enum QueryType {
        PERSONA, POST
    }

    QueryType queryType;
    FacetType facetType;
    String alias;
    long startDate;  // seconds not milliseconds
    long endDate;    // seconds not milliseconds
    String query;
    String fquery;
    String field;
    String sort;
    String start;
    String end;
    String gap;
    String method;
    String limit;

    SolrQuery solrQuery;
    ArrayList<String> filters = new ArrayList<String>();

    public SolrQueryFacet(QueryType qtype, FacetType ftype) {
        queryType = qtype;
        facetType = ftype;
        startDate = 0;
        endDate = 0;
        query = null;
        fquery = null;
        field = null;
        sort = null;
        start = null;
        end = null;
        gap = null;
        method = null;
        limit="-1";
    }

    public SolrQueryFacet(SolrFacetParameter params) throws Exception {
        Field[] fields = params.getClass().getDeclaredFields();
        for (Field f : fields) {
            Method getter = params.getClass().getMethod("get" + StringUtils.capitalize(f.getName()));
            Method setter = getClass().getMethod("set" + StringUtils.capitalize(f.getName()), f.getType());
            setter.invoke(this, getter.invoke(params));
        }
    }

    public QueryType getQueryType() {
        return queryType;
    }

    public void setQueryType(QueryType queryType) {
        this.queryType = queryType;
    }

    public FacetType getFacetType() {
        return facetType;
    }

    public SolrQueryFacet setFacetType(FacetType facetType) {
        this.facetType = facetType;
        return this;
    }

    public String getAlias() {
        return alias;

    }

    public SolrQueryFacet setAlias(String alias) {
        this.alias = alias;
        return this;
    }

    public long getStartDate() {
        return startDate;
    }

    public SolrQueryFacet setStartDate(long startDate) {
        this.startDate = startDate;
        return this;
    }

    public long getEndDate() {
        return endDate;
    }

    public SolrQueryFacet setEndDate(long endDate) {
        this.endDate = endDate;
        return this;
    }

    public String getQuery() {
        return query;
    }

    public SolrQueryFacet setQuery(String query) {
        this.query = query;
        return this;
    }

    public String getFquery() {
        return fquery;
    }

    public SolrQueryFacet setFquery(String fquery) {
        this.fquery = fquery;
        return this;
    }

    public String getField() {
        return field;
    }

    public SolrQueryFacet setField(String field) {
        this.field = field;
        return this;
    }

    public String getSort() {
        return sort;
    }

    public SolrQueryFacet setSort(String sort) {
        this.sort = sort;
        return this;
    }

    public String getStart() {
        return start;
    }

    public SolrQueryFacet setStart(String start) {
        this.start = start;
        return this;
    }

    public String getEnd() {
        return end;
    }

    public SolrQueryFacet setEnd(String end) {
        this.end = end;
        return this;
    }

    public String getGap() {
        return gap;
    }

    public SolrQueryFacet setGap(String gap) {
        this.gap = gap;
        return this;
    }

    public SolrQuery getSolrQuery() {
        return solrQuery;
    }

    public List<String> getFilters() {
        return filters;
    }

    public SolrQueryFacet addFilters(List<String> filters) {
        if(filters !=null)
            this.filters.addAll(filters);
        return this;
    }

    public String getMethod() {
        return method;
    }

    public SolrQueryFacet setMethod(String method) {
        this.method = method;
        return this;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }


    protected void makeSolrQuery() throws Exception {

        SolrFacet facet = null;

        switch (facetType) {
            case FIELD:
                facet = new SolrFacetField.FieldBuilder(field)
                        .setSort(sort)
                        .setMethod(method)
                        .setLimit(limit)
                        .build();

                solrQuery = facet.createSolrQuery();
                break;
            case RANGE:
                facet = new SolrFacetRange.RangeBuilder(field)
                        .setStart(start)
                        .setEnd(end)
                        .setGap(gap)
                        .setMethod(method)
                        .setSort(sort)
                        .setLimit(limit)
                        .build();
                solrQuery = facet.createSolrQuery();
                break;
            case DATE:
                facet = new SolrFacetDate.DateBuilder(field)
                        .setStart(start)
                        .setEnd(end)
                        .setGap(gap)
                        .setMethod(method)
                        .setSort(sort)
                        .setLimit(limit)
                        .build();
                solrQuery = facet.createSolrQuery();
                break;
            case QUERY:
                List<String> queries = new ArrayList<String>();
                queries.add(fquery);

                facet= new SolrFacetQuery.FacetQueryBuilder()
                        .setMethod(method)
                        .setSort(sort)
                        .setQueries(queries)
                        .build();

        }

    }

    public ResultList<FacetResult> execute(CloudSolrServer solrServer) {

        if (solrServer == null) {
            return null;
        }

        ResultList<FacetResult> ret = new ResultList<FacetResult>();

        try {

            makeSolrQuery();

            if (query == null) {
                solrQuery.setQuery("*:*");
            } else {
                solrQuery.setQuery(query);
            }

            if (queryType.equals(QueryType.POST)) {
                solrQuery.addFilterQuery(String.format(
                        "createDate: [ %d TO %d ]", startDate, endDate));
            }

            for (String filter : filters) {
                solrQuery.addFilterQuery(filter);
            }

            QueryResponse response = solrServer.query(solrQuery, SolrRequest.METHOD.POST);

            ret.setTotalFound(response.getResults().getNumFound());

            List<FacetResult> output = new ArrayList<FacetResult>();

            switch (facetType) {

                case FIELD:
                case DATE:
                    List<FacetField> fieldOutput;
                    if (facetType == FacetType.FIELD)
                        fieldOutput= response.getFacetFields();
                    else
                        fieldOutput= response.getFacetDates();


                    for (FacetField f : fieldOutput) {
                        if (f.getName().equals(field)) {
                            FacetResult result = new FacetResult();
                            result.setAlias(alias);
                            result.setField(field);
                            for (FacetField.Count count : f.getValues()) {
                                if(count.getCount()!=0)
                                    result.addResult(count.getName(),count.getCount());
                            }

                            output.add(result);
                            ret.setResults(output);
                        }
                    }
                    break;
                case RANGE:
                    List<RangeFacet> rangeOutput = response.getFacetRanges();
                    for (RangeFacet f : rangeOutput) {
                        if (f.getName().equals(field)) {
                            FacetResult result = new FacetResult();
                            result.setAlias(alias);
                            result.setField(field);
                            for (Object c : f.getCounts()) {
                                RangeFacet.Count count = (RangeFacet.Count) c;
                                result.addResult(count.getValue(),count.getCount());
                            }
                            output.add(result);
                            ret.setResults(output);
                        }
                    }
                    break;
                case QUERY:
                    Map<String,Integer> queryResult= response.getFacetQuery();
                    FacetResult result = new FacetResult();

                    for(String value: queryResult.keySet()) {
                        result.addResult(value, queryResult.get(value));
                    }
                    output.add(result);
                    ret.setResults(output);
            }

        } catch (SolrServerException ex) {
            logger.error("solr Client Exception", ex);
            ret.setErrorMsg(ex.getMessage());
        } catch (Exception ex) {
            logger.error("Solr Client Exception", ex);
            ret.setErrorMsg(ex.getMessage());
        }

        return ret;
    }

}
