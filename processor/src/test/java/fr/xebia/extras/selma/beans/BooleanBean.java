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
package fr.xebia.extras.selma.beans;

/**
 * Bean used to demonstrate all kind of boolean properties setter and getter
 */
public class BooleanBean {

    boolean primitive;
    boolean primitiveIs;

    Boolean boxed;

    Boolean boxedIs;

    public boolean getPrimitiveIs() {
        return primitiveIs;
    }

    public void setPrimitiveIs(boolean primitiveIs) {
        this.primitiveIs = primitiveIs;
    }

    public Boolean isBoxedIs() {
        return boxedIs;
    }

    public void setBoxedIs(Boolean boxedIs) {
        this.boxedIs = boxedIs;
    }

    public boolean isPrimitive() {
        return primitive;
    }

    public void setPrimitive(boolean primitive) {
        this.primitive = primitive;
    }

    public Boolean getBoxed() {
        return boxed;
    }

    public void setBoxed(Boolean boxed) {
        this.boxed = boxed;
    }
}
