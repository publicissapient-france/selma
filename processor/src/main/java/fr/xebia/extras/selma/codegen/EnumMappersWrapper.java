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

import fr.xebia.extras.selma.EnumMapper;

import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by slemesle on 29/09/2014.
 */
public class EnumMappersWrapper {

    public static final String DEFAULT_ENUM = "fr.xebia.extras.selma.EnumMapper";
    private final MapperGeneratorContext context;
    private final Element reportElement;

    private final Map<InOutType, MappingBuilder> registryMap;
    private final HashMap<InOutType, AnnotationWrapper> unusedEnumMappers;
    private EnumMappersWrapper parent = null;


    public EnumMappersWrapper(List<AnnotationWrapper> annotationWrappers, MapperGeneratorContext context, Element reportElement) {
        this.context = context;
        this.reportElement = reportElement;
        unusedEnumMappers = new HashMap<InOutType, AnnotationWrapper>();
        registryMap = new HashMap<InOutType, MappingBuilder>();

        if (annotationWrappers != null) {
            for (AnnotationWrapper enumMapper : annotationWrappers) {

                buildEnumMapper(enumMapper);
            }
        }

    }

    public EnumMappersWrapper(EnumMappersWrapper parent, List<AnnotationWrapper> enums, Element reportElement) {
        this(enums, parent.context, reportElement);
        this.parent = parent;
    }


    private void buildEnumMapper(AnnotationWrapper enumMapper) {
        InOutType inOutType = new InOutType(enumMapper.getAsTypeMirror("from"), enumMapper.getAsTypeMirror("to"), false);
        buildEnumMapperForInOutType(enumMapper, inOutType);
    }

    private void buildEnumMapperForInOutType(AnnotationWrapper enumMapper, InOutType inOutType) {
        String defaultValue = enumMapper.getAsString("defaultValue");

        if (!inOutType.areEnums()) {
            context.error(enumMapper.asElement(), "Invalid type given in @EnumMapper one of from=%s and to=%s is not an Enum.\\n You should only use enum types here", inOutType.in(), inOutType.out());
        } else if (inOutType.in().toString().contains(DEFAULT_ENUM) || inOutType.out().toString().contains(DEFAULT_ENUM)) {
            context.error(enumMapper.asElement(), "EnumMapper miss use: from and to are mandatory in @EnumMapper when not used on a method that maps enumerations.\\n You should define from and to for this EnumMapper.");
        } else if (!defaultValue.isEmpty() && !MappingBuilder.collectEnumValues(inOutType.outAsTypeElement()).contains(defaultValue)) {
            context.error(reportElement, "Invalid default value for @EnumMapper(from=%s.class, to=%s.class, default=\"%s\") %s.%s does not exist", inOutType.in(), inOutType.out(), defaultValue, inOutType.out(), defaultValue);
        } else {

            MappingBuilder res = MappingBuilder.newCustomEnumMapper(inOutType, defaultValue);

            unusedEnumMappers.put(inOutType, enumMapper);
            registryMap.put(inOutType, res);
            registryMap.put(new InOutType(inOutType, true), res);
        }
    }


    public void buildForMethod(MethodWrapper methodWrapper) {
        if (methodWrapper.hasEnumMapper()) {

            TypeMirror enumOut = methodWrapper.returnType();
            TypeMirror enumIn = methodWrapper.firstParameterType();
            InOutType inOutType = new InOutType(enumIn, enumOut, false);

            AnnotationWrapper enumMapper = AnnotationWrapper.buildFor(context, methodWrapper.element(), EnumMapper.class);

            // when mapping is used on a method mapping one enum to another we can use these values
            if (!inOutType.areEnums()) {

                buildEnumMapper(enumMapper);
            } else {
                // Use from and to in annotation
                buildEnumMapperForInOutType(enumMapper, inOutType);
            }
        }
    }

    public MappingBuilder get(InOutType inOutType) {

        MappingBuilder res = registryMap.get(inOutType);
        if (res != null) {
            unusedEnumMappers.remove(inOutType);
        } else if (parent != null) { // Search in parent after current scope
            res = parent.get(inOutType);
        }

        return res;
    }

    public void reportUnused() {

        for (Map.Entry<InOutType, AnnotationWrapper> entry : unusedEnumMappers.entrySet()) {
            context.warn(entry.getValue().getAnnotatedElement(), "@EnumMapper(from=%s, to=%s) is never used", entry.getKey().in(), entry.getKey().out());
        }
    }
}
