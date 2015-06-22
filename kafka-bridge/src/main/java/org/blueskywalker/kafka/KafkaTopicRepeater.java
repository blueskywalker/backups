package org.blueskywalker.kafka;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import org.apache.log4j.Logger;

import java.util.Properties;

/**
 * Created by kkim on 6/22/15.
 */
public class KafkaTopicRepeater extends Thread {
    static final Logger logger = Logger.getLogger(KafkaTopicRepeater.class);

    //
    final Properties properties;
    final KafkaStream<byte[], byte[]> stream;
    final Producer<String, String> producer;
    final String topic;

    public KafkaTopicRepeater(int id, final KafkaStream<byte[], byte[]> stream, final Properties properties) {
        super("KafkaTopicRepeater-" + String.valueOf(id));
        this.stream = stream;
        this.properties = properties;
        topic = properties.getProperty("destination.topic");
        if (topic == null) {
            throw new IllegalArgumentException("destination.topic");
        }

        properties.put("client.id", KafkaBridge.class.getName());
        properties.put("serializer.class", "kafka.serializer.StringEncoder");
        properties.put("partitioner.class", "org.blueskywalker.kafka.SimpleRoundRobinPartitioner");

        final ProducerConfig config = new ProducerConfig(properties);
        producer = new Producer<String, String>(config);

    }

    @Override
    public void run() {
        ConsumerIterator<byte[], byte[]> iterator = stream.iterator();

        while (iterator != null && iterator.hasNext()) {
            String msg = new String(iterator.next().message());
            producer.send(new KeyedMessage<String, String>(topic, "roundRobin", msg));

        }

        logger.info(String.format("Producer (%d) is done.", getId()));
    }
}
