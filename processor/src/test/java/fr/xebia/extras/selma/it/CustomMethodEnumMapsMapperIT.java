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
package fr.xebia.extras.selma.it;

import fr.xebia.extras.selma.Selma;
import fr.xebia.extras.selma.beans.EnumA;
import fr.xebia.extras.selma.beans.EnumB;
import fr.xebia.extras.selma.beans.EnumBeanA;
import fr.xebia.extras.selma.beans.EnumBeanB;
import fr.xebia.extras.selma.it.mappers.CustomMethodEnumMapsMapper;
import fr.xebia.extras.selma.it.mappers.CustomMethodEnumNoDefaultMapsMapper;
import fr.xebia.extras.selma.it.utils.Compile;
import fr.xebia.extras.selma.it.utils.IntegrationTestBase;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by slemesle on 12/03/2014.
 */
@Compile(withClasses = {CustomMethodEnumMapsMapper.class, CustomMethodEnumNoDefaultMapsMapper.class})
public class CustomMethodEnumMapsMapperIT extends IntegrationTestBase {


    @Test
    public void given_a_mapper_with_unused_method_enumMapper_when_compiled_then_compilation_should_raise_a_warning_for_unused_enumMapper() throws Exception {

        assertCompilationWarning(CustomMethodEnumMapsMapper.class, "CityOut asEnumBeanIn(CityIn in);", "@EnumMapper(from=fr.xebia.extras.selma.beans.EnumIn, to=fr.xebia.extras.selma.beans.EnumOut) is never used");
    }

    @Test
    public void given_a_mapper_for_2_enum_with_default_value_on_method_when_input_is_not_mappable_then_default_value_should_be_used() {
        CustomMethodEnumMapsMapper mapper = Selma.mapper(CustomMethodEnumMapsMapper.class);

        EnumBeanA source = new EnumBeanA();
        source.setMyEnum(EnumA.B);

        EnumBeanB res = mapper.asEnumBeanB(source);

        Assert.assertEquals(EnumB.C, res.getMyEnum());
    }


    @Test
    public void given_a_mapper_update_graph_for_2_enum_with_default_value_on_method_when_input_is_not_mappable_then_default_value_should_be_used() {
        CustomMethodEnumMapsMapper mapper = Selma.mapper(CustomMethodEnumMapsMapper.class);

        EnumBeanA source = new EnumBeanA();
        source.setMyEnum(EnumA.B);

        EnumBeanB res = mapper.asEnumB(source, null);

        Assert.assertEquals(EnumB.C, res.getMyEnum());
    }


    @Test
    public void given_a_mapper_for_2_enum_without_default_value_on_method_when_input_is_not_mappable_then_default_to_null() {
        CustomMethodEnumNoDefaultMapsMapper mapper = Selma.mapper(CustomMethodEnumNoDefaultMapsMapper.class);

        EnumBeanA source = new EnumBeanA();
        source.setMyEnum(EnumA.B);

        EnumBeanB res = mapper.asEnumBeanBNoDefault(source);

        Assert.assertNull(res.getMyEnum());
    }

    @Test
    public void given_a_mapper_update_graph_for_2_enum_without_default_value_on_method_when_input_is_not_mappable_then_default_to_null() {
        CustomMethodEnumNoDefaultMapsMapper mapper = Selma.mapper(CustomMethodEnumNoDefaultMapsMapper.class);

        EnumBeanA source = new EnumBeanA();
        source.setMyEnum(EnumA.B);

        EnumBeanB res = mapper.asEnumBeanBNoDefault(source, null);

        Assert.assertNull(res.getMyEnum());
    }


}
