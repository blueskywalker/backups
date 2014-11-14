package com.sysomos.jerry.test.custom.query;

import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.queries.function.FunctionQuery;
import org.apache.lucene.queries.function.FunctionValues;
import org.apache.lucene.queries.function.ValueSource;
import org.apache.lucene.queries.function.valuesource.FieldCacheSource;
import org.apache.lucene.queries.function.valuesource.LongFieldSource;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.SortField;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.TextResponseWriter;
import org.apache.solr.schema.DateField;
import org.apache.solr.schema.SchemaField;
import org.apache.solr.schema.TrieDateField;
import org.apache.solr.search.QParser;
import org.apache.solr.search.QParserPlugin;
import org.apache.solr.search.SyntaxError;


import java.io.IOException;
import java.util.Map;

/**
 * Created by kkim on 11/4/14.
 */
public class NewsDateInfluenceCustomQParserPlugin extends QParserPlugin {



    private static class NewsDataInfluenceCustomParser extends QParser {

        private Query innerQuery;
        private QParser parser;

        public NewsDataInfluenceCustomParser(String qstr, SolrParams localParams, SolrParams params, SolrQueryRequest req) {
            super(qstr, localParams, params, req);
            try {
                parser = getParser(qstr,"lucene",getReq());
                this.innerQuery = parser.parse();

            } catch (SyntaxError syntaxError) {
                throw new RuntimeException("error parsing query",syntaxError);
            }
        }

        @Override
        public Query parse() throws SyntaxError {
            SchemaField f = getReq().getSchema().getField("timestamp");

            ValueSource source = f.getType().getValueSource(f,parser);
            return new NewsDateInfluenceCustomQuery(innerQuery,new FunctionQuery(source));
        }
    }

    @Override
    public QParser createParser(String s, SolrParams solrParams, SolrParams solrParams2, SolrQueryRequest solrQueryRequest) {
        return new NewsDataInfluenceCustomParser(s,solrParams,solrParams2,solrQueryRequest);
    }

    @Override
    public void init(NamedList namedList) {
        System.out.println(namedList.toString());

    }
}
