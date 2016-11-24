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
import fr.xebia.extras.selma.beans.Person;
import fr.xebia.extras.selma.beans.PersonDto;
import fr.xebia.extras.selma.beans.PersonImpl;
import fr.xebia.extras.selma.it.mappers.PersonMapper;
import fr.xebia.extras.selma.it.utils.Compile;
import fr.xebia.extras.selma.it.utils.IntegrationTestBase;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 */
@Compile(withClasses = PersonMapper.class)
public class PersonMapperIT extends IntegrationTestBase {

    public static final String NOM = "nom";
    public static final String PRENOM = "prenom";

    @Test
    public void beanMapper_should_interface() throws Exception {
        PersonMapper mapper = Selma.getMapper(PersonMapper.class);
        Person person = new PersonImpl(NOM, PRENOM);
        PersonDto personDto = mapper.toDto(person);
        assertThat(personDto.getNom()).isEqualTo(NOM);
        assertThat(personDto.getPrenom()).isEqualTo(PRENOM);

    }

}
