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
package fr.xebia.extras.selma.it.heritance;

import fr.xebia.extras.selma.Selma;
import fr.xebia.extras.selma.it.heritance.beans.Mail;
import fr.xebia.extras.selma.it.heritance.beans.MailDto;
import fr.xebia.extras.selma.it.utils.Compile;
import fr.xebia.extras.selma.it.utils.IntegrationTestBase;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by slemesle on 06/06/2017.
 */
@Compile(withClasses = MailMapper.class, withPackage = "fr.xebia.extras.selma.it.heritance.beans")
public class MailMapperIT extends IntegrationTestBase {

    @Test
    public void should_map_field_from_inherited_class() throws Exception {
        Mail source = new Mail("monid");
        source.setAdresse("mon adresse");

        MailMapper mapper = Selma.builder(MailMapper.class).build();

        MailDto res = mapper.asMailDto(source);
        assertThat(res.getId()).isEqualTo(source.getId());
        assertThat(res.getEmail()).isEqualTo(source.getAdresse());

    }
}
