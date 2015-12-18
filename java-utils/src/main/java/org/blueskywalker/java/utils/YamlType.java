package org.blueskywalker.java.utils;

import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.yaml.snakeyaml.Yaml;

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

    public YamlType(InputStream is) {
        this(new Yaml().load(is));
    }

    public YamlType(LinkedHashMap m) {
        super(m);
    }

    public YamlType getYaml(String key) {
        return new YamlType(get(key));
    }

    public Boolean getBoolean(String key) {
        return (Boolean) get(key);
    }

    public Boolean getBoolean(String key,Boolean value) {
        return containsKey(key)?(Boolean)get(key):value;
    }

    public Integer getInteger(String key) {
        return (Integer) get(key);
    }

    public Integer getInteger(String key,Integer value) {
        return containsKey(key)?(Integer) get(key):value;
    }

    public Long getLong(String key) {
        return (Long) get(key);
    }

    public Long getLong(String key, Long value) {
        return containsKey(key)?getLong(key):value;
    }

    public String getString(String key) {
        if (get(key) instanceof Number ) {
            return String.valueOf(get(key));
        }
        return (String) get(key);
    }

    public String getString(String key,String value) {
        return containsKey(key)?getString(key):value;
    }

    public Properties getProperties() {
        Properties ret = new Properties();
        ret.putAll(this);
        return ret;

    }
}

