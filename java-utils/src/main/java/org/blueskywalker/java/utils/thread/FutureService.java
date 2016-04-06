package org.blueskywalker.java.utils.thread;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;

/**
 *
 * @author kkim
 * @param <T>
 */
public class FutureService<T> {

    public static final Logger LOGGER = Logger.getLogger(FutureService.class);
    protected final ExecutorService service;
    protected final CopyOnWriteArrayList<Future<T>> results;


    public FutureService() {
        service = Executors.newCachedThreadPool();
        results = new CopyOnWriteArrayList<>();
    }


    public void submit(Callable<T> work) {
        results.add(service.submit(work));
    }

    public List<T> getResults() throws InterruptedException, ExecutionException {
        List<T> ret = new ArrayList<>();
        Iterator<Future<T>> iterator = results.iterator();
        while (iterator.hasNext()) {
            Future<T> f = iterator.next();
            if (f.isDone()) {
                results.remove(f);
                ret.add((T) f.get());
            }
        }
        return ret;
    }

    public int size() {
        return results.size();
    }

    public void shutdownAndAwaitTermination() {
        service.shutdown(); // Disable new tasks from being submitted
        try {
            // Wait a while for existing tasks to terminate
            if (!service.awaitTermination(60, TimeUnit.SECONDS)) {
                service.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!service.awaitTermination(60, TimeUnit.SECONDS)) {
                    LOGGER.error("Pool did not terminate");
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
