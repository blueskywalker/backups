package org.blueskywalker.java.utils;

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

    public KafkaProducerService(Runnable[] runners) {
        super(runners);

    }

    public static abstract class KafkaProducerRunnable implements Runnable {

        public static final String PRODUCER_TOPIC = "producer.topic";
        protected final String topic;
        protected final Producer<String, String> producer;
        protected final ProducerConfig config;
        protected final Object option;

        public KafkaProducerRunnable(Properties properties) {
            this(properties,null);
        }

        public KafkaProducerRunnable(Properties properties,Object option) {
            this.option=option;
            topic = properties.getProperty(PRODUCER_TOPIC);
            if (topic == null) {
                throw new IllegalArgumentException(PRODUCER_TOPIC);
            }
            properties.put("client.id", KafkaProducerService.class.getName());
            properties.put("serializer.class", "kafka.serializer.StringEncoder");
            properties.put("partitioner.class", SimplePartitioner.class.getName());
            config = new ProducerConfig(properties);

            producer = new Producer<String, String>(config);
        }

        public void send(String msg) {
            producer.send(new KeyedMessage<String, String>(topic, "random", msg));
        }

        public Object getOption() {
            return option;
        }

    }
}
