package com.sysomos.grid.tools.backup;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import org.apache.log4j.Logger;

import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by kkim on 5/22/15.
 */
public class BackupProducer implements Runnable {
    public static Logger logger = Logger.getLogger(BackupProducer.class);
    final int id;
    final KafkaStream<byte[], byte[]> stream;
    final Properties properties;
    final ArrayBlockingQueue<String> queue;


    public BackupProducer(int id, KafkaStream<byte[], byte[]> stream, final ArrayBlockingQueue<String> queue, Properties properties) {
        this.id = id;
        this.stream = stream;
        this.queue=queue;
        this.properties = properties;
    }

    public void run() {

        ConsumerIterator<byte[], byte[]> iterator = stream.iterator();

        while (iterator != null && iterator.hasNext()) {
            try {
                queue.put(new String(iterator.next().message()));
            } catch (InterruptedException e) {
                logger.error("QUEUE PUT FAIL",e);
            }
        }

        logger.info(String.format("JOB(%d) is done.", id));
    }

}
