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
package fr.xebia.extras.selma.it.mappers;

/**
 * Created by slemesle on 07/05/2014.
 */
public class NoDefaultConstructorCustomMapper {

    private final String test;

    public NoDefaultConstructorCustomMapper(String test) {

        this.test = test;
    }


    public String longAsString(Long id) {
        return (id != null ? id.toString() : null);
    }
}
