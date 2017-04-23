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
import java.util.List;
import java.util.Map;

/**
 *
 */
public class MappingRegistry {

    final Map<InOutType, MappingBuilder> registryMap;
    final Map<InOutType, MappingBuilder> interceptorMap;
    final MapperGeneratorContext context;
    private FieldsWrapper fields;
    private CustomMapperWrapper customMappers;
    private EnumMappersWrapper enumMappers;
    private ImmutableTypesWrapper immutableTypes;


    public MappingRegistry(MapperGeneratorContext context) {
        this.registryMap = new HashMap<InOutType, MappingBuilder>();
        this.interceptorMap = new HashMap<InOutType, MappingBuilder>();
        this.context = context;
    }


    public MappingRegistry(MappingRegistry registry) {
        this.registryMap = new HashMap<InOutType, MappingBuilder>(registry.registryMap);
        this.interceptorMap = new HashMap<InOutType, MappingBuilder>(registry.interceptorMap);
        this.context = registry.context;
        this.customMappers = registry.customMappers;
        this.enumMappers = registry.enumMappers;
        this.immutableTypes = registry.immutableTypes;
        this.fields = registry.fields;
    }

    public MappingBuilder findMappingFor(InOutType inOutType) {

        MappingBuilder res = customMappers.getMapper(inOutType);
        // First look in registry

        if (res == null) {
            res = enumMappers.get(inOutType);
        }
        if (res == null) {
            res = registryMap.get(inOutType);
        }
        if (res == null) {
            res = immutableTypes.get(inOutType);
        }
        if (res == null) {
            // look in chain

            res = MappingBuilder.getBuilderFor(context, inOutType);
            if (res != null && !inOutType.areDeclared()) {
                registryMap.put(inOutType, res);
            }

        }

        return res;
    }

    public MappingBuilder mappingInterceptor(List<InOutType> inOutType) {
        return customMappers.getMappingInterceptor(inOutType);
    }

    public void fields(FieldsWrapper fields) {

        this.fields = fields;
    }

    public FieldsWrapper fields() {
        return fields;
    }

    public void customMappers(CustomMapperWrapper customMapers) {

        this.customMappers = customMapers;
    }

    public void enumMappers(EnumMappersWrapper enumMappers) {

        this.enumMappers = enumMappers;
    }

    public void immutableTypes(ImmutableTypesWrapper immutablesMapper) {
        this.immutableTypes = immutablesMapper;
    }

    public EnumMappersWrapper getEnumMappers() {
        return enumMappers;
    }
}
