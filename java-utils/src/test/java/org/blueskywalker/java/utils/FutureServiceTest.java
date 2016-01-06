package org.blueskywalker.java.utils;

import org.junit.Test;

import java.util.concurrent.*;

import static org.junit.Assert.*;

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
        FutureService<String> service = new FutureService<>();
        service.execute(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "hello";
            }
        });
        service.execute(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "world";
            }
        });

        for(String result : service.getResults()) {
            System.out.println(result);
        }

        service.shutdownAndAwaitTermination();
    }
}