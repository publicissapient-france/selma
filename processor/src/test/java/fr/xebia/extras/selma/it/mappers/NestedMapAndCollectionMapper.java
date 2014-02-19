/*
 * Copyright 2013  Xebia and Séven Le Mesle
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
package fr.xebia.extras.selma.it.mappers;

import fr.xebia.extras.selma.Mapper;

import java.util.*;

/**
 * Mapper used to demonstrate nested collections and Map support
 */
@Mapper
public interface NestedMapAndCollectionMapper {


    List<Set<String>> convertListOfSet(ArrayList<HashSet<String>> list);


    List<Map<Integer, String>> convertListOfMap(ArrayList<Map<Integer, String>> list);


    Map<Integer, List<String>> convertMapOfList(Map<Integer, List<String>> map);


    Map<List<String>, Map<String, String>> convertMapOfListForMap(Map<List<String>, Map<String, String>> map);


}
