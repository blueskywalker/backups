package org.blueskywalker.java.utils;


import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author kkim
 */
public class ThreadServiceCounter {

    final CheckableThread[] service;

    public interface CheckableRunner extends Runnable {
        public void init();
        public int checkPoint();
        public boolean checkAndDo(int counter);
        public void finish();
    }

    public ThreadServiceCounter(int size, CheckableRunner runner) {
        this(size,Thread.NORM_PRIORITY,runner);
    }

    public ThreadServiceCounter(int size, int priority,CheckableRunner runner) {
        service = new CheckableThread[size];

        for (int i = 0; i < service.length; i++) {
            service[i] = new CheckableThread(runner);
            service[i].setPriority(priority);
        }
    }

    public ThreadServiceCounter(CheckableThread[] threads) {
        this.service = threads;
    }

    public void start() {
        for (CheckableThread t : service) {
            if(t !=null) t.start();
        }
    }

    public static class CheckableThread extends Thread {

        final AtomicBoolean done;
        final CheckableRunner runner;
        final int checkpoint;

        public CheckableThread(final CheckableRunner runner) {
            done = new AtomicBoolean(false);
            this.runner = runner;
            this.checkpoint = runner.checkPoint();
        }

        @Override
        public void run() {
            int counter = 0;
            runner.init();
            try {
                while (!done.get()) {
                    if(!runner.checkAndDo(counter))
                        break;
                    runner.run();
                    counter++;
                    if (counter == checkpoint) {
                        counter = 0;
                    }
                }
            } finally {
                runner.finish();
            }
        }

        public void shutdown() {
            done.set(true);
            runner.finish();
        }

    }

    public void join() throws InterruptedException {
        for (Thread t : service) {
            if(t!= null) t.join();
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
}
