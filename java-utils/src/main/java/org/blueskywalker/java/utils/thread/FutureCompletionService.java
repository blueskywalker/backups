package org.blueskywalker.java.utils.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.TimeUnit;


/**
 *
 * @author kkim
 */
public class FutureCompletionService<T> extends FutureService<T> {

    final ExecutorCompletionService completion;

    public FutureCompletionService() {
        super();
        completion = new ExecutorCompletionService(service);
    }

    @Override
    public void submit(Callable<T> work) {
        completion.submit(work);
    }

    public T waitAndGet() throws InterruptedException, ExecutionException {
        return (T) completion.poll().get();
    }

    public T waitAndGet(long timeout) throws InterruptedException {
        return (T) completion.poll(timeout, TimeUnit.MILLISECONDS);
    }
}

