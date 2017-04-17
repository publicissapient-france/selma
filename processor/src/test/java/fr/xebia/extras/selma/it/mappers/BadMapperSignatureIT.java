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
package fr.xebia.extras.selma.it.mappers;

import fr.xebia.extras.selma.it.utils.Compile;
import fr.xebia.extras.selma.it.utils.IntegrationTestBase;
import org.junit.Test;

/**
 *
 */
@Compile(withClasses = BadMapperSignature.class, shouldFail = true)
public class BadMapperSignatureIT extends IntegrationTestBase {

    @Test
    public void bad_mapper_signature_compilation_should_fail_on_no_in_parameters() throws Exception {

        assertCompilationError(BadMapperSignature.class, "String mapString();", "@Mapper method mapString can not have less than one parameter");

    }

    @Test
    public void bad_mapper_signature_compilation_should_fail_on_void() throws Exception {

        assertCompilationError(BadMapperSignature.class, "void mapTutu(String in);", "@Mapper method mapTutu can not return void");

    }

}
