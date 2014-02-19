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
package fr.xebia.extras.selma.it;

import fr.xebia.extras.selma.Selma;
import fr.xebia.extras.selma.beans.ExtendedCityIn;
import fr.xebia.extras.selma.beans.ExtendedCityOut;
import fr.xebia.extras.selma.it.mappers.InheritedMapper;
import fr.xebia.extras.selma.it.mappers.NestedMapAndCollectionMapper;
import fr.xebia.extras.selma.it.utils.Compile;
import fr.xebia.extras.selma.it.utils.IntegrationTestBase;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

/**
 *
 */
@Compile(withClasses = NestedMapAndCollectionMapper.class)
public class NestedMapAndCollectionsMapperIT extends IntegrationTestBase {

    @Test
    public void should_convert_list_of_set(){

        ArrayList<HashSet<String>> in = new ArrayList<HashSet<String>>();
        in.add(new HashSet<String>(Arrays.asList("un", null, "deux")));
        in.add(null);
        in.add(new HashSet<String>(Arrays.asList("trois", null, "quatre")));

        NestedMapAndCollectionMapper mapper = Selma.mapper(NestedMapAndCollectionMapper.class);

        List<Set<String>> res = mapper.convertListOfSet(in);

        Assert.assertEquals(3, res.size());
        Assert.assertEquals(3, res.get(0).size());
        Assert.assertTrue(res.get(0).contains("un"));
        Assert.assertTrue(res.get(0).contains(null));
        Assert.assertTrue(res.get(0).contains("deux"));

        Assert.assertNull(res.get(1));

        Assert.assertEquals(3, res.get(2).size());
        Assert.assertTrue(res.get(2).contains("trois"));
        Assert.assertTrue(res.get(2).contains(null));
        Assert.assertTrue(res.get(2).contains("quatre"));

    }


    @Test
    public void should_convert_list_of_map(){
        NestedMapAndCollectionMapper mapper = Selma.mapper(NestedMapAndCollectionMapper.class);

        ArrayList<Map<Integer,String>> in = new ArrayList<Map<Integer,String>>();
        in.add(new HashMap<Integer, String>());
        in.get(0).put(1, "un");
        in.add(null);
        in.add(new LinkedHashMap<Integer, String>());
        in.get(2).put(1, "un");

        List<Map<Integer,String>> res = mapper.convertListOfMap(in);

        Assert.assertEquals(3, res.size());
        Assert.assertEquals(1, res.get(0).size());
        Assert.assertTrue(res.get(0).containsKey(1));
        Assert.assertEquals("un", res.get(0).get(1));

        Assert.assertNull(res.get(1));

        Assert.assertEquals(1, res.get(2).size());
        Assert.assertTrue(res.get(2).containsKey(1));
        Assert.assertEquals("un", res.get(2).get(1));

    }



    @Test
    public void should_convert_map_of_list(){
        NestedMapAndCollectionMapper mapper = Selma.mapper(NestedMapAndCollectionMapper.class);

        Map<Integer,List<String>> in = new HashMap<Integer, List<String>>();

        in.put(1, null);
        in.put(null, Arrays.asList("un", "deux", null));
        in.put(2, new ArrayList<String>());


        Map<Integer,List<String>>  res = mapper.convertMapOfList(in);

        Assert.assertEquals(3, res.size());
        Assert.assertNull(res.get(1));
        Assert.assertEquals(3, res.get(null).size());
        Assert.assertEquals(Arrays.asList("un", "deux", null), res.get(null));
        Assert.assertNotNull(res.get(2));

    }



    @Test
    public void should_convert_map_of_list_for_map(){
        NestedMapAndCollectionMapper mapper = Selma.mapper(NestedMapAndCollectionMapper.class);

        Map<List<String>, Map<String, String>> in = new HashMap<List<String>, Map<String, String>>();
        HashMap<String, String> firstMap = new HashMap<String, String>();
        firstMap.put("first", "first");
        HashMap<String, String> secondMap = new HashMap<String, String>();
        secondMap.put("second", "second");
        in.put(Arrays.asList("deux", "trois"), secondMap);
        in.put(Arrays.asList("un"), firstMap);
        in.put(null, null);

        Map<List<String>, Map<String, String>> res = mapper.convertMapOfListForMap(in);

        Assert.assertEquals(3, res.size());
        Assert.assertNull(res.get(null));

        Assert.assertTrue(res.get(Arrays.asList("deux", "trois")).containsKey("second"));
        Assert.assertTrue(res.get(Arrays.asList("deux", "trois")).containsValue("second"));
        Assert.assertTrue(res.get(Arrays.asList("un")).containsKey("first"));
        Assert.assertTrue(res.get(Arrays.asList("un")).containsValue("first"));
    }



}
