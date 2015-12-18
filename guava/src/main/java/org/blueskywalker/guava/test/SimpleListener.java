package org.blueskywalker.guava.test;

import com.google.common.eventbus.Subscribe;

/**
 * Created by kkim on 8/26/15.
 */
public class SimpleListener {
    @Subscribe
    public void task(String sub) {
        System.out.println("do task(" + sub + ")");
    }
}
