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

import fr.xebia.extras.selma.it.mappers.NotSupportedMapping;
import fr.xebia.extras.selma.it.utils.Compile;
import fr.xebia.extras.selma.it.utils.IntegrationTestBase;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: slemesle
 * Date: 22/11/2013
 * Time: 13:5
 */
@Compile(withClasses = NotSupportedMapping.class, shouldFail = true)
public class NotSupportedMappingIT extends IntegrationTestBase {

    @Test
    public void not_supported_mapping_without_ignore_should_fail_compilation() throws Exception {

        assertCompilationError(NotSupportedMapping.class, "File map(File in);", "Failed to generate mapping method for type java.io.File to java.io.File");
    }
}
