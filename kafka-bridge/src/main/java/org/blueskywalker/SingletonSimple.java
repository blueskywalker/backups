package org.blueskywalker;

/**
 * Created by kkim on 4/9/16.
 */
public class SingletonSimple {

    static final SingletonSimple instance = new SingletonSimple();

    private SingletonSimple() {}

    public SingletonSimple getInstance() {
        return instance;
    }
}
