package com.sysomos.os.kafka.consumer;

import org.apache.log4j.Logger;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by kkim on 5/3/16.
 */
public class ThreadGroup {

    public static Logger logger = Logger.getLogger(ThreadGroup.class);
    protected final ServiceThread[] service;
    protected final Runnable [] runners;
    protected final Object param;


    public ThreadGroup(Runnable[] runners, int priority, Object param) {
        this.runners = runners;
        this.param=param;
        service = new ServiceThread[runners.length];
        setPriority(priority);
    }

    public ThreadGroup(Runnable[] runners, int priority) {
        this(runners,priority,null);
    }

    public ThreadGroup(Runnable[] runners) {
        this(runners,Thread.NORM_PRIORITY);
    }

    public ThreadGroup(Runnable[] runners, Object param) {
        this(runners,Thread.NORM_PRIORITY,param);
    }



    protected void setPriority(int priority) {
        for (int i = 0; i < service.length; i++) {
            service[i] = new ServiceThread(runners[i],null);
            service[i].setPriority(priority);
        }
    }

    public void start() {
        for (ServiceThread t : service) {
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

    public void shutdown() throws InterruptedException {
        for (int i = 0; i < service.length; i++) {
            if (service[i] != null) {
                service[i].shutdown();
                service[i].join();
                service[i] = null;
            }
        }
    }

    public static class ServiceThread extends Thread {

        protected final Runnable runner;
        protected AtomicBoolean done;
        protected Object param;

        public ServiceThread(Runnable runner,Object param) {
            this.param = param;
            this.runner = runner;
            done = new AtomicBoolean(false);
        }

        @Override
        public void run() {
            while (!done.get()) {
                runner.run();
            }
        }

        public void shutdown() {
            done.set(true);
        }

        public Object param() {
            return param;
        }
    }
}
