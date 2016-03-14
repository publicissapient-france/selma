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
package fr.xebia.extras.selma.it.factory;

import fr.xebia.extras.selma.beans.CityOut;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Generic bean factory
 */
public class BeanFactory {

    Map<String, AtomicLong> buildMap = new HashMap<String, AtomicLong>();

    public <T>  T newInstance(Class<T> targetType) {

        if (buildMap.containsKey(targetType.getName())){
            buildMap.get(targetType.getName()).incrementAndGet();
        } else {
            buildMap.put(targetType.getName(), new AtomicLong(1));
        }
        try {
            return targetType.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

/*
    public CityOut newCity(){
        return new CityOut();
    }
*/

    public Map<String, AtomicLong> getBuildMap() {
        return buildMap;
    }
}
