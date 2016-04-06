package org.blueskywalker.lucene.parse;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;

import java.util.HashSet;

/**
 * Created by kkim on 2/22/16.
 */
public class ParseTest {

    public static void main(String[] args) {
        QueryParser parser= new QueryParser("main",new WhitespaceAnalyzer());

        try {
            Query query = parser.parse("hello AND name:\"jame carmeron\"");

            HashSet<Term> terms = new HashSet<>();
            query.extractTerms(terms);

            for (Term term : terms) {
                System.out.println(term);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
