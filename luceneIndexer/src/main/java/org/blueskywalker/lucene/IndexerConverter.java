package org.blueskywalker.lucene;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockFactory;
import org.apache.lucene.util.Version;
import org.apache.solr.core.HdfsDirectoryFactory;
import org.apache.solr.store.blockcache.BlockDirectory;
import org.apache.solr.store.hdfs.HdfsDirectory;
import org.apache.solr.store.hdfs.HdfsLockFactory;

import java.io.IOException;

/**
 * Created by kkim on 8/18/15.
 */
public class IndexerConverter {

    public static void main(String[] args) {

        Configuration conf = new Configuration();

        conf.addResource("/etc/hadoop/conf/core-site.xml");
        conf.addResource("/etc/hadoop/conf/hdfs-site.xml");
        //conf.set("fs.defaultFS","hdfs://slrpnn151");

        if (args.length < 2 ) {
            System.out.printf("%s source target\n", IndexerConverter.class.getName());
            System.exit(-1);
        }

        try {
            Path source = new Path(args[0]);
            Path target = new Path(args[1]);

            FileSystem fs = FileSystem.get(conf);
            if(!fs.exists(target)) {
                fs.mkdirs(target);
            }


            HdfsDirectory sourceDirectory = new HdfsDirectory(source,conf);
            sourceDirectory.setLockFactory(new HdfsLockFactory(source,conf));

            HdfsDirectory targetDirectory = new HdfsDirectory(target,conf);
            targetDirectory.setLockFactory(new HdfsLockFactory(target,conf));

            sourceDirectory.setLockFactory(new HdfsLockFactory(source, conf));
            targetDirectory.setLockFactory(new HdfsLockFactory(target, conf));
            IndexWriterConfig indexConf = new IndexWriterConfig(Version.LATEST, new WhitespaceAnalyzer());
            indexConf.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
            IndexWriter writer = new IndexWriter(targetDirectory,indexConf);
            writer.commit();


            writer.addIndexes(sourceDirectory);
            writer.forceMerge(1);
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

