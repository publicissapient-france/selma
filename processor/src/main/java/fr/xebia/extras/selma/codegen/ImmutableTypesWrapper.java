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

import javax.lang.model.element.TypeElement;
import java.util.*;

/**
 * Created by slemesle on 17/10/2014.
 */
public class ImmutableTypesWrapper {
    private final AnnotationWrapper mapper;
    private final MapperGeneratorContext context;
    private final Map<InOutType, MappingBuilder> immutables;
    private final Set<InOutType> unusedImmutables;


    public ImmutableTypesWrapper(AnnotationWrapper mapper, MapperGeneratorContext context) {
        this.mapper = mapper;
        this.context = context;
        this.immutables = new HashMap<InOutType, MappingBuilder>();
        this.unusedImmutables = new HashSet<InOutType>();

        List<String> immutables = mapper.getAsStrings("withImmutables");
        for (String immutableClass : immutables) {
            final TypeElement element = context.elements.getTypeElement(immutableClass.replace(".class", ""));

            InOutType ioType = new InOutType(element.asType(), element.asType(), false);
            this.immutables.put(ioType, MappingBuilder.newImmutable());
            this.unusedImmutables.add(ioType);
        }
    }


    public MappingBuilder get(InOutType inOutType) {
        MappingBuilder mappingBuilder = immutables.get(inOutType);

        if (mappingBuilder != null) {
            unusedImmutables.remove(inOutType);
        }
        return mappingBuilder;
    }

    public void reportUnused() {
        for (InOutType immutableIO : unusedImmutables) {
            context.warn(mapper.getAnnotatedElement(), "Immutable class \"%s\" is never used", immutableIO.in());
        }
    }

}
