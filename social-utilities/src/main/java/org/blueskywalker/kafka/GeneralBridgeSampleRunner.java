package org.blueskywalker.kafka;

/**
 * Created by kkim on 12/4/15.
 */


import org.blueskywalker.twitter.TweetDetector;
import org.blueskywalker.yaml.YamlType;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author kkim
 */
public class GeneralBridgeSampleRunner extends GeneralBridgeRunner {

    public GeneralBridgeSampleRunner() throws Exception {
        super();
    }

    @Override
    protected Runnable[] getRepeater(int n, Properties properties) {
        Runnable[] ret = new Runnable[n];
        for (int i = 0; i < n; i++) {
            ret[i] = new SamplingRepeater(properties,yaml.getYaml("sampling"));
        }
        return ret;
    }

    static class SamplingRepeater extends GeneralBridgeRepeator {

        final int rate;
        final boolean excludeCompliance;
        final AtomicInteger counter = new AtomicInteger();

        public SamplingRepeater(Properties properties, YamlType sample) {
            super(properties);
            rate = sample.getInteger("rate");
            excludeCompliance = sample.getBoolean("exclude.compliance");
        }

        @Override
        public void run() {
            RepeaterStatus status = (RepeaterStatus) getOption();

            if(excludeCompliance && TweetDetector.isCompliance(getMsg())) {
                return ;
            }

            if ((status.counter%rate) == 0) {
                producer.send(getMsg());
                counter.incrementAndGet();
            }

            status.increaseCounter();
            if (status.counter == MESSAGE_COUNT) {
                long diff = System.currentTimeMillis() - status.timestamp;
                double duration = (double)(diff / 1000.0);
                if (duration > 1.0) {
                    logger.info(String.format("MESSAGE %,d/%,d - %,.2f/sec",counter.get(), MESSAGE_COUNT,counter.get()/duration));
                } else {
                    logger.info(String.format("MESSAGE %,d/%,d - %,.2f/ms",counter.get(), MESSAGE_COUNT,counter.get()/((double)diff)));
                }
                status.setTimestamp(System.currentTimeMillis());
                counter.set(0);
                status.resetCounter();
            }
        }

    }

    public static void main(String[] args) {
        try {
            final GeneralBridgeSampleRunner bridge = new GeneralBridgeSampleRunner();

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
