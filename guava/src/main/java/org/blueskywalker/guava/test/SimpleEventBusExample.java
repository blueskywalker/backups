package org.blueskywalker.guava.test;

import com.google.common.eventbus.EventBus;

/**
 * Created by kkim on 8/26/15.
 */
public class SimpleEventBusExample {
    public static void main(String[] args) {
        EventBus eventBus = new EventBus();
        eventBus.register(new SimpleListener());
        System.out.println("Post Simple EventBus Example");
        eventBus.post("Simple EventBus Example");
    }
}
