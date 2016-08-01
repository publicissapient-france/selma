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
package fr.xebia.extras.selma.it.custom.mapper;

import fr.xebia.extras.selma.it.utils.Compile;
import fr.xebia.extras.selma.it.utils.IntegrationTestBase;
import org.junit.Test;

/**
 * Created by slemesle on 01/08/2016.
 */
@Compile(withClasses = FailOnConflictMapper.class, shouldFail = true)
public class FailOnConflictsMapperIT extends IntegrationTestBase {


    @Test
    public void conflicting_custom_methods_should_rais_error() throws Exception {
        // Given
        //      we use @Field with a direct field 'street'
        //      and a custom mapper CustomStreetMapper.class
        //      that contains only : 'int incrementAge(int age);'
        // When
        //      we compile the mapper 'FaillingFieldToFieldWithCustomMapper.class'
        // Then
        //      the compilation fails
        //      and raise an error for asAddressOut method in FaillingFieldToFieldWithCustomMapper.class
        //      and the error tells 'Custom mapping method not found'
        //      for field 'street" in bean AddressIn with CustomStreetMapper
        assertCompilationError(FailOnConflictMapper.class,
                "public String mapCode(String code){",
                "Conflicting custom mapper method");
    }

}
