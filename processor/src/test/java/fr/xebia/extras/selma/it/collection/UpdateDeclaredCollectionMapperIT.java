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
package fr.xebia.extras.selma.it.collection;

import fr.xebia.extras.selma.Selma;
import fr.xebia.extras.selma.beans.*;
import fr.xebia.extras.selma.it.utils.Compile;
import fr.xebia.extras.selma.it.utils.IntegrationTestBase;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 *
 */
@Compile(withClasses = UpdateDeclaredCollectionMapper.class)
public class UpdateDeclaredCollectionMapperIT extends IntegrationTestBase {

    @Test
    public void should_ignore_update_on_collections() {
        // Given
        final UpdateDeclaredCollectionMapper mapper = Selma.builder(UpdateDeclaredCollectionMapper.class).build();
        final Library source = new Library();
        source.setName("test");
        source.setBooks(new ArrayList<Book>());
        Book book = new Book();
        book.setName("book");
        book.setAuthor("me");
        source.getBooks().add(book);

        // When
        final LibraryDTO destination = mapper.updateLibrary(source, new LibraryDTO());

        // Then
        assertThat(destination, notNullValue());
        assertThat(destination.getBooks().size(), is(1));
        assertThat(destination.getBooks().get(0).getAuthor(), equalTo(book.getAuthor()));
        assertThat(destination.getBooks().get(0).getName(), equalTo(book.getName()));

    }


}
