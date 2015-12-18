package org.blueskywalker.utils.queue.publisher;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by kkim on 8/5/15.
 */
public class PublisherQueue<E> extends ArrayQueue<PublisherQueueItem<E>> {

    static class SubscriberSession {
        final static AtomicInteger lastSeqenceId = new AtomicInteger(-1);
        final int sid;
        final Thread tid;
        int takeIndex;

        public SubscriberSession(Thread t) {
            this.tid = t;
            this.sid = lastSeqenceId.getAndIncrement();
            takeIndex=0;
        }

    }

    static class SubscriberGroup extends ConcurrentHashMap<Thread,SubscriberSession> {
        int maxTakeIndex=-1;
        void update(SubscriberSession session) {
            maxTakeIndex=Math.max(maxTakeIndex, session.takeIndex);
        }
    }

    final SubscriberGroup subscribers;

    public PublisherQueue(int capacity,Thread ... threads) {
        super(capacity);
        subscribers = new SubscriberGroup();
        for (Thread t : threads) {
            subscribers.put(t,new SubscriberSession(t));
        }
    }

    public void putItem (E payload) throws InterruptedException {
        super.put(new PublisherQueueItem<E>(payload,subscribers.size()));
    }

    public E takeItem(Thread t) throws InterruptedException {

        SubscriberSession session = subscribers.get(t.getId());
        if(session==null)
            return null;

        final ReentrantLock lock = this.lock;
        lock.lockInterruptibly();
        try {
            while (count == 0)
                notEmpty.await();
            PublisherQueueItem<E> item = super.itemAt(session.takeIndex);
            if(item==null)
                return null;

            item.setRead(session.sid);
            if(item.isReadAll()) {
                extract();
            }
            session.takeIndex=inc(session.takeIndex);
            return item.getPayload();
        } finally {
            lock.unlock();
        }
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
