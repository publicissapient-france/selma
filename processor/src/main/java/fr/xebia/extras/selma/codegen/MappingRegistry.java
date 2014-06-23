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
package fr.xebia.extras.selma.codegen;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class MappingRegistry {

    public static final String DEFAULT_ENUM = "fr.xebia.extras.selma.EnumMapper";
    final Map<InOutType, MappingBuilder> registryMap;
    final Map<InOutType, MappingBuilder> interceptorMap;
    final MapperGeneratorContext context;
    private FieldsWrapper fields;


    public MappingRegistry(MapperGeneratorContext context) {
        this.registryMap = new HashMap<InOutType, MappingBuilder>();
        this.interceptorMap = new HashMap<InOutType, MappingBuilder>();
        this.context = context;
    }


    public MappingRegistry(MappingRegistry registry) {
        this.registryMap = new HashMap<InOutType, MappingBuilder>(registry.registryMap);
        this.interceptorMap = new HashMap<InOutType, MappingBuilder>(registry.interceptorMap);
        this.context = registry.context;
    }

    public MappingBuilder findMappingFor(InOutType inOutType) {

        MappingBuilder res;
        // First look in registry
        if (registryMap.get(inOutType) != null) {
            res = registryMap.get(inOutType);
        } else { // look in chain

            res = MappingBuilder.getBuilderFor(context, inOutType);
            if (res != null && !inOutType.areDeclared()) {
                registryMap.put(inOutType, res);
            }

        }

        return res;
    }

    /**
     * Adds a custom mapping method to the registry for later use at codegen.
     *
     * @param customMapper
     * @param method
     */
    public void pushCustomMapper(final String customMapper, final MethodWrapper method) {


        InOutType inOutType = method.inOutType();
        MappingBuilder res = MappingBuilder.newCustomMapper(inOutType, String.format("%s.%s", customMapper, method.getSimpleName()));

        registryMap.put(inOutType, res);
    }

    public void pushCustomEnumMapper(AnnotationWrapper enumMapper) {

        InOutType inOutType = new InOutType(enumMapper.getAsTypeMirror("from"), enumMapper.getAsTypeMirror("to"));
        pushCustomEnumMapper(inOutType, enumMapper);
    }

    public void pushCustomEnumMapper(InOutType inOutType, AnnotationWrapper enumMapper) {

        String defaultValue = enumMapper.getAsString("defaultValue");

        if (!inOutType.areEnums()){
            context.error(enumMapper.asElement(), "Invalid type given in @EnumMapper one of from=%s and to=%s is not an Enum.\\n You should only use enum types here", inOutType.in(), inOutType.out());
        } else if (inOutType.in().toString().contains(DEFAULT_ENUM) || inOutType.out().toString().contains(DEFAULT_ENUM)){

            context.error(enumMapper.asElement(), "EnumMapper miss use: from and to are mandatory in @EnumMapper when not used on a method that maps enumerations.\\n You should define from and to for this EnumMapper.");
        } else {

            MappingBuilder res = MappingBuilder.newCustomEnumMapper(inOutType, defaultValue);
            registryMap.put(inOutType, res);
        }

    }

    public void pushMappingInterceptor(String customMapper, MethodWrapper method) {
        InOutType inOutType = method.inOutArgs();
        MappingBuilder res = MappingBuilder.newMappingInterceptor(inOutType, String.format("%s.%s", customMapper, method.getSimpleName()));
        interceptorMap.put(inOutType, res);
    }

    public MappingBuilder mappingInterceptor(InOutType inOutType) {
        return interceptorMap.get(inOutType);
    }

    public void fields(FieldsWrapper fields) {

        this.fields = fields;
    }

    public FieldsWrapper fields() {
        return fields;
    }
}
