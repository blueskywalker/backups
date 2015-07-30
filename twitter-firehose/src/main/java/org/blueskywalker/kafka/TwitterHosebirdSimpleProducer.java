package org.blueskywalker.kafka;

import java.util.Map;
import java.util.Properties;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import org.apache.log4j.Logger;

/**
 *
 * @author kkim
 */
public class TwitterHosebirdSimpleProducer {
    protected static Logger logger = Logger.getLogger(TwitterHosebirdSimpleProducer.class);
    private Producer<String,String> producer;
    private String topic;

    public TwitterHosebirdSimpleProducer(Map<String,String> kafka) {
        Properties properties = new Properties();
        topic = kafka.get("topic");
        properties.putAll(kafka);
        properties.put("client.id", "TwitterFirebirdProducer");
        properties.put("serializer.class", "kafka.serializer.StringEncoder");
        properties.put("partitioner.class", "com.mwired.grid.datasource.hosebird.kafka.TwitterSimplePartitioner");
        ProducerConfig conf = new ProducerConfig(properties);
        producer = new Producer<String,String>(conf);
    }

    public synchronized void send(String msg) {
        producer.send(new KeyedMessage<String,String>(topic,"round-robin",msg));
    }
}


