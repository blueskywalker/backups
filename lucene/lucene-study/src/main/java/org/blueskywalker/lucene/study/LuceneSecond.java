package org.blueskywalker.lucene.study;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.SortedDocValuesField;
import org.apache.lucene.index.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;

import java.io.IOException;

/**
 * Created by kkim on 1/8/16.
 */
public class LuceneSecond {

    public static void main(String[] args) {
        try {
            Analyzer analyzer = new StandardAnalyzer();
            Directory directory = new RAMDirectory();
            IndexWriterConfig config = new IndexWriterConfig(Version.LATEST, analyzer);
            IndexWriter indexWriter = new IndexWriter(directory, config);
            Document document = new Document();
            document.add(new SortedDocValuesField("sorted_string", new BytesRef("hello")));
            indexWriter.addDocument(document);
            document = new Document();
            document.add(new SortedDocValuesField("sorted_string", new BytesRef("world")));
            indexWriter.addDocument(document);
            indexWriter.commit();
            indexWriter.close();
            IndexReader reader = DirectoryReader.open(directory);
            document = reader.document(0);
            System.out.println("doc 0: " + document.toString());
            document = reader.document(1);
            System.out.println("doc 1: " + document.toString());
            for (AtomicReaderContext context : reader.leaves()) {
                AtomicReader atomicReader = context.reader();
                SortedDocValues sortedDocValues = DocValues.getSorted(atomicReader, "sorted_string");
                System.out.println("Value count: " + sortedDocValues.getValueCount());
                System.out.println("doc 0 sorted_string: " + sortedDocValues.get(0).utf8ToString());
                System.out.println("doc 1 sorted_string: " + sortedDocValues.get(1).utf8ToString());
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
