package com.sysomos.grid.tools.backup;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.log4j.Logger;
import scala.util.parsing.combinator.testing.Str;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.TimeZone;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.GZIPOutputStream;

/**
 * Created by kkim on 5/28/15.
 */
public class BackupConsumer extends Thread {

    protected static Logger logger = Logger.getLogger(BackupConsumer.class);

    public final static String BACKUP_DIR_KEY = "backup.dir";
    public final static String BACKUP_FILE_PREFIX = "backup.file.prefix";
    public final static String HADOOP_CONF_DIR = "hadoop.conf.dir";
    public final static String BACKUP_MESSAGE_PER_FILE = "backup.file.message.count";

    private final ArrayBlockingQueue<String> queue;
    private final Properties properties;
    private final FileSystem fs;
    private boolean done;
    private GZIPOutputStream gzip;
    private static AtomicInteger fileCounter = new AtomicInteger(0);
    private final int messageCount;
    private String fileName;
    private final String filePrefix;

    public BackupConsumer(final ArrayBlockingQueue<String> queue, final Properties properties) throws IOException {
        this.queue = queue;
        this.properties = properties;
        Configuration configuration = new Configuration();
        String baseDir = properties.getProperty(HADOOP_CONF_DIR, "/etc/hadoop/conf");
        configuration.addResource(new Path(String.format("%s/core-site.xml", baseDir)));
        configuration.addResource(new Path(String.format("%s/hdfs-site.xml", baseDir)));
        fs = FileSystem.get(configuration);
        done = false;
        messageCount = Integer.valueOf(properties.getProperty(BACKUP_MESSAGE_PER_FILE,"100000"));
        filePrefix=properties.getProperty(BACKUP_FILE_PREFIX,"BACKUP");
    }


    protected void open() throws IOException {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHH");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        long currentTime = System.currentTimeMillis();
        String hour = format.format(new Date(currentTime));
        String rootDir = String.format("%s/%s", properties.getProperty(BACKUP_DIR_KEY, "/backup"), hour);

        Path rootPath = new Path(rootDir);
        synchronized (fileCounter) {
            if (!fs.exists(rootPath)) {
                fs.mkdirs(rootPath);
                fileCounter.set(0);
                logger.info(String.format("CREATE DIRECTORY - %s", rootDir));
            } else {
                RemoteIterator<LocatedFileStatus> iter = fs.listFiles(rootPath, false);
                int max=0;
                while(iter.hasNext()) {
                    LocatedFileStatus s = iter.next();
                    if(s.isFile()) {
                        String [] fileParts = s.getPath().getName().split("[_\\.]");
                        if(fileParts.length == 4) {
                            try {
                                max = Math.max(max,Integer.valueOf(fileParts[2]));
                            } catch (Exception e) {
                                ;
                            }
                        }
                    }
                }
                fileCounter.set(max+1);
            }
        }

        int fileNumber = fileCounter.getAndIncrement();
        fileName = String.format("%s/%s_%s_%06d.gz", rootDir,filePrefix,hour,fileNumber);
        Path path = new Path(fileName);
        FSDataOutputStream fsdos = fs.create(path);
        gzip = new GZIPOutputStream(fsdos);
    }

    protected void close() throws IOException {
        if(gzip!= null) {
            logger.info("gzip is closing....");
            gzip.finish();
            gzip.close();
            logger.info(String.format("FILE %s is created",fileName));
            fileName=null;
        }
        gzip = null;
    }

    @Override
    public void run() {
        try {
            int count=0;
            String msg=null;
            while (!done) {
                try {
                    msg = queue.take();

                    if (gzip == null) {
                        open();
                    }

                    gzip.write(msg.getBytes("UTF-8"));
                    count++;

                    if (count == messageCount) {
                        close();
                        count=0;
                    }
                } catch (InterruptedException e) {
                    logger.error("MSG TAKE FAIL", e);
                }
            }

            // flush all queue
            while((msg=queue.poll())!=null) {
                gzip.write(msg.getBytes("UTF-8"));
            }

            close();
        } catch (IOException e) {
            logger.error("FILE IO FAIL", e);
        }
        logger.info(String.format("BACKUP CONSUMER (%d) is done",getId()));
    }

    public void shutdown() throws InterruptedException {
        done=true;
        this.join();
    }
}
