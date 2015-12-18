package org.blueskywalker.lucene;

import java.io.Serializable;
import java.util.List;

/**
 * Created by kkim on 8/6/15.
 */
public class PercolatorOutput implements Serializable {

    private static final long serialVersionUID = -3313432255148988318L;

    final PercolatorId msgId;
    final List<PercolatorId> qids;

    public PercolatorOutput(PercolatorId msgId, List<PercolatorId> qids) {
        this.msgId = msgId;
        this.qids = qids;
    }

    public PercolatorId getMsgId() {
        return msgId;
    }

    public List<PercolatorId> getQids() {
        return qids;
    }
}
