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
package fr.xebia.extras.selma.it.custom.cyclic;

import fr.xebia.extras.selma.Selma;
import fr.xebia.extras.selma.it.utils.Compile;
import fr.xebia.extras.selma.it.utils.IntegrationTestBase;
import org.junit.Test;

/**
 * Created by slemesle on 19/11/14.
 */
@Compile(withClasses = {CyclicBookMapper.class, CyclicChainedMappers.class})
public class CustomCyclicChainedMapperIT extends IntegrationTestBase {


    @Test
    public void should_map_field_with_custom_mapper() throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        CyclicChainedMappers cyclicChainedMappers = Selma.builder(CyclicChainedMappers.class)
                                                         .withCustom(Selma.mapper(CyclicBookMapper.class)).build();


    }


}
