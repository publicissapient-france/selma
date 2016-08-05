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
package fr.xebia.extras.selma.it.collection;

import fr.xebia.extras.selma.Mapper;
import fr.xebia.extras.selma.beans.RawBean;
import fr.xebia.extras.selma.beans.RawListBean;
import fr.xebia.extras.selma.beans.RawMapBean;

/**
 * Have to use separate methods (and thus beans) as JDK7 doesn't reliably print multiple compile errors from the same
 * compilation method (ie the clone() methods).
 */
@Mapper
public interface RawBeanMapper {

    RawBean clone(RawBean rawBean);

    RawListBean clone(RawListBean rawBean);

    RawMapBean clone(RawMapBean rawBean);
}
