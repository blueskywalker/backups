package com.sysomos.grid.tools.backup.twitter;

import com.sysomos.grid.tools.backup.BackupProducer;
import com.sysomos.grid.tools.backup.BackupTool;
import kafka.consumer.KafkaStream;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


/************
 * * * *
 * *
 * Created by kkim on 5/22/15.
 */
public class TwitterBackupTool extends BackupTool  {
    private final BlockingQueue<String> compliance;

    public TwitterBackupTool() throws IOException {
        super();
        int queueSize = Integer.valueOf(properties.getProperty(BACKUP_MESSAGE_QUEUE_SIZE, "10000"));
        compliance= new ArrayBlockingQueue<String>(queueSize);
    }

    public BlockingQueue<String> getCompliance() {
        return compliance;
    }

    @Override
    public void run() {
        logger.info("BackupTool is started");
        // setting
        Map<String, Integer> topicMap = new HashMap<String, Integer>();
        topicMap.put(topic, threadCount);

        Map<String, List<KafkaStream<byte[], byte[]>>> consumerStreamsMap = kafkaConsumer.createMessageStreams(topicMap);


        // producer
        for (final KafkaStream<byte[], byte[]> stream : consumerStreamsMap.get(topic)) {
            BackupProducer p = new TwitterProducer(stream, this);
            producerGroup.add(p);
            p.start();
        }

        // consumer
        try {
            int consumerSize = Integer.valueOf(properties.getProperty(BACKUP_IO_THREAD_COUNT, "2"));
            for (int i = 0; i < consumerSize; i++) {
                consumerGroup.add(new TwitterStatusConsumer(this));
                consumerGroup.get(i).start();
            }

            int complianceSize = consumerSize/2;

            if(complianceSize==0) {
                complianceSize = (consumerSize==0)? 0 : 1;
            }

            for(int i=0;i<complianceSize;i++) {
                consumerGroup.add(new TwitterComplianceConsumer(this));
                consumerGroup.get(i+consumerSize).start();
            }

        } catch (IOException e) {
            logger.error("FILE IO ERROR", e);
        }


        while (!done) {
            try {
                synchronized (this) {
                    wait();
                }
            } catch (InterruptedException e) {
                logger.error("INTERRUPTED", e);
            }

        }

        logger.info("BackupTool is done");
    }

    public static void main(String[] args) {

        try {
            final TwitterBackupTool backupTool = new TwitterBackupTool();

            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    backupTool.shutdown();
                }
            });

            backupTool.run();

            logger.info("BACKUP HAS DONE");


        } catch (Exception e) {
            logger.error("BACKUP HAS STOPPED", e);
        }

        System.exit(0);
    }
}
