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

import fr.xebia.extras.selma.beans.NoGetterBean;
import fr.xebia.extras.selma.beans.PersonIn;
import fr.xebia.extras.selma.beans.PersonOut;
import fr.xebia.extras.selma.it.mappers.MissingPropertyMapper;
import fr.xebia.extras.selma.it.mappers.MissingPropertyMapsMapper;
import fr.xebia.extras.selma.it.utils.Compile;
import fr.xebia.extras.selma.it.utils.IntegrationTestBase;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
@Compile(withClasses = MissingPropertyMapsMapper.class, shouldFail = true)
public class FailingMissingPropertyMapsMappersIT extends IntegrationTestBase {



    @Test
    public void compilation_should_fail_on_missing_out_property_without_ignore() throws Exception {

        assertCompilationError(PersonIn.class, "public boolean isMale() {", String.format("setter for field male from source bean %s is missing in destination bean %s", PersonIn.class.getName(), PersonOut.class.getName()));
        Assert.assertEquals(3, compilationErrorCount());
    }

    @Test
    public void compilation_should_fail_on_missing_in_property_without_ignore() throws Exception {

        assertCompilationError(PersonOut.class, "public void setBiography(String biography) {", String.format("setter for field biography from destination bean %s has no getter in source bean %s", PersonOut.class.getName(), PersonIn.class.getName()));
        Assert.assertEquals(3, compilationErrorCount());

    }


    @Test
    public void compilation_should_fail_on_missing_getter_in_source_property_without_ignore() throws Exception {

        assertCompilationError(NoGetterBean.class, "public void setField(String field) {", String.format("setter for field field from destination bean %s has no getter in source bean %s", NoGetterBean.class.getName(), NoGetterBean.class.getName()));
        Assert.assertEquals(3, compilationErrorCount());

    }

}
