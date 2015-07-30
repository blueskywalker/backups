package org.blueskywalker.twitter.backup;

import org.apache.log4j.Logger;
import org.blueskywalker.threads.ThreadServiceCounter;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

/**
 *
 * @author kkim
 */
public class GZipWriter implements ThreadServiceCounter.CheckableRunner {
    static final Logger logger = Logger.getLogger(GZipWriter.class);
    OutputStream gzip;
    TwitterStatusSaver parent;

    String filename;

    public GZipWriter(TwitterStatusSaver parent) {
        gzip=null;
        this.parent=parent;
    }

    public void open() throws FileNotFoundException, IOException {
        filename = parent.getFileName();
        gzip = new BufferedOutputStream(new GZIPOutputStream(
                new FileOutputStream(filename)));
        logger.info("OPEN :"+ filename);
    }

    public void save(String msg) throws IOException {
        gzip.write(msg.getBytes());
    }

    public void close() throws IOException {
        if (gzip != null) {
            gzip.close();
            logger.info("CLOSE :" + filename);
            gzip = null;
        }
    }


    public void init() {

    }


    public int checkPoint() {
        return parent.MAX_LINE;
    }


    public void checkAndDo(int counter) {
        if (counter == 0) {
            try {
                close();
                open();
            } catch (IOException ex) {
                logger.error(ex, ex);
                Runtime.getRuntime().exit(-1);
            }
        }
    }

    public void finish() {
        try {
            close();
        } catch (IOException ex) {
            logger.error(ex, ex);
        }
    }

    public void run() {
        String msg = parent.queue.poll();
        try {
            if (msg == null) {
                Thread.sleep(1000);
            } else {
                save(msg);
            }
        } catch (Exception ex) {
            logger.error(ex, ex);
        }
    }

    public void shutdown()  {
        try {
            close();
        } catch (IOException ex) {
            logger.error(ex,ex);
        }
    }
}
