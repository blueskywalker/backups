/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mwired.grid.commons.commons.solr.custom;

import com.mwired.grid.commons.model.post.PostPropAndColMap;
import java.io.IOException;
import java.util.Date;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.queries.CustomScoreProvider;
import org.apache.lucene.queries.CustomScoreQuery;
import org.apache.lucene.queries.function.FunctionQuery;
import org.apache.lucene.queries.function.ValueSource;
import org.apache.lucene.search.Query;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.schema.SchemaField;
import org.apache.solr.search.QParser;
import org.apache.solr.search.QParserPlugin;
import org.apache.solr.search.SyntaxError;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;

/**
 *
 * @author kkim
 */
public abstract class NewsDateInfluenceCustomeScoreQParsePlugin extends QParserPlugin {

    public static final int MAGIC_DAY_BOOST = 132;
    public static final int MAGIC_INFLUENCE_BOOST = 2;
    public static final int MAGIC_ADJUST_VALUE = 100;

    abstract int getThreadhold();

    @Override
    public QParser createParser(String query, SolrParams sp, SolrParams sp1, SolrQueryRequest sqr) {
        return new QParser(query, sp, sp1, sqr) {
            @Override
            public Query parse() throws SyntaxError {
                QParser parser = getParser(this.qstr, "lucene", this.req);
                Query inner = parser.parse();
                SchemaField createDate = getReq().getSchema().getField(PostPropAndColMap.CREATE_DATE);
                SchemaField influence = getReq().getSchema().getField(PostPropAndColMap.INFLUENCE_SCORE);
                ValueSource influence_source = influence.getType().getValueSource(influence, parser);
                ValueSource createDate_source = createDate.getType().getValueSource(createDate, parser);
                
                return new NewsDateInfluenceCustomQuery(inner, 
                        new FunctionQuery(influence_source),
                        new FunctionQuery(createDate_source));
            }
        };
    }

    class NewsDateInfluenceCustomQuery extends CustomScoreQuery {

        ValueSource influence;
        ValueSource createDate;

        public NewsDateInfluenceCustomQuery(Query subQuery, FunctionQuery influence, FunctionQuery createDate) {
            super(subQuery,influence,createDate);
            this.influence = influence.getValueSource();
            this.createDate = createDate.getValueSource();
            setStrict(true);
        }

        @Override
        protected CustomScoreProvider getCustomScoreProvider(AtomicReaderContext context) throws IOException {
            return new CustomScoreProvider(context) {
                @Override
                public float customScore(int doc, float subQueryScore, float[] valSrcScores) throws IOException {
                    byte influence_score = influence.getValues(null, context).byteVal(doc);
                    Date baseDate = new Date(createDate.getValues(null, context).longVal(doc));
                    float value = getDayId(baseDate) * MAGIC_DAY_BOOST 
                            + influence_score * MAGIC_INFLUENCE_BOOST
                            + getQhr(baseDate);
                    if (influence_score < getThreadhold()) {
                        value /= MAGIC_ADJUST_VALUE;
                    }
                    return value;
                }

                private int getDayId(Date from) {
                    DateTime nowday = new DateTime(from.getTime(), DateTimeZone.UTC);
                    DateTime beginning = new DateTime(0);
                    return Days.daysBetween(beginning, nowday).getDays();
                }

                private int getQhr(Date from) {
                    DateTime now = new DateTime(from.getTime(), DateTimeZone.UTC);
                    return now.getMinuteOfDay() / 15; /* 15 minutes granurality */

                }
            };
        }

    }

    @Override
    public void init(NamedList nl) {
        
    }

}
