package com.sysomos.jerry.test.custom.query;

import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.queries.CustomScoreProvider;
import org.apache.lucene.queries.CustomScoreQuery;
import org.apache.lucene.queries.function.FunctionQuery;
import org.apache.lucene.queries.function.FunctionValues;
import org.apache.lucene.queries.function.ValueSource;
import org.apache.lucene.search.Query;

import java.io.IOException;

/**
 * Created by kkim on 11/4/14.
 */
public class NewsDateInfluenceCustomQuery extends CustomScoreQuery {

    ValueSource source;
    public NewsDateInfluenceCustomQuery(Query subQuery, FunctionQuery fquery) {
        super(subQuery,fquery);
        source=fquery.getValueSource();

        setStrict(true);
    }

    @Override
    protected CustomScoreProvider getCustomScoreProvider(AtomicReaderContext context) throws IOException {
       return new NewsDateInfluenceCustomScoreProvider(context,source.getValues(null,context));
    }

    class NewsDateInfluenceCustomScoreProvider extends CustomScoreProvider {
        FunctionValues values;
        public NewsDateInfluenceCustomScoreProvider(AtomicReaderContext context,
                                                    FunctionValues values) {
            super(context);
            this.values=values;
        }

        @Override
        public float customScore(int doc, float subQueryScore, float valSrcScore) throws IOException {

            long ts=values.longVal(doc);
            return 1.0f;
        }


    }
}
