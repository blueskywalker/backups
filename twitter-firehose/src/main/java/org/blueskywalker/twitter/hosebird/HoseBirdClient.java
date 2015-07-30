package org.blueskywalker.twitter.hosebird;

/**
 * Created by kkim on 7/29/15.
 */
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.RealTimeEnterpriseStreamingEndpoint;
import com.twitter.hbc.core.endpoint.StatusesFirehoseEndpoint;
import com.twitter.hbc.core.endpoint.StatusesSampleEndpoint;
import com.twitter.hbc.core.endpoint.StreamingEndpoint;
import com.twitter.hbc.core.processor.LineStringProcessor;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.BasicClient;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.BasicAuth;
import com.twitter.hbc.httpclient.auth.OAuth1;
import com.yammer.metrics.core.MetricsRegistry;
import com.yammer.metrics.reporting.CsvReporter;
import com.yammer.metrics.reporting.JmxReporter;
import java.io.File;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import org.blueskywalker.twitter.backup.TwitterStatusSaver;


public class HoseBirdClient implements  Runnable {

    public static final Logger logger = Logger.getLogger(HoseBirdClient.class);
    private static final MetricsRegistry metrics = new MetricsRegistry();
    public static final int INITIAL_QUEUE_SIZE = 1000;
    public static final String SYSOMOS_FETCHER = "sysomos-twitter-collector";
    private final BlockingQueue<String> queue;
    private BasicClient client;
    private final YamlType conf;
    private final TwitterStatusSaver backup;
    private final File reporter;
    private final HoseBirdListener listener;

    HoseBirdClient(YamlType properties) {
        queue = new LinkedBlockingQueue<String>(INITIAL_QUEUE_SIZE);
        this.conf = properties;
        backup = new TwitterStatusSaver(conf.getYaml("backup"), metrics);
        listener = new HoseBirdListener(this, backup);
        reporter = new File(conf.getYaml("metrics").getString("file"));
    }

    public void authentication() throws IllegalArgumentException {
        String target = (String) conf.get("endpoint");

        Authentication auth;

        if (target.equalsIgnoreCase("Enterprise")) {
            // get conf from yaml
            Map<String, String> properties = (Map<String, String>) conf.get(target);
            String username = properties.get("user.name");
            String password = properties.get("user.password");
            String account = properties.get("account");
            String label = properties.get("label");
            String product = properties.get("product");

            logger.info(String.format(
                    "\nuser.name=[%s]\nuser.password=[%s]\naccount=[%s]\nlabel=[%s]\nproduct=[%s]",
                    username, password, account, label, product));
            // authentication
            auth = new BasicAuth(username, password);
            RealTimeEnterpriseStreamingEndpoint endpoint = new RealTimeEnterpriseStreamingEndpoint(account, product, label);
            // set up client
            client = new ClientBuilder()
                    .name("sampleExampleClient")
                    .hosts(Constants.ENTERPRISE_STREAM_HOST)
                    .endpoint(endpoint)
                    .authentication(auth)
                    .processor(new LineStringProcessor(queue))
                    .build();
        } else {
            // get conf from yaml
            Map<String, String> properties = (Map<String, String>) conf.get(target);
            String consumerKey = properties.get("consumer.key");
            String consumerSecret = properties.get("consumer.secret");
            String token = properties.get("access.token");
            String secret = properties.get("access.secret");

            logger.info(String.format(
                    "\nconsumer.key=[%s]\nconsumer.secret=[%s]\naccess.token=[%s]\naccess.secret=[%s]",
                    consumerKey, consumerSecret, token, secret));
            // authentication
            auth = new OAuth1(consumerKey, consumerSecret, token, secret);

            StreamingEndpoint endpoint;
            Hosts hosts;
            if (target.equals("firehose")) {
                StatusesFirehoseEndpoint firehose = new StatusesFirehoseEndpoint();
                endpoint = firehose;
                hosts = new HttpHosts("https://partnerstream2.twitter.com");
            } else {
                if (!target.equals("sample")) {
                    throw new IllegalArgumentException(target);
                }
                StatusesSampleEndpoint samplePoint = new StatusesSampleEndpoint();
                samplePoint.stallWarnings(false);
                endpoint = samplePoint;
                hosts = HttpHosts.STREAM_HOST;
            }

            // set up client
            client = new ClientBuilder()
                    .name(SYSOMOS_FETCHER)
                    .hosts(hosts)
                    .endpoint(endpoint)
                    .authentication(auth)
                    .processor(new StringDelimitedProcessor(queue))
                    .build();

        }
        listener.setClient();
    }

    public BasicClient getClient() {
        return client;
    }

    public void run() {
        try {
            authentication();

            // Establish a connection
            client.connect();

            //ConsoleReporter.enable(1, TimeUnit.MINUTES);
            JmxReporter.startDefault(metrics);
            CsvReporter.enable(reporter, 1, TimeUnit.HOURS);

            listener.start();
            backup.start();

            listener.waitUntilDone();
            backup.waitUntilDone();

        } catch (Exception ex) {
            logger.error(ex, ex);
        } finally {
            client.stop();
        }
    }

    public BlockingQueue<String> getQueue() {
        return queue;
    }

    public YamlType getConf() {
        return conf;
    }

    public MetricsRegistry getMetrics() {
        return metrics;
    }

    public void shutdown() {
        listener.shutdown();
        backup.shutdown();
    }

}
