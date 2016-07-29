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
package fr.xebia.extras.selma.it;

import fr.xebia.extras.selma.Selma;
import fr.xebia.extras.selma.beans.AddressIn;
import fr.xebia.extras.selma.beans.AddressOut;
import fr.xebia.extras.selma.beans.CityIn;
import fr.xebia.extras.selma.beans.CityOut;
import fr.xebia.extras.selma.it.mappers.UpdateGraphBeanMapper;
import fr.xebia.extras.selma.it.utils.Compile;
import fr.xebia.extras.selma.it.utils.IntegrationTestBase;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
@Compile(withClasses = UpdateGraphBeanMapper.class)
public class UpdateGraphBeanMapperIT extends IntegrationTestBase {

    @Test
    public void given_in_bean_with_nested_and_new_empty_bean_when_update_graph_should_return_populated_out_bean() throws Exception {


        UpdateGraphBeanMapper mapper = Selma.getMapper(UpdateGraphBeanMapper.class);

        AddressIn addressIn = new AddressIn();
        addressIn.setCity(new CityIn());
        addressIn.getCity().setCapital(true);
        addressIn.getCity().setName("Paris");
        addressIn.getCity().setPopulation(3 * 1000 * 1000);

        addressIn.setPrincipal(true);
        addressIn.setNumber(55);
        addressIn.setStreet("rue de la truanderie");

        AddressOut addressOut = new AddressOut();

        AddressOut res = mapper.asAddressOut(addressIn, addressOut);

        Assert.assertNotNull(res);
        Assert.assertTrue(res == addressOut);
        verifyAddress(addressIn, addressOut);
    }

    @Test
    public void given_in_bean_with_nested_and_new_full_bean_when_update_graph_should_return_populated_out_bean() throws Exception {


        UpdateGraphBeanMapper mapper = Selma.getMapper(UpdateGraphBeanMapper.class);

        AddressIn addressIn = new AddressIn();
        addressIn.setCity(new CityIn());
        addressIn.getCity().setCapital(true);
        addressIn.getCity().setName("Paris");
        addressIn.getCity().setPopulation(3 * 1000 * 1000);

        addressIn.setPrincipal(true);
        addressIn.setNumber(55);
        addressIn.setStreet("rue de la truanderie");

        AddressOut addressOut = new AddressOut();
        addressOut.setCity(new CityOut());
        addressOut.getCity().setCapital(false);
        addressOut.getCity().setName("Lyon");
        addressOut.getCity().setPopulation(3 * 1000 * 100);

        addressOut.setPrincipal(false);
        addressOut.setNumber(42);
        addressOut.setStreet("main street");

        AddressOut res = mapper.asAddressOut(addressIn, addressOut);

        Assert.assertNotNull(res);
        Assert.assertTrue(res == addressOut);
        verifyAddress(addressIn, addressOut);
    }

    @Test
    public void given_in_bean_with_nested_and_null_bean_when_update_graph_should_return_new_populated_out_bean() throws Exception {


        UpdateGraphBeanMapper mapper = Selma.getMapper(UpdateGraphBeanMapper.class);

        AddressIn addressIn = new AddressIn();
        addressIn.setCity(new CityIn());
        addressIn.getCity().setCapital(true);
        addressIn.getCity().setName("Paris");
        addressIn.getCity().setPopulation(3 * 1000 * 1000);

        addressIn.setPrincipal(true);
        addressIn.setNumber(55);
        addressIn.setStreet("rue de la truanderie");


        AddressOut res = mapper.asAddressOut(addressIn, null);

        Assert.assertNotNull(res);
        verifyAddress(addressIn, res);
    }

    @Test
    public void given_in_bean_with_nested_null_and_null_bean_when_update_graph_should_return_new_populated_out_bean_with_nested_null() throws Exception {


        UpdateGraphBeanMapper mapper = Selma.getMapper(UpdateGraphBeanMapper.class);

        AddressIn addressIn = new AddressIn();
        addressIn.setCity(null);
        addressIn.setPrincipal(true);
        addressIn.setNumber(55);
        addressIn.setStreet("rue de la truanderie");


        AddressOut res = mapper.asAddressOut(addressIn, null);

        Assert.assertNotNull(res);
        verifyAddress(addressIn, res);
    }

    @Test
    public void given_in_bean_with_nested_null_and_full_bean_when_update_graph_should_return_populated_out_bean_with_nested_null() throws Exception {


        UpdateGraphBeanMapper mapper = Selma.getMapper(UpdateGraphBeanMapper.class);

        AddressIn addressIn = new AddressIn();
        addressIn.setCity(null);
        addressIn.setPrincipal(true);
        addressIn.setNumber(55);
        addressIn.setStreet("rue de la truanderie");

        AddressOut addressOut = new AddressOut();
        addressOut.setCity(new CityOut());
        addressOut.getCity().setCapital(false);
        addressOut.getCity().setName("Lyon");
        addressOut.getCity().setPopulation(3 * 1000 * 100);

        addressOut.setPrincipal(false);
        addressOut.setNumber(42);
        addressOut.setStreet("main street");

        AddressOut res = mapper.asAddressOut(addressIn, addressOut);

        Assert.assertNotNull(res);
        Assert.assertTrue(res == addressOut);
        verifyAddress(addressIn, res);
    }


    @Test
    public void given_in_bean_null_and_null_bean_when_update_graph_should_return_null() throws Exception {

        UpdateGraphBeanMapper mapper = Selma.getMapper(UpdateGraphBeanMapper.class);

        AddressOut res = mapper.asAddressOut(null, null);

        Assert.assertNull(res);
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
