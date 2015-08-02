package com.sysomos.grid.tools.backup;

import com.sun.jersey.json.impl.BufferingInputOutputStream;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.log4j.Logger;
import scala.util.parsing.combinator.testing.Str;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.TimeZone;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.Deflater;
import java.util.zip.GZIPOutputStream;

/**
 * Created by kkim on 5/28/15.
 */
public class BackupConsumer extends Thread {

    protected static Logger logger = Logger.getLogger(BackupConsumer.class);

    public final static String BACKUP_MESSAGE_PER_FILE = "backup.file.message.count";
    public final static String BACKUP_FILE_PREFIX = "backup.file.prefix";
    public final static String BACKUP_DIR_KEY = "backup.dir";

    private final BlockingQueue<String> queue;
    private final Properties properties;
    private final FileSystem fs;
    private GZIPOutputStream gzip;
    private static AtomicInteger fileCounter = new AtomicInteger(0);
    private final int messageCount;
    private String fileName;
    private final String filePrefix;
    final BackupTool tool;
    boolean done;

    public BackupConsumer(final BackupTool tool) throws IOException {
        this.tool = tool;
        this.queue = tool.getQueue();
        this.properties = tool.getProperties();
        this.fs = tool.getFs();
        messageCount = Integer.valueOf(properties.getProperty(BACKUP_MESSAGE_PER_FILE, "100000"));
        filePrefix = properties.getProperty(BACKUP_FILE_PREFIX, "BACKUP");
        done=false;
    }


    protected void open() throws IOException {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        long currentTime = System.currentTimeMillis();
        String day = format.format(new Date(currentTime));
        String rootDir = String.format("%s/%s", properties.getProperty(BACKUP_DIR_KEY, "/backup"), day);

        Path rootPath = new Path(rootDir);
        synchronized (tool) {
            if (!fs.exists(rootPath)) {
                fs.mkdirs(rootPath);
                fileCounter.set(0);
                logger.info(String.format("CREATE DIRECTORY - %s", rootDir));
            } else {
                RemoteIterator<LocatedFileStatus> iter = fs.listFiles(rootPath, false);
                int max = 0;
                while (iter.hasNext()) {
                    LocatedFileStatus s = iter.next();
                    if (s.isFile()) {
                        String[] fileParts = s.getPath().getName().split("[_\\.]");
                        if (fileParts.length == 4) {
                            try {
                                max = Math.max(max, Integer.valueOf(fileParts[2]));
                            } catch (Exception e) {
                                logger.error(e,e);
                            }
                        }
                    }
                }
                fileCounter.set(max + 1);
            }

            format = new SimpleDateFormat("yyyyMMddHH");
            format.setTimeZone(TimeZone.getTimeZone("UTC"));
            String hour = format.format(new Date(currentTime));
            int fileNumber = fileCounter.getAndIncrement();
            fileName = String.format("%s/%s_%s_%06d.gz", rootDir, filePrefix, hour, fileNumber);
            Path path = new Path(fileName);
            logger.info("CREATE-" + path.getName());
            FSDataOutputStream fsdos = fs.create(path);
            gzip = new GZIPOutputStream(fsdos);
        }
    }

    protected void close() {
        if (gzip != null) {

            logger.info("gzip is closing....");
            try {
                gzip.finish();
                gzip.close();
            } catch (IOException e) {
                logger.error(e,e);
            }
            logger.info(String.format("FILE %s is closed", fileName));

        }
        fileName = null;
        gzip = null;
    }

    String addNewLine(String json) {
        return json + "\n";
    }

    @Override
    public void run() {
        logger.info(String.format("BACKUP CONSUMER (%d) has started", getId()));

        int count = 0;

        String msg;
        try {
            while (!done && !tool.isDone()) {
                msg = queue.poll(10, TimeUnit.SECONDS);
                if(msg == null) continue;

                if (gzip == null) {
                    open();
                }

                gzip.write(msg.getBytes("UTF-8"));
                count++;

                if (count == messageCount) {
                    close();
                    count = 0;
                }
            }
            logger.info("RECEIVE DONE MESSAGE");

            while((msg=queue.poll())!=null) {
                gzip.write(addNewLine(msg).getBytes("UTF-8"));
            }

        } catch (Exception e) {
            logger.error(e, e);
        } finally {
            close();
        }

        tool.setDone(true);
        logger.info(String.format("BACKUP CONSUMER (%d) is done", getId()));

    }

    public void setDone() {
        this.done = true;
    }
}
