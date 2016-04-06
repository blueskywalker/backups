package org.blueskywalker.flume.twitter;

import java.util.Map;

/**
 * Created by kkim on 3/21/16.
 */
public class JsonTwitterHashTagFilter implements JsonFilterFunction {

    public boolean match(Map<String, Object> source) {
        return ((Map<String, Object>)source.get("entities")).get("hashtags")!=null;
    }
}
