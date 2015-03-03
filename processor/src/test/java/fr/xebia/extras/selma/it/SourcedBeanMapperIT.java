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
import fr.xebia.extras.selma.it.mappers.*;
import fr.xebia.extras.selma.it.utils.Compile;
import fr.xebia.extras.selma.it.utils.IntegrationTestBase;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * Test selma uses the factory
 */
@Compile(withClasses = {BeanMapper.class, CustomMapperSupport.class, CustomMapper.class,
        CustomMapperWithoutSource.class, SourcedBeanMapper.class, CustomSourcedBeanMapper.class})
public class SourcedBeanMapperIT extends IntegrationTestBase {


    @Test
    public void should_use_source_for_bean_instantiation_with_matching_constructor() {

        AddressIn in = new AddressIn();

        in.setCity(new CityIn());
        in.setExtras(Arrays.asList("one", "two", null, "three"));
        in.setNumber(1337);
        in.setStreet("HtwoGTwo");
        in.setPrincipal(false);
        in.getCity().setCapital(true);
        in.getCity().setName("Paris");
        in.getCity().setPopulation(13371337);
        DataSource dataSource = new DataSource();

        SourcedBeanMapper mapper = Selma.mapper(SourcedBeanMapper.class, dataSource);

        AddressOutWithDataSource res = mapper.asAddressOutWithDataSource(in);

        Assert.assertNotNull(res);
        Assert.assertNotNull(res.dataSource);
        Assert.assertNotNull(res.getCity());
        Assert.assertNotNull(res.getCity().dataSource);
    }


    @Test
    public void should_use_source_for_bean_instantiation_with_default_constructor() {

        AddressIn in = new AddressIn();

        in.setCity(new CityIn());
        in.setExtras(Arrays.asList("one", "two", null, "three"));
        in.setNumber(1337);
        in.setStreet("HtwoGTwo");
        in.setPrincipal(false);
        in.getCity().setCapital(true);
        in.getCity().setName("Paris");
        in.getCity().setPopulation(13371337);
        DataSource dataSource = new DataSource();

        SourcedBeanMapper mapper = Selma.mapper(SourcedBeanMapper.class, dataSource);

        AddressOut res = mapper.asAddressOut(in);

        Assert.assertNotNull(res);
        Assert.assertNotNull(res.getCity());
    }


    @Test
    public void should_use_source_for_custom_mapper_instantiation_with_matching_constructor() {

        AddressIn in = new AddressIn();

        in.setCity(new CityIn());
        in.setExtras(Arrays.asList("one", "two", null, "three"));
        in.setNumber(1337);
        in.setStreet("HtwoGTwo");
        in.setPrincipal(false);
        in.getCity().setCapital(true);
        in.getCity().setName("Paris");
        in.getCity().setPopulation(13371337);
        DataSource dataSource = new DataSource();

        CustomSourcedBeanMapper mapper = Selma.mapper(CustomSourcedBeanMapper.class, dataSource);

        AddressOutWithDataSource res = mapper.asAddressOut(in);

        Assert.assertNotNull(res);
        Assert.assertNotNull(res.dataSource);
        Assert.assertNotNull(res.getCity());
        Assert.assertNotNull(res.getCity().dataSource);
        Assert.assertEquals(in.getCity().getName() + " Mapped by CustomMapper", res.getCity().getName());
        Assert.assertEquals(in.getCity().getPopulation() + 10000, res.getCity().getPopulation());
        Assert.assertEquals(in.getCity().isCapital(), res.getCity().isCapital());

    }

    @Test
    public void should_use_source_for_custom_mapper_instantiation_with_default_constructor() {

        AddressIn in = new AddressIn();

        in.setCity(new CityIn());
        in.setExtras(Arrays.asList("one", "two", null, "three"));
        in.setNumber(1337);
        in.setStreet("HtwoGTwo");
        in.setPrincipal(false);
        in.getCity().setCapital(true);
        in.getCity().setName("Paris");
        in.getCity().setPopulation(13371337);
        DataSource dataSource = new DataSource();

        CustomSourcedBeanMapper mapper = Selma.mapper(CustomSourcedBeanMapper.class, dataSource);

        AddressOutWithDataSource res = mapper.asAddressOutWithoutDataSource(in);

        Assert.assertNotNull(res);
        Assert.assertNotNull(res.dataSource);
        Assert.assertNotNull(res.getCity());
        Assert.assertEquals(in.getCity().getName() + " Mapped by CustomMapper", res.getCity().getName());
        Assert.assertEquals(in.getCity().getPopulation() + 10000, res.getCity().getPopulation());
        Assert.assertEquals(in.getCity().isCapital(), res.getCity().isCapital());

    }

}
