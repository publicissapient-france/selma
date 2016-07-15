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
import fr.xebia.extras.selma.it.generic.beans.GenericBean;
import fr.xebia.extras.selma.it.generic.beans.GenericListBean;
import fr.xebia.extras.selma.it.generic.beans.SimpleDTO;
import fr.xebia.extras.selma.it.generic.beans.SimpleDomain;
import fr.xebia.extras.selma.it.utils.Compile;
import fr.xebia.extras.selma.it.utils.IntegrationTestBase;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

/**
 * This class checks that Selma resolves properly defined generic properties of a bean
 * This test Bug #101 - https://github.com/xebia-france/selma/issues/101
 */
@Compile(withClasses = {GenericSourceBeanMapper.class, GenericBean.class, SimpleDomain.class, SimpleDTO.class, GenericListBean.class})
public class GenericSourceBeanIT extends IntegrationTestBase {

    @Test
    public void beanMapper_should_map_properties_resolving_generics() throws Exception {

        GenericSourceBeanMapper mapper = Selma.mapper(GenericSourceBeanMapper.class);

        GenericBean<SimpleDomain> source = new GenericBean<SimpleDomain>();
        source.setProperty(new SimpleDomain());
        source.getProperty().setName("Domain Name");

        GenericBean<SimpleDTO> res =  mapper.asSimpleDTO(source);

        Assert.assertNotNull("Mapping GenericBean result should not be null", res);
        Assert.assertNotNull("Mapping Property result should not be null", res.getProperty());
        Assert.assertEquals(source.getProperty().getName(), res.getProperty().getName());
    }


    @Test
    public void beanMapper_should_map_collections_resolving_generics() throws Exception {

        GenericSourceBeanMapper mapper = Selma.mapper(GenericSourceBeanMapper.class);

        GenericListBean<SimpleDomain> source = new GenericListBean<SimpleDomain>();
        source.setPropertyList(new ArrayList<SimpleDomain>());
        source.getPropertyList().add(new SimpleDomain("Domain Name"));

        GenericListBean<SimpleDTO> res =  mapper.asSimpleDTO(source);

        Assert.assertNotNull("Mapping GenericBean result should not be null", res);
        Assert.assertNotNull("Mapping Property result should not be null", res.getPropertyList());
        Assert.assertEquals(source.getPropertyList().size(), res.getPropertyList().size());
        Assert.assertEquals(source.getPropertyList().get(0).getName(), res.getPropertyList().get(0).getName());

    }


}
