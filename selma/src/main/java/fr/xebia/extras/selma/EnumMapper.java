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
 * This annotation can be used inside the Mapper annotation to define global mapping properties for enumeration.
 * By default Selma, maps enum values considering that source enum and destination enum should have same values.
 * With this annotation, Selma will map identical values from one bean to the other and by default use the given default value.
 */
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Inherited
public @interface EnumMapper {

    /**
     * Enumeration used as source of mapping. This parameter is needed only if used at class level.
     */
    Class<? extends Enum> from() default DefaultEnum.class ;

    /**
     * Enumeration used as destination of mapping. This parameter is needed only if used at class level.
     */
    Class<? extends Enum> to() default DefaultEnum.class;

    /**
     * Default value used when from enum value is not mappable to to enum.
     * if the default value is not specified, then mapping will default to null.
     */
    String defaultValue() default "";

}

/**
 * This enum is there only for default values on from and to in EnumMapper Annotation
 */
enum DefaultEnum{}