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

import fr.xebia.extras.selma.it.utils.Compile;
import fr.xebia.extras.selma.it.utils.IntegrationTestBase;
import org.junit.Test;

/**
 * Created by slemesle on 27/11/14.
 */
@Compile(withClasses = {
        FailingMissingFieldInMapsMapper.class
}, shouldFail = true)
public class FailingMissingFieldsInMapsMapperIT extends IntegrationTestBase {

    @Test
    public void given_maps_with_ignore_SOURCE_should_skip_silently_missing_in_source_and_raise_error_in_destination() throws Exception {
        assertCompilationError(FailingMissingFieldInMapsMapper.class, "PersonOut mapMissingSource(PersonIn in);",
                "setter for field male from source bean fr.xebia.extras.selma.beans.PersonIn is missing in destination bean fr.xebia.extras.selma.beans.PersonOut");
    }


    @Test
    public void given_maps_with_ignore_DESTINATION_should_skip_silently_missing_in_destination_and_raise_error_in_source() throws Exception {
        assertCompilationError(FailingMissingFieldInMapsMapper.class, "PersonOut mapMissingDestination(PersonIn in);",
                "setter for field biography from destination bean fr.xebia.extras.selma.beans.PersonOut has no getter in source bean fr.xebia.extras.selma.beans.PersonIn");
    }

    @Test
    public void given_maps_with_ignore_NONE_should_raise_error_missing_in_destination_and_source() throws Exception {
        assertCompilationError(FailingMissingFieldInMapsMapper.class, "PersonOut mapMissingNone(PersonIn in);",
                "setter for field biography from destination bean fr.xebia.extras.selma.beans.PersonOut has no getter in source bean fr.xebia.extras.selma.beans.PersonIn");
        assertCompilationError(FailingMissingFieldInMapsMapper.class, "PersonOut mapMissingNone(PersonIn in);",
                "setter for field male from source bean fr.xebia.extras.selma.beans.PersonIn is missing in destination bean fr.xebia.extras.selma.beans.PersonOut");
    }


}
