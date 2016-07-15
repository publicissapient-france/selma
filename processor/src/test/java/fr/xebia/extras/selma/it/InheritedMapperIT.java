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
import fr.xebia.extras.selma.beans.ExtendedCityIn;
import fr.xebia.extras.selma.beans.ExtendedCityOut;
import fr.xebia.extras.selma.it.mappers.InheritedMapper;
import fr.xebia.extras.selma.it.utils.Compile;
import fr.xebia.extras.selma.it.utils.IntegrationTestBase;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
@Compile(withClasses = InheritedMapper.class)
public class InheritedMapperIT extends IntegrationTestBase {

    @Test
    public void given_extended_bean_selma_should_map_inherited_fields() {

        ExtendedCityIn in = new ExtendedCityIn();
        in.setCapital(true);
        in.setName("Paris");
        in.setPopulation(100);
        in.setAddedField("plus plus");

        InheritedMapper mapper = Selma.mapper(InheritedMapper.class);
        ExtendedCityOut res = mapper.asExtendedCity(in);

        Assert.assertEquals(in.getName(), res.getName());
        Assert.assertEquals(in.getPopulation(), res.getPopulation());
        Assert.assertEquals(in.isCapital(), res.isCapital());
        Assert.assertEquals(in.getAddedField(), res.getAddedField());

    }

}
