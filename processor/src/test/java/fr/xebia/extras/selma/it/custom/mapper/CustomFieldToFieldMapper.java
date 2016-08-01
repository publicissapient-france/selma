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

import fr.xebia.extras.selma.Field;
import fr.xebia.extras.selma.Mapper;
import fr.xebia.extras.selma.Maps;
import fr.xebia.extras.selma.beans.PersonIn;
import fr.xebia.extras.selma.beans.PersonOut;

/**
 * Created by slemesle on 30/07/16.
 */
@Mapper(
        withIgnoreFields = {"fr.xebia.extras.selma.beans.PersonIn.male", "fr.xebia.extras.selma.beans.PersonOut.biography"},
        withCustomFields = {
                @Field( value = {"age", "age"},
                        withCustom = CustomFieldToFieldMapper.CustomF2FMapper.class)
        }
)
public interface CustomFieldToFieldMapper {

    String FROM_CUSTOM_FIELD2FIELD_MAPPING = " from custom field2field mapping";
    int AGE_INCREMENT = 42;

    @Maps(withCustomFields = {
            @Field(value = {"firstName", "firstName"}, withCustom = CustomF2FMapper.class)
    })
    PersonOut mapWithCustom(PersonIn in);

    @Maps(withCustomFields = {
            @Field(value = {"firstName", "firstName"}, withCustom = CustomF2FMapper.class)
    })
    PersonIn mapWithCustom(PersonOut in);


    /**
     * This mapper is called to map the firstname field
     */
    class CustomF2FMapper {

        public String mapFirstname(String firstname){
            return firstname + FROM_CUSTOM_FIELD2FIELD_MAPPING;
        }

        public int incrementAge(int age) {
            return age + AGE_INCREMENT;
        }
    }
}

