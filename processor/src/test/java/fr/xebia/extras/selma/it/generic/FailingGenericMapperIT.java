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
package fr.xebia.extras.selma.it.generic;

import fr.xebia.extras.selma.it.utils.Compile;
import fr.xebia.extras.selma.it.utils.IntegrationTestBase;
import org.junit.Test;

/**
 *
 */
@Compile(withClasses = PageListMapper.class, withPackage = "fr.xebia.extras.selma.it.generic.beans", shouldFail = true)
public class FailingGenericMapperIT extends IntegrationTestBase {

    @Test
    public void mapper_with_undefined_type_parameters_should_fail() throws Exception {
        assertCompilationError(PageListMapper.class, "public interface PageListMapper<T extends MongoEntity, S> {",
                "Generic type parameters are not supported on mapper class or interface");
    }

}
