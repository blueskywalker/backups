package org.blueskywalker.kafka;

/**
 * Created by kkim on 12/4/15.
 */


import java.io.InputStream;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.blueskywalker.thread.KafkaConsumerService;
import org.blueskywalker.thread.KafkaProducerService;
import org.blueskywalker.yaml.YamlType;

/*
 *  Copyright 2015
 *  Created by Sysomos Grid
 *
 */
/**
 *
 * @author kkim
 */
public class GeneralBridgeRunner {

    static final Logger logger = Logger.getLogger(GeneralBridgeRunner.class);
    final KafkaConsumerService kafkaConsumer;
    final YamlType yaml;


    public GeneralBridgeRunner() throws Exception {
        InputStream is = getClass().getClassLoader().getResourceAsStream("bridge.yml");
        if (is==null) {
            throw new Exception("bridge.yml not exits");
        }

        yaml = new YamlType(is);
        Properties from = new Properties();
        Properties to = new Properties();
        from.putAll(yaml.getYaml("from").getStringMap());
        to.putAll(yaml.getYaml("to").getStringMap());
        Runnable [] runners = getRepeater(Integer.valueOf(from.getProperty("no.thread")), to);
        kafkaConsumer = new KafkaConsumerService(runners, from);
    }

    static class RepeaterStatus {
        int     counter;
        long  timestamp;

        public RepeaterStatus(int counter, long timestamp) {
            this.counter = counter;
            this.timestamp = timestamp;
        }

        public void increaseCounter() {
            counter++;
        }

        public int getCounter() {
            return counter;
        }

        public void resetCounter() {
            counter = 0;
        }

        public double getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

    }

    public static class KafkaProducer extends KafkaProducerService.KafkaProducerRunnable {

        public KafkaProducer(Properties properties) {
            super(properties);
        }

        public void run() {
            ; /* nothing for run */
        }

    }

    static protected class GeneralBridgeRepeator extends KafkaConsumerService.KafkaRunnable {

        final static int MESSAGE_COUNT = 100000;
        final KafkaProducer producer;

        public GeneralBridgeRepeator(Properties properties) {
            super(new RepeaterStatus(0,System.currentTimeMillis()));
            producer = new KafkaProducer(properties);
        }

        public void run() {
            producer.send(getMsg());
            RepeaterStatus status = (RepeaterStatus)getOption();
            status.increaseCounter();
            if (status.counter == MESSAGE_COUNT) {
                long  diff = System.currentTimeMillis()-status.timestamp;
                double duration = diff/1000.0;

                if(duration < 1.0) {
                    logger.info(String.format("MESSAGE %,d - %,.2f/ms",MESSAGE_COUNT,MESSAGE_COUNT/((double)diff)));
                } else {
                    logger.info(String.format("MESSAGE %,d - %,.2f/sec",MESSAGE_COUNT,MESSAGE_COUNT/duration));
                }
                status.setTimestamp(System.currentTimeMillis());
                status.resetCounter();
            }
        }
    }

    protected Runnable[] getRepeater(int n,Properties properties) {
        Runnable [] ret = new Runnable[n];
        for(int i=0;i<n;i++) {
            ret[i] = new GeneralBridgeRepeator(properties);
        }
        return ret;
    }

    public void run() throws InterruptedException {
        kafkaConsumer.start();
        Thread.sleep(10000);
        kafkaConsumer.join();
    }

    public void shutdown() {
        kafkaConsumer.shutdown();
    }


    public static void main(String[] args) {
        try {
            final GeneralBridgeRunner bridge = new GeneralBridgeRunner();

            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    bridge.shutdown();
                }
            });

            bridge.run();

            logger.info("Bridge Process is done.");
            System.exit(0);

        } catch (Exception e) {
            logger.error(e, e);
        }

    }
}
