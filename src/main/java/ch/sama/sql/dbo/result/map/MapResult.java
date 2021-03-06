package ch.sama.sql.dbo.result.map;

import java.util.*;
import java.util.stream.Collectors;

public class MapResult {
    private Map<String, Object> map;
    
    MapResult() {
        map = new LinkedHashMap<String, Object>();
    }

    public Object get(String key) {
        return map.get(key);
    }

    public Object put(String key, Object value) {
        return map.put(key, value);
    }

    public Set<String> getKeySet() {
        return map.keySet();
    }

    public boolean containsKey(String key) {
        return map.containsKey(key);
    }

    public<T> T getAs(String key, Class<T> type) {
        Object o = get(key);

        if (o == null) {
            return null;
        }

        if (type.isAssignableFrom(o.getClass())) {
            return type.cast(o);
        }

        throw new ClassCastException(getErrorMessage(key, o.getClass().getName(), type.getName()));
    }

    private String getErrorMessage(String key, String actual, String expected) {
        return "{" + actual + "} cannot be cast to {" + expected + "}";
    }

    public String getAsString(String key) {
        return getAs(key, String.class);
    }

    public Integer getAsInt(String key) {
        return getAs(key, Integer.class);
    }

    public Long getAsLong(String key) {
        return getAs(key, Long.class);
    }

    public Short getAsShort(String key) {
        return getAs(key, Short.class);
    }

    public Double getAsDouble(String key) {
        return getAs(key, Double.class);
    }

    public Float getAsFloat(String key) {
        return getAs(key, Float.class);
    }

    public Date getAsDate(String key) {
        return getAs(key, Date.class);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> copy = new LinkedHashMap<String, Object>();
        copy.putAll(map);

        return copy;
    }

    public String getString() {
        String data = map.keySet().stream()
                .map(key -> {
                    String val;
                    try {
                        val = map.get(key).toString();
                    } catch (NullPointerException e) {
                        val = "";
                    }

                    return "\t\"" + key + "\": \"" + val + "\"";
                })
                .collect(Collectors.joining(",\n"));

        return "{\n" + data + "\n}";
    }
}
