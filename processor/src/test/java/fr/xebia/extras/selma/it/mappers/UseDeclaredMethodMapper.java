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
package fr.xebia.extras.selma.it.mappers;

import fr.xebia.extras.selma.Mapper;
import fr.xebia.extras.selma.beans.AddressIn;
import fr.xebia.extras.selma.beans.AddressOut;
import fr.xebia.extras.selma.beans.PersonIn;
import fr.xebia.extras.selma.beans.PersonOut;

/**
 *
 */
@Mapper(ignoreMissingProperties = true, withFinalMappers = false, withInstanceCache = false)
public interface UseDeclaredMethodMapper {

    /**
     * Handles mapping of PersonIn to PersonOut
     *
     * @return
     */
    PersonOut asPersonOut(PersonIn in);

    /**
     * Handles mapping of AddressIn to AddressOut.
     * should be called by asPersonOut() method to map persons addresses
     *
     * @param in
     * @return
     */
    AddressOut asAddressOut(AddressIn in);


}
