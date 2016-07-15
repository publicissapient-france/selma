/**
 *
 */
package fr.xebia.extras.selma;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple instance cache
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class SimpleInstanceCache implements InstanceCache {
    Map map = new HashMap();

    public <IN, OUT> OUT get(IN in) {
        return (OUT) map.get(in);
    }

    public <IN, OUT> void put(IN in, OUT out) {
        map.put(in, out);
    }


}
