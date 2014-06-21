/*
 * Copyright 2013 Xebia and Séven Le Mesle
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
import fr.xebia.extras.selma.beans.Book;
import fr.xebia.extras.selma.beans.BookDTO;
import fr.xebia.extras.selma.beans.Library;
import fr.xebia.extras.selma.beans.LibraryDTO;
import fr.xebia.extras.selma.it.mappers.IgnoreFullyQualifiedNameClassField;
import fr.xebia.extras.selma.it.mappers.IgnoreSimpleNameClassField;
import fr.xebia.extras.selma.it.utils.Compile;
import fr.xebia.extras.selma.it.utils.IntegrationTestBase;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 *
 */
@Compile(withClasses = {IgnoreSimpleNameClassField.class, IgnoreFullyQualifiedNameClassField.class})
public class FullyQualifiedIgnoreFieldMapperIT extends IntegrationTestBase {

    @Test
    public void given_simple_name_ignore_fields_for_in_bean_method_generated_mapper_should_ignore_them() {

        Library in = new Library();
        in.setName("my library");

        Book book = new Book();
        book.setName("Bilbo the Hobit");
        book.setAuthor("JRR Tolkien");
        in.setBooks(Arrays.asList(book));

        LibraryDTO res = Selma.getMapper(IgnoreSimpleNameClassField.class).asLibraryDTO(in);

        Assert.assertNotNull(res);

        Assert.assertThat(res.getBooks().size(), CoreMatchers.equalTo(1));
        Assert.assertThat(res.getBooks().get(0).getName(), CoreMatchers.equalTo(book.getName()));
        Assert.assertThat(res.getBooks().get(0).getAuthor(), CoreMatchers.equalTo(book.getAuthor()));
    }

    @Test
    public void given_explicit_ignore_fields_for_in_bean_generated_mapper_should_ignore_them() {

        Library in = new Library();
        in.setName("my library");

        Book book = new Book();
        book.setName("Bilbo the Hobit");
        book.setAuthor("JRR Tolkien");
        in.setBooks(Arrays.asList(book));

        LibraryDTO res = Selma.getMapper(IgnoreFullyQualifiedNameClassField.class).asLibraryDTO(in);

        Assert.assertNotNull(res);

        Assert.assertThat(res.getBooks().size(), CoreMatchers.equalTo(1));
        Assert.assertThat(res.getBooks().get(0).getName(), CoreMatchers.equalTo(book.getName()));
        Assert.assertThat(res.getBooks().get(0).getAuthor(), CoreMatchers.equalTo(book.getAuthor()));
    }

    @Test
    public void given_explicit_ignore_fields_for_out_bean_generated_mapper_should_ignore_them() {

        //Given
        LibraryDTO in = new LibraryDTO();
        BookDTO book = new BookDTO();
        book.setName("Bilbo the Hobit");
        book.setAuthor("JRR Tolkien");
        in.setBooks(Arrays.asList(book));

        //When
        Library res = Selma.getMapper(IgnoreFullyQualifiedNameClassField.class).asLibrary(in);

        //Then
        Assert.assertNotNull(res);
        Assert.assertNull(res.getName());

        Assert.assertThat(res.getBooks().size(), CoreMatchers.equalTo(1));
        Assert.assertThat(res.getBooks().get(0).getName(), CoreMatchers.equalTo(book.getName()));
        Assert.assertThat(res.getBooks().get(0).getAuthor(), CoreMatchers.equalTo(book.getAuthor()));
    }


    @Test
    public void given_simple_name_ignore_fields_for_out_bean_generated_mapper_should_ignore_them() {

        LibraryDTO in = new LibraryDTO();

        BookDTO book = new BookDTO();
        book.setName("Bilbo the Hobit");
        book.setAuthor("JRR Tolkien");
        in.setBooks(Arrays.asList(book));

        Library res = Selma.getMapper(IgnoreSimpleNameClassField.class).asLibrary(in);

        Assert.assertNotNull(res);
        Assert.assertNull(res.getName());

        Assert.assertThat(res.getBooks().size(), CoreMatchers.equalTo(1));
        Assert.assertThat(res.getBooks().get(0).getName(), CoreMatchers.equalTo(book.getName()));
        Assert.assertThat(res.getBooks().get(0).getAuthor(), CoreMatchers.equalTo(book.getAuthor()));
    }


}
