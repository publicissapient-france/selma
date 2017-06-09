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
package fr.xebia.extras.selma.it.tostring;

import fr.xebia.extras.selma.Selma;
import fr.xebia.extras.selma.it.mappers.NotNullMapper;
import fr.xebia.extras.selma.it.tostring.EnumToStringMapper.EnumContainer;
import fr.xebia.extras.selma.it.utils.Compile;
import fr.xebia.extras.selma.it.utils.IntegrationTestBase;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import static fr.xebia.extras.selma.it.tostring.EnumToStringMapper.EnumToString.CODE2;
import static org.junit.Assert.*;

/**
 *
 */
@Compile(withClasses = {EnumToStringMapper.class, PrimitivesOrBoxedToStringMapper.class})
public class ToStringMapperIT extends IntegrationTestBase {

    @Test
    public void given_enum_to_string_method_selma_should_return_String_representation_of_the_enum(){
        EnumToStringMapper mapper = Selma.builder(EnumToStringMapper.class).build();

        String res = mapper.enumToString(CODE2);

        assertThat(res, CoreMatchers.is(CODE2.toString()));

    }

    @Test
    public void given_embedded_enum_to_embedded_string_method_selma_should_return_String_representation_of_the_enum(){
        EnumToStringMapper mapper = Selma.builder(EnumToStringMapper.class).build();
        EnumContainer enumContainer = new EnumContainer();
        enumContainer.setCode(EnumToStringMapper.EnumToString.CODE1);

        EnumToStringMapper.StringContainer res = mapper.embeddedEnumToString(enumContainer);

        assertThat(res, CoreMatchers.notNullValue());
        assertThat(res.getCode(), CoreMatchers.is(EnumToStringMapper.EnumToString.CODE1.toString()));

    }


    @Test
    public void given_embedded_null_enum_to_embedded_string_method_selma_should_return_null(){
        EnumToStringMapper mapper = Selma.builder(EnumToStringMapper.class).build();
        EnumContainer enumContainer = new EnumContainer();

        EnumToStringMapper.StringContainer res = mapper.embeddedEnumToString(enumContainer);

        assertThat(res.getCode(), CoreMatchers.nullValue());
    }

    @Test
    public void given_embedded_update_enum_to_embedded_string_method_selma_should_return_string(){
        EnumToStringMapper mapper = Selma.builder(EnumToStringMapper.class).build();
        EnumContainer enumContainer = new EnumContainer();
        enumContainer.setCode(CODE2);
        EnumToStringMapper.StringContainer res = new EnumToStringMapper.StringContainer();
        res.setCode("CODE1");

        res = mapper.embeddedUpdateEnumToString(enumContainer, res);

        assertThat(res.getCode(), CoreMatchers.is(CODE2.toString()));
    }

    @Test
    public void given_embedded_update_enum_to_embedded_string_method_selma_should_return_null(){
        EnumToStringMapper mapper = Selma.builder(EnumToStringMapper.class).build();
        EnumContainer enumContainer = new EnumContainer();
        EnumToStringMapper.StringContainer res = new EnumToStringMapper.StringContainer();
        res.setCode("CODE1");

        res = mapper.embeddedUpdateEnumToString(enumContainer, res);

        assertThat(res.getCode(), CoreMatchers.nullValue());
    }


    @Test
    public void given_embedded_null_Boolean_to_embedded_string_method_selma_should_return_null(){
        PrimitivesOrBoxedToStringMapper mapper = Selma.builder(PrimitivesOrBoxedToStringMapper.class).build();
        PrimitivesOrBoxedToStringMapper.BoxedContainer boxedContainer = new PrimitivesOrBoxedToStringMapper.BoxedContainer();

        PrimitivesOrBoxedToStringMapper.StringContainer res = mapper.embeddedBoxedToString(boxedContainer);

        assertThat(res.getFlag(), CoreMatchers.nullValue());
    }

    @Test
    public void given_embedded_update_null_Boolean_to_embedded_string_method_selma_should_return_null(){
        PrimitivesOrBoxedToStringMapper mapper = Selma.builder(PrimitivesOrBoxedToStringMapper.class).build();
        PrimitivesOrBoxedToStringMapper.BoxedContainer boxedContainer = new PrimitivesOrBoxedToStringMapper.BoxedContainer();
        PrimitivesOrBoxedToStringMapper.StringContainer res = new PrimitivesOrBoxedToStringMapper.StringContainer();
        res.setFlag("Flag");
        res = mapper.embeddedBoxedToString(boxedContainer, res);

        // Ignore null value is enabled so out flag should not change
        assertThat(res.getFlag(), CoreMatchers.is("Flag"));

        boxedContainer.setFlag(Boolean.TRUE);
        res = mapper.embeddedBoxedToString(boxedContainer, res);

        // Ignore null value is enabled so out flag should  change
        assertThat(res.getFlag(), CoreMatchers.is("true"));
    }


