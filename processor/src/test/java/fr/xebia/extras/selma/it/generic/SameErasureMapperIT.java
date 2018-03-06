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
package fr.xebia.extras.selma.it.generic;

import fr.xebia.extras.selma.Selma;
import fr.xebia.extras.selma.it.generic.beans.Records;
import fr.xebia.extras.selma.it.generic.beans.RecordsDAO;
import fr.xebia.extras.selma.it.generic.beans.Value;
import fr.xebia.extras.selma.it.utils.Compile;
import fr.xebia.extras.selma.it.utils.IntegrationTestBase;
import org.junit.Assert;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 */
@Compile(withClasses = {SameErasureMapper.class}, withPackage = "fr.xebia.extras.selma.it.generic.beans")
public class SameErasureMapperIT extends IntegrationTestBase {

    @Test
    public void beanMapper_should_map_properties_resolving_generics() throws Exception {


        SameErasureMapper mapper = Selma.getMapper(SameErasureMapper.class);

        Records records = new Records();
        records.setSpeed(new Value<Double>());
        records.getSpeed().setValue(2d);
        records.getSpeed().setUnit("km");
        records.setType(new Value<String>());
        records.getType().setValue("coucou");
        records.getType().setUnit("txt");

        RecordsDAO res = mapper.asRecordsDAO(records);

        Assert.assertNotNull(res);
        Assert.assertNotNull(res.getSpeed());
        assertThat(res.getSpeed().getUnit()).isEqualTo(records.getSpeed().getUnit());
        assertThat(res.getSpeed().getValue()).isEqualTo(records.getSpeed().getValue());
        Assert.assertNotNull(res.getType());
        assertThat(res.getType().getUnit()).isEqualTo(records.getType().getUnit());
        assertThat(res.getType().getValue()).isEqualTo(records.getType().getValue());
    }


}
