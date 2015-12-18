package org.blueskywalker.lucene;

import java.io.Serializable;

/**
 * Created by kkim on 8/6/15.
 */
public class PercolatorId implements Serializable {

    private static final long serialVersionUID = 8494933232420221341L;

    String id;

    public PercolatorId(String id) {
        this.id = id;
    }

    public synchronized String getId() {
        return id;
    }

    public synchronized void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        return id.equals(obj);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }


    @Override
    public String toString() {
        return id.toString();
    }

}
