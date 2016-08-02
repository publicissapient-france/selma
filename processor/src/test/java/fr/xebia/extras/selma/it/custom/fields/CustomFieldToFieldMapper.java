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
import fr.xebia.extras.selma.beans.*;

import java.util.List;

/**
 * Created by slemesle on 30/07/16.
 */
@Mapper(
        withIgnoreFields = {"fr.xebia.extras.selma.beans.PersonIn.male", "fr.xebia.extras.selma.beans.PersonOut.biography"},
        withCustomFields = {
                @Field( value = "age", withCustom = CustomFieldToFieldMapper.CustomAgeAndNamesMapper.class)
        }
)
public interface CustomFieldToFieldMapper {

    String FROM_CUSTOM_FIELD2FIELD_MAPPING = " from custom field2field mapping";
    int AGE_INCREMENT = 42;
    String STRING_TEMPLATE_FOR_STREET = " %s from %s  with @Field(value = \"street\", withCustom = CustomStreetMapper.class)";
    String INTERCEPTED_BY_CUSTOM_INTERCEPTOR = "intercepted by CustomInterceptor";

    @Maps(withCustomFields = {
            @Field(value = "firstName", withCustom = CustomAgeAndNamesMapper.class),
            @Field(value = "street", withCustom = CustomStreetMapper.class)
    })
    PersonOut mapWithCustom(PersonIn in);

    @Maps(withCustomFields = {
            @Field(value = {"firstName", "lastName"}, withCustom = CustomAgeAndNamesMapper.class)
    })
    PersonIn asPersonInInvertingNames(PersonOut in);

    /**
     * Test the use of an interceptor against a field
     */
    @Maps(withIgnoreFields = "Library.name",
            withCustomFields = {
        @Field(value = "books", withCustom = CustomBookInterceptor.class)
    }) LibraryDTO asBookDTO(Library in);



/* TODO Add a test for interceptor on embedded field mapping
    @Maps(withIgnoreFields = "Library.name",
            withCustomFields = {
        @Field(value = "books", withCustom = CustomBookInterceptor.class)
    }) ProposalDto asProposalDTO(Proposal in);
*/

    /**
     * This mapper is called to map the firstname field
     */
    class CustomAgeAndNamesMapper {

        public String mapFirstname(String firstname){
            return firstname + FROM_CUSTOM_FIELD2FIELD_MAPPING;
        }

        public int incrementAge(int age) {
            return age + AGE_INCREMENT;
        }
    }

    class CustomStreetMapper {
        public String mapStreet(String street){
            return String.format(STRING_TEMPLATE_FOR_STREET,
                    street, CustomStreetMapper.class);
        }
    }

    class CustomBookInterceptor {

        public void intercept(List<Book> in, List<BookDTO> out){
            for (BookDTO book : out)
                book.setAuthor(INTERCEPTED_BY_CUSTOM_INTERCEPTOR);
        }
    }

    class CustomInterceptor {

        public void intercept(List<Book> in, List<BookDTO> out){
            for (BookDTO book : out)
                book.setAuthor(INTERCEPTED_BY_CUSTOM_INTERCEPTOR);
        }
    }
}

