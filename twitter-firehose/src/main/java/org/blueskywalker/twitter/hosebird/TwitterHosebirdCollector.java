package org.blueskywalker.twitter.hosebird;

import java.io.InputStream;
import org.apache.log4j.Logger;
import org.blueskywalker.zookeeper.ZKElectableClient;
import org.yaml.snakeyaml.Yaml;

/**
 *
 * @author kkim
 */
public class TwitterHosebirdCollector extends ZKElectableClient {

    public static final Logger logger = Logger.getLogger(TwitterHosebirdCollector.class);
    public static final String CONF_FILE = "twitter-hosebird.yaml";

    public static void main(String[] args) {
        final Yaml yaml = new Yaml();
        InputStream is
                = TwitterHosebirdCollector.class.getClassLoader().getResourceAsStream(CONF_FILE);

        final YamlType properties = new YamlType(yaml.load(is));

        try {
            final TwitterHosebirdCollector collector = new TwitterHosebirdCollector(properties);

            Runtime.getRuntime().addShutdownHook(new Thread() {

                @Override
                public void run() {
                    collector.shutdown();
                }

            });
            collector.run();

        } catch (Exception e) {
            logger.error(e, e);
        }

        logger.info("Twitter Collector has been DONE");
    }

    private final HoseBirdClient hosebird;
    boolean isFirstRun = true;
    boolean wasLeader = false;

    /**
     *
     * @param properties
     */
    public TwitterHosebirdCollector(YamlType properties) throws Exception {
        super(properties);
        hosebird = new HoseBirdClient(properties);

    }

    protected void performRole() {

        if (isFirstRun || wasLeader != getCachedIsLeader()) {
            if(getCachedIsLeader()) {
                logger.info("TWITTER COLLECTOR START");
                hosebird.run();
            } else {
                logger.info("WAIT TO BE A LEADER");
            }
        } else {
            logger.info("ZooTestElectableClient::performRole:: work  was not performed (" + getElectionGUIDZNodePath() + ")");
        }

        isFirstRun = false;
        wasLeader = getCachedIsLeader();
    }

    void shutdown() {
        logger.info("STARTING SHUTDOWN");
        hosebird.shutdown();
        logger.info("END SHUTDOWN");
    }
}
