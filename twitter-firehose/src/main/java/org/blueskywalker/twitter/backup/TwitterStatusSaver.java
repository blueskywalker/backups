package org.blueskywalker.twitter.backup;

import com.yammer.metrics.core.Gauge;
import com.yammer.metrics.core.MetricsRegistry;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.log4j.Logger;
import org.blueskywalker.threads.ThreadServiceCounter;
import org.blueskywalker.twitter.hosebird.YamlType;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/**
 *
 * @author kkim
 */
public class TwitterStatusSaver {

    public static final Logger logger = Logger.getLogger(TwitterStatusSaver.class);
    final ConcurrentLinkedQueue<String> queue;
    final ThreadServiceCounter service;
    final Map<String, Object> conf;
    final AtomicBoolean done;
    final AtomicInteger fileId;
    final int MAX_LINE;
    final String out_dir;
    final String prefix;

    public TwitterStatusSaver(final YamlType properties,final MetricsRegistry metrics) {
        conf = properties;
        queue = new ConcurrentLinkedQueue<String>();
        done = new AtomicBoolean(false);
        fileId = new AtomicInteger(0);
        MAX_LINE = (Integer) conf.get("max.line");
        out_dir = (String) conf.get("output");
        prefix = (String) conf.get("prefix");
        int nThread = (Integer) conf.get("no.thread");

        ThreadServiceCounter.CheckableThread[] workers = new ThreadServiceCounter.CheckableThread[nThread];
        for(int i=0;i<workers.length;i++) {
            workers[i] = new ThreadServiceCounter.CheckableThread(new GZipWriter(this));
        }

        service = new ThreadServiceCounter(workers);

        metrics.newGauge(TwitterStatusSaver.class, "backup.queue.size", new Gauge<Integer>() {

            @Override
            public Integer value() {
                return queue.size();
            }
        });
    }

    public boolean write(String msg) {
        assert null != queue;
        return queue.offer(msg);
    }

    public void start() {
        checkExistingFile();
        service.start();
    }

    protected void checkExistingFile() {
        String now = new DateTime(DateTimeZone.UTC).toString();
        String date = now.substring(0, 10);
        String filename = String.format("%s/%s", out_dir, date);
        Path filepath = Paths.get(filename);
        if (Files.exists(filepath, LinkOption.NOFOLLOW_LINKS)) {
            File[] list = filepath.toFile().listFiles(new FilenameFilter() {

                public boolean accept(File dir, String name) {
                    return name.startsWith(prefix) && name.endsWith("gz");
                }
            });

            int max = -1;
            for (File f : list) {
                String[] parts = f.getName().split("[_\\.]");
                if (parts.length == 3) {
                    max = Math.max(max, Integer.valueOf(parts[1]));
                }
            }
            fileId.set(max + 1);
        }

    }

    public synchronized String getFileName() throws IOException {

        String now = new DateTime(DateTimeZone.UTC).toString();
        String date = now.substring(0, 10);
        String filename = String.format("%s/%s", out_dir, date);
        Path filepath = Paths.get(filename);
        if (!Files.exists(filepath, LinkOption.NOFOLLOW_LINKS)) {
            filepath.toFile().mkdirs();
            fileId.set(0);
        }

        return String.format("%s/%s/%s_%06d.gz", out_dir, date,
                prefix, fileId.getAndIncrement());
    }

    public void waitUntilDone() throws InterruptedException {
        service.join();
    }

    public void shutdown() {
        service.shutdown();
    }
}