    @Test
    public void given_embedded_update_Boolean_to_embedded_string_method_selma_should_return_boolean_value(){
        PrimitivesOrBoxedToStringMapper mapper = Selma.builder(PrimitivesOrBoxedToStringMapper.class).build();
        PrimitivesOrBoxedToStringMapper.BoxedContainer boxedContainer = new PrimitivesOrBoxedToStringMapper.BoxedContainer();
        boxedContainer.setFlag(Boolean.TRUE);
        PrimitivesOrBoxedToStringMapper.StringContainer res = new PrimitivesOrBoxedToStringMapper.StringContainer();
        res = mapper.embeddedBoxedToString(boxedContainer, res);

        assertThat(res.getFlag(), CoreMatchers.is(Boolean.TRUE.toString()));
    }

    @Test
    public void given_embedded_Boolean_to_embedded_string_method_selma_should_return_boolean_value(){
        PrimitivesOrBoxedToStringMapper mapper = Selma.builder(PrimitivesOrBoxedToStringMapper.class).build();
        PrimitivesOrBoxedToStringMapper.BoxedContainer boxedContainer = new PrimitivesOrBoxedToStringMapper.BoxedContainer();
        boxedContainer.setFlag(Boolean.TRUE);
        PrimitivesOrBoxedToStringMapper.StringContainer res = mapper.embeddedBoxedToString(boxedContainer);

        assertThat(res.getFlag(), CoreMatchers.is(Boolean.TRUE.toString()));
    }


    @Test
    public void given_embedded_int_to_embedded_string_method_selma_should_return_intString(){
        PrimitivesOrBoxedToStringMapper mapper = Selma.builder(PrimitivesOrBoxedToStringMapper.class).build();
        PrimitivesOrBoxedToStringMapper.IntContainer intContainer = new PrimitivesOrBoxedToStringMapper.IntContainer();
        intContainer.setFlag(42);

        PrimitivesOrBoxedToStringMapper.StringContainer res = mapper.embeddedPrimitiveToString(intContainer);

        assertThat(res.getFlag(), CoreMatchers.is("42"));
    }

    @Test
    public void given_embedded_undef_int_to_embedded_string_method_selma_should_return_default_int(){
        PrimitivesOrBoxedToStringMapper mapper = Selma.builder(PrimitivesOrBoxedToStringMapper.class).build();
        PrimitivesOrBoxedToStringMapper.IntContainer intContainer = new PrimitivesOrBoxedToStringMapper.IntContainer();

        PrimitivesOrBoxedToStringMapper.StringContainer res = mapper.embeddedPrimitiveToString(intContainer);

        assertThat(res.getFlag(), CoreMatchers.is("0"));
    }

    @Test
    public void given_embedded_update_int_to_embedded_string_method_selma_should_return_intString(){
        PrimitivesOrBoxedToStringMapper mapper = Selma.builder(PrimitivesOrBoxedToStringMapper.class).build();
        PrimitivesOrBoxedToStringMapper.IntContainer intContainer = new PrimitivesOrBoxedToStringMapper.IntContainer();
        intContainer.setFlag(42);

        PrimitivesOrBoxedToStringMapper.StringContainer res = new PrimitivesOrBoxedToStringMapper.StringContainer();
        res = mapper.embeddedPrimitiveToString(intContainer, res);

        assertThat(res.getFlag(), CoreMatchers.is("42"));
    }

    @Test
    public void given_embedded_update_undef_int_to_embedded_string_method_selma_should_return_default_int(){
        PrimitivesOrBoxedToStringMapper mapper = Selma.builder(PrimitivesOrBoxedToStringMapper.class).build();
        PrimitivesOrBoxedToStringMapper.IntContainer intContainer = new PrimitivesOrBoxedToStringMapper.IntContainer();

        PrimitivesOrBoxedToStringMapper.StringContainer res = new PrimitivesOrBoxedToStringMapper.StringContainer();
        res = mapper.embeddedPrimitiveToString(intContainer, res);

        assertThat(res.getFlag(), CoreMatchers.is("0"));
    }








}
