package org.blueskywalker.lucene;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.index.memory.MemoryIndex;
import org.apache.lucene.util.Version;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kkim on 8/6/15.
 */
public class Percolator implements  Runnable {
    static final Logger logger = Logger.getLogger(Percolator.class);

    static final String F_CONTENT = "content";
    final Version version;
    final List<PercolatorQuery> queries;
    final MemoryIndex index;
    final PercolatorRequestQueue request;
    final PercolatorResponseQueue response;
    boolean done;

    public Percolator(Version version, List<PercolatorQuery> queries,
                      PercolatorRequestQueue request,
                      PercolatorResponseQueue response) {
        this.version = version;
        this.queries = queries;
        this.request = request;
        this.response = response;

        index = new MemoryIndex();
        done=false;
    }

    List<PercolatorId> match(String doc) {
        index.reset();
        index.addField(F_CONTENT,doc,new SimpleAnalyzer(version));

        ArrayList<PercolatorId> ret = new ArrayList<PercolatorId>();

        for(PercolatorQuery qry : queries) {
            if(index.search(qry.getQuery())> 0.0f) {
                ret.add(qry.getId());
            }
        }

        return ret;
    }

    public void run() {

        while(!done) {
            try {
                PercolatorMessage msg = request.take();

                List<PercolatorId> qids = match(msg.getMsg());

                response.put(new PercolatorOutput(msg.getId(),qids));
            } catch (InterruptedException e) {
                logger.error(e,e);
            }
        }
    }
}
