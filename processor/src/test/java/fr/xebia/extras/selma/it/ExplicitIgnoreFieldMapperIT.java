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
import fr.xebia.extras.selma.it.mappers.IgnoreFieldMapper;
import fr.xebia.extras.selma.it.utils.Compile;
import fr.xebia.extras.selma.it.utils.IntegrationTestBase;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;

/**
 *
 */
@Compile(withClasses = IgnoreFieldMapper.class)
public class ExplicitIgnoreFieldMapperIT extends IntegrationTestBase {

    @Test
    public void given_explicit_ignore_fields_not_existing_for_class_compiler_should_report_warning() throws Exception {

        assertCompilationWarning(IgnoreFieldMapper.class, "public interface IgnoreFieldMapper {", "Ignored field \"notUsedClassField\" is never used");
    }

    @Test
    public void given_explicit_ignore_fields_not_existing_for_method_compiler_should_report_warning() throws Exception {

        assertCompilationWarning(IgnoreFieldMapper.class, "public interface IgnoreFieldMapper {", "Ignored field \"notUsedMethodField\" is never used");
    }

    @Test
    public void given_explicit_ignore_fields_for_method_generated_mapper_should_ignore_them() {

        PersonIn in = new PersonIn();
        in.setAge(11);
        in.setBirthDay(new Date());
        in.setEnumIn(EnumIn.VAL_1);
        in.setIndices(new Long[]{1l, 2l, 3l});
        in.setMale(true);
        in.setFirstName("john");
        in.setLastName("Doe");
        in.setAddress(new AddressIn());
        in.getAddress().setCity(new CityIn());
        in.getAddress().setExtras(Arrays.asList("134", "1234", "543"));
        in.getAddress().setPrincipal(false);
        in.getAddress().setNumber(42);
        in.getAddress().setStreet("Victor Hugo");
        in.getAddress().getCity().setName("Paris");
        in.getAddress().getCity().setCapital(true);
        in.getAddress().getCity().setPopulation(10);

        PersonOut res = Selma.getMapper(IgnoreFieldMapper.class).asPersonOut(in);

        Assert.assertNotNull(res);
        Assert.assertNull(res.getAddress().getExtras());
        Assert.assertNull(res.getBiography());
        Assert.assertNotNull(res.getAddress().getCity());
        Assert.assertNull(res.getAddress().getCity().getName());
    }

    @Test
    public void given_explicit_ignore_fields_for_interface_generated_mapper_should_ignore_them() {

        CityIn in = new CityIn();
        in.setName("Paris");
        in.setPopulation(120);
        in.setCapital(true);
        CityOut res = Selma.getMapper(IgnoreFieldMapper.class).asCityOut(in);

        Assert.assertNotNull(res);
        Assert.assertNull(res.getName());
        Assert.assertEquals(in.getPopulation(), res.getPopulation());
        Assert.assertEquals(in.isCapital(), res.isCapital());
    }

    @Test
    public void given_explicit_ignore_fields_for_update_graph_method_generated_mapper_should_ignore_them() {

        PersonIn in = new PersonIn();
        in.setAge(11);
        in.setBirthDay(new Date());
        in.setEnumIn(EnumIn.VAL_1);
        in.setIndices(new Long[]{1l, 2l, 3l});
        in.setMale(true);
        in.setFirstName("john");
        in.setLastName("Doe");
        in.setAddress(new AddressIn());
        in.getAddress().setCity(new CityIn());
        in.getAddress().setExtras(Arrays.asList("134", "1234", "543"));
        in.getAddress().setPrincipal(false);
        in.getAddress().setNumber(42);
        in.getAddress().setStreet("Victor Hugo");
        in.getAddress().getCity().setName("Paris");
        in.getAddress().getCity().setCapital(true);
        in.getAddress().getCity().setPopulation(10);

        PersonOut dest = new PersonOut();
        PersonOut res = Selma.getMapper(IgnoreFieldMapper.class).asPersonOut(in, dest);

        Assert.assertNotNull(res);
        Assert.assertNull(res.getAddress().getExtras());
        Assert.assertNull(res.getBiography());
        Assert.assertNotNull(res.getAddress().getCity());
        Assert.assertNull(res.getAddress().getCity().getName());
    }

    @Test
    public void given_explicit_ignore_fields_for_interface_on_update_graph_generated_mapper_should_ignore_them() {

        CityIn in = new CityIn();
        in.setName("Paris");
        in.setPopulation(120);
        in.setCapital(true);

        CityOut dest = new CityOut();
        CityOut res = Selma.getMapper(IgnoreFieldMapper.class).asCityOut(in, dest);

        Assert.assertNotNull(res);
        Assert.assertNull(res.getName());
        Assert.assertEquals(in.getPopulation(), res.getPopulation());
        Assert.assertEquals(in.isCapital(), res.isCapital());
    }


}
