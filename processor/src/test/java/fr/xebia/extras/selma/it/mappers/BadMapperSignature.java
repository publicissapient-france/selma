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
package fr.xebia.extras.selma.it.mappers;

import fr.xebia.extras.selma.Mapper;

/**
 * Created with IntelliJ IDEA.
 * User: slemesle
 * Date: 22/11/2013
 * Time: 14:27
 * To change this template use File | Settings | File Templates.
 */
@Mapper
public interface BadMapperSignature {

    String mapThreeParameters(String in, String inBis, String inTer);


    String mapDifferentTypes(boolean in);


    String mapTwoParametersDifferentTypes(String in, boolean out);

}
