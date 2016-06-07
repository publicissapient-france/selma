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
import fr.xebia.extras.selma.beans.*;
import fr.xebia.extras.selma.it.mappers.BeanMapper;
import fr.xebia.extras.selma.it.utils.Compile;
import fr.xebia.extras.selma.it.utils.IntegrationTestBase;
import junit.framework.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

/**
 *
 */
@Compile(withClasses = BeanMapper.class)
public class BeanMapperIT extends IntegrationTestBase {

    @Test
    public void beanMapper_should_map_properties()throws Exception{


        BeanMapper mapper = Selma.getMapper(BeanMapper.class);

        PersonIn personIn = new PersonIn();
        personIn.setAge(10);
        personIn.setBirthDay(new Date());
        personIn.setFirstName("Odile");
        personIn.setLastName("Dere");
        personIn.setMale(true);
        personIn.setNatural(Boolean.TRUE);

        personIn.setIndices(new Long[]{23l, 42l, null, 55l});

        personIn.setTags(new ArrayList<String>());
        personIn.getTags().add("coucou");
        personIn.getTags().add(null);
        personIn.getTags().add("cucu");

        personIn.setEnumIn(EnumIn.VAL_2);



        PersonOut res = mapper.asPersonOut(personIn);

        assertNotNull(res);
        assertEquals(personIn.getAge(), res.getAge());
        assertEquals(personIn.getBirthDay(), res.getBirthDay());
        assertEquals(personIn.getFirstName(), res.getFirstName());
        assertEquals(personIn.getLastName(), res.getLastName());
        org.junit.Assert.assertArrayEquals(personIn.getIndices(), res.getIndices());
        org.junit.Assert.assertEquals(personIn.getTags(), res.getTags());
        org.junit.Assert.assertEquals(personIn.getEnumIn().name(), res.getEnumIn().name());
        Assert.assertTrue(personIn.isNatural().booleanValue() == res.isNatural().booleanValue());
    }


    @Test
    public void beanMapper_should_map_properties_with_null_safety()throws Exception{

        BeanMapper mapper = Selma.getMapper(BeanMapper.class);

        PersonIn personIn = new PersonIn();
        personIn.setAge(10);
        personIn.setBirthDay(null);
        personIn.setFirstName(null);
        personIn.setLastName(null);
        personIn.setMale(true);
        personIn.setIndices(null);
        personIn.setTags(null);
        personIn.setEnumIn(null);


        PersonOut res = mapper.asPersonOut(personIn);

        assertNotNull(res);
        assertEquals(personIn.getAge(), res.getAge());
        assertNull(res.getBirthDay());
        assertNull(res.getFirstName());
        assertNull(res.getLastName());
        assertNull(res.getIndices());
        assertNull(res.getTags());
        assertNull(res.getEnumIn());
    }

    @Test
    public void beanMapper_should_map_first_level_nested_beans_with_safety()throws Exception{

        BeanMapper mapper = Selma.getMapper(BeanMapper.class);

        PersonIn personIn = new PersonIn();
        personIn.setAge(10);
        personIn.setBirthDay(null);
        personIn.setFirstName(null);
        personIn.setLastName(null);
        personIn.setMale(true);
        personIn.setIndices(null);
        personIn.setTags(null);
        personIn.setEnumIn(null);
        personIn.setAddress(new AddressIn());
        personIn.getAddress().setPrincipal(true);
        personIn.getAddress().setNumber(55);
        personIn.getAddress().setStreet("rue de la truanderie");
        personIn.setAddressBis(new AddressIn());

        PersonOut res = mapper.asPersonOut(personIn);

        assertNotNull(res);
        assertEquals(personIn.getAge(), res.getAge());
        assertNull(res.getBirthDay());
        assertNull(res.getFirstName());
        assertNull(res.getLastName());
        assertNull(res.getIndices());
        assertNull(res.getTags());
        assertNull(res.getEnumIn());
        verifyAddress(personIn.getAddress(), res.getAddress());
        verifyAddress(personIn.getAddressBis(), res.getAddressBis());
    }


    @Test
    public void beanMapper_should_map_second_level_nested_beans_with_safety()throws Exception{

        BeanMapper mapper = Selma.getMapper(BeanMapper.class);

        PersonIn personIn = new PersonIn();
        personIn.setAddress(new AddressIn());
        personIn.getAddress().setCity(new CityIn());
        personIn.getAddress().getCity().setCapital(true);
        personIn.getAddress().getCity().setName("Paris");
        personIn.getAddress().getCity().setPopulation(3*1000*1000);

        personIn.getAddress().setPrincipal(true);
        personIn.getAddress().setNumber(55);
        personIn.getAddress().setStreet("rue de la truanderie");

        PersonOut res = mapper.asPersonOut(personIn);

        assertNotNull(res);
        verifyAddress(personIn.getAddress(), res.getAddress());
        verifyAddress(personIn.getAddressBis(), res.getAddressBis());
    }

    private void verifyAddress(AddressIn address, AddressOut address1) {
        if (address == null) {
            assertNull(address1);
        } else {
            assertEquals(address.getStreet(), address1.getStreet());
            assertEquals(address.getNumber(), address1.getNumber());
            assertEquals(address.getExtras(), address1.getExtras());

            verifyCity(address.getCity(), address1.getCity());
        }
    }

    private void verifyCity(CityIn city, CityOut city1) {
        if (city == null) {
            assertNull(city1);
        } else {
            assertEquals(city.getName(), city1.getName());
            assertEquals(city.getPopulation(), city1.getPopulation());
            assertEquals(city.isCapital(), city1.isCapital());
        }
    }

}
