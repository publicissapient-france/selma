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
import fr.xebia.extras.selma.it.utils.Compile;
import fr.xebia.extras.selma.it.utils.IntegrationTestBase;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 *
 */
@Compile(withClasses = IterableMapper.class)
public class IterableMapperIT extends IntegrationTestBase {

    @Test
    public void should_populate_iterable_from_collection() {
        // Given
        final IterableMapper mapper = Selma.builder(IterableMapper.class).build();
        final Collection<Integer> source = Arrays.asList(1, 2, 3);

        // When
        final Iterable<String> destination = mapper.toIterables(source);

        // Then
        assertThat(destination, notNullValue());
        // assertThat(destination, equalTo(source.getStrings()));
    }

    @Test
    public void should_populate_collection_from_iterable() {
        // Given
        final IterableMapper mapper = Selma.builder(IterableMapper.class).build();
        final Iterable<Integer> source = Arrays.asList(1, 2, 3, 4);

        // When
        final Collection<String> destination = mapper.toCollection(source);

        // Then
        assertThat(destination, notNullValue());
        // assertThat(destination, equalTo(source.getStrings()));
    }

}
