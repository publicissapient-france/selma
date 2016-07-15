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
import fr.xebia.extras.selma.beans.CollectionBeanDefensiveDestination;
import fr.xebia.extras.selma.beans.CollectionBeanDestination;
import fr.xebia.extras.selma.beans.CollectionBeanSource;
import fr.xebia.extras.selma.it.utils.Compile;
import fr.xebia.extras.selma.it.utils.IntegrationTestBase;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 *
 */
@Compile(withClasses = CollectionMapper.class)
public class CollectionsMapperIT extends IntegrationTestBase {

    @Test
    public void should_populate_collection_from_getter_with_allow_getter() {
        // Given
        final CollectionMapper mapper = Selma.builder(CollectionMapper.class).build();
        final CollectionBeanSource source = new CollectionBeanSource(Arrays.asList("un", "deux", "trois"));

        // When
        final CollectionBeanDestination destination = mapper.asCollectionBeanDestination(source);

        // Then
        assertThat(destination, notNullValue());
        assertThat(destination.getStrings(), equalTo(source.getStrings()));

    }

    @Test
    public void should_populate_collection_in_defensive_copy_setter() {
        // Given
        final CollectionMapper mapper = Selma.builder(CollectionMapper.class).build();
        final CollectionBeanSource source = new CollectionBeanSource(Arrays.asList("un", "deux", "trois"));

        // When
        final CollectionBeanDefensiveDestination destination = mapper.asCollectionBeanDefensiveDestination(source);

        // Then
        assertThat(destination, notNullValue());
        assertThat(destination.getStrings(), equalTo(source.getStrings()));

    }

}
