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

import fr.xebia.extras.selma.Selma;
import fr.xebia.extras.selma.beans.*;
import fr.xebia.extras.selma.it.utils.Compile;
import fr.xebia.extras.selma.it.utils.IntegrationTestBase;
import org.junit.Test;

import java.util.ArrayList;

import static fr.xebia.extras.selma.it.custom.fields.CustomFieldToFieldMapper.AGE_INCREMENT;
import static fr.xebia.extras.selma.it.custom.fields.CustomFieldToFieldMapper.FROM_CUSTOM_FIELD2FIELD_MAPPING;
import static fr.xebia.extras.selma.it.custom.fields.CustomFieldToFieldMapper.STRING_TEMPLATE_FOR_STREET;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by slemesle on 19/11/14.
 */
@Compile(withClasses = CustomFieldToFieldMapper.class)
public class CustomFieldToFieldMapperIT extends IntegrationTestBase {

    public static final String DOC_API = "Doc API";

    @Test
    public void should_map_field_with_custom_mapper() throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        CustomFieldToFieldMapper mapper = Selma.getMapper(CustomFieldToFieldMapper.class);

        PersonIn personIn = new PersonIn();
        personIn.setFirstName("Firstname");
        personIn.setLastName("Lastname");
        personIn.setAge(42);
        personIn.setAddress(new AddressIn());
        personIn.getAddress().setCity(new CityIn());
        personIn.getAddress().getCity().setCapital(true);
        personIn.getAddress().getCity().setName("Paris");
        personIn.getAddress().getCity().setPopulation(3 * 1000 * 1000);

        personIn.getAddress().setPrincipal(true);
        personIn.getAddress().setNumber(55);
        personIn.getAddress().setStreet("rue de la truanderie");

        PersonOut res = mapper.mapWithCustom(personIn);

        assertNotNull(res);

        assertEquals(personIn.getFirstName() + FROM_CUSTOM_FIELD2FIELD_MAPPING,
                res.getFirstName());
        assertEquals(personIn.getLastName(), res.getLastName());
        assertEquals(personIn.getAge() + AGE_INCREMENT, res.getAge());

        String street = String.format(STRING_TEMPLATE_FOR_STREET, personIn.getAddress().getStreet(),
                CustomFieldToFieldMapper.CustomStreetMapper.class);
        assertEquals(street, res.getAddress().getStreet());
        assertEquals(personIn.getAddress().getNumber(), res.getAddress().getNumber());
        assertEquals(personIn.getAddress().getExtras(), res.getAddress().getExtras());

        assertEquals(personIn.getAddress().getCity().getName(), res.getAddress().getCity().getName());
        assertEquals(personIn.getAddress().getCity().getPopulation(), res.getAddress().getCity().getPopulation());
        assertEquals(personIn.getAddress().getCity().isCapital(), res.getAddress().getCity().isCapital());
    }

    @Test
    public void should_map_specific_field_to_field_with_custom_mapper() throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        // GIVEN
        CustomFieldToFieldMapper mapper = Selma.getMapper(CustomFieldToFieldMapper.class);

        PersonOut personOut = new PersonOut();
        personOut.setFirstName("Firstname");
        personOut.setLastName("Lastname");
        personOut.setAge(42);
        personOut.setAddress(new AddressOut());
        personOut.getAddress().setCity(new CityOut());
        personOut.getAddress().getCity().setCapital(true);
        personOut.getAddress().getCity().setName("Paris");
        personOut.getAddress().getCity().setPopulation(3 * 1000 * 1000);

        personOut.getAddress().setPrincipal(true);
        personOut.getAddress().setNumber(55);
        personOut.getAddress().setStreet("rue de la truanderie");

        // WHEN
        PersonIn res = mapper.asPersonInInvertingNames(personOut);

        // THEN
        assertNotNull(res);

        assertEquals(personOut.getFirstName() + FROM_CUSTOM_FIELD2FIELD_MAPPING, res.getLastName());
        assertEquals(personOut.getLastName() + FROM_CUSTOM_FIELD2FIELD_MAPPING, res.getFirstName());
        assertEquals(personOut.getAge() + AGE_INCREMENT, res.getAge());

        assertEquals(personOut.getAddress().getStreet(), res.getAddress().getStreet());
        assertEquals(personOut.getAddress().getNumber(), res.getAddress().getNumber());
        assertEquals(personOut.getAddress().getExtras(), res.getAddress().getExtras());

        assertEquals(personOut.getAddress().getCity().getName(), res.getAddress().getCity().getName());
        assertEquals(personOut.getAddress().getCity().getPopulation(), res.getAddress().getCity().getPopulation());
        assertEquals(personOut.getAddress().getCity().isCapital(), res.getAddress().getCity().isCapital());
    }

    @Test
    public void should_call_interceptor_on_custom_field() throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        // GIVEN
        CustomFieldToFieldMapper mapper = Selma.getMapper(CustomFieldToFieldMapper.class);
        Library lib = new Library();
        lib.setName("Selma documentation");
        lib.setBooks(new ArrayList<Book>());
        Book book = new Book();
        book.setName(DOC_API);
        book.setAuthor("slemesle");
        lib.getBooks().add(book);

        //When
        LibraryDTO res = mapper.asBookDTO(lib);

        // THEN
        assertNotNull(res);
        assertNotNull(res.getBooks());
        assertEquals(1, res.getBooks().size());
        assertEquals(DOC_API, res.getBooks().get(0).getName());
        assertEquals(CustomFieldToFieldMapper.INTERCEPTED_BY_CUSTOM_INTERCEPTOR, res.getBooks().get(0).getAuthor());
    }


}
