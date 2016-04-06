package org.blueskywalker.java.utils.thread;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

/**
 *
 * @author kkim
 */
public class KafkaConsumerService extends ThreadService {

    public static final String CONSUMER_TOPIC = "consumer.topic";
    final ConsumerConnector kafkaConsumer;
    final Map<String, List<KafkaStream<byte[], byte[]>>> consumerStreamsMap;
    final String topic;

    public KafkaConsumerService(Runnable[] runners, Properties properties) throws IllegalArgumentException {
        this(runners,properties,null);
    }

    public KafkaConsumerService(Runnable[] runners, Properties properties,Object param) throws  IllegalArgumentException {
        super(runners,param);

        topic = (String) properties.get(CONSUMER_TOPIC);
        if(topic==null) throw new IllegalArgumentException(topic);
        kafkaConsumer = Consumer.createJavaConsumerConnector(new ConsumerConfig(properties));
        Map<String, Integer> topicMap = new HashMap<String, Integer>();
        topicMap.put(topic, runners.length);
        consumerStreamsMap = kafkaConsumer.createMessageStreams(topicMap);
        int counter = 0;
        for (final KafkaStream<byte[], byte[]> stream : consumerStreamsMap.get(topic)) {
            service[counter] = new KafkaConsumerThread(runners[counter], stream);
            service[counter].setPriority(Thread.NORM_PRIORITY);
            counter++;
        }
    }


    public static abstract class KafkaRunnable implements Runnable {
        final Object option;
        String msg;

        public KafkaRunnable() {
            this(null);
        }

        public KafkaRunnable(Object option) {
            this.option = option;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public Object getOption() {
            return option;
        }
    }

    public static class KafkaConsumerThread extends ServiceThread {

        final KafkaStream<byte[], byte[]> stream;

        public KafkaConsumerThread(Runnable runner, Object param) {
            super(runner, param);
            stream = (KafkaStream<byte[], byte[]>) param;
        }

        @Override
        public void run() {
            ConsumerIterator<byte[], byte[]> iterator = stream.iterator();
            KafkaRunnable kafkaRunner = (KafkaRunnable) runner;
            while (iterator != null && iterator.hasNext() && !done.get()) {
                kafkaRunner.setMsg(new String(iterator.next().message()));
                kafkaRunner.run();
            }
        }

    }
}
