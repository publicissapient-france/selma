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
package fr.xebia.extras.selma.it.custom.interceptor;

import fr.xebia.extras.selma.Selma;
import fr.xebia.extras.selma.beans.AddressIn;
import fr.xebia.extras.selma.beans.CityIn;
import fr.xebia.extras.selma.beans.PersonIn;
import fr.xebia.extras.selma.beans.PersonOut;
import fr.xebia.extras.selma.it.utils.Compile;
import fr.xebia.extras.selma.it.utils.IntegrationTestBase;
import junit.framework.Assert;
import org.junit.Test;

/**
 *
 */
@Compile(
        withClasses = {
                MappingInterceptorForImmutable.class, MappingInterceptorForMutable.class,
                NeverUsedMappingInterceptorInMapper.class, MappingInterceptorInMaps.class,
                NeverUsedMappingInterceptorInMaps.class
        }
)
public class MappingInterceptorInMapsIT extends IntegrationTestBase {


    @Test
    public void should_map_bean_with_custom_mapper() throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        MappingInterceptorInMaps mapper = Selma.getMapper(MappingInterceptorInMaps.class);

        PersonIn personIn = new PersonIn();
        personIn.setFirstName("Selma");
        personIn.setAddress(new AddressIn());
        personIn.getAddress().setCity(new CityIn());
        personIn.getAddress().getCity().setCapital(true);
        personIn.getAddress().getCity().setName("Paris");
        personIn.getAddress().getCity().setPopulation(3 * 1000 * 1000);

        personIn.getAddress().setPrincipal(true);
        personIn.getAddress().setNumber(55);
        personIn.getAddress().setStreet("rue de la truanderie");

        PersonOut res = mapper.mapWithInterceptor(personIn);

        Assert.assertNotNull(res);

        Assert.assertEquals(personIn.getAddress().getStreet(), res.getAddress().getStreet());
        Assert.assertEquals(personIn.getAddress().getNumber(), res.getAddress().getNumber());
        Assert.assertEquals(personIn.getAddress().getExtras(), res.getAddress().getExtras());

        Assert.assertEquals(personIn.getAddress().getCity().getName(), res.getAddress().getCity().getName());
        Assert.assertEquals(personIn.getAddress().getCity().getPopulation(), res.getAddress().getCity().getPopulation());
        Assert.assertEquals(personIn.getAddress().getCity().isCapital(), res.getAddress().getCity().isCapital());

        Assert.assertEquals(personIn.getFirstName() + MappingInterceptorForImmutable.SALT, res.getBiography());
    }

    @Test
    public void given_bean_with_custom_mapper_when_update_graph_then_should_update_graph_with_custom() throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        MappingInterceptorInMaps mapper = Selma.getMapper(MappingInterceptorInMaps.class);

        PersonIn personIn = new PersonIn();
        personIn.setFirstName("Selma");
        personIn.setAddress(new AddressIn());
        personIn.getAddress().setCity(new CityIn());
        personIn.getAddress().getCity().setCapital(true);
        personIn.getAddress().getCity().setName("Paris");
        personIn.getAddress().getCity().setPopulation(3 * 1000 * 1000);

        personIn.getAddress().setPrincipal(true);
        personIn.getAddress().setNumber(55);
        personIn.getAddress().setStreet("rue de la truanderie");

        PersonOut out = new PersonOut();
        PersonOut res = mapper.mapWithInterceptor(personIn, out);

        Assert.assertNotNull(res);
        Assert.assertTrue(out == res);

        Assert.assertEquals(personIn.getAddress().getStreet(), res.getAddress().getStreet());
        Assert.assertEquals(personIn.getAddress().getNumber(), res.getAddress().getNumber());
        Assert.assertEquals(personIn.getAddress().getExtras(), res.getAddress().getExtras());

        Assert.assertEquals(personIn.getAddress().getCity().getName(), res.getAddress().getCity().getName());
        Assert.assertEquals(personIn.getAddress().getCity().getPopulation(), res.getAddress().getCity().getPopulation());
        Assert.assertEquals(personIn.getAddress().getCity().isCapital(), res.getAddress().getCity().isCapital());

        // Here we test the use of the real targetted salt
        Assert.assertEquals(personIn.getFirstName() + MappingInterceptorForMutable.SALT, res.getBiography());
    }

    @Test
    public void given_mapping_interceptor_in_mapper_overriden_in_maps_then_it_should_be_reported_unused_in_warnings() throws Exception {
        assertCompilationWarning(MappingInterceptorInMaps.class,"public interface MappingInterceptorInMaps {",
                "Custom interceptor method \"fr.xebia.extras.selma.it.custom.interceptor.NeverUsedMappingInterceptorInMapper.intercept\" is never used");
        Assert.assertEquals(2, compilationWarningCount());
    }

    @Test
    public void given_mapping_interceptor_in_maps_never_used_then_it_should_be_reported_unused_in_warnings() throws Exception {
        assertCompilationWarning(MappingInterceptorInMaps.class,"PersonOut mapWithInterceptor(PersonIn in);",
                "Custom interceptor method \"fr.xebia.extras.selma.it.custom.interceptor.NeverUsedMappingInterceptorInMaps.intercept\" is never used");
        Assert.assertEquals(2, compilationWarningCount());
    }

}
