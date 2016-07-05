package org.openl.rules.mapping;

import java.util.HashMap;
import java.util.Map;

/**
 * Class that represents user defined parameters. Actually is just wrapper class
 * for key-value pair map.
 *
 * Intended to formalize user defined variables.
 */
public class MappingParameters {

    private Map<Object, Object> map = new HashMap<Object, Object>();

    public void put(Object key, Object value) {
        map.put(key, value);
    }

    public Object get(Object key) {
        return map.get(key);
    }

    public void remove(Object key) {
        map.remove(key);
    }

    public void removeAll() {
        map.clear();
    }

    public boolean contains(Object key) {
        return map.containsKey(key);
    }

}
