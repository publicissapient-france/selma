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

import fr.xebia.extras.selma.beans.CityOut;
import fr.xebia.extras.selma.beans.DataSource;
import fr.xebia.extras.selma.it.mappers.BadMapperSignature;
import fr.xebia.extras.selma.it.mappers.SelmaSourcedTestMapper;
import fr.xebia.extras.selma.it.mappers.SelmaTestMapper;
import fr.xebia.extras.selma.it.utils.Compile;
import fr.xebia.extras.selma.it.utils.IntegrationTestBase;
import fr.xebia.extras.selma.Selma;
import org.junit.Assert;
import org.junit.Test;

/**
 */
@Compile(withClasses = {SelmaTestMapper.class, SelmaSourcedTestMapper.class})
public class SelmaIT extends IntegrationTestBase {


    @Test
    public void xmapper_static_should_return_same_mapper_in_two_times_call(){

        SelmaTestMapper mapper = Selma.mapper(SelmaTestMapper.class);

        Assert.assertTrue(mapper == Selma.mapper(SelmaTestMapper.class));

    }

    @Test
    public void xmapper_static_with_factory_should_return_same_mapper_in_two_times_call(){

        SelmaTestMapper mapper = Selma.getMapper(SelmaTestMapper.class);

        Assert.assertTrue(mapper == Selma.getMapper(SelmaTestMapper.class));
    }


    @Test
    public void xmapper_static_with_source_should_return_same_mapper_in_two_times_call(){


        DataSource dataSource = new DataSource();
        SelmaSourcedTestMapper mapper = Selma.mapper(SelmaSourcedTestMapper.class, dataSource);
        Assert.assertTrue(mapper == Selma.mapper(SelmaSourcedTestMapper.class, dataSource));

    }


    @Test(expected = IllegalArgumentException.class)
    public void xmapper_should_raise_illegal_given_not_fitting_source(){

        CityOut dataSource = new CityOut();
        SelmaSourcedTestMapper mapper = Selma.mapper(SelmaSourcedTestMapper.class, dataSource);
        Assert.assertTrue(mapper == Selma.mapper(SelmaSourcedTestMapper.class, dataSource));
    }


    @Test(expected = IllegalArgumentException.class)
    public void xmapper_should_raise_illegalArgException_on_not_existing_mapper(){

        BadMapperSignature mapper = Selma.mapper(BadMapperSignature.class);
    }






}
