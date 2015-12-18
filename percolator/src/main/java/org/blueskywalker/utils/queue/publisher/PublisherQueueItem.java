package org.blueskywalker.utils.queue.publisher;

import java.util.BitSet;

/**
 * Created by kkim on 8/5/15.
 */
public class PublisherQueueItem<T> implements Cloneable {
    final T payload;
    final long timestamp;
    final BitSet notRead;

    public PublisherQueueItem(T payload,int noSubscribers) {
        this(payload, System.currentTimeMillis(), noSubscribers);
    }

    private PublisherQueueItem(T payload,long timestamp,int noSubscribers ) {
        this.payload = payload;
        notRead = new BitSet(noSubscribers);
        notRead.set(0,notRead.length()-1);
        this.timestamp=timestamp;
    }
    public T getPayload() {
        return payload;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setReadFromSubscriber(PublisherQueue.SubscriberSession session) {

    }

    public boolean isReadAll() {
        return notRead.isEmpty();
    }

    public void setRead(int index) {
        notRead.set(index,false);
    }

    protected PublisherQueueItem<T>  clone() throws CloneNotSupportedException {
        return new PublisherQueueItem<T>(payload,timestamp,notRead.length());
    }
}
