package org.blueskywalker.twitter.hosebird;


import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author kkim
 */
public class YamlType  extends LinkedHashMap<String,Object> {
    private static final long serialVersionUID = 1L;

    public YamlType() {
    }

    public YamlType(Object load) {
        super((Map<String,Object>)load);
    }

    public YamlType(LinkedHashMap m) {
        super(m);
    }
    public YamlType getYaml(String key) {
        return new YamlType(get(key));
    }

    public Integer getInteger(String key) {
        return (Integer) get(key);
    }

    public String getString(String key) {
        return (String) get(key);
    }
    public Map<String,String> getStringMap() {
        Map<String,String> ret = new HashMap<String,String>();
        for(Map.Entry<String,Object> e : entrySet()) {
            ret.put(e.getKey(), String.valueOf(e.getValue()));
        }
        return ret;
    }
}

