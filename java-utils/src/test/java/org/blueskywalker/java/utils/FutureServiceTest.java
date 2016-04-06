package org.blueskywalker.java.utils;

import org.blueskywalker.java.utils.thread.FutureService;
import org.junit.Test;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;

/**
 * Created by kkim on 1/2/16.
 */
public class FutureServiceTest {

    @Test
    public void testBasicFuture() {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("Asynchronous Task");
            }
        });
        Future future = service.submit(new Callable() {
            @Override
            public Object call() throws Exception {
                System.out.println("Asynchronous Call");
                return "Callable result";
            }
        });

        try {
            System.out.println("future.get() = " + future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFutureService () throws ExecutionException, InterruptedException {
        final FutureService<String> service = new FutureService<>();
        service.submit(()->{return "hello";});
        service.submit(()->{return "world";});

        for(String result : service.getResults()) {
            System.out.println(result);
        }


        service.shutdownAndAwaitTermination();
    }
}