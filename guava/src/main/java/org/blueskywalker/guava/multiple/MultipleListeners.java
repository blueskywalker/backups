package org.blueskywalker.guava.multiple;

import com.google.common.eventbus.Subscribe;

/**
 * Created by kkim on 8/26/15.
 */
public class MultipleListeners {
    @Subscribe
    public void task1(String s) {
        System.out.println("do task1(" + s +")");
    }
    @Subscribe
    public void task2(String s) {
        System.out.println("do task2(" + s +")");
    }

    @Subscribe
    public void intTask(Integer i) {
        System.out.println("do intTask(" + i +")");
    }
}
