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
package fr.xebia.extras.selma.it;

import org.junit.Assert;
import org.junit.Test;

import fr.xebia.extras.selma.Selma;
import fr.xebia.extras.selma.beans.CircularReference;
import fr.xebia.extras.selma.it.mappers.CircularReferenceMapper;
import fr.xebia.extras.selma.it.utils.Compile;
import fr.xebia.extras.selma.it.utils.IntegrationTestBase;

/**
 * Created by slemesle on 12/03/2014.
 */
@Compile(withClasses = { CircularReferenceMapper.class })
public class CircularReferenceMapperIT extends IntegrationTestBase {

    @Test
	public void circularReferenceTest() throws Exception {

		final CircularReferenceMapper mapper = Selma.mapper(CircularReferenceMapper.class);

		CircularReference in1 = new CircularReference("1");
		CircularReference in2 = new CircularReference("2");

		in1.setRef(in2);
		in2.setRef(in1);

		CircularReference out1 = mapper.as(in1);
		Assert.assertEquals(in1.getValue(), out1.getValue());
		Assert.assertEquals(in2.getValue(), out1.getRef().getValue());
		Assert.assertEquals(out1, out1.getRef().getRef());

    }


}
