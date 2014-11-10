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

import fr.xebia.extras.selma.Mappings;

/**
 * Created by slemesle on 10/11/14.
 */
public class MappingsWrapper {

    //private final IgnoreFieldsWrapper ignoreFieldsWrapper;
    private final FieldsWrapper customFields;
    private final AnnotationWrapper mappings;

    public MappingsWrapper(MapperGeneratorContext context, SourceConfiguration configuration, MethodWrapper method, MappingRegistry mappingRegistry) {

        mappings = AnnotationWrapper.buildFor(context, method.element(), Mappings.class);

     //   ignoreFieldsWrapper = new IgnoreFieldsWrapper(context, method.element(), configuration.ignoredFields());

        this.customFields = new FieldsWrapper(context, method, mappingRegistry.fields(), mappings == null ? null :mappings.getAsAnnotationWrapper("withCustomFields"));
    }

    public void reportUnused() {
        customFields.reportUnused();
    }

    public String getFieldFor(String field) {
        return customFields.getFieldFor(field);
    }
}
