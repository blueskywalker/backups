package org.blueskywalker.kafka;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by kkim on 6/22/15.
 */
public class KafkaBridge implements  Runnable {
    static final Logger logger = Logger.getLogger(KafkaBridge.class);
    //constants
    static final String PROPERTY_FILE="kafka-bridge.properties";
    //members
    final Properties properties;
    final ConsumerConnector consumer;
    final String topic;
    final int threadCount;
    final Thread[] producers;

    public KafkaBridge() throws IOException {
        properties = new Properties();
        InputStream is = getClass().getClassLoader().getResourceAsStream(PROPERTY_FILE);
        properties.load(is);

        topic = properties.getProperty("source.topic");
        if (topic == null) {
            throw new IllegalArgumentException("source.topic");
        }
        threadCount = Integer.valueOf(properties.getProperty("thread.count", "1"));
        consumer = Consumer.createJavaConsumerConnector(new ConsumerConfig(properties));
        producers = new Thread[threadCount];
    }


    public void run() {
        logger.info("Kafka-Bridge has started");
        // setting
        Map<String, Integer> topicMap = new HashMap<String, Integer>();
        topicMap.put(topic, threadCount);

        Map<String, List<KafkaStream<byte[], byte[]>>> consumerStreamsMap = consumer.createMessageStreams(topicMap);

        int index=0;
        // producer
        for (final KafkaStream<byte[], byte[]> stream : consumerStreamsMap.get(topic)) {
            producers[index]=new KafkaTopicRepeater(index,stream,properties);
            producers[index].start();
            index++;

        }

        try {
            Thread.sleep(60000);
            join();
        } catch (InterruptedException e) {
            logger.error(e,e);
        }

        logger.info("Kafka-Bridge has finished");
    }

    public void join() throws InterruptedException {
        for(Thread t : producers) {
            t.join();
        }
    }

    public void shutdown() {
        consumer.shutdown();
    }


    public static void main(String[] args) {

        try {
            final KafkaBridge bridge = new KafkaBridge();

            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    bridge.shutdown();
                }
            });

            bridge.run();

        } catch (IOException e) {
            logger.error(e,e);
        }
    }
}
