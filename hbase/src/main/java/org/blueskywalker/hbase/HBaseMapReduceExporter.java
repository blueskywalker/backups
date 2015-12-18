package org.blueskywalker.hbase;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.Tool;

import java.io.IOException;

/**
 * Created by kkim on 8/7/15.
 */
public class HBaseMapReduceExporter extends Configuration implements Tool {

    static final String JOB_ID = "HBaseExporter";
    final String tableName;
    final String outputDir;
    Configuration config;

    public HBaseMapReduceExporter(String tableName, String outputDir)  {
        this.tableName = tableName;
        this.outputDir = outputDir;
        setConf(HBaseConfiguration.create());
    }


    public int run(String[] strings) throws Exception {

        Job job = Job.getInstance(getConf());
        job.setJobName(String.format("%s-%s", JOB_ID, tableName));
        job.setJarByClass(HBaseMapReduceExporter.class);
        TableMapReduceUtil.addDependencyJars(job);

        Scan scan = new Scan();
        scan.setCaching(500);
        scan.setCacheBlocks(false);

        //TableMapReduceUtil.initTableMapperJob(tableName,scan, );


        return 0;
    }

    public void setConf(Configuration configuration) {
        config=configuration;
    }

    public Configuration getConf() {
        return config;
    }
}
