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
import fr.xebia.extras.selma.beans.PersonIn;
import fr.xebia.extras.selma.it.custom.fields.FaillingFieldToFieldWithCustomMapper.CustomEmptyMapper;
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
                         " Mapping field inAddressOut.getCity().getName() from source bean " +AddressOut.class.getCanonicalName()+
                         ", using field @fr.xebia.extras.selma.Field(value={\"city.name\", \"street\"}," +
                         " withCustom="+CustomStreetMapper.class.getCanonicalName()+".class)");
    }

    @Test
    public void no_custom_mapping_method_for_direct_field_should_fail() throws Exception {
        // Given
        //      we use @Field with a direct field 'firstName'
        //      on a mapper FaillingFieldToFieldWithCustomMapper
        //      with a method 'PersonOut asPersonOut(PersonIn in);'
        //      and a custom mapper CustomEmptyMapper.class
        //      that contains no method
        // When
        //      we compile the mapper 'FaillingFieldToFieldWithCustomMapper.class'
        // Then
        //      the compilation fails
        //      and raise an error for CustomEmptyMapper
        //      and the error tells 'No valid mapping method found in CustomEmptyMapper'
        //      Another compilation error reports no mapping method found when mapping the field
         assertCompilationError(FaillingFieldToFieldWithCustomMapper.class,
                 "PersonOut asPersonOut(PersonIn in);",
                 "No valid mapping method found in custom selma class "+
                         CustomEmptyMapper.class.getCanonicalName());
         assertCompilationError(FaillingFieldToFieldWithCustomMapper.class,
                 "PersonOut asPersonOut(PersonIn in);",
                 "Custom mapping method not found:" +
                 " Mapping field firstname from source bean " +PersonIn.class.getCanonicalName()+
                 ", using field @fr.xebia.extras.selma.Field(value={\"firstName\"}," +
                 " withCustom="+CustomEmptyMapper.class.getCanonicalName()+".class)");
    }
}
