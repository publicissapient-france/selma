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
import fr.xebia.extras.selma.beans.*;
import fr.xebia.extras.selma.it.mappers.MissingPropertyMapsMapper;
import fr.xebia.extras.selma.it.utils.Compile;
import fr.xebia.extras.selma.it.utils.IntegrationTestBase;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

/**
 * Created by slemesle on 27/02/2017.
 */
@Compile(withClasses = { FailingAggregationMapper.class }, shouldFail = true)
public class FailingAggregationMapperIT extends IntegrationTestBase {

    @Test
    public void given_2_in_beans_mapping_should_fail_on_missing_propety_in_source_beans() throws Exception {
        assertCompilationError(FailingAggregationMapper.class,
                "AggregatedBean mapFromAggregate(FirstBean first, SecondBean second);",
                "setter for field missingproperty from destination bean fr.xebia.extras.selma.beans.AggregatedBean has " +
                        "no getter in one of source beans !");
        Assert.assertEquals(1, compilationErrorCount());
    }
}
