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

import fr.xebia.extras.selma.Field;
import fr.xebia.extras.selma.Mapper;
import fr.xebia.extras.selma.Maps;
import fr.xebia.extras.selma.beans.AddressIn;
import fr.xebia.extras.selma.beans.AddressOut;
import fr.xebia.extras.selma.beans.PersonIn;
import fr.xebia.extras.selma.beans.PersonOut;

/**
 * Created by slemesle on 01/08/2016.
 */
@Mapper(withCustomFields = {
        // Custom direct field street expecting a custom mapper method for its type String to String
        @Field(value = {"street"}, withCustom = FaillingFieldToFieldWithCustomMapper.CustomStreetMapper.class)
})
public interface FaillingFieldToFieldWithCustomMapper {

    AddressOut asAddressOut(AddressIn in);

    @Maps(withCustomFields = {
        // Custom embedded field street expecting a custom mapper method for its type String to String
        @Field(value = {"city.name", "street"}, withCustom = FaillingFieldToFieldWithCustomMapper.CustomStreetMapper.class)
    })
    AddressIn asAddressIn(AddressOut in);

    @Maps(withCustomFields = {
        // Custom direct field firstName expecting a custom mapper method for its type String to String
        @Field(value = {"firstName"}, withCustom = CustomEmptyMapper.class)
    })
    PersonOut asPersonOut(PersonIn in);

    /**
     * Custom mapper providing a int to int method
     */
    class CustomStreetMapper {

        public int incrementAge(int age) {
            return age + 1;
        }

    }

    /**
     * Custom empty mapper
     * This mapper contains no method
     */
    class CustomEmptyMapper {
    }
}
