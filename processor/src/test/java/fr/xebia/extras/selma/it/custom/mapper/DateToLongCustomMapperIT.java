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
package fr.xebia.extras.selma.it.custom.mapper;

import fr.xebia.extras.selma.it.utils.Compile;
import fr.xebia.extras.selma.it.utils.IntegrationTestBase;
import org.junit.Test;

/**
 * Created by slemesle on 19/11/14.
 */
@Compile(withClasses = {DateToLongMapper.class, DateMapper.class}, shouldFail = true)
public class DateToLongCustomMapperIT extends IntegrationTestBase {

    @Test
    public void should_fail_Date_to_long_with_custom_Date_to_Long_mapper() throws Exception {
        assertCompilationError(DateToLongMapper.class, "TimestampPojo asTimestampPojo(DatePojo src);",
                "Failed to generate mapping method for type java.util.Date to long not supported on fr.xebia.extras.selma.it.custom.mapper.DateToLongMapper.asTimestampPojo(fr.xebia.extras.selma.beans.DatePojo) !");

    }

}
