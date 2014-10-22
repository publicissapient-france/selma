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

import javax.lang.model.type.TypeMirror;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by slemesle on 29/09/2014.
 */
public class EnumMappersWrapper {

    public static final String DEFAULT_ENUM = "fr.xebia.extras.selma.EnumMapper";
    private final AnnotationWrapper mapperClassAnnotation;
    private final MapperGeneratorContext context;

    private final Map<InOutType, MappingBuilder> registryMap;
    private final HashMap<InOutType, AnnotationWrapper> unusedEnumMappers;

    public EnumMappersWrapper(AnnotationWrapper mapperClassAnnotation, MapperGeneratorContext context) {
        this.mapperClassAnnotation = mapperClassAnnotation;
        this.context = context;
        unusedEnumMappers = new HashMap<InOutType, AnnotationWrapper>();
        registryMap = new HashMap<InOutType, MappingBuilder>();

        for (AnnotationWrapper enumMapper : mapperClassAnnotation.getAsAnnotationWrapper("withEnums")) {

            buildEnumMapper(enumMapper);
        }

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
        } else {

            MappingBuilder res = MappingBuilder.newCustomEnumMapper(inOutType, defaultValue);

            unusedEnumMappers.put(inOutType, enumMapper);
            registryMap.put(inOutType, res);
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
        }
        return res;
    }

    public void reportUnused() {

        for (Map.Entry<InOutType, AnnotationWrapper> entry : unusedEnumMappers.entrySet()) {
            context.warn(entry.getValue().getAnnotatedElement(), "@EnumMapper(from=%s, to=%s) is never used", entry.getKey().in(), entry.getKey().out());
        }
    }
}
