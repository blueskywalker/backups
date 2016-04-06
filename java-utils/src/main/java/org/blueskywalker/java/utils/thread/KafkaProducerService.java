/*
 *  Copyright 2015
 *  Created by Sysomos Grid
 *
 */
package org.blueskywalker.java.utils.thread;

import java.util.Properties;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.Partitioner;
import kafka.producer.ProducerConfig;
import kafka.utils.VerifiableProperties;

/**
 *
 * @author kkim
 */
public class KafkaProducerService extends ThreadService {

    public static final String PRODUCER_TOPIC = "producer.topic";

    public static class SimplePartitioner implements Partitioner {

        int index;

        public SimplePartitioner(VerifiableProperties props) {
            index = 0;
        }

        public synchronized int partition(Object key, int a_numPartitions) {

            if (index < 0) {
                index = 0;
            }
            return index++ % a_numPartitions;
        }
    }

    final protected KafkaProducer producer;

    public KafkaProducerService(Runnable[] runners, Properties properties) {
        super(runners, properties);
        producer = new KafkaProducer(properties);
    }

    public static class KafkaProducer {
        protected final String topic;
        protected final Producer<String, String> producer;
        protected final ProducerConfig config;

        public KafkaProducer(Properties properties) {
            topic = properties.getProperty(PRODUCER_TOPIC);
            if (topic == null) {
                throw new IllegalArgumentException(PRODUCER_TOPIC);
            }
            properties.put("client.id", KafkaProducerService.class.getName());
            properties.put("serializer.class", "kafka.serializer.StringEncoder");
            properties.put("partitioner.class", SimplePartitioner.class.getName());
            config = new ProducerConfig(properties);

            producer = new Producer<>(config);

        }

        public void send(String msg) {
            producer.send(new KeyedMessage<>(topic,"", msg));
        }
    }

    public KafkaProducer getProducer() {
        return producer;
    }

    public static abstract class KafkaProducerRunnable implements Runnable {
        final KafkaProducer producer;

        public KafkaProducerRunnable(KafkaProducer producer) {
            this.producer=producer;
        }

        public void send(String msg) {
            producer.send(msg);
        }
    }

}
