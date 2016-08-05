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
package fr.xebia.extras.selma.it.collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import fr.xebia.extras.selma.beans.RawBean;
import fr.xebia.extras.selma.beans.RawListBean;
import fr.xebia.extras.selma.beans.RawMapBean;
import fr.xebia.extras.selma.it.utils.Compile;
import fr.xebia.extras.selma.it.utils.IntegrationTestBase;
import org.junit.Test;

/**
 * Has to be split into multiple mapper classes as JDK7 doesn't reliably print all the compiler error messages.
 */
@Compile(withClasses = RawBeanMapper.class, shouldFail = true)
public class FailingRawBeanMapperIT extends IntegrationTestBase {

    @Test
    public void compilation_should_fail_on_object_type() throws Exception {
        assertCompilationError(RawBeanMapper.class,
                "RawBean clone(RawBean rawBean);",
                String.format("Failed to generate mapping method for type java.lang.Object to java.lang.Object not " +
                        "supported on fr.xebia.extras.selma.it.collection.RawBeanMapper.clone(%s) !",
                        RawBean.class.getName()));
        assertThat(compilationErrorCount(), equalTo(4));
    }

    @Test
    public void compilation_should_fail_on_raw_collection() throws Exception {
        assertCompilationError(RawBeanMapper.class,
                "RawListBean clone(RawListBean rawBean);",
                String.format("Failed to generate mapping method for type java.lang.Object to java.lang.Object not supported " +
                        "on fr.xebia.extras.selma.it.collection.RawBeanMapper.clone(%s) !",
                        RawListBean.class.getName()));
        assertThat(compilationErrorCount(), equalTo(4));
    }

    @Test
    public void compilation_should_fail_on_raw_map() throws Exception {
        assertCompilationError(RawBeanMapper.class,
                "RawMapBean clone(RawMapBean rawBean);",
                String.format("Failed to generate mapping method for type java.lang.Object to java.lang.Object not supported " +
                                "on fr.xebia.extras.selma.it.collection.RawBeanMapper.clone(%s) !",
                        RawMapBean.class.getName()));
        assertThat(compilationErrorCount(), equalTo(4));
    }

}
