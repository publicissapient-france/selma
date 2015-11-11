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

import fr.xebia.extras.selma.Mapper;
import fr.xebia.extras.selma.Maps;
import fr.xebia.extras.selma.beans.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by slemesle on 19/11/14.
 */
@Mapper(
        withIgnoreFields = {"fr.xebia.extras.selma.beans.PersonIn.male", "fr.xebia.extras.selma.beans.PersonOut.biography"}
)
public abstract class AbstractMapperWithCustom {

    public static final int NUMBER_INCREMENT = 10000;

    public abstract PersonOut asPersonOut(PersonIn in);

    public abstract CityOut asCityOut(CityIn in);

    /**
     * Custom mapper inside the Mapper class
     */
    public AddressOut mapAddress(AddressIn addressIn){
        AddressOut res= null;
        if (addressIn != null){
            res = new AddressOut();
            res.setCity(this.asCityOut(addressIn.getCity()));
            res.setExtras(addressIn.getExtras() == null ? null : new ArrayList<String>(addressIn.getExtras()));
            res.setNumber(addressIn.getNumber() + NUMBER_INCREMENT);
            res.setPrincipal(addressIn.isPrincipal());
            res.setStreet(addressIn.getStreet());
        }
        return res;
    }


}
