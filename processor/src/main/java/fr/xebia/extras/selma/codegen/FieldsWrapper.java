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

import fr.xebia.extras.selma.Fields;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by slemesle on 23/06/2014.
 */
public class FieldsWrapper {

    private MapperGeneratorContext context;
    private Element element;
    private FieldMap fieldsRegistry;
    private FieldMap unusedFields;
    private FieldsWrapper parent = null;

    private FieldsWrapper(MapperGeneratorContext context, Element element) {
        this.context = context;
        this.element = element;
        this.fieldsRegistry = new FieldMap(element);
    }

    public FieldsWrapper(MapperGeneratorContext context, MethodWrapper mapperMethod, FieldsWrapper parent, List<AnnotationWrapper> withCustomFields) {
        this(context, mapperMethod.element());

        if (mapperMethod.hasFields()) {
            processFields(context, element);
        }

        if (withCustomFields != null && withCustomFields.size() > 0){
            processFieldList(context, withCustomFields);
        }

        this.unusedFields = new FieldMap(fieldsRegistry);
        this.parent = parent;
    }


    public FieldsWrapper(MapperGeneratorContext context, TypeElement type, AnnotationWrapper mapper) {
        this(context, type);

        processFields(context, type);
        processFieldsFromMapper(context, mapper);

        this.unusedFields = new FieldMap(fieldsRegistry);
        this.parent = null;
    }

    private void processFieldsFromMapper(MapperGeneratorContext context, AnnotationWrapper mapper) {
        List<AnnotationWrapper> withCustomFields = mapper.getAsAnnotationWrapper("withCustomFields");
        processFieldList(context, withCustomFields);
    }

    private void processFields(MapperGeneratorContext context, Element type) {
        AnnotationWrapper fields = AnnotationWrapper.buildFor(context, type, Fields.class);
        if (fields != null) {
            processFieldList(context, fields.getAsAnnotationWrapper("value"));
        }

    }

    private void processFieldList(MapperGeneratorContext context,  List<AnnotationWrapper> fields) {
        for (AnnotationWrapper field : fields) {
            List<String> fieldPair = field.getAsStrings("value");
            if (fieldPair.size() != 2) {
                context.error(element, "Invalid @Field use, @Field should have 2 strings which link one field to another");
            } else {
                fieldsRegistry.push(fieldPair.get(0).toLowerCase(), fieldPair.get(1).toLowerCase());
            }
        }
    }


    public List<Field> getFieldFor(String field, DeclaredType sourceType, DeclaredType destinationType) {

        final String sourceFqcn = sourceType.toString().toLowerCase();
        final String sourceSimpleName = sourceType.asElement().getSimpleName().toString().toLowerCase();
        final String destinationFqcn = destinationType.toString().toLowerCase();
        final String destinationSimpleName = destinationType.asElement().getSimpleName().toString().toLowerCase();
        boolean foundHere = true;

        List<Field> res = fieldsRegistry.getStartingWith(field);

        if (res.isEmpty()) {

            // Look if we have simple name or fqcn field
            res = fieldsRegistry.getStartingWith(sourceFqcn + "." + field);
            if (res.isEmpty()){
                res = fieldsRegistry.getStartingWith(sourceSimpleName + "." + field);
                if (res.isEmpty()){
                    if (parent != null) { // If we have a parent pass the call to it since we did not find anything here
                        res = parent.getFieldFor(field, sourceType, destinationType);
                        foundHere = false;
                    }

                }
            }

        }

        if (foundHere && res != null) {  // This field mapping is now considered as used
            for (Field f : res) {
                unusedFields.remove(f.to);
            }
        }

        if (res != null){ // Remove fqcn or simple name from field
            for (Field re : res) {
                re.removeDestinationPrefix(destinationFqcn, destinationSimpleName);
                re.removeSourcePrefix(sourceFqcn, sourceSimpleName);
            }
        }

        return res;
    }


    public void reportUnused() {
        for (Map.Entry<String, String> unusedPair : unusedFields.entrySet()) {
            context.warn(element, "Custom @Field({\"%s\",\"%s\"}) mapping is never used !", unusedPair.getKey(), unusedPair.getValue());
        }
    }
}
