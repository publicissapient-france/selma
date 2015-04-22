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
import java.util.Collection;

import static fr.xebia.extras.selma.IgnoreMissing.DEFAULT;
import static fr.xebia.extras.selma.IgnoreMissing.NONE;

/**
 * Mapper annotation used to describe specific mappings for methods inside a mapper interface.
 */
@Target({ElementType.METHOD})
@Inherited
public @interface Maps {

    /**
     * How should selma processor handle properties not referenced in both bean.
     * Set this to :
     * {@code IgnoreMissing.NONE} if you want to selma to report compiler error for fields in source and destination
     * that can not be mapped (this is the default value).
     * {@code IgnoreMissing.SOURCE} if you want selma to report compiler error only for fields from destination bean that are missing
     * in source.
     * {@code IgnoreMissing.DESTINATION} if you want selma to report compiler error for fields from source bean that are
     * missing in destination bean.
     * {@code IgnoreMising.ALL} If you want selma to report compiler error for any missing fields from source bean and destination
     */
    IgnoreMissing withIgnoreMissing() default DEFAULT;

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


    /**
     * By default Selma uses a setter to provide new mapped collections. Passing this attribute to true will
     * make Selma use a getter to map collections if the setter does not exist
     */
     CollectionMappingStrategy withCollectionStrategy() default CollectionMappingStrategy.DEFAULT;

}
