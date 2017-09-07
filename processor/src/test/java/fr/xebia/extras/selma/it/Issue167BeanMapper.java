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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import fr.xebia.extras.selma.Selma;
import fr.xebia.extras.selma.beans.EnumIn;
import fr.xebia.extras.selma.beans.NoEmptyConstructorPersonOut;
import fr.xebia.extras.selma.beans.PersonIn;
import fr.xebia.extras.selma.it.mappers.MapperForIssue167;
import fr.xebia.extras.selma.it.utils.Compile;
import fr.xebia.extras.selma.it.utils.IntegrationTestBase;

/**
 *
 */
@Compile(withClasses = MapperForIssue167.class)
public class Issue167BeanMapper extends IntegrationTestBase {

  @Test
  public void beanMapper_should_map_properties() throws Exception {

    MapperForIssue167 mapper = Selma.getMapper(MapperForIssue167.class);

    PersonIn personIn = new PersonIn();
    personIn.setAge(10);
    personIn.setBirthDay(new Date());
    personIn.setFirstName("Odile");
    personIn.setLastName("Dere");
    personIn.setMale(true);
    personIn.setNatural(Boolean.TRUE);

    personIn.setIndices(new Long[] { 23l, 42l, null, 55l });

    personIn.setTags(new ArrayList<String>());
    personIn.getTags().add("coucou");
    personIn.getTags().add(null);
    personIn.getTags().add("cucu");

    personIn.setEnumIn(EnumIn.VAL_2);

    NoEmptyConstructorPersonOut res = mapper.merge(personIn, new NoEmptyConstructorPersonOut("name"));

    assertNotNull(res);
    assertEquals(personIn.getAge(), res.getAge());
    assertEquals(personIn.getBirthDay(), res.getBirthDay());
    assertEquals(personIn.getFirstName(), res.getFirstName());
    assertEquals(personIn.getLastName(), res.getLastName());
    org.junit.Assert.assertArrayEquals(personIn.getIndices(), res.getIndices());
    org.junit.Assert.assertEquals(personIn.getTags(), res.getTags());
    org.junit.Assert.assertEquals(personIn.getEnumIn().name(), res.getEnumIn().name());
    Assert.assertTrue(personIn.isNatural().booleanValue() == res.isNatural().booleanValue());
  }

}
