package org.blueskywalker.lucene.study;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import java.io.IOException;

/**
 * Created by kkim on 1/7/16.
 */
public class LuceneFirst {

    public static void main(String[] args) {


        try {
            Analyzer analyzer = new WhitespaceAnalyzer();
            Directory directory = new RAMDirectory();

            IndexWriterConfig config = new IndexWriterConfig(Version.LATEST,analyzer);

            IndexWriter indexWriter = new IndexWriter(directory,config);
            Document doc = new Document();

            String text = "Lucene is an Information Retrieval library written in Java";

            doc.add(new TextField("text",text, Field.Store.YES));

            indexWriter.addDocument(doc);

            indexWriter.commit();

            indexWriter.close();

            IndexReader indexReader = DirectoryReader.open(directory);

            IndexSearcher indexSearcher = new IndexSearcher(indexReader);

            QueryParser parser = new QueryParser("text",analyzer);

            Query query = parser.parse("Lucene");

            int hitsPerPage = 10;

            TopDocs docs = indexSearcher.search(query, hitsPerPage);

            ScoreDoc[] hits = docs.scoreDocs;

            int end = Math.min(docs.totalHits, hitsPerPage);

            System.out.println("Total Hits: " + docs.totalHits);
            System.out.print("Results: ");
            for (int i = 0; i < end; i++) {
                Document d = indexSearcher.doc(hits[i].doc);
                System.out.println("Content: " + d.get("text"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
