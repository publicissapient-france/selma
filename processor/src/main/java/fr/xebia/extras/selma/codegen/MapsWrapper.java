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

import fr.xebia.extras.selma.Maps;

import javax.lang.model.type.DeclaredType;

/**
 * Created by slemesle on 10/11/14.
 */
public class MapsWrapper {

    public static final String WITH_CUSTOM_FIELDS = "withCustomFields";
    public static final String WITH_IGNORE_FIELDS = "withIgnoreFields";
    public static final String WITH_ENUMS = "withEnums";
    public static final String IGNORE_MISSING_PROPERTIES = "ignoreMissingProperties";
    //private final IgnoreFieldsWrapper ignoreFields;
    private final FieldsWrapper customFields;
    private final AnnotationWrapper maps;
    private final MappingRegistry registry;
    private final IgnoreFieldsWrapper ignoreFields;
    private final EnumMappersWrapper enumMappers;
    private final MethodWrapper method;
    private final MapperWrapper mapperWrapper;
    private final MapperGeneratorContext context;
    private boolean ignoreMissingProperties;

    public MapsWrapper(MethodWrapper method, MapperWrapper mapperWrapper) {

        this.method = method;
        this.mapperWrapper = mapperWrapper;
        this.registry = new MappingRegistry(mapperWrapper.registry());
        this.context = mapperWrapper.context();

        maps = AnnotationWrapper.buildFor(context, method.element(), Maps.class);

        ignoreFields = new IgnoreFieldsWrapper(context, method.element(), mapperWrapper.ignoredFields(), maps == null ? null : maps.getAsStrings(WITH_IGNORE_FIELDS));

        this.customFields = new FieldsWrapper(context, method, mapperWrapper.fields(), maps == null ? null : maps.getAsAnnotationWrapper(WITH_CUSTOM_FIELDS));

        enumMappers = new EnumMappersWrapper(registry.getEnumMappers(),  maps == null ? null : maps.getAsAnnotationWrapper(WITH_ENUMS), method.element());
        registry.enumMappers(enumMappers);

        if (mapperWrapper.isIgnoreMissingProperties()){
            ignoreMissingProperties = true;
        } else if (maps != null && maps.getAsBoolean(IGNORE_MISSING_PROPERTIES)) {
            ignoreMissingProperties = true;
        } else {
            ignoreMissingProperties = false;
        }

    }

    public void reportUnused() {
        // Report unused ignore fields
        ignoreFields.reportUnusedFields();

        // Report unused custom field to field
        customFields.reportUnused();

        // Report unused custom enum mappers
        enumMappers.reportUnused();
    }

    public String getFieldFor(String field) {
        return customFields.getFieldFor(field);
    }

    public MappingBuilder mappingInterceptor(InOutType inOutType) {
        return registry.mappingInterceptor(inOutType);
    }

    public MappingBuilder findMappingFor(InOutType inOutType) {
        return registry.findMappingFor(inOutType);
    }

    public boolean isIgnoredField(String field, DeclaredType declaredType) {
        return ignoreFields.isIgnoredField(field, declaredType);
    }

    public boolean isIgnoreMissingProperties() {
        return ignoreMissingProperties;
    }
}
