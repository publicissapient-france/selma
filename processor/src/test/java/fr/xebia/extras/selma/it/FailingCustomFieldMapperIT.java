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

import fr.xebia.extras.selma.it.mappers.FailingCustomFieldMapper;
import fr.xebia.extras.selma.it.utils.Compile;
import fr.xebia.extras.selma.it.utils.IntegrationTestBase;
import org.junit.Test;

/**
 * Created by slemesle on 12/03/2014.
 */
@Compile(withClasses = {FailingCustomFieldMapper.class}, shouldFail = true)
public class FailingCustomFieldMapperIT extends IntegrationTestBase {

    @Test
    public void given_a_mapper_with_field_mapping_when_field_does_not_contains_2_string_then_should_raise_error() throws Exception {

        assertCompilationError(FailingCustomFieldMapper.class,
                "public interface FailingCustomFieldMapper {",
                "Invalid @Field signature, bad value count in value array:" +
                        " @fr.xebia.extras.selma.Field({\"prenom\", \"firstname\", \"ddd\"})");
        assertCompilationError(FailingCustomFieldMapper.class,
                "SimplePersonDto asPersonDtoReverseName(SimplePerson in);",
                "Invalid @Field signature, bad value count in value array: @fr.xebia.extras.selma.Field({\"nom\"})");
        assertCompilationError(FailingCustomFieldMapper.class,
                "AddressOut asAdress(AddressIn in);",
                "Invalid @Field signature, empty string for fields are forbidden: @fr.xebia.extras.selma.Field({\"empty\", \"\"})");

    }


}
