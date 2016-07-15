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
package fr.xebia.extras.selma.it;

import fr.xebia.extras.selma.Selma;
import fr.xebia.extras.selma.beans.StringList;
import fr.xebia.extras.selma.beans.StringMap;
import fr.xebia.extras.selma.it.mappers.CollectionInheritanceMapper;
import fr.xebia.extras.selma.it.utils.Compile;
import fr.xebia.extras.selma.it.utils.IntegrationTestBase;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by slemesle on 12/03/2014.
 */
@Compile(withClasses = {CollectionInheritanceMapper.class})
public class CollectionInheritanceMapperIT extends IntegrationTestBase {

    @Test
    public void mapTest() throws Exception {

        StringMap map = new StringMap();
        map.put("key1", "value1");
        map.put("key2", "value2");

        CollectionInheritanceMapper mapper = Selma.mapper(CollectionInheritanceMapper.class);
        Assert.assertEquals(map, mapper.as(map));
        Assert.assertNotSame(map, mapper.as(map));

    }

    @Test
    public void listTest() throws Exception {

        StringList list = new StringList();
        list.add("value1");
        list.add("value2");

        CollectionInheritanceMapper mapper = Selma.mapper(CollectionInheritanceMapper.class);
        Assert.assertEquals(list, mapper.as(list));
        Assert.assertNotSame(list, mapper.as(list));

    }
}
