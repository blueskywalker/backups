package org.blueskywalker.lucene.index;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterObjectFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.GZIPInputStream;

/**
 * Created by kkim on 1/9/16.
 */
public class TwitterWriter {

    public static void main(String[] args)  {
        Path tweetFile = Paths.get("/Users/kkim/tmp/twitter/twitter.json.gz");



        try {
            Analyzer analyzer = new WhitespaceAnalyzer();
            Directory directory = new SimpleFSDirectory(new File("/tmp/twitter/index.dat"));

            IndexWriterConfig writerConfig = new IndexWriterConfig(Version.LATEST,analyzer);

            IndexWriter writer = new IndexWriter(directory,writerConfig);

            InputStream is = Files.newInputStream(tweetFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(new GZIPInputStream(is)));
            br.lines().forEach((j)->{
                try {

                    Document doc = new Document();

                    Status status =  TwitterObjectFactory.createStatus(j);
                    doc.add(new TextField("TEXT",status.getText(), Field.Store.YES));
                    doc.add(new LongField("ID",status.getId(), Field.Store.YES));

                    writer.addDocument(doc);

                } catch (TwitterException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            writer.commit();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
