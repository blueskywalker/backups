package com.sysomos.grid.tools.backup;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

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
    public final static String HADOOP_CONF_DIR = "hadoop.conf.dir";


    // members
    private final Properties properties = new Properties();
    private final int threadCount;
    private final String topic;
    private final ConsumerConnector kafkaConsumer;
    private final BlockingQueue<String> queue;
    private final List<BackupProducer> producerGroup;
    private final List<BackupConsumer> consumerGroup;
    private final FileSystem fs;
    private boolean done;

    public BackupTool() throws IOException {

        InputStream is = getClass().getClassLoader().getResourceAsStream(PROPERTY_FILE);
        properties.load(is);

        topic = properties.getProperty(BACKUP_TOPIC, "backup");
        threadCount = Integer.valueOf(properties.getProperty(BACKUP_TOPIC_THREAD_COUNT, "1"));
        kafkaConsumer = Consumer.createJavaConsumerConnector(new ConsumerConfig(properties));
        int queueSize = Integer.valueOf(properties.getProperty(BACKUP_MESSAGE_QUEUE_SIZE, "10000"));
        queue = new ArrayBlockingQueue<String>(queueSize);
        // HDFS
        Configuration configuration = new Configuration();
        String baseDir = properties.getProperty(HADOOP_CONF_DIR, "/etc/hadoop/conf");
        configuration.addResource(new Path(String.format("%s/core-site.xml", baseDir)));
        configuration.addResource(new Path(String.format("%s/hdfs-site.xml", baseDir)));
        //configuration.setBoolean("fs.manual.shutdown",true); // not working
        configuration.setBoolean("fs.automatic.close", false);
        fs = FileSystem.get(configuration);
        consumerGroup = new ArrayList<BackupConsumer>();
        producerGroup = new ArrayList<BackupProducer>();
        done = false;
    }

    public void run() {


        logger.info("BackupTool is started");
        // setting
        Map<String, Integer> topicMap = new HashMap<String, Integer>();
        topicMap.put(topic, threadCount);

        Map<String, List<KafkaStream<byte[], byte[]>>> consumerStreamsMap = kafkaConsumer.createMessageStreams(topicMap);


        // producer
        for (final KafkaStream<byte[], byte[]> stream : consumerStreamsMap.get(topic)) {
            BackupProducer p = new BackupProducer(stream, this);
            producerGroup.add(p);
            p.start();
        }

        // consumer
        try {
            int consumerSize = Integer.valueOf(properties.getProperty(BACKUP_IO_THREAD_COUNT, "2"));
            for (int i = 0; i < consumerSize; i++) {
                consumerGroup.add(new BackupConsumer(this));
                consumerGroup.get(i).start();
            }
        } catch (IOException e) {
            logger.error("FILE IO ERROR", e);
        }


        while (!done) {
            try {
                synchronized (this) {
                    wait();
                }
            } catch (InterruptedException e) {
                logger.error("INTERRUPTED", e);
            }

        }

        logger.info("BackupTool is done");
    }


    public void shutdown() {
        logger.info("SHUTDOWN SIGNAL");
        try {

            for (BackupConsumer c : consumerGroup) {
                c.setDone();
            }
            logger.info("KAFKA SHUTDOWN");

            kafkaConsumer.shutdown();
            logger.info("Producer is waiting");
            for (BackupProducer p : producerGroup) {
                p.join();
            }
            logger.info("Consumer is closing");
            for (BackupConsumer c : consumerGroup) {
                c.join();
            }

            logger.info("FILESYSTEM CLOSE");
            fs.close();

            done = true;
            synchronized (this) {
                notify();
            }
        } catch (Exception e) {
            logger.error("BACKUP_TOOL", e);
        }
    }

    public Properties getProperties() {
        return properties;
    }

    public BlockingQueue<String> getQueue() {
        return queue;
    }

    public FileSystem getFs() {
        return fs;
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


        } catch (Exception e) {
            logger.error("BACKUP HAS STOPPED", e);
        }

        System.exit(0);
    }

}
