package org.blueskywalker.java.utils;


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;

/**
 *
 * @author kkim
 */
public class ThreadQueue {

    public static Logger logger = Logger.getLogger(ThreadQueue.class);
    final BlockingQueue<Runnable> queue;
    final int size;
    final WorkerPoolThread[] threadPool;

    public ThreadQueue(int size) {
        this(size,Thread.NORM_PRIORITY);
    }

    public ThreadQueue(int size, int priority) {
        this.size = size;
        queue = new LinkedBlockingQueue<Runnable>(size);
        threadPool = new WorkerPoolThread[size];

        for (int i = 0; i < size; i++) {
            threadPool[i] = new WorkerPoolThread(queue);
            threadPool[i].setPriority(priority);
        }
    }

    public void start() {
        for(WorkerPoolThread t: threadPool) {
            if(t!=null) t.start();
        }
    }

    public static class WorkerPoolThread extends Thread {

        final BlockingQueue<Runnable> queue;
        boolean done;

        public WorkerPoolThread(final BlockingQueue<Runnable> queue) {
            this.queue = queue;
            done = false;
        }

        @Override
        public void run() {
            try {
                while (!done) {
                    Runnable runner = queue.poll(1, TimeUnit.MINUTES);
                    if (runner != null) {
                        runner.run();
                    }
                }
            } catch (InterruptedException ex) {
                logger.error(ex, ex);
            }
        }

        public synchronized void shutdown() {
            done = true;
        }
    }

    public synchronized void submit(Runnable r) throws InterruptedException {
        queue.put(r);
    }

    public void shutdown() {
        for (int i = 0; i < size; i++) {
            if (threadPool[i] != null) {
                threadPool[i].shutdown();
                threadPool[i] = null;
            }
        }

    }
}
