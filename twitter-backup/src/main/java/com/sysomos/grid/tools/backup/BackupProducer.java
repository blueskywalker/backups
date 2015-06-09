package com.sysomos.grid.tools.backup;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import org.apache.log4j.Logger;

import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by kkim on 5/22/15.
 */
public class BackupProducer extends Thread {
    public static Logger logger = Logger.getLogger(BackupProducer.class);

    final KafkaStream<byte[], byte[]> stream;
    final Properties properties;
    final ArrayBlockingQueue<String> queue;

    public BackupProducer(final KafkaStream<byte[], byte[]> stream,final BackupTool tool) {
        this.stream = stream;
        this.queue = tool.getQueue();
        this.properties = tool.getProperties();
    }

    @Override
    public void run() {

        ConsumerIterator<byte[], byte[]> iterator = stream.iterator();

        synchronized (this) {
            while (iterator != null && iterator.hasNext()) {
                try {
                    queue.put(new String(iterator.next().message()));
                } catch (InterruptedException e) {
                    logger.error("QUEUE PUT FAIL", e);
                }
            }
        }

        logger.info(String.format("Producer (%d) is done.", getId()));
    }

}
