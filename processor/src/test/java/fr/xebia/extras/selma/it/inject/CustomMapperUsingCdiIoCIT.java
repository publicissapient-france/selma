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
package fr.xebia.extras.selma.it.inject;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;

import org.apache.webbeans.config.WebBeansContext;
import org.apache.webbeans.spi.ContainerLifecycle;
import org.junit.Assert;
import org.junit.Test;

import fr.xebia.extras.selma.beans.AddressIn;
import fr.xebia.extras.selma.beans.AddressOut;
import fr.xebia.extras.selma.beans.CityIn;
import fr.xebia.extras.selma.it.custom.mapper.CustomImmutableMapper;
import fr.xebia.extras.selma.it.utils.Compile;
import fr.xebia.extras.selma.it.utils.IntegrationTestBase;

@Compile(withClasses = { AddressMapperCDI.class, CustomImmutableMapperClass.class, BeanFactoryClass.class })
public class CustomMapperUsingCdiIoCIT extends IntegrationTestBase {

	@Test
	public void given_custom_as_selma_mapper_should_map_bean_with_it()
			throws IllegalAccessException, InstantiationException, ClassNotFoundException {

		final WebBeansContext currentInstance = WebBeansContext.currentInstance();

		final ContainerLifecycle container = currentInstance.getService(ContainerLifecycle.class);

		container.startApplication(null);

		final BeanManager beanManager = container.getBeanManager();

		final Set<Bean<?>> beans = beanManager.getBeans(AddressMapperCDI.class);

		final Iterator<Bean<?>> iterator = beans.iterator();
		final Bean<?> bean = iterator.next();
		final Object create = bean.create(null);
		final AddressMapperCDI addressMapper = (AddressMapperCDI) create;

		final AddressIn addressIn = new AddressIn();
		addressIn.setCity(new CityIn());
		addressIn.getCity().setCapital(true);
		addressIn.getCity().setName("Paris");
		addressIn.getCity().setPopulation(3 * 1000 * 1000);
		addressIn.setPrincipal(true);
		addressIn.setNumber(55);
		addressIn.setStreet("rue de la truanderie");
		addressIn.setExtras(Arrays.asList("titi", "toto"));

		final AddressOut res = addressMapper.asAddressOut(addressIn);

		Assert.assertNotNull(res);

		Assert.assertEquals(addressIn.getStreet(), res.getStreet());
		Assert.assertEquals(addressIn.getNumber(), res.getNumber());
		Assert.assertNull(res.getExtras());

		Assert.assertEquals(addressIn.getCity().getName() + CustomImmutableMapper.IMMUTABLY_MAPPED, res.getCity().getName());
		Assert.assertEquals(addressIn.getCity().getPopulation() + CustomImmutableMapper.POPULATION_INC, res.getCity().getPopulation());
		Assert.assertEquals(addressIn.getCity().isCapital(), res.getCity().isCapital());
	}
}
