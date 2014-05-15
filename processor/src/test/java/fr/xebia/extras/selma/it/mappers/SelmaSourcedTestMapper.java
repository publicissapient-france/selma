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

/**
 * Created with IntelliJ IDEA.
 * User: slemesle
 * Date: 29/11/2013
 * Time: 01:44
 * To change this template use File | Settings | File Templates.
 */

import fr.xebia.extras.selma.Mapper;
import fr.xebia.extras.selma.beans.DataSource;

/**
 *
 */
@Mapper(withSources = DataSource.class)
public interface SelmaSourcedTestMapper {

    String asString(String in);

}
