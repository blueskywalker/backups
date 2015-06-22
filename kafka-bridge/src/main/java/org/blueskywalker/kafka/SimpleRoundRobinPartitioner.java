package org.blueskywalker.kafka;

import kafka.producer.Partitioner;
import kafka.utils.VerifiableProperties;

/**
 * Created by kkim on 6/22/15.
 */
public class SimpleRoundRobinPartitioner implements Partitioner {
    private int index;

    public SimpleRoundRobinPartitioner(VerifiableProperties props) {
        index=0;

    }

    public int partition(Object key, int numPartitions) {

        if(index < 0)
            index=0;

        return index++ % numPartitions;
    }
}
