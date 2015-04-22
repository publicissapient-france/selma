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
package fr.xebia.extras.selma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Target;

import static fr.xebia.extras.selma.IgnoreMissing.DEFAULT;
import static fr.xebia.extras.selma.IoC.NO;

/**
 * Mapper annotation used to denote interfaces that needs mappers implementation to be built.
 */
@Target({ElementType.TYPE})
@Inherited
public @interface Mapper {

    /**
     * Wether compilation should fail when one field from in bean is missing in out bean<br/>
     * By default, compilation will fail and report error. Setting this to true will allow Selma to skip
     * the missing field NO MAPPING CODE WILL BE GENERATED FOR THE MISSING FIELD.
     *
     * @deprecated since 0.10, please use withIgnoreMissing instead.
     */
    @Deprecated boolean ignoreMissingProperties() default false;

    /**
     * How should selma processor handle properties not referenced in both bean.
     * Set this to :
     * IgnoreMissing.NONE if you want to selma to report compiler error for fields in source and destination
     * that can not be mapped (this is the default value).
     * IgnoreMissing.SOURCE if you want selma to report compiler error only for fields from destination bean that are missing
     * in source.
     * IgnoreMissing.DESTINATION if you want selma to report compiler error for fields from source bean that are
     * missing in destination bean.
     * IgnoreMising.ALL If you want selma to report compiler error for any missing fields from source bean and destination
     */
    IgnoreMissing withIgnoreMissing() default DEFAULT;

    /**
     * Wether compilation should fail when Selma finds a situation where it can not generate mapping code.<br/>
     * Reason is not supported in base code for this Type to Type conversion.
     * By default compilation should fail at code generation time.
     * If you prefer to generate a method that raises UnsupportedException when trying to map the field set this to true.
     */
    boolean ignoreNotSupported() default false;


    /**
     * Add a list of custom mapper class.
     * A custom mapper is a class that gives one or more method :
     * <p/>
     * public OutType methodName(InType in)
     * <p/>
     * These methods will be called to handle custom mapping of in bean to the OutType
     */
    Class<?>[] withCustom() default {};

    /**
     * Add one or more class for which instance should be passed to the out bean constructor.
     * This aims to replace the use of a Factory, in fact if you need a factory, most of the time it is because beans
     * have a specific constructor parameter that need to be filled in.
     *
     * @return
     */
    Class<?>[] withSources() default {};

    /**
     * Add one or more custom configuration for enum to enum mapping with default values.
     */
    EnumMapper[] withEnums() default {};


    /**
     * This allows to disable use of *final* modifier for generated mappers classes
     */
    boolean withFinalMappers() default true;

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
     * <li>Only give the property name ("propertyName")</li>
     * <li>Class and property name ("MyClass.propertyName")</li>
     * <li>Fully qualified class and property name ("org.mypackage.MyClass.propertyName")</li>
     * </ul>
     */
    String[] withIgnoreFields() default {};

    /**
     * This is used to describe specific field to field mapping. If you need field "toto" to be mapped to "tutu", you should add
     * a @Field annotation here.
     * <code>
     *
     * @Mapper(withCustomFields={@Field({"toto", "tutu"})})
     * </code>
     */
    Field[] withCustomFields() default {};

    /**
     * Defines the dependency injection model to be used. Supported models are :<br/>
     * NO used by default to disable dependency injection.
     * SPRING will use Spring annotations to expose the implemented mapper in Spring and injects its dependencies.
     */
    IoC withIoC() default NO;

    /**
     * By default Selma uses a setter to provide new mapped collections. Passing this attribute to ALLOW_GETTER will
     * make Selma use a getter to map collections if the setter does not exist.
     */
     CollectionMappingStrategy withCollectionStrategy() default CollectionMappingStrategy.DEFAULT;

}
