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
import fr.xebia.extras.selma.beans.CityIn;
import fr.xebia.extras.selma.beans.PersonIn;
import fr.xebia.extras.selma.beans.PersonOut;
import fr.xebia.extras.selma.it.mappers.UseDeclaredMethodMapper;
import fr.xebia.extras.selma.it.utils.Compile;
import fr.xebia.extras.selma.it.utils.IntegrationTestBase;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;

/**
 *
 */
@Compile(withClasses = UseDeclaredMethodMapper.class)
public class UseDeclaredMethodMapperIT extends IntegrationTestBase {

    @Test
    public void given_an_interface_with_2_methods_one_handling_nested_property_of_the_other_compilation_should_succeed() throws Exception {
        Assert.assertTrue(compilationSuccess());
    }

    @Test
    public void s3lm4_should_use_interface_declared_method_name_to_map_nested_properties() {

        PersonIn in = new PersonIn();
        in.setAddress(new AddressIn());
        in.getAddress().setPrincipal(true);
        in.getAddress().setNumber(1234565432);
        in.getAddress().setExtras(Arrays.asList("one", "two", "three"));
        in.getAddress().setCity(new CityIn());
        in.getAddress().getCity().setPopulation(13432234);
        in.getAddress().getCity().setName("TREZERGBV");
        in.getAddress().getCity().setCapital(false);
        in.setAddressBis(new AddressIn());

        UseDeclaredMethodMapper mapper = Selma.mapper(UseDeclaredMethodMapper.class);

        mapper = Mockito.spy(mapper);

        PersonOut res = mapper.asPersonOut(in);

        Mockito.verify(mapper).asPersonOut(in);
        Mockito.verify(mapper).asAddressOut(in.getAddress());
        Mockito.verify(mapper).asAddressOut(in.getAddressBis());

    }

}
