package org.blueskywalker;

/**
 * Created by kkim on 3/30/16.
 */
public class Singleton {

    private static Singleton instance;

    private Singleton() {

    }

    public synchronized Singleton instance() {
        if (instance==null) {
            instance = new Singleton();
            return instance;
        }

        return instance;
    }

}
