package com.sysomos.grid.tools.backup;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/************
 * * * *
 * *
 * Created by kkim on 5/22/15.
 */

public class BackupTool implements Runnable {
    protected static final Logger logger = Logger.getLogger(BackupTool.class);

    // CONSTANTS
    public static final String PROPERTY_FILE = "backup.properties";
    public static final String BACKUP_TOPIC = "backup.topic";
    public static final String BACKUP_TOPIC_THREAD_COUNT = "backup.producer.count";
    public static final String BACKUP_MESSAGE_QUEUE_SIZE = "backup.message.queue.size";
    public static final String BACKUP_IO_THREAD_COUNT = "backup.consumer.count";

    // members
    private final Properties properties = new Properties();
    private final int threadCount;
    private final String topic;
    private final ConsumerConnector consumer;
    private final ExecutorService executor;
    private final ArrayBlockingQueue<String> queue;
    private final List<Future> tasks;
    private final List<BackupConsumer> consumerGroup;

    public BackupTool() throws IOException {

        InputStream is = getClass().getClassLoader().getResourceAsStream(PROPERTY_FILE);
        properties.load(is);

        topic = properties.getProperty(BACKUP_TOPIC, "backup");
        threadCount = Integer.valueOf(properties.getProperty(BACKUP_TOPIC_THREAD_COUNT, "1"));
        consumer = Consumer.createJavaConsumerConnector(new ConsumerConfig(properties));
        executor = Executors.newFixedThreadPool(threadCount);
        int queueSize = Integer.valueOf(properties.getProperty(BACKUP_MESSAGE_QUEUE_SIZE, "10000"));
        queue = new ArrayBlockingQueue<String>(queueSize);
        tasks = new ArrayList<Future>();
        consumerGroup = new ArrayList<BackupConsumer>();

    }

    public void run() {

        // setting
        Map<String, Integer> topicMap = new HashMap<String, Integer>();
        topicMap.put(topic, threadCount);

        Map<String, List<KafkaStream<byte[], byte[]>>> consumerStreamsMap = consumer.createMessageStreams(topicMap);

        int counter = 0;

        // producer
        for (final KafkaStream<byte[], byte[]> stream : consumerStreamsMap.get(topic)) {
            tasks.add(executor.submit(new BackupProducer(counter, stream, queue, properties)));
            counter++;
        }

        // consumer
        try {
            int consumerSize = Integer.valueOf(properties.getProperty(BACKUP_IO_THREAD_COUNT, "2"));
            for (int i = 0; i < consumerSize; i++) {
                consumerGroup.add(new BackupConsumer(queue, properties));
                consumerGroup.get(i).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(120000);

        } catch (InterruptedException ex) {
            logger.warn("WAIT FAIL", ex);
        }

        join();

    }

    protected void join()  {
        boolean allDone = true;
        do {
            allDone = true;
            for (Future f : tasks) {
                if (!f.isCancelled() && !f.isDone()) {
                    allDone = false;
                    break;
                }
            }
        } while (!allDone);
    }

    public void shutdown() {
        logger.info("Shutdown start");
        for(BackupConsumer c : consumerGroup) {
            try {
                c.shutdown();
            } catch (InterruptedException e) {
                logger.error(String.format("Consumer (%d) is Failed",c.getId()),e);
            }
        }

        consumer.shutdown();
        executor.shutdown();

        logger.info("shutdown done");
    }

    public static void main(String[] args) {

        try {
            final BackupTool backupTool = new BackupTool();

            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    backupTool.shutdown();
                }
            });
            backupTool.run();

            logger.info("BACKUP HAS DONE");
            System.exit(0);

        } catch (Exception e) {
            logger.error("BACKUP HAS STOPPED", e);
        }
    }

}
