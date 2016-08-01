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
 * Define a fieldname based mapping.
 * A @Field link 2 property names in any order to map them.
 *
 * @Field({"name", "firstname"}) will map name to firstname and reverse.<br/>
 * <p/>
 * You can use a CustomMapper here using withCustom = CustomMapper.class. This mapper
 * will be used to convert the defined field pair.
 * @Field({"name", "firstname"}, withCustom = MyCustomMapper.class) will map name to firstname
 * and reverse using the eligible custom mapper method.
 * <p/>
 * To simplify custom field to field mappers, Selma allows to define only one value when withCustom
 * is declared. This can be usefull when you want to define a specific mapper for a property defined
 * in both source and destination.
 * @Field({"name"}, withCustom = MyCustomMapper.class) will map name to name using the eligible
 * custom mapper method.
 */
@Target({ElementType.ANNOTATION_TYPE})
@Inherited
public @interface Field {

    /**
     * Contains the target field names with potential embedded path.
     * Should contain two values except when @withCustom is defined.
     */
    String[] value();

    /**
     * Contains the class of the custom mapper where Selma will search a corresponding
     * mapping method. Default to Object.class to discover when withCustom is defined.
     */
    Class<?> withCustom() default Object.class;

}
