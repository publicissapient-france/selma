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
    public void should_raise_compilation_error_when_no_default_constructor_is_present() throws Exception {

        assertCompilationError(NoDefaultConstructorCustomMapper.class, "public class NoDefaultConstructorCustomMapper {", "No default public constructor found in custom mapping class");
    }


}
