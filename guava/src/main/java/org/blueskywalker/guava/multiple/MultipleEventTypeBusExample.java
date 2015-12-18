package org.blueskywalker.guava.multiple;

import com.google.common.eventbus.EventBus;

/**
 * Created by kkim on 8/26/15.
 */
public class MultipleEventTypeBusExample {
    public static void main(String[] args) {
        EventBus eventBus = new EventBus();
        eventBus.register(new MultipleListeners());
        System.out.println("Post 'Multiple Listeners Example'");
        eventBus.post("Multiple Listeners Example");
        eventBus.post(1);
    }
}
