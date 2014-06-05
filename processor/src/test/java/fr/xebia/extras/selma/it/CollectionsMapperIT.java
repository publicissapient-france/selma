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
package fr.xebia.extras.selma.it;

import fr.xebia.extras.selma.Selma;
import fr.xebia.extras.selma.it.mappers.CollectionsMapper;
import fr.xebia.extras.selma.it.utils.Compile;
import fr.xebia.extras.selma.it.utils.IntegrationTestBase;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by slemesle on 05/06/2014.
 */
@Compile(withClasses = CollectionsMapper.class)
public class CollectionsMapperIT extends IntegrationTestBase {

    @Test
    public void should_create_new_set_given_a_TreeSet() {
        TreeSet<String> in = new TreeSet(Arrays.asList("dodo", "didi", "dudu"));

        CollectionsMapper mapper = Selma.builder(CollectionsMapper.class).build();

        Set<String> out = mapper.asSet(in);

        assertThat(out.size(), is(in.size()));
    }

    /**
     * Bug #21 - Fix TreeSet constructor call
     */
    @Test public void should_create_TreeSet_given_a_TreeSet() {

        TreeSet<String> in = new TreeSet(Arrays.asList("dodo", "didi", "dudu"));

        CollectionsMapper mapper = Selma.builder(CollectionsMapper.class).build();

        TreeSet<String> out = mapper.asTreeSet(in);

        assertThat(in.size(), is(out.size()));

    }

    @Test public void should_create_TreeSet_given_a_List() {

        List<String> in = Arrays.asList("dodo", "didi", "dudu", "dodo");

        CollectionsMapper mapper = Selma.builder(CollectionsMapper.class).build();

        TreeSet<String> out = mapper.listAsSet(in);

        assertThat(in.size(), is(out.size() + 1));

    }

}
