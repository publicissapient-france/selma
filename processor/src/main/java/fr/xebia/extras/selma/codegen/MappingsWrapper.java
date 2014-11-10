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
public class MappingsWrapper {

    public static final String WITH_CUSTOM_FIELDS = "withCustomFields";
    public static final String WITH_IGNORE_FIELDS = "withIgnoreFields";
    //private final IgnoreFieldsWrapper ignoreFields;
    private final FieldsWrapper customFields;
    private final AnnotationWrapper mappings;
    private final MappingRegistry registry;
    private final IgnoreFieldsWrapper ignoreFields;

    public MappingsWrapper(MapperGeneratorContext context, SourceConfiguration configuration, MethodWrapper method, MappingRegistry mappingRegistry) {

        this.registry = new MappingRegistry(mappingRegistry);

        mappings = AnnotationWrapper.buildFor(context, method.element(), Maps.class);

        ignoreFields = new IgnoreFieldsWrapper(context, method.element(), configuration.ignoredFields(), mappings == null ? null :mappings.getAsStrings(WITH_IGNORE_FIELDS));

        this.customFields = new FieldsWrapper(context, method, mappingRegistry.fields(), mappings == null ? null :mappings.getAsAnnotationWrapper(WITH_CUSTOM_FIELDS));
    }

    public void reportUnused() {
        // Report unused ignore fields
        ignoreFields.reportUnusedFields();

        // Report unused custom field to field
        customFields.reportUnused();
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
}
