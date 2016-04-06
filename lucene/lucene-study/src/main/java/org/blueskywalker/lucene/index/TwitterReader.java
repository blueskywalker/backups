package org.blueskywalker.lucene.index;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.BytesRef;

import java.io.File;
import java.io.IOException;

/**
 * Created by kkim on 1/9/16.
 */
public class TwitterReader {

    public static void main(String[] args) {
        try {
            Analyzer analyzer = new WhitespaceAnalyzer();
            Directory directory = new SimpleFSDirectory(new File("/tmp/twitter/index.dat"));

            IndexReader reader = DirectoryReader.open(directory);


            BinaryDocValues cache = FieldCache.DEFAULT.getTerms(
                    SlowCompositeReaderWrapper.wrap(reader),"TEXT",true);

            for(int i=0;i<reader.maxDoc();i++) {
                BytesRef bytesRef = cache.get(i);
                System.out.println(i + ": " + bytesRef.utf8ToString());
            }
            /*
            IndexSearcher searcher = new IndexSearcher(reader);

            QueryParser parser = new QueryParser("text", analyzer);

            Query query = parser.parse("TEXT:fashion");

            TopDocs docs = searcher.search(query, 10);



            for (ScoreDoc sd : docs.scoreDocs) {
                System.out.println(searcher.doc(sd.doc).get("TEXT"));
            }
            */

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}