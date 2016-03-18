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

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import fr.xebia.extras.selma.beans.CityOut;

/**
 * Generic bean factory
 */
public class BeanFactory {

    AtomicInteger newInstance = new AtomicInteger();
    AtomicInteger newCityCalled = new AtomicInteger();
    AtomicInteger newOutObjectCalled = new AtomicInteger();

    public <T>  T newInstance(Class<T> targetType) {
        newInstance.incrementAndGet();
        try {
            return targetType.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public <T extends OutObject>  T newOutObjectInstance(Class<T> targetType) {
        newOutObjectCalled.incrementAndGet();
        try {
            return targetType.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public CityOut newCity(){
        newCityCalled.incrementAndGet();
        return new CityOut();
    }

    public BuildingOut newBuilding() {
        return BuildingOut.create();
    }

    public Map getBuildMap(){
        return null;
    }
}
