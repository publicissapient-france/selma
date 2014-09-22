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
import fr.xebia.extras.selma.beans.CityOut;
import fr.xebia.extras.selma.beans.DataSource;
import fr.xebia.extras.selma.it.mappers.*;
import fr.xebia.extras.selma.it.utils.Compile;
import fr.xebia.extras.selma.it.utils.IntegrationTestBase;
import org.junit.Assert;
import org.junit.Test;

import static fr.xebia.extras.selma.Selma.*;

/**
 */
@Compile(withClasses = {SelmaTestMapper.class, SelmaSourcedTestMapper.class, SelmaCustomTestMapper.class, CustomMapper.class})
public class SelmaIT extends IntegrationTestBase {


    @Test
    public void selma_static_should_return_same_mapper_in_two_times_call() {

        SelmaTestMapper mapper = Selma.getMapper(SelmaTestMapper.class);

        Assert.assertTrue(mapper == Selma.getMapper(SelmaTestMapper.class));

    }

    @Test
    public void selma_static_with_factory_should_return_same_mapper_in_two_times_call() {

        SelmaTestMapper mapper = Selma.getMapper(SelmaTestMapper.class);

        Assert.assertTrue(mapper == Selma.getMapper(SelmaTestMapper.class));
    }


    @Test
    public void selma_static_with_source_should_return_same_mapper_in_two_times_call() {

        DataSource dataSource = new DataSource();
        SelmaSourcedTestMapper mapper = mapper(SelmaSourcedTestMapper.class, dataSource);
        Assert.assertTrue(mapper == mapper(SelmaSourcedTestMapper.class, dataSource));
    }


    @Test(expected = IllegalArgumentException.class)
    public void selma_should_raise_illegal_given_not_fitting_source() {

        CityOut dataSource = new CityOut();
        SelmaSourcedTestMapper mapper = mapper(SelmaSourcedTestMapper.class, dataSource);
        Assert.assertTrue(mapper == mapper(SelmaSourcedTestMapper.class, dataSource));
    }


    @Test(expected = IllegalArgumentException.class)
    public void selma_should_raise_illegalArgException_on_not_existing_mapper() {

        BadMapperSignature mapper = Selma.getMapper(BadMapperSignature.class);
    }

    @Test
    public void given_a_single_mapper_class_selma_dsl_should_build_and_cache_it_for_later_use() {
        SelmaTestMapper mapper = builder(SelmaTestMapper.class).build();
        Assert.assertTrue(mapper == mapper(SelmaTestMapper.class));
    }

    @Test
    public void given_a_single_mapper_with_diasbled_cahce_class_selma_dsl_should_build_it_two_times() {
        SelmaTestMapper mapper = builder(SelmaTestMapper.class).disableCache().build();
        Assert.assertFalse(mapper == mapper(SelmaTestMapper.class));
    }

    @Test
    public void given_a_mapper_and_custom_source_class_selma_dsl_should_build_it() {
        CustomMapper customMapper = new CustomMapper();
        DataSource dataSource = new DataSource();
        SelmaCustomTestMapper mapper = builder(SelmaCustomTestMapper.class).withCustom(customMapper).withSources(dataSource).build();
        Assert.assertTrue(mapper == getMapper(SelmaCustomTestMapper.class, dataSource, customMapper));
    }

    @Test(expected = IllegalArgumentException.class)
    public void given_a_mapper_and_custom_class_when_needs_a_source_selma_dsl_should_raise_exception() {
        CustomMapper customMapper = new CustomMapper();
        DataSource dataSource = new DataSource();
        SelmaCustomTestMapper mapper = builder(SelmaCustomTestMapper.class).withCustom(customMapper).build();
        Assert.fail();
    }

}
