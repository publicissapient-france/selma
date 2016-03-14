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
package fr.xebia.extras.selma.it.factory;

import fr.xebia.extras.selma.Selma;
import fr.xebia.extras.selma.beans.AddressIn;
import fr.xebia.extras.selma.beans.AddressOut;
import fr.xebia.extras.selma.beans.CityIn;
import fr.xebia.extras.selma.it.utils.Compile;
import fr.xebia.extras.selma.it.utils.IntegrationTestBase;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 *
 */
@Compile(withClasses = {BeanFactory.class, FactoryMapper.class})
public class FactoryMapperIT extends IntegrationTestBase {

    @Test
    public void given_a_valid_factory_class_mapper_should_use_it_to_instantiate_beans() throws Exception {
        BeanFactory factory = new BeanFactory();
        FactoryMapper mapper = Selma.builder(FactoryMapper.class).withFactories(factory).build();

        AddressIn address = new AddressIn();
        address.setCity(new CityIn());
        address.setExtras(Arrays.asList(new String []{"134", "1234", "543"}));
        address.setPrincipal(false);
        address.setNumber(42);
        address.setStreet("Victor Hugo");
        address.getCity().setName("Paris");
        address.getCity().setCapital(true);
        address.getCity().setPopulation(10);

        AddressOut res = mapper.asAddressOut(address);

        Assert.assertNotNull(res);
        Assert.assertEquals(2, factory.getBuildMap().size());
    }

}
