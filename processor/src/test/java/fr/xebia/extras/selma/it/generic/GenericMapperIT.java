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
package fr.xebia.extras.selma.it.generic;

import fr.xebia.extras.selma.Selma;
import fr.xebia.extras.selma.beans.AddressIn;
import fr.xebia.extras.selma.beans.AddressOut;
import fr.xebia.extras.selma.beans.CityIn;
import fr.xebia.extras.selma.beans.CityOut;
import fr.xebia.extras.selma.it.generic.beans.Bar;
import fr.xebia.extras.selma.it.generic.beans.Foo;
import fr.xebia.extras.selma.it.utils.Compile;
import fr.xebia.extras.selma.it.utils.IntegrationTestBase;
import org.junit.Assert;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 */
@Compile(withClasses = {SpecificMapper.class, GenericMapper.class, FooMapper.class}, withPackage = "fr.xebia.extras.selma.it.generic.beans")
public class GenericMapperIT extends IntegrationTestBase {

    @Test
    public void beanMapper_should_map_properties_resolving_generics() throws Exception {


        SpecificMapper mapper = Selma.getMapper(SpecificMapper.class);

        AddressIn addressIn = new AddressIn();
        addressIn.setPrincipal(true);
        addressIn.setNumber(55);
        addressIn.setStreet("rue de la truanderie");
        addressIn.setCity(new CityIn());
        addressIn.getCity().setCapital(true);
        addressIn.getCity().setName("Paris");
        addressIn.getCity().setPopulation(3 * 1000 * 1000);

        AddressOut res = mapper.asEntity(addressIn);

        Assert.assertNotNull(res);
        verifyAddress(addressIn, res);
    }

    @Test
    public void beanMapper_should_update_properties_resolving_generics() throws Exception {


        SpecificMapper mapper = Selma.getMapper(SpecificMapper.class);

        AddressIn addressIn = new AddressIn();
        addressIn.setPrincipal(true);
        addressIn.setNumber(55);
        addressIn.setStreet("rue de la truanderie");
        addressIn.setCity(new CityIn());
        addressIn.getCity().setCapital(true);
        addressIn.getCity().setName("Paris");
        addressIn.getCity().setPopulation(3 * 1000 * 1000);
        AddressOut res = new AddressOut();
        AddressOut result = mapper.updateEntity(addressIn, res);

        Assert.assertNotNull(result);
        Assert.assertTrue(res == result);
        verifyAddress(addressIn, res);
    }

    @Test
    public void fooMapper_should_map_generic_foo_to_bar_with_embedded_custom_field() throws Exception {
        FooMapper mapper = Selma.builder(FooMapper.class).build();
        Foo<Bar> in = new Foo<Bar>();
        in.setItem(new Bar());
        in.getItem().setDescription("description");
        in.getItem().setId(1l);

        Bar res = mapper.asBar(in);
        assertThat(res.getId()).isEqualTo(in.getItem().getId());
        assertThat(res.getDescription()).isEqualTo(in.getItem().getDescription());
    }

    private void verifyAddress(AddressIn address, AddressOut address1) {
        if (address == null) {
            Assert.assertNull(address1);
        } else {
            Assert.assertEquals(address.getStreet(), address1.getStreet());
            Assert.assertEquals(address.getNumber(), address1.getNumber());
            Assert.assertEquals(address.getExtras(), address1.getExtras());

            verifyCity(address.getCity(), address1.getCity());
        }
    }

    private void verifyCity(CityIn city, CityOut city1) {
        if (city == null) {
            Assert.assertNull(city1);
        } else {
            Assert.assertEquals(city.getName(), city1.getName());
            Assert.assertEquals(city.getPopulation(), city1.getPopulation());
            Assert.assertEquals(city.isCapital(), city1.isCapital());
        }
    }

}
