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
package fr.xebia.extras.selma.it.custom.fields;

import fr.xebia.extras.selma.beans.AddressIn;
import fr.xebia.extras.selma.beans.AddressOut;
import fr.xebia.extras.selma.it.custom.fields.FaillingFieldToFieldWithCustomMapper.CustomStreetMapper;
import fr.xebia.extras.selma.it.utils.Compile;
import fr.xebia.extras.selma.it.utils.IntegrationTestBase;
import org.junit.Test;

/**
 * Created by slemesle on 01/08/2016.
 */
@Compile(withClasses = FaillingFieldToFieldWithCustomMapper.class, shouldFail = true)
public class FaillingFieldToFieldWithCustomMapperIT extends IntegrationTestBase {


    @Test
    public void no_custom_mapping_found_for_field_should_fail() throws Exception {
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
         assertCompilationError(FaillingFieldToFieldWithCustomMapper.class,
                 "AddressOut asAddressOut(AddressIn in);",
                 "Custom mapping method not found:" +
                         " Mapping field street from source bean " +AddressIn.class.getCanonicalName()+
                         ", using field @fr.xebia.extras.selma.Field(value={\"street\"}," +
                         " withCustom="+CustomStreetMapper.class.getCanonicalName()+".class)");
    }

    @Test
    public void no_custom_mapping_found_for_embedded_field_should_fail() throws Exception {
        // Given
        //      we use @Field with an embedded field 'city.name' to 'street'
        //      on a mapper FaillingFieldToFieldWithCustomMapper
        //      with a method 'AddressIn asAddressIn(AddressOut in);'
        //      and a custom mapper CustomStreetMapper.class
        //      that contains only : 'int incrementAge(int age);'
        // When
        //      we compile the mapper 'FaillingFieldToFieldWithCustomMapper.class'
        // Then
        //      the compilation fails
        //      and raise an error for asAddressIn method in FaillingFieldToFieldWithCustomMapper.class
        //      and the error tells 'Custom mapping method not found'
        //      for field 'city.name" in bean AddressOut with CustomStreetMapper
         assertCompilationError(FaillingFieldToFieldWithCustomMapper.class,
                 "AddressIn asAddressIn(AddressOut in);",
                 "Custom mapping method not found:" +
                         " Mapping field in.getCity().getName() from source bean " +AddressOut.class.getCanonicalName()+
                         ", using field @fr.xebia.extras.selma.Field(value={\"city.name\", \"street\"}," +
                         " withCustom="+CustomStreetMapper.class.getCanonicalName()+".class)");
    }
}
