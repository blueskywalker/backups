package org.blueskywalker.lucene.local;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.CompoundFileDirectory;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;

/**
 * Created by kkim on 8/18/15.
 */
public class IndexLocalWriter {

    public static void main(String[] args) {
        try {
            Directory directory = FSDirectory.open(new File("/tmp/solr/core"));
            Analyzer analyzer = new StandardAnalyzer();
            IndexWriterConfig iwc = new IndexWriterConfig(Version.LATEST,analyzer);

            IndexWriter writer = new IndexWriter(directory,iwc);

            writer.commit();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
