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

import fr.xebia.extras.selma.beans.CityIn;
import fr.xebia.extras.selma.beans.CityOut;
import fr.xebia.extras.selma.beans.DataSource;

/**
 *
 */
public abstract class BadCustomMapper {


    public static CityOut staticMethod(CityIn in) {
        return null;
    }

    CityOut notPublicStaticMethod(CityIn in) {
        return null;
    }

    public void voidMethod(CityIn in) {
        return;
    }

    public CityOut noParameterMethod() {
        return null;
    }

    public CityOut twoParameterMethod(CityIn in, DataSource dataSource) {
        return null;
    }

    public CityOut threeParameterMethod(CityIn in, CityIn in2, CityIn in3) {
        return null;
    }


}
