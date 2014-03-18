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
import fr.xebia.extras.selma.it.mappers.IncompleteEnumMatchingMapper;
import fr.xebia.extras.selma.it.utils.Compile;
import fr.xebia.extras.selma.it.utils.IntegrationTestBase;
import org.junit.Test;

/**
 * Created by slemesle on 12/03/2014.
 */
@Compile(withClasses = IncompleteEnumMatchingMapper.class, shouldFail = true)
public class IncompleteEnumMapperIT extends IntegrationTestBase {

    @Test(expected = IllegalArgumentException.class)
    public void given_a_mapper_for_2_enums_where_one_is_subset_of_the_other_compilation_should_fail(){
        IncompleteEnumMatchingMapper mapper = Selma.mapper(IncompleteEnumMatchingMapper.class);

    }


}
