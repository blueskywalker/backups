package org.blueskywalker.yaml;

/**
 * Created by kkim on 12/4/15.
 */
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
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

    public Boolean getBoolean(String key,Boolean basic) {
        return containsKey(key)?(Boolean)get(key):basic;
    }

    public Integer getInteger(String key) {
        return (Integer) get(key);
    }

    public Integer getInteger(String key,Integer basic) {
        return containsKey(key)?(Integer) get(key):basic;
    }

    public String getString(String key) {
        if (get(key) instanceof Number ) {
            return String.valueOf(get(key));
        }
        return (String) get(key);
    }

    public String getString(String key,String basic) {
        return containsKey(key)?getString(key):basic;
    }


    public Map<String,String> getStringMap() {
        Map<String,String> ret = new HashMap<String,String>();
        for(Map.Entry<String,Object> e : entrySet()) {
            ret.put(e.getKey(), String.valueOf(e.getValue()));
        }
        return ret;
    }

    public Properties getProperties() {
        return new Properties();

    }
}
