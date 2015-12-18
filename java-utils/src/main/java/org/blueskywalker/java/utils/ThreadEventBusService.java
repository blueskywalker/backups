package org.blueskywalker.java.utils;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.log4j.Logger;

/**
 *
 * @author kkim
 */
public class ThreadEventBusService {

    public static Logger logger = Logger.getLogger(ThreadService.class);
    protected final EventBusThread[] service;
    protected final Runnable[] runners;
    protected final EventBus ebus;

    /**
     *
     */
    public static abstract class EventBustListener implements Runnable {

        Object post;

        public void setPost(Object post) {
            this.post = post;
        }

        public Object getPost() {
            return post;
        }

        abstract public void run();
    }

    public ThreadEventBusService(EventBustListener[] runners, int priority) {
        this.runners = runners;
        service = new EventBusThread[runners.length];
        ebus = new EventBus();
        setPriority(priority);

    }

    public void post(Object data) {
        ebus.post(data);
    }

    protected void setPriority(int priority) {
        for (int i = 0; i < service.length; i++) {
            service[i] = new EventBusThread((EventBustListener) runners[i], ebus);
            service[i].setPriority(priority);
            ebus.register(service[i]);
        }
    }

    public void start() {
        for (EventBusThread t : service) {
            if (t != null) {
                t.start();
            }
        }
    }

    public void join() throws InterruptedException {
        for (Thread t : service) {
            if (t != null) {
                t.join();
            }
        }
    }

    public void shutdown() {
        for (int i = 0; i < service.length; i++) {
            if (service[i] != null) {
                service[i].shutdown();
                service[i] = null;
            }
        }
    }

    public static class EventBusThread extends Thread {

        protected final EventBustListener runner;
        protected AtomicBoolean done;
        protected final Object param;
        protected Object post;

        public EventBusThread(EventBustListener runner, Object param) {
            this.param = param;
            this.runner = runner;
            done = new AtomicBoolean(false);
            post = null;
        }

        @Override
        public void run() {
            while (!done.get()) {
                while (post == null) {
                    synchronized (this) {
                        try {
                            wait(5000);
                            continue;
                        } catch (InterruptedException ex) {
                            break;
                        }
                    }
                }
                if (post != null) {
                    runner.setPost(post);
                    runner.run();
                }
            }
        }

        @Subscribe
        public synchronized void task(Object post) {
            this.post = post;
            notifyAll();
        }

        public Object getPost() {
            return post;
        }

        public void shutdown() {
            done.set(true);
        }

    }

}
