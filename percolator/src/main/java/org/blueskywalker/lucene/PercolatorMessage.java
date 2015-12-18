package org.blueskywalker.lucene;

import java.io.Serializable;

/**
 * Created by kkim on 8/6/15.
 */
public class PercolatorMessage implements Serializable {

    private static final long serialVersionUID = 1954332580536848712L;

    final PercolatorId id;
    final String msg;

    public PercolatorMessage(PercolatorId id, String msg) {
        this.id = id;
        this.msg = msg;
    }

    public PercolatorId getId() {
        return id;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PercolatorMessage that = (PercolatorMessage) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        return !(msg != null ? !msg.equals(that.msg) : that.msg != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (msg != null ? msg.hashCode() : 0);
        return result;
    }
}
