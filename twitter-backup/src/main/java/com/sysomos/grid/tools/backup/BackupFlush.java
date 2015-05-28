package com.sysomos.grid.tools.backup;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.GZIPOutputStream;

/**
 * Created by kkim on 5/23/15.
 */
public class BackupFlush extends Thread {
    protected static Logger logger = Logger.getLogger(BackupFlush.class);
    final static String BACKUP_DIR_KEY="backup.dir";
    final static String BACKUP_IO_COUNT="backup.io.thread.count";
    final static String BACKUP_IO_QUEUE_SIZE="backup.io.queue.size";
    final LinkedBlockingQueue<ArrayList<String>> queue;
    final Properties properties;
    final FileSystem fs;
    final ExecutorService service;
    final int defaultQueueSize;
    final AtomicInteger workingThreads;
    boolean done;

    public BackupFlush(Properties properties) throws IOException {
        super();
        done=false;
        this.properties=properties;
        Configuration configuration = new Configuration();
        String hadoopConfDir = "hadoop.conf.dir";
        String baseDir=properties.getProperty(hadoopConfDir);
        configuration.addResource(new Path(String.format("%s/core-site.xml",baseDir)));
        configuration.addResource(new Path(String.format("%s/hdfs-site.xml",baseDir)));

        fs = FileSystem.get(configuration);
        int threadSize = Integer.valueOf(properties.getProperty(BACKUP_IO_COUNT, "2"));
        service = Executors.newFixedThreadPool(threadSize);
        defaultQueueSize=Integer.valueOf(properties.getProperty(BACKUP_IO_QUEUE_SIZE,"100"));
        queue = new LinkedBlockingQueue<ArrayList<String>>(defaultQueueSize);
        workingThreads = new AtomicInteger(0);
    }

    public synchronized void add(ArrayList<String> list) throws InterruptedException {
        queue.put(list);
        if (this.getState() == State.WAITING) {
            this.notify();
            logger.info(String.format("THREAD(%d) will wake",this.getId()));
        }
    }

    public static class Flusher implements Runnable {
        final ArrayList<String> data;
        final Properties properties;
        final FileSystem fs;
        final AtomicInteger counter;
        public Flusher(final ArrayList<String> data,final Properties properties,final FileSystem fs,final AtomicInteger counter) {
            this.data = data;
            this.properties=properties;
            this.fs = fs;
            this.counter=counter;
        }

        public void run()  {

            try {
                counter.incrementAndGet();

                SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhh");
                long currentTime = System.currentTimeMillis();
                String rootDir = String.format("%s/%s",properties.getProperty(BACKUP_DIR_KEY, "/"),format.format(new Date(currentTime)));
                Path rootPath = new Path(rootDir);

                if(!fs.exists(rootPath)) {
                    fs.mkdirs(rootPath);
                    logger.info(String.format("CREATE DIRECTORY - %s",rootDir));
                }

                String fileName = String.format("%s/%d.gz", rootDir,  currentTime/ 1000);
                Path path = new Path(fileName);
                FSDataOutputStream fsdos = fs.create(path);
                GZIPOutputStream gzip = new GZIPOutputStream(fsdos);

                for (String msg : data) {
                    gzip.write(msg.getBytes("UTF-8"));
                }

                gzip.close();
                logger.info(String.format("FILE(%s) is created",fileName));
                counter.decrementAndGet();
            } catch (Exception e) {
                logger.error("Writing Failed",e);
            }
        }
    }

    @Override
    public void run() {

        while (!done) {
            ArrayList<String> out = queue.poll();
            if (out == null) {
                try {
                    synchronized (this) {
                        logger.info(String.format("THEAD(%d) is waiting",this.getId()));
                        this.wait();
                    }
                } catch (InterruptedException e) {
                    logger.error("Interrupted", e);
                }
            } else {
                service.submit(new Flusher(out,properties,fs,workingThreads));
                logger.info("launch Flusher");
            }
        }
        logger.info("wait for threads");

        while (workingThreads.intValue()!=0) {
            try {
                sleep(60000); // 1 minutes
            } catch (InterruptedException e) {
                logger.error("Thread is canceled",e);
            }
        }

        service.shutdown();
        try {
            service.awaitTermination(3, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            logger.error("shutdown is interrupted",e);
        }
    }

    public void shutdown() {
        done=true;
    }
}
