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
import fr.xebia.extras.selma.beans.EnumBeanIn;
import fr.xebia.extras.selma.beans.EnumBeanOut;
import fr.xebia.extras.selma.beans.EnumIn;
import fr.xebia.extras.selma.it.mappers.SameEnumMapperIt;
import fr.xebia.extras.selma.it.utils.Compile;
import fr.xebia.extras.selma.it.utils.IntegrationTestBase;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by slemesle on 12/03/2014.
 */
@Compile(withClasses = {SameEnumMapperIt.class})
public class SameEnumMapperIT extends IntegrationTestBase {

    @Test
    public void given_a_mapper_for_same_enum_mapper_should_return_same_value() {
        SameEnumMapperIt mapper = Selma.mapper(SameEnumMapperIt.class);

        EnumIn in = EnumIn.VAL_1;
        EnumIn res = mapper.asEnumIn(in);

        Assert.assertEquals(EnumIn.VAL_1, res);
        Assert.assertTrue(res == in);
    }

    @Test
    public void given_a_mapper_for_same_enum_mapper_when_null_should_return_null() {
        SameEnumMapperIt mapper = Selma.mapper(SameEnumMapperIt.class);

        EnumIn res = mapper.asEnumIn(null);
        Assert.assertNull(res);
    }

    @Test
    public void given_a_mapper_for_bean_with_same_enum_field_mapper_should_return_same_value() {
        SameEnumMapperIt mapper = Selma.mapper(SameEnumMapperIt.class);

        EnumBeanIn in = new EnumBeanIn();
        in.setEnumIn(EnumIn.VAL_1);

        EnumBeanOut res = mapper.asEnumBean(in);

        Assert.assertEquals(EnumIn.VAL_1, res.getEnumIn());
        Assert.assertTrue(res.getEnumIn() == in.getEnumIn());
    }

    @Test
    public void given_a_mapper_for_bean_with_same_enum_mapper_when_null_should_return_null() {
        SameEnumMapperIt mapper = Selma.mapper(SameEnumMapperIt.class);

        EnumBeanOut res = mapper.asEnumBean(new EnumBeanIn());
        Assert.assertNull(res.getEnumIn());
    }


}
