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
package fr.xebia.extras.selma.codegen;

import javax.lang.model.element.*;
import javax.lang.model.util.ElementFilter;
import java.util.Iterator;
import java.util.List;

/**
 * Created by slemesle on 03/03/15.
 */
public class TypeConstructorWrapper {


    public final boolean hasDefaultConstructor;
    public final boolean hasMatchingSourcesConstructor;
    private final MapperGeneratorContext context;
    private final TypeElement typeElement;

    public TypeConstructorWrapper(MapperGeneratorContext context, TypeElement typeElement) {
        this.context = context;
        this.typeElement = typeElement;

        boolean defaultConstructor = false;
        boolean matchingSourcesConstructor = false;
        List<ExecutableElement> constructors = ElementFilter.constructorsIn(typeElement.getEnclosedElements());
        for (ExecutableElement constructor : constructors) {
            if (constructor.getModifiers().contains(Modifier.PUBLIC) && !constructor.getModifiers().contains(Modifier.ABSTRACT)) {
                int paramsCount = constructor.getParameters().size();
                if (paramsCount == 0) {
                    defaultConstructor = true;
                }
                if (!matchingSourcesConstructor && paramsCount == context.getSourcesCount()) {
                    matchingSourcesConstructor = validateParametersTypes(constructor);
                }
            }
        }
        hasMatchingSourcesConstructor = matchingSourcesConstructor;
        hasDefaultConstructor = defaultConstructor;
    }

    private boolean validateParametersTypes(ExecutableElement constructor) {
        boolean res = true;
        List<? extends VariableElement> parameters = constructor.getParameters();
        Iterator<TypeElement> sources = context.sources().iterator();
        for (VariableElement parameter : parameters) {
            TypeElement sourceType = sources.next();
            if (!parameter.asType().equals(sourceType.asType())){
                res = false;
                break;
            }
        }
        return res;
    }


    public boolean hasCallableConstructor() {
        return hasDefaultConstructor || hasMatchingSourcesConstructor;
    }
}
