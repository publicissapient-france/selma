/**
 *
 */
package fr.xebia.extras.selma;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Simple instance cache
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class SimpleInstanceCache implements InstanceCache {

    private final static ThreadLocal<ThreadLocalInstanceCache> threadContext = new ThreadLocal<ThreadLocalInstanceCache>(){
        @Override protected ThreadLocalInstanceCache initialValue() {
            return new ThreadLocalInstanceCache();
        }
    };

    public <IN, OUT> OUT get(IN in) {
        return (OUT) threadContext.get().getMap().get(in);
    }

    /**
     * Put an instance of IN in the cache as key for the OUT instance to be mapped.
     * Increment the mappingMethodRank, to keep track of the visited mapping method count.
     */
    public <IN, OUT> void put(IN in, OUT out) {
        threadContext.get().getMap().put(in, out);
    }

    /**
     * Put an instance of IN in the cache as key for the OUT instance to be mapped.
     * Increment the mappingMethodRank, to keep track of the visited mapping method count.
     */
    public void push() {
        threadContext.get().getMappingMethodRank().incrementAndGet();
    }

    /**
     * Called when exiting a generated mapping method, count down the mappingMethodRank.
     * When rank goes down to 0, clear the underlying map.
     */
    public void pop(){
        int rank = threadContext.get().getMappingMethodRank().decrementAndGet();
        if (rank == 0){
            threadContext.get().getMap().clear();
        }
    }

    static class ThreadLocalInstanceCache {
        private final AtomicInteger mappingMethodRank = new AtomicInteger(0);
        private final HashMap map = new HashMap();

        public AtomicInteger getMappingMethodRank() {
            return mappingMethodRank;
        }

        public HashMap getMap() {
            return map;
        }
    }

}
