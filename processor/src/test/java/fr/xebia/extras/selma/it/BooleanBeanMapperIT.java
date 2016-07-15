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
import fr.xebia.extras.selma.beans.BooleanBean;
import fr.xebia.extras.selma.it.mappers.BooleanBeanMapper;
import fr.xebia.extras.selma.it.utils.Compile;
import fr.xebia.extras.selma.it.utils.IntegrationTestBase;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: slemesle
 * Date: 29/11/2013
 * Time: 11:18
 * To change this template use File | Settings | File Templates.
 */
@Compile(withClasses = BooleanBeanMapper.class)
public class BooleanBeanMapperIT extends IntegrationTestBase {

    @Test
    public void should_map_primitive_boolean_with_is_getter() {
        BooleanBean in = new BooleanBean();
        in.setPrimitive(true);

        BooleanBeanMapper mapper = Selma.mapper(BooleanBeanMapper.class);
        BooleanBean res = mapper.asBooleanBean(in);
        Assert.assertTrue(res.isPrimitive());
    }

    @Test
    public void should_map_boxed_boolean_with_is_getter() {
        BooleanBean in = new BooleanBean();
        in.setBoxedIs(true);

        BooleanBeanMapper mapper = Selma.mapper(BooleanBeanMapper.class);
        BooleanBean res = mapper.asBooleanBean(in);
        Assert.assertTrue(res.isBoxedIs());
    }

    @Test
    public void should_map_boxed_boolean_with_get_getter() {
        BooleanBean in = new BooleanBean();
        in.setBoxed(true);

        BooleanBeanMapper mapper = Selma.mapper(BooleanBeanMapper.class);
        BooleanBean res = mapper.asBooleanBean(in);
        Assert.assertTrue(res.getBoxed());
    }

    @Test
    public void should_map_primitive_boolean_with_get_getter() {
        BooleanBean in = new BooleanBean();
        in.setPrimitiveIs(true);

        BooleanBeanMapper mapper = Selma.mapper(BooleanBeanMapper.class);
        BooleanBean res = mapper.asBooleanBean(in);
        Assert.assertTrue(res.getPrimitiveIs());
    }

    @Test
    public void should_map_primitive_boolean_to_boxed_boolean() {

        BooleanBeanMapper mapper = Selma.mapper(BooleanBeanMapper.class);
        Boolean res = mapper.asBoolean(true);
        Assert.assertTrue(res);
    }

    @Test
    public void should_map_boxed_boolean_to_primitive_boolean() {

        BooleanBeanMapper mapper = Selma.mapper(BooleanBeanMapper.class);
        boolean res = mapper.asBoolean(Boolean.TRUE);
        Assert.assertTrue(res);
    }


}
