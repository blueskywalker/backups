package org.blueskywalker.java.utils.thread;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 *
 * @author kkim
 */
public class FutureWaitFor<T> extends FutureCompletionService<T> {

    final Collection<Callable<T>> works;

    public FutureWaitFor(Collection<Callable<T>> works) {
        super();
        this.works=works;
    }

    public void execute() {
        for(Callable<T> c: works) {
            completion.submit(c);
        }
    }

    public List<T> waitFor() throws InterruptedException, ExecutionException {
        ArrayList<T> result = new ArrayList<T>();

        while(result.size()< works.size()) {
            result.add((T) completion.poll().get());
        }

        return result;
    }
}
