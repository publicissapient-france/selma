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
package fr.xebia.extras.selma.it.generic;

import fr.xebia.extras.selma.Mapper;
import fr.xebia.extras.selma.it.generic.beans.GenericBean;
import fr.xebia.extras.selma.it.generic.beans.GenericListBean;
import fr.xebia.extras.selma.it.generic.beans.SimpleDTO;
import fr.xebia.extras.selma.it.generic.beans.SimpleDomain;

/**
 * Created by slemesle on 15/07/2016.
 */
@Mapper
public interface GenericSourceBeanMapper {

    /**
     * Mapps a Generic bean to another resolving parameterized type on a simple property
     * @param source
     * @return
     */
    GenericBean<SimpleDTO> asSimpleDTO(GenericBean<SimpleDomain> source);


    /**
     * Mapps a Generic bean to another resolving parameterized type on a list property
     * @param source
     * @return
     */
    GenericListBean<SimpleDTO> asSimpleDTO(GenericListBean<SimpleDomain> source);
}
