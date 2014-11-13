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

import fr.xebia.extras.selma.it.mappers.BadCustomMapper;
import fr.xebia.extras.selma.it.mappers.EmptyCustomMapper;
import fr.xebia.extras.selma.it.mappers.FailingCustomMapper;
import fr.xebia.extras.selma.it.mappers.NoDefaultConstructorCustomMapper;
import fr.xebia.extras.selma.it.utils.Compile;
import fr.xebia.extras.selma.it.utils.IntegrationTestBase;
import org.junit.Test;

/**
 * Verifies compilation time validation for CustomMapping
 */
@Compile(withClasses = {FailingCustomMapper.class, BadCustomMapper.class, EmptyCustomMapper.class, NoDefaultConstructorCustomMapper.class}, shouldFail = true)
public class FailingCustomMapperIT extends IntegrationTestBase {

    @Test
    public void should_raise_compilation_error_when_no_valid_mapping_method_found() throws Exception {

        assertCompilationError(EmptyCustomMapper.class, "public class EmptyCustomMapper {", "No valid mapping method found in custom selma class ");
    }

    @Test
    public void should_raise_warning_for_not_public_valid_method() throws Exception {

        assertCompilationError(BadCustomMapper.class, "public abstract class BadCustomMapper {", "No valid mapping method found in custom selma class ");
        assertCompilationWarning(BadCustomMapper.class, "CityOut notPublicStaticMethod(CityIn in) {", "Custom mapping method should be *public* (Fix modifiers of the method) on notPublicStaticMethod");

    }

    @Test
    public void should_raise_warning_for_void_method() throws Exception {

        assertCompilationError(BadCustomMapper.class, "public abstract class BadCustomMapper {", "No valid mapping method found in custom selma class ");
        assertCompilationWarning(BadCustomMapper.class, "public void voidMethod(CityIn in) {", "Custom mapping method should have a return type and one or two parameters and interceptor method should be void and have two parameters (Fix method signature) on voidMethod");
    }

    @Test
    public void should_raise_warning_for_method_not_having_1_parameter() throws Exception {

        assertCompilationError(BadCustomMapper.class, "public abstract class BadCustomMapper {", "No valid mapping method found in custom selma class ");
        assertCompilationWarning(BadCustomMapper.class, "public CityOut noParameterMethod() {", "Custom mapping method should have a return type and one or two parameters and interceptor method should be void and have two parameters (Fix method signature) on noParameterMethod");
        assertCompilationWarning(BadCustomMapper.class, "public CityOut twoParameterMethod(CityIn in, DataSource dataSource) {", "Custom mapping method should have a return type and one or two parameters and interceptor method should be void and have two parameters (Fix method signature) on twoParameterMethod");
        assertCompilationWarning(BadCustomMapper.class, "public CityOut threeParameterMethod(CityIn in, CityIn in2, CityIn in3) {", "Custom mapping method should have a return type and one or two parameters and interceptor method should be void and have two parameters (Fix method signature) on threeParameterMethod");
    }

    @Test
    public void should_raise_warning_for_static_methods() throws Exception {

        assertCompilationError(BadCustomMapper.class, "public abstract class BadCustomMapper {", "No valid mapping method found in custom selma class ");
        assertCompilationWarning(BadCustomMapper.class, "public static CityOut staticMethod(CityIn in) {", "Custom mapping method can not be *static* (Fix modifiers of the method) on staticMethod");
    }

    @Test
    public void should_raise_warning_for_abstract_method() throws Exception {

        assertCompilationError(BadCustomMapper.class, "public abstract class BadCustomMapper {", "No valid mapping method found in custom selma class ");
        assertCompilationWarning(BadCustomMapper.class, "public abstract CityOut abstractMethod(CityIn in);", "Custom mapping method can not be *abstract* (Fix modifiers of the method) on abstractMethod");
    }

    @Test
    public void should_raise_compilation_error_when_no_default_constructor_is_present() throws Exception {

        assertCompilationError(NoDefaultConstructorCustomMapper.class, "public class NoDefaultConstructorCustomMapper {", "No default public constructor found in custom mapping class");
    }


}
