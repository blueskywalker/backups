package org.blueskywalker.flume.twitter;

import java.util.Map;

/**
 * Created by kkim on 3/21/16.
 */
public interface JsonFilterFunction {

    public boolean match(Map<String,Object> source);
}
