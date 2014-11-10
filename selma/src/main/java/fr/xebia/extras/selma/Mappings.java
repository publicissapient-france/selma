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
 * Mapper annotation used to describe specific mappings for methods inside a mapper interface.
 */
@Target({ElementType.METHOD})
@Inherited
public @interface Mappings {


    /**
     * Wether compilation should fail when one field from in bean is missing in out bean<br/>
     * By default, compilation will fail and report error. Setting this to true will allow Selma to skip
     * the missing field NO MAPPING CODE WILL BE GENERATED FOR THE MISSING FIELD.
     */
    boolean ignoreMissingProperties() default false;

    /**
     * Add a list of custom mapper class.
     * A custom mapper is a class that gives one or more method :
     *
     * public OutType methodName(InType in)
     *
     * These methods will be called to handle custom mapping of in bean to the OutType
     */
    Class<?>[] withCustom() default {};

    /**
     * Add one or more custom configuration for enum to enum mapping with default values.
     */
    EnumMapper[] withEnums() default {};

    /**
     * This is used to declare custom immutable types. Selma will copy by reference these types, when it meets same
     * type for matching fields in source and destination bean.
     */
    Class<?>[] withImmutables() default {};

    /**
     * This is used to describe fields to be ignored in the generated mapping methods. This allows to skip specific properties
     * not needed in the destination bean or missing from one or both source and destination bean. Please notice that all strings described
     * here are ignoring case.
     * This support 3 kinds of notations to describe the property to ignore :
     * <ul>
     *     <li>Only give the property name ("propertyName")</li>
     *     <li>Class and property name ("MyClass.propertyName")</li>
     *     <li>Fully qualified class and property name ("org.mypackage.MyClass.propertyName")</li>
     * </ul>
     */
    String[] withIgnoreFields() default {};

    /**
     * This is used to describe specific field to field mapping. If you need field "toto" to be mapped to "tutu", you should add
     * a @Field annotation here.
     * <code>
     *    @Mapper(withCustomFields={@Field({"toto", "tutu"})})
     * </code>
     */
    Field[] withCustomFields() default {};



}
