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
package fr.xebia.extras.selma.it.ignore;

import fr.xebia.extras.selma.Selma;
import fr.xebia.extras.selma.beans.EnumIn;
import fr.xebia.extras.selma.beans.PersonIn;
import fr.xebia.extras.selma.beans.PersonOut;
import fr.xebia.extras.selma.it.utils.Compile;
import fr.xebia.extras.selma.it.utils.IntegrationTestBase;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by slemesle on 27/11/14.
 */
@Compile(withClasses = {
        IgnoreMissingFieldsInMapperMapper.class, IgnoreMissingFieldsInMapsMapper.class
})
public class IgnoreMissingFieldsInMapperIT extends IntegrationTestBase {

    @Test
    public void given_mapper_with_ignore_ALL_should_skip_silently_missing_fields() {
        IgnoreMissingFieldsInMapperMapper mapper = Selma.getMapper(IgnoreMissingFieldsInMapperMapper.class);

        PersonIn personIn = getPersonIn();

        PersonOut res = mapper.map(personIn);

        assertPersonOut(personIn, res);

    }

    @Test
    public void given_maps_with_ignore_ALL_should_skip_silently_missing_fields() {
        IgnoreMissingFieldsInMapsMapper mapper = Selma.getMapper(IgnoreMissingFieldsInMapsMapper.class);

        PersonIn personIn = getPersonIn();

        PersonOut res = mapper.map(personIn);

        assertPersonOut(personIn, res);

    }

    private void assertPersonOut(PersonIn personIn, PersonOut res) {
        Assert.assertNotNull(res);
        Assert.assertNull(res.getBiography());
        Assert.assertEquals(personIn.getAge(), res.getAge());
        Assert.assertEquals(personIn.getBirthDay(), res.getBirthDay());
        Assert.assertEquals(personIn.getFirstName(), res.getFirstName());
        Assert.assertEquals(personIn.getLastName(), res.getLastName());
        org.junit.Assert.assertArrayEquals(personIn.getIndices(), res.getIndices());
        org.junit.Assert.assertEquals(personIn.getTags(), res.getTags());
        org.junit.Assert.assertEquals(personIn.getEnumIn().name(), res.getEnumIn().name());
        Assert.assertTrue(personIn.isNatural().booleanValue() == res.isNatural().booleanValue());
    }

    private PersonIn getPersonIn() {
        PersonIn personIn = new PersonIn();
        personIn.setAge(10);
        personIn.setBirthDay(new Date());
        personIn.setFirstName("Odile");
        personIn.setLastName("Dere");
        personIn.setMale(true);
        personIn.setNatural(Boolean.TRUE);

        personIn.setIndices(new Long[]{23l, 42l, null, 55l});

        personIn.setTags(new ArrayList<String>());
        personIn.getTags().add("coucou");
        personIn.getTags().add(null);
        personIn.getTags().add("cucu");

        personIn.setEnumIn(EnumIn.VAL_2);
        return personIn;
    }
}
