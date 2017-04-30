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
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 *
 */
@Compile(withClasses = {
        BeanFactory.class,
        FactoryMapper.class,
        OutObject.class,
        ExtendedAddressIn.class,
        ExtendedAddressOut.class,
        BuildingIn.class,
        BuildingOut.class
})
public class FactoryMapperIT extends IntegrationTestBase {

    @Test
    public void given_a_valid_factory_class_mapper_should_use_it_to_instantiate_beans() throws Exception {
        BeanFactory factory = new BeanFactory();
        FactoryMapper mapper = Selma.builder(FactoryMapper.class).withFactories(factory).build();

        AddressIn address = new AddressIn();
        address.setCity(new CityIn());
        address.setExtras(Arrays.asList("134", "1234", "543"));
        address.setPrincipal(false);
        address.setNumber(42);
        address.setStreet("Victor Hugo");
        address.getCity().setName("Paris");
        address.getCity().setCapital(true);
        address.getCity().setPopulation(10);

        AddressOut res = mapper.asAddressOut(address);

        assertNotNull(res);
        assertEquals("Generic factory should have been called once for AddressOut", 1, factory.newInstance.get());
        assertEquals("Static factory should have been called once for CityOut", 1, factory.newCityCalled.get());
    }

    @Test
    public void given_a_factory_method_never_used_compiler_should_report_it() throws Exception {
        assertCompilationWarning(FactoryMapper.class,
                "public interface FactoryMapper {",
                "Factory method \"fr.xebia.extras.selma.it.factory.BeanFactory.getBuildMap\" is never used");
    }

    @Test
    public void given_a_valid_factory_class_mapper_should_use_closest_factory_to_instantiate_beans() throws Exception {
        BeanFactory factory = new BeanFactory();
        FactoryMapper mapper = Selma.builder(FactoryMapper.class).withFactories(factory).build();

        ExtendedAddressIn address = new ExtendedAddressIn();
        address.setCity(new CityIn());
        address.setExtras(Arrays.asList("134", "1234", "543"));
        address.setPrincipal(false);
        address.setNumber(42);
        address.setStreet("Victor Hugo");
        address.getCity().setName("Paris");
        address.getCity().setCapital(true);
        address.getCity().setPopulation(10);
        address.setBuilding(new BuildingIn());
        address.getBuilding().setName("Pierre Humbert");
        address.getBuilding().setNumber(167);
        address.getBuilding().setStreet("Victor Hugo");

        ExtendedAddressOut res = mapper.asExtendedAddressOut(address);

        assertNotNull(res);
        assertEquals("Generic newOutObjectInstance factory should have been called once for ExtendedAddressOut",
                1, factory.newOutObjectCalled.get());
        assertEquals("Static factory should have been called once for CityOut", 1, factory.newCityCalled.get());
        assertThat(res.getBuilding(), notNullValue());
    }
}
