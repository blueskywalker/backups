package com.sysomos.grid.tools.backup;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import org.apache.log4j.Logger;

import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by kkim on 5/22/15.
 */
public class BackupProducer extends Thread {
    public static Logger logger = Logger.getLogger(BackupProducer.class);

    final KafkaStream<byte[], byte[]> stream;
    final BlockingQueue<String> queue;

    public BackupProducer(final KafkaStream<byte[], byte[]> stream, final BackupTool tool) {
        this.stream = stream;
        this.queue = tool.getQueue();

    }

    @Override
    public void run() {

        ConsumerIterator<byte[], byte[]> iterator = stream.iterator();

        while (iterator != null && iterator.hasNext()) {
            try {
                queue.put(new String(iterator.next().message()));
            } catch (InterruptedException e) {
                logger.error("QUEUE PUT FAIL", e);
            }
        }

        logger.info(String.format("Producer (%d) is done.", getId()));
    }

}
