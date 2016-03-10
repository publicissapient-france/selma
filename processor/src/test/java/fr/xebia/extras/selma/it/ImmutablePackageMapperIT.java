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

import org.junit.Assert;
import org.junit.Test;

import fr.xebia.extras.selma.Selma;
import fr.xebia.extras.selma.beans.immutable.ImmutablePackageBean;
import fr.xebia.extras.selma.it.mappers.ImmutablePackageMapper;
import fr.xebia.extras.selma.it.utils.Compile;
import fr.xebia.extras.selma.it.utils.IntegrationTestBase;

/**
 *
 */
@Compile(withClasses = ImmutablePackageMapper.class)
public class ImmutablePackageMapperIT extends IntegrationTestBase {

	@Test
	public void testImmutablePackage() throws Exception {

		ImmutablePackageBean bean = new ImmutablePackageBean();

		ImmutablePackageMapper mapper = Selma.mapper(ImmutablePackageMapper.class);
		bean.setValue("test");
		Assert.assertSame(bean, mapper.as(bean));
	}

}
