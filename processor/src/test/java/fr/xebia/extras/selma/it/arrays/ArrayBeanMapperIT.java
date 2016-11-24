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
package fr.xebia.extras.selma.it.arrays;

import fr.xebia.extras.selma.Selma;
import fr.xebia.extras.selma.it.utils.Compile;
import fr.xebia.extras.selma.it.utils.IntegrationTestBase;
import org.junit.Test;

/**
 * Created by slemesle on 24/11/2016.
 */
@Compile(withClasses = {ArrayBeanMapper.class, ArrayBeanDestination.class,
        ArrayBeanSource.class, BeanDestination.class, BeanSource.class})
public class ArrayBeanMapperIT extends IntegrationTestBase {

    @Test
    public void mapping_array_of_beans_should_work(){
        ArrayBeanSource source = new ArrayBeanSource();
        source.setBeans(new BeanSource[0]);

        ArrayBeanMapper arrayBeanMapper = Selma.builder(ArrayBeanMapper.class).build();
        ArrayBeanDestination res = arrayBeanMapper.asArrayBeanDestination(source);

    }
}
