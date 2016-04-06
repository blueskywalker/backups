package org.blueskywalker.java.utils;

import org.blueskywalker.java.utils.thread.StreamThreadService;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by kkim on 1/2/16.
 */
public class StreamThreadServiceTest {
    StreamThreadService service;

    @Before
    public void setUp() throws Exception {

        StreamThreadService.StreamThreadRunner [] runners =
                new StreamThreadService.StreamThreadRunner [1];
        runners[0] = new StreamThreadService.StreamThreadRunner<String, String>() {
            @Override
            public void run() {
                System.out.println("here we are");
                setOutput(getInput());
            }
        };

        service = new StreamThreadService(runners);

    }

    @Test
    public void send() throws Exception {
        service.start();
        service.send("hello");
        Thread.sleep(10);
        System.out.println(service.receive());
        service.shutdown();
        service.join();

    }
}