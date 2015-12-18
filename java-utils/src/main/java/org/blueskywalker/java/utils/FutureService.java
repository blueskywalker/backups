package org.blueskywalker.java.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;

/**
 *
 * @author kkim
 */
public class FutureService<T> {
    public static final Logger logger = Logger.getLogger(FutureService.class);
    final ExecutorService service;
    final List<Future<T>> results;

    public FutureService() {
        service = Executors.newCachedThreadPool();
        results = Collections.synchronizedList(new LinkedList<Future<T>>());
    }

    public synchronized void addList(Future<T> f) {
        results.add(f);
    }

    public void execute(Callable<T> call) {
        Future<T> future = service.submit(call);
        addList(future);
    }

    public List<T> getResults() throws InterruptedException, ExecutionException {
        List<T> ret = new ArrayList<T>();

        for(Future f : results) {
            if(f.isDone()) {
                results.remove(f);
                ret.add((T)f.get());
            }
        }
        return ret;
    }

    public void shutdownAndAwaitTermination() {
        service.shutdown(); // Disable new tasks from being submitted
        try {
            // Wait a while for existing tasks to terminate
            if (!service.awaitTermination(60, TimeUnit.SECONDS)) {
                service.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!service.awaitTermination(60, TimeUnit.SECONDS)) {
                    logger.error("Pool did not terminate");
                }
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            service.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
    }
}
