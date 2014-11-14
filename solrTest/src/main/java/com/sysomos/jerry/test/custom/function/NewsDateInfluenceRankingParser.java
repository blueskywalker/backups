package com.sysomos.jerry.test.custom.function;

import org.apache.lucene.queries.function.ValueSource;
import org.apache.solr.search.FunctionQParser;
import org.apache.solr.search.SyntaxError;
import org.apache.solr.search.ValueSourceParser;

/**
 * Created by kkim on 11/4/14.
 */
public class NewsDateInfluenceRankingParser extends ValueSourceParser {

    @Override
    public ValueSource parse(FunctionQParser functionQParser) throws SyntaxError {
        ValueSource source = functionQParser.parseValueSource();

        return new NewsDateInfluenceRanking(source);
    }
}
