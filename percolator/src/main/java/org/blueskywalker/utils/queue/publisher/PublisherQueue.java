package org.blueskywalker.utils.queue.publisher;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by kkim on 8/5/15.
 */
public class PublisherQueue<E> extends ArrayQueue<PublisherQueueItem<E>> {

    static class SubscriberSession {
        final static AtomicInteger lastSeqenceId = new AtomicInteger(-1);
        final int sid;
        final int tid;
        int takeIndex;

        public SubscriberSession(int tid) {
            this.tid = tid;
            this.sid = lastSeqenceId.getAndIncrement();
        }

        void setTakeIndex(int takeIndex) {
            this.takeIndex=takeIndex;
        }
    }

    static class SubscriberGroup extends ConcurrentHashMap<Integer,SubscriberSession> {
        int maxTakeIndex=-1;
        void update(SubscriberSession session) {
            maxTakeIndex=Math.max(maxTakeIndex, session.takeIndex);
        }
    }

    final SubscriberGroup subscribers;

    public PublisherQueue(int capacity) {
        super(capacity);
        subscribers = new SubscriberGroup();
    }

    public void putItem (E payload) throws InterruptedException {
        super.put(new PublisherQueueItem<E>(payload));
    }

    public E takeItem(Thread t) {
        PublisherQueueItem<E> item = super.peek();
        if(item==null)
            return null;

        Publisher

    }

    @Override
    public PublisherQueueItem<E> take() throws InterruptedException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void put(PublisherQueueItem<E> ePublisherQueueItem) throws InterruptedException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    void removeAt(int i) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean offer(PublisherQueueItem<E> ePublisherQueueItem, long timeout, TimeUnit unit) throws InterruptedException {
        throw new UnsupportedOperationException();
    }

    @Override
    public PublisherQueueItem<E> poll(long timeout, TimeUnit unit) throws InterruptedException {
        throw new UnsupportedOperationException();
    }

    @Override
    public PublisherQueueItem<E> peek() {
        throw new UnsupportedOperationException();
    }

    @Override
    public PublisherQueueItem<E> remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(PublisherQueueItem<E> ePublisherQueueItem) {
        throw new UnsupportedOperationException();
    }
}
