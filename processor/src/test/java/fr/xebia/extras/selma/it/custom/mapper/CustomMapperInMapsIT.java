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
package fr.xebia.extras.selma.it.custom.mapper;

import fr.xebia.extras.selma.Selma;
import fr.xebia.extras.selma.beans.AddressIn;
import fr.xebia.extras.selma.beans.CityIn;
import fr.xebia.extras.selma.beans.PersonIn;
import fr.xebia.extras.selma.beans.PersonOut;
import fr.xebia.extras.selma.it.utils.Compile;
import fr.xebia.extras.selma.it.utils.IntegrationTestBase;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by slemesle on 19/11/14.
 */
@Compile(withClasses = {CustomMapperInMaps.class, CustomMutableMapper.class,
        CustomImmutableMapper.class, CustomImmutableMapperInMapper.class, NeverUsedCustomMapper.class})
public class CustomMapperInMapsIT extends IntegrationTestBase {

    @Test
    public void given_immutable_custom_in_maps_should_map_bean_with_it() throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        CustomMapperInMaps mapper = Selma.getMapper(CustomMapperInMaps.class);

        PersonIn personIn = new PersonIn();
        personIn.setAddress(new AddressIn());
        personIn.getAddress().setCity(new CityIn());
        personIn.getAddress().getCity().setCapital(true);
        personIn.getAddress().getCity().setName("Paris");
        personIn.getAddress().getCity().setPopulation(3 * 1000 * 1000);

        personIn.getAddress().setPrincipal(true);
        personIn.getAddress().setNumber(55);
        personIn.getAddress().setStreet("rue de la truanderie");

        PersonOut res = mapper.mapWithCustom(personIn);

        Assert.assertNotNull(res);

        Assert.assertEquals(personIn.getAddress().getStreet(), res.getAddress().getStreet());
        Assert.assertEquals(personIn.getAddress().getNumber(), res.getAddress().getNumber());
        Assert.assertEquals(personIn.getAddress().getExtras(), res.getAddress().getExtras());

        Assert.assertEquals(personIn.getAddress().getCity().getName() + CustomImmutableMapper.IMMUTABLY_MAPPED, res.getAddress().getCity().getName());
        Assert.assertEquals(personIn.getAddress().getCity().getPopulation() + CustomImmutableMapper.POPULATION_INC, res.getAddress().getCity().getPopulation());
        Assert.assertEquals(personIn.getAddress().getCity().isCapital(), res.getAddress().getCity().isCapital());
    }

    @Test
    public void given_mutable_mapper_in_maps_should_map_bean_with_it() throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        CustomMapperInMaps mapper = Selma.getMapper(CustomMapperInMaps.class);

        PersonIn personIn = new PersonIn();
        personIn.setAddress(new AddressIn());
        personIn.getAddress().setCity(new CityIn());
        personIn.getAddress().getCity().setCapital(true);
        personIn.getAddress().getCity().setName("Paris");
        personIn.getAddress().getCity().setPopulation(3 * 1000 * 1000);

        personIn.getAddress().setPrincipal(true);
        personIn.getAddress().setNumber(55);
        personIn.getAddress().setStreet("rue de la truanderie");

        PersonOut out = new PersonOut();
        PersonOut res = mapper.mapWithCustom(personIn, out);

        Assert.assertNotNull(res);
        Assert.assertTrue(out == res);
        Assert.assertEquals(personIn.getAddress().getStreet(), res.getAddress().getStreet());
        Assert.assertEquals(personIn.getAddress().getNumber(), res.getAddress().getNumber());
        Assert.assertEquals(personIn.getAddress().getExtras(), res.getAddress().getExtras());

        Assert.assertEquals(personIn.getAddress().getCity().getName() + CustomMutableMapper.IMMUTABLY_MAPPED, res.getAddress().getCity().getName());
        Assert.assertEquals(personIn.getAddress().getCity().getPopulation() + CustomMutableMapper.POPULATION_INC, res.getAddress().getCity().getPopulation());
        Assert.assertEquals(personIn.getAddress().getCity().isCapital(), res.getAddress().getCity().isCapital());
    }

    @Test
    public void given_custom_mapper_in_mapper_overriden_by_maps_should_be_reported_unused() throws Exception {
        assertCompilationWarning(CustomMapperInMaps.class, "public interface CustomMapperInMaps {", "Custom mapping method \"fr.xebia.extras.selma.it.custom.mapper.CustomImmutableMapperInMapper.mapCity\" is never used");
        Assert.assertEquals(2, compilationWarningCount());
    }


    @Test
    public void given_custom_mapper_in_maps_never_used_should_be_reported_unused() throws Exception {
        assertCompilationWarning(CustomMapperInMaps.class, "PersonOut mapWithCustom(PersonIn in, PersonOut out);", "Custom mapping method \"fr.xebia.extras.selma.it.custom.mapper.NeverUsedCustomMapper.asBookDTO\" is never used");
        Assert.assertEquals(2, compilationWarningCount());
    }
}
