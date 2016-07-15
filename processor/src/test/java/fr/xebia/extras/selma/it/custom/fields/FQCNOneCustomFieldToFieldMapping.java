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

import fr.xebia.extras.selma.Field;
import fr.xebia.extras.selma.Mapper;
import fr.xebia.extras.selma.Maps;
import fr.xebia.extras.selma.beans.SimpleContacts;
import fr.xebia.extras.selma.beans.SimpleContactsDto;

/**
 * Created by slemesle on 21/11/14.
 */
@Mapper
public interface FQCNOneCustomFieldToFieldMapping {

    @Maps(withCustomFields = {
            @Field({"SimplePerson.prenom", "firstname"}), @Field({"fr.xebia.extras.selma.beans.SimplePerson.nom", "lastname"})
    }) SimpleContactsDto asContactsDto(SimpleContacts in);
}
