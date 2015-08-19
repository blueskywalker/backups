package org.blueskywalker.lucene;

import org.apache.lucene.search.Query;

import java.io.Serializable;

/**
 * Created by kkim on 8/6/15.
 */
public class PercolatorQuery implements Serializable {

    private static final long serialVersionUID = 2813217363150407850L;

    final PercolatorId id;
    final Query query;


    public PercolatorQuery(PercolatorId id, Query query) {
        this.id = id;
        this.query = query;
    }

    public PercolatorId getId() {
        return id;
    }

    public Query getQuery() {
        return query;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PercolatorQuery that = (PercolatorQuery) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        return !(query != null ? !query.equals(that.query) : that.query != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (query != null ? query.hashCode() : 0);
        return result;
    }
}
