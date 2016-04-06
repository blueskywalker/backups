package org.blueskywalker.flume.twitter;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;
import org.boon.json.ObjectMapper;
import org.boon.json.ObjectMapperFactory;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by kkim on 3/21/16.
 */
public class JsonFilter implements Interceptor {

    private List<String> fields;
    private ObjectMapper mapper;
    private JsonFilterFunction filter;

    public JsonFilter(List<String> fields, JsonFilterFunction filter) {
        this.fields = fields;
        this.filter = filter;
    }

    public void initialize() {
        mapper = ObjectMapperFactory.create();
    }

    protected Object getValue(Map<String, Object> source, String field) {

        String[] keys = field.split("\\.");
        if (keys.length == 1) {
            if (source.containsKey(keys[0])) {
                return source.get(keys[0]);
            }
        } else {
            if (source.containsKey(keys[0]) &&
                    (source.get(keys[0]) instanceof HashMap)) {
                return getValue((Map<String, Object>) source.get(keys[0]), Joiner.on(".").join(Arrays.copyOfRange(keys, 1, keys.length)));
            }
        }
        return null;
    }

    protected void putValue(Map<String, Object> target, String field, Object value) {
        String[] keys = field.split("\\.");
        if (keys.length == 1) {
            target.put(field, value);
        } else {
            if (!target.containsKey(keys[0]))
                target.put(keys[0], Maps.newHashMap());
            String second = Joiner.on(".").join(Arrays.copyOfRange(keys, 1, keys.length));
            putValue((Map<String, Object>) target.get(keys[0]), second, value);
        }
    }

    protected Map<String, Object> process(Map<String, Object> source) {
        Map<String, Object> results = Maps.newHashMap();

        for (String field : fields) {
            String[] optFields = field.split(":");
            Object value = getValue(source, optFields[0]);
            if (value != null) {
                putValue(results, optFields.length > 1 ? optFields[1] : optFields[0], value);
            }
        }
        return results;
    }

    public Event intercept(Event event) {
        Map<String, Object> twitter = mapper.fromJson(event.getBody(), Map.class);

        if (filter != null && !filter.match(twitter))
            return null;

        event.setBody(mapper.toJson(process(twitter)).getBytes());
        return event;
    }

    public List<Event> intercept(List<Event> events) {
        List<Event> results = Lists.newArrayList();

        for (Event event : events) {
            results.add(intercept(event));
        }
        return results;
    }

    public void close() {

    }

    public static class Builder implements Interceptor.Builder {
        List<String> fields;
        JsonFilterFunction function;

        public Interceptor build() {
            return new JsonFilter(fields, function);
        }

        public void configure(Context context) {
            fields = Arrays.asList(context.getString("fields").split(",")).stream().map(String::trim).collect(Collectors.toList());
            String filterFunction = context.getString("filter");
            if (filterFunction != null) {
                try {
                    Class<?> clazz = Class.forName(filterFunction);
                    function = (JsonFilterFunction) clazz.newInstance();
                } catch (Exception e) {
                    ;
                }
            }
        }
    }

    /*
    public static void main(String[] args) {
        List<String> fields = Arrays.asList("text","entity.hashtag","1.2.3.4");
        JsonFilter tf = new JsonFilter(fields,new JsonTwitterHashTagFilter());
        HashMap<String,Object> data = Maps.newHashMap();
        data.put("text","hello data");
        HashMap<String,Object>  tmp = Maps.newHashMap();
        tmp.put("hashTag","#hello");
        data.put("entity",tmp);
        tmp.put("1",Maps.newHashMap());
        ((HashMap<String,Object>)tmp.get("1")).put("2",Maps.newHashMap());
        ((HashMap<String,Object>)((HashMap<String,Object>)tmp.get("1")).get("2")).put("3",Maps.newHashMap());
        ((HashMap<String,Object>)((HashMap<String,Object>)((HashMap<String,Object>)tmp.get("1")).get("2")).get("3")).put("4","data");

        tf.process(data);
    }
    */
}
