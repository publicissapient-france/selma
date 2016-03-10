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
package fr.xebia.extras.selma.it.inject;

import fr.xebia.extras.selma.it.utils.Compile;
import fr.xebia.extras.selma.it.utils.IntegrationTestBase;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


@Compile(withClasses = {AddressMapperWithName.class, CustomImmutableMapperClass.class})
public class CustomMapperUsingSpringIoCWithServiceNameIT extends IntegrationTestBase {


    @Test
    public void given_mapper_name_as_selma_mapper_should_retrieve_it() throws IllegalAccessException, InstantiationException, ClassNotFoundException {

        ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfiguration.class);
        AddressMapperWithName addressMapperByName = (AddressMapperWithName) context.getBean("addressSelmaMapper");
        Assert.assertNotNull(addressMapperByName);
        AddressMapperWithName addressMapper = context.getBean(AddressMapperWithName.class);
        Assert.assertEquals(addressMapper,addressMapperByName);
    }

}
