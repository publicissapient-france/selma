/*
 * Copyright 2013  Séven Le Mesle
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

import java.util.HashMap;
import java.util.Map;

/**
 * Created by slemesle on 27/03/2014.
 */
public class BidiMap<K> {

    private final Map<K,K> from;
    private final Map<K,K> to;

    public BidiMap() {
        from = new HashMap<K, K>();
        to = new HashMap<K, K>();
    }

    public BidiMap(BidiMap<K> clone) {
        from = new HashMap<K, K>(clone.from);
        to = new HashMap<K, K>(clone.to);
    }

    public BidiMap(Map<K, K> from, Map<K, K> to) {
        this.from = from;
        this.to = to;
    }


    public void push(K _from, K _to){
        from.put(_from, _to);
        to.put(_to, _from);
    }

    public K get(K key){
        K val = from.get(key);
        if (val == null){
            val = to.get(key);
        }
        return val;
    }
}
