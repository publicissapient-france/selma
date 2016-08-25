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

import fr.xebia.extras.selma.IoC;
import fr.xebia.extras.selma.Mapper;
import fr.xebia.extras.selma.beans.AddressIn;
import fr.xebia.extras.selma.beans.AddressOut;

/**
 * Created by slemesle on 25/03/15.
 */
@Mapper(withIgnoreFields = "extras", withCustom = CustomImmutableMapperClass.class, withFactories = BeanFactoryClass.class, withIoC = IoC.CDI, withFinalMappers = false)
public interface AddressMapperCDI {

	AddressOut asAddressOut(AddressIn in);

}
