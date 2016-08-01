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
import fr.xebia.extras.selma.beans.SimpleContacts;
import fr.xebia.extras.selma.beans.SimpleContactsDto;
import fr.xebia.extras.selma.beans.SimplePerson;
import fr.xebia.extras.selma.beans.SimplePersonDto;
import fr.xebia.extras.selma.it.utils.Compile;
import fr.xebia.extras.selma.it.utils.IntegrationTestBase;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by slemesle on 21/11/14.
 */
@Compile(withClasses = {
        FQCNOneCustomFieldToFieldMapping.class, FQCNFullCustomFieldToFieldMapping.class
})
public class FQCNFieldToFieldInMapsIT extends IntegrationTestBase {


    @Test
    public void given_a_mapper_with_custom_field_mapping_should_use_described_name_to_name_for_mapping() throws Exception {
        // Given
        FQCNOneCustomFieldToFieldMapping mapper = Selma.mapper(FQCNOneCustomFieldToFieldMapping.class);

        SimpleContacts contacts = new SimpleContacts();

        contacts.setContacts(new ArrayList<SimplePerson>());

        SimplePerson person = new SimplePerson();
        person.setNom("toto");
        person.setPrenom("tutu");

        contacts.getContacts().add(person);

        // When

        SimpleContactsDto contactsDto = mapper.asContactsDto(contacts);

        // Then

        Assert.assertEquals(1, contacts.getContacts().size());
        SimplePersonDto personDto = contactsDto.getContacts().get(0);

        Assert.assertEquals(person.getPrenom(), personDto.getFirstName());
        Assert.assertEquals(person.getNom(), personDto.getLastName());
    }

    @Test
    public void given_a_mapper_with_custom_field_mapping_should_use_qualified_described_name_to_name_for_mapping() throws Exception {
        // Given
        FQCNFullCustomFieldToFieldMapping mapper = Selma.mapper(FQCNFullCustomFieldToFieldMapping.class);

        SimpleContacts contacts = new SimpleContacts();

        contacts.setContacts(new ArrayList<SimplePerson>());

        SimplePerson person = new SimplePerson();
        person.setNom("toto");
        person.setPrenom("tutu");

        contacts.getContacts().add(person);

        // When

        SimpleContactsDto contactsDto = mapper.asQualifiedContactsDto(contacts);

        // Then

        Assert.assertEquals(1, contacts.getContacts().size());
        SimplePersonDto personDto = contactsDto.getContacts().get(0);

        Assert.assertEquals(person.getNom(), personDto.getFirstName());
        Assert.assertEquals(person.getPrenom(), personDto.getLastName());

    }

    @Test
    public void given_a_mapper_and_a_maps_with_fields_not_used_then_should_report_unused() throws Exception {
        assertCompilationWarning(FQCNFullCustomFieldToFieldMapping.class, "public interface FQCNFullCustomFieldToFieldMapping {", "Custom @Field({\"bbb\",\"ttt\"}) mapping is never used !");
        assertCompilationWarning(FQCNFullCustomFieldToFieldMapping.class, "SimpleContactsDto asQualifiedContactsDto(SimpleContacts in);", "Custom @Field({\"pec\",\"rec\"}) mapping is never used !");
        // Assert.assertEquals(2, compilationWarningCount());
    }
}
