/*
 * Copyright 2013 Xebia and Séven Le Mesle
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package fr.xebia.extras.selma.codegen;

import javax.lang.model.element.TypeElement;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created with IntelliJ IDEA.
 * User: slemesle
 * Date: 19/11/2013
 * Time: 00:14
 * To change this template use File | Settings | File Templates.
 */
public class CollectionsRegistry {

    static final Map<String, String> registry = new HashMap<String, String>();

    static {

        registry.put(Set.class.getName(), HashSet.class.getName());
        registry.put(SortedSet.class.getName(), TreeSet.class.getName());
        registry.put(BlockingDeque.class.getName(), LinkedBlockingDeque.class.getName());
        registry.put(BlockingQueue.class.getName(), LinkedBlockingQueue.class.getName());
        registry.put(Queue.class.getName(), PriorityQueue.class.getName());
        registry.put(Deque.class.getName(), ArrayDeque.class.getName());
        registry.put(List.class.getName(), ArrayList.class.getName());
        registry.put(NavigableSet.class.getName(), ConcurrentSkipListSet.class.getName());
        registry.put(Map.class.getName(), HashMap.class.getName());
        registry.put(ConcurrentMap.class.getName(), ConcurrentHashMap.class.getName());
        registry.put(ConcurrentNavigableMap.class.getName(), ConcurrentSkipListMap.class.getName());
        registry.put(NavigableMap.class.getName(), TreeMap.class.getName());
        registry.put(SortedMap.class.getName(), TreeMap.class.getName());
        registry.put(Collection.class.getName(), ArrayList.class.getName());
        registry.put(Iterable.class.getName(), ArrayList.class.getName());
    }

    public static String findImplementationForType(TypeElement typeElement) {
        return registry.get(typeElement.getQualifiedName().toString());
    }
}
