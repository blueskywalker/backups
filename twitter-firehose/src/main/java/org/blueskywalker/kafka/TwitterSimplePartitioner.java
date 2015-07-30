package org.blueskywalker.kafka;

import kafka.producer.Partitioner;
import kafka.utils.VerifiableProperties;

/**
 *
 * @author kkim
 */
public class TwitterSimplePartitioner implements Partitioner {
    int index;

    public TwitterSimplePartitioner(VerifiableProperties prob) {
        index=0;
    }

    public int partition(Object o, int no) {
        index = (index<0)?0:index;
        return (index++%no);
    }

}
