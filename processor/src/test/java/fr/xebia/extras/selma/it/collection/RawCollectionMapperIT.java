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
package fr.xebia.extras.selma.it.collection;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.xebia.extras.selma.Selma;
import fr.xebia.extras.selma.beans.RawCollectionBeanDestination;
import fr.xebia.extras.selma.beans.RawCollectionBeanSource;
import fr.xebia.extras.selma.it.utils.Compile;
import fr.xebia.extras.selma.it.utils.IntegrationTestBase;
import org.junit.Before;
import org.junit.Test;

/**
 *
 */
@Compile(withClasses = RawCollectionMapper.class)
public class RawCollectionMapperIT extends IntegrationTestBase {
    private RawCollectionMapper mapper;
    private RawCollectionBeanSource source;

    @Before
    public void before() {
        mapper = Selma.builder(RawCollectionMapper.class).build();
        source = new RawCollectionBeanSource();
        List<String> stringList = new ArrayList<String>();
        stringList.add("param1");
        stringList.add("param2");
        source.setStringList(stringList);

        Map<Integer, String> intMap = new HashMap<Integer, String>();
        intMap.put(2, "param2");
        intMap.put(4, "param4");
        intMap.put(6, "param6");
        source.setIntMap(intMap);
    }

    @Test
    public void should_populate_parameterized_to_raw() {
        // When
        final RawCollectionBeanDestination destination = mapper.asParameterizedToRawCollectionBeanDestination(source);

        // Then
        assertThat(destination, notNullValue());
        assertThat(destination.getStringList(), equalTo((List) source.getStringList()));
        assertThat(destination.getIntMap(), equalTo((Map) source.getIntMap()));
    }
}
