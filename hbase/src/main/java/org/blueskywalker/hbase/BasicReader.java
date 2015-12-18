package org.blueskywalker.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.output.NullOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;

/**
 * Created by kkim on 8/7/15.
 */
public class BasicReader extends Configured implements Tool {

    public int run(String[] args) throws Exception {
        final String tableName=args[0];
        Job job = new Job(conf, "BasicReader");
        job.setJarByClass(BasicReader.class);     // class that contains mapper

        TableMapReduceUtil.addDependencyJars(job);

        Scan scan = new Scan();
        scan.setCaching(500);        // 1 is the default in Scan, which will be bad for MapReduce jobs
        scan.setCacheBlocks(false);  // don't set to true for MR jobs


        TableMapReduceUtil.initTableMapperJob(
                tableName,        // input HBase table name
                scan,             // Scan instance to control CF and attribute selection
                BasicReaderMapper.class,   // mapper
                null,             // mapper output key
                null,             // mapper output value
                job);
        job.setNumReduceTasks(0);
        job.setOutputFormatClass(NullOutputFormat.class);   // because we aren't emitting anything from mapper

        boolean b = job.waitForCompletion(false);

        return 0;
    }

    public static void main(String[] args) {

        Configuration conf = HBaseConfiguration.create();
        conf.addResource(new Path("/etc/hbase/conf/hbase-site.xml"));
        try {
            ToolRunner.run(conf, new BasicReader(), args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
