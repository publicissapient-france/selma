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
package fr.xebia.extras.selma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Target;

/**
 * Define a fieldname based mapping, @Field can only contain 2 string as values that will map names.
 *
 * @Field({"name", "firstname"}) will map name to firstname and reverse.<br/>
 */
@Target({ElementType.ANNOTATION_TYPE})
@Inherited
public @interface Field {

    /**
     * Value Should contain 2 values and its definition is mandatory.
     * Compilation will fail on mis-configuration.
     */
    String[] value();


    Class<?> withCustom() default Object.class;
}

