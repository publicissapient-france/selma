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

/**
 * @IgnoreFields annotation should be placed on Mapper interface methods to specify
 * explicit ignore fields.
 * This annotation can be used at method level to specify specific names to skip only in the scope of the method,
 * or at the interface level to apply ignored names to all mapping methods of the interface
 * @deprecated Since 0.10, You should use @Mapper(withIgnoreFields={}) or @Mappings(withIgnoreFields={}).
 */
@Deprecated()
@Target({ElementType.METHOD, ElementType.TYPE})
@Inherited
public @interface IgnoreFields {

    /**
     * give field or property names to skip at mapping code generation time
     *
     * @return
     */
    String[] value();

}
