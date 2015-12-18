package org.blueskywalker.utils.thread;


import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author kkim
 */
public class ThreadService {

    protected final ServiceThread [] service;
    protected final Runnable runner;


    public ThreadService(int size,Runnable runner) {
        this(size,Thread.NORM_PRIORITY,runner);
    }

    public ThreadService(int size,int priority,Runnable runner) {
        service = new ServiceThread[size];
        this.runner=runner;
        setService(priority);
    }

    protected void setService(int priority) {
        for(int i=0;i<service.length;i++) {
            service[i] = new ServiceThread(i,this.runner);
            service[i].setPriority(priority);
        }
    }

    public void start() {
        for(ServiceThread t: service) {
            if(t!=null)  t.start();
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
                assert runner != null;
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
            if(t!=null)  t.join();
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
