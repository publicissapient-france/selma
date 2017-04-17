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
package fr.xebia.extras.selma.it.aggregation;

import fr.xebia.extras.selma.Selma;
import fr.xebia.extras.selma.beans.AggregatedBean;
import fr.xebia.extras.selma.beans.FirstBean;
import fr.xebia.extras.selma.beans.SecondBean;
import fr.xebia.extras.selma.it.utils.Compile;
import fr.xebia.extras.selma.it.utils.IntegrationTestBase;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

/**
 * Created by slemesle on 27/02/2017.
 */
@Compile(withClasses = AggregationMapper.class)
public class AggregationMapperIT extends IntegrationTestBase {

    @Test
    public void given_2_in_beans_mapping_should_aggregate_in_destination_bean(){
        // Given
        FirstBean first = new FirstBean();
        first.setFirstName("Toto");
        first.setLastName("de toor");
        first.setAge(35l);

        SecondBean second = new SecondBean();
        second.setJobTitle("CFO");
        second.setSalary(150000l);
        second.setStartDate(new Date());

        AggregationMapper mapper = Selma.builder(AggregationMapper.class).build();

        // When
        AggregatedBean res = mapper.mapFromAggregate(first, second);

        // Then
        Assert.assertEquals(first.getFirstName(), res.getFirstName());
        Assert.assertEquals(first.getLastName(), res.getLastName());
        Assert.assertEquals(first.getAge(), res.getAge());
        Assert.assertEquals(second.getJobTitle(), res.getJobTitle());
        Assert.assertEquals(second.getSalary(), res.getSalary());
        Assert.assertEquals(second.getStartDate(), res.getStartDate());
    }
}
