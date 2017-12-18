package cn.egoa.sharehelper.rxbus.event;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ryan on 2017/12/18.
 */
public class ChangeEvent implements Serializable {
    private Map<String, String> mapString = new HashMap<>();
    private Map<String, Integer> mapInteger = new HashMap<>();
    private Map<String, Boolean> mapBoolean = new HashMap<>();
    private Map<String, Object> mapObject = new HashMap<>();
    public String eventTag = "";

    public void putString(String key, String value) {
        mapString.put(key, value);
    }

    public String getString(String key) {
        return mapString.get(key);
    }

    public void putBoolean(String key, Boolean value) {
        mapBoolean.put(key, value);
    }

    public Boolean getBoolean(String key) {
        return mapBoolean.get(key);
    }

    public void putInteger(String key, Integer value) {
        mapInteger.put(key, value);
    }

    public Integer getInteger(String key) {
        return mapInteger.get(key);
    }

    public void putObject(String key, Object value) {
        mapObject.put(key, value);
    }

    public Object getObject(String key) {
        return mapObject.get(key);
    }
}