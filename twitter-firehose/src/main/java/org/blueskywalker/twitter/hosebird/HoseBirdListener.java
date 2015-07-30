package org.blueskywalker.twitter.hosebird;


import com.twitter.hbc.httpclient.BasicClient;
import com.yammer.metrics.core.Gauge;
import com.yammer.metrics.core.Meter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.log4j.Logger;
import org.blueskywalker.kafka.TwitterHosebirdSimpleProducer;
import org.blueskywalker.threads.ThreadService;
import org.blueskywalker.twitter.backup.TwitterStatusSaver;

/**
 *
 * @author kkim
 */
public class HoseBirdListener implements Runnable {

    public static final Logger logger = Logger.getLogger(HoseBirdListener.class);
    private final ThreadService services;
    private final HoseBirdClient hosebird;
    private final int timeout;
    private BasicClient client;
    private final BlockingQueue<String> queue;
    private final TwitterHosebirdSimpleProducer producer;
    private final TwitterStatusSaver backup;
    private final AtomicLong counter;
    private final Meter tweetCounter;


    public HoseBirdListener(HoseBirdClient hosebird, TwitterStatusSaver backup) {
        this.hosebird = hosebird;
        this.backup = backup;
        YamlType listener = hosebird.getConf().getYaml("listener");
        this.timeout = listener.getInteger("timeout");
        int threadCnt = listener.getInteger("no.thread");
        services = new ThreadService(threadCnt, this);
        counter = new AtomicLong(0);
        queue = hosebird.getQueue();
        producer = new TwitterHosebirdSimpleProducer(hosebird.getConf().getYaml("kafka").getStringMap() );
        tweetCounter = hosebird.getMetrics().newMeter(HoseBirdClient.class,"message-rate", "perSecond", TimeUnit.SECONDS);
        hosebird.getMetrics().newGauge(HoseBirdClient.class, "messages",
                new Gauge<Long>() {

                    @Override
                    public Long value() {
                        return counter.get();
                    }
                });

        hosebird.getMetrics().newGauge(HoseBirdClient.class, "event.queue.size",
                new Gauge<Integer>() {

                    @Override
                    public Integer value() {
                        return queue.size();
                    }
                });
    }

    public void start() {
        services.start();
    }

    public void setClient() {
        client = hosebird.getClient();
    }

    public void run() {
        try {
            if (client.isDone()) {
                logger.error("Client connection closed unexpectedly: " + client.getExitEvent().getMessage());
                Runtime.getRuntime().exit(-1);
            }
            String msg = queue.poll(timeout, TimeUnit.SECONDS);
            if (msg == null) {
                logger.info(String.format("Did not receive a message in %s seconds",timeout));
            } else {
                //System.out.print(msg);
                backup.write(msg);
                producer.send(msg);
                tweetCounter.mark();

                // metrics
                long cntMsg=counter.incrementAndGet();
                if(cntMsg<0) { counter.set(0); cntMsg=0; }
                if((cntMsg%10000)==0) {
                    logger.info("CURRENT COLLECTED MESSAGES : " + String.valueOf(cntMsg));
                }

            }
        } catch (Exception ex) {
            logger.error(ex,ex);
            Runtime.getRuntime().exit(-1);
        }
    }

    public void waitUntilDone() throws InterruptedException {
        services.join();
    }

    public void shutdown() {
        services.shutdown();
    }
}
