package com.sysomos.grid.tools.backup;



import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Created by kkim on 5/29/15.
 */
public class BackupConsumerTest {

    @Test
    public void TestFileList() {

        Configuration conf = new Configuration();
        //conf.addResource("/Users/kkim/tmp/etc/hadoop/conf/core-site.xml");
        //conf.addResource("/Users/kkim/tmp/etc/hadoop/conf/hdfs-site.xml");
        conf.set("fs.defaultFS","rtnn01:8020");

        try {
            FileSystem fs  = FileSystem.get(conf);
            RemoteIterator<LocatedFileStatus> iter = fs.listFiles(new Path("/backup/Twitter/2015053000"), false);
            while(iter.hasNext()) {
                LocatedFileStatus status = iter.next();

                String name = status.getPath().getName();

                String [] fileParts = name.split("[_\\.]");

                System.out.println(Integer.valueOf(fileParts[2]));

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}