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
 * The InheritMaps annotation is used on a mapping method to specify that it should inherit the complete @Maps
 * configuration of another mapping method.
 *
 * By default the Maps annotation is inherited from the mapping method with the same type pair in and out or reverse.
 * But you can specify a specific method from which you want to inherit.
 */
@Target({ElementType.METHOD})
@Inherited
public @interface InheritMaps {

    /**
     * This optional parameter may be used to specify the method name from which you want to inherit the Maps
     * configuration.
     * @return
     */
    String method() default "";
}
