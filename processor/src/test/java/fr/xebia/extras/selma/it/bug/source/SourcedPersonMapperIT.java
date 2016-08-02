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
package fr.xebia.extras.selma.it.bug.source;

import fr.xebia.extras.selma.Selma;
import fr.xebia.extras.selma.it.utils.Compile;
import fr.xebia.extras.selma.it.utils.IntegrationTestBase;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

/**
 * Created by slemesle on 02/08/2016.
 */
@Compile(withClasses = {Person.class, PersonDto.class, SourcedPersonMapper.class})
public class SourcedPersonMapperIT extends IntegrationTestBase {

    @Test
    public void should_use_declared_source(){
        Date dateSource = new Date();
        SourcedPersonMapper mapper = Selma.builder(SourcedPersonMapper.class).withSources(dateSource).build();

        Person person = new Person("firstname");
        PersonDto res = mapper.personToPersonDto(person);

        Assert.assertNotNull(res);
        Assert.assertEquals(person.getFirstName(), res.getFirstName());
        Assert.assertEquals(dateSource, res.birthDate);
    }
}
