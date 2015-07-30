package org.blueskywalker.threads;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author kkim
 */
public class ThreadService {

    protected final ServiceThread [] service;
    protected final Runnable runner;

    public ThreadService(int size,Runnable runner) {
        service = new ServiceThread[size];
        this.runner=runner;
        setService();
    }

    protected void setService() {
        for(int i=0;i<service.length;i++) {
            service[i] = new ServiceThread(i,this.runner);
        }
    }

    public void start() {
        for(ServiceThread t: service) {
            t.start();
        }
    }

    public static class ServiceThread extends Thread {
        private final Runnable runner;
        private AtomicBoolean done;
        private int id;

        public ServiceThread(int id,Runnable runner) {
            this.id=id;
            this.runner=runner;
            done=new AtomicBoolean(false);
        }

        @Override
        public void run() {
            while(!done.get()) {
                runner.run();
            }
        }

        public void shutdown() {
            done.set(true);
        }

        public int id() {
            return id;
        }
    }

    public void join() throws InterruptedException {
        for(Thread t : service) {
            t.join();
        }
    }

    public void shutdown() {
        for(int i=0;i<service.length;i++) {
            if(service[i]!=null) {
                service[i].shutdown();
                service[i]=null;
            }
        }
    }

}
