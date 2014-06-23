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
import java.util.List;
import java.util.Map;

/**
 * Created by slemesle on 23/06/2014.
 */
public class FieldsWrapper {

    private MapperGeneratorContext context;
    private Element element;
    private BidiMap<String> fieldsRegistry;
    private BidiMap<String> unusedFields;
    private FieldsWrapper parent = null;

    private FieldsWrapper(MapperGeneratorContext context, Element element) {
        this.context = context;
        this.element = element;
        this.fieldsRegistry = new BidiMap<String>();
    }

    public FieldsWrapper(MapperGeneratorContext context, MethodWrapper mapperMethod, FieldsWrapper parent) {
        this(context, mapperMethod.element());

        if (mapperMethod.hasFields()) {
            processFields(context, element);
        }

        this.unusedFields = new BidiMap<String>(fieldsRegistry);
        this.parent = parent;
    }


    public FieldsWrapper(MapperGeneratorContext context, TypeElement type) {
        this(context, (Element) type);

        processFields(context, type);

        this.unusedFields = new BidiMap<String>(fieldsRegistry);
        this.parent = null;
    }

    private void processFields(MapperGeneratorContext context, Element type) {
        AnnotationWrapper fields = AnnotationWrapper.buildFor(context, type, Fields.class);
        if (fields != null) {
            for (AnnotationWrapper field : fields.getAsAnnotationWrapper("value")) {
                List<String> fieldPair = field.getAsStrings("value");
                if (fieldPair.size() != 2) {
                    context.error(element, "Invalid @Field use, @Field should have 2 strings which link one field to another");
                } else {
                    fieldsRegistry.push(fieldPair.get(0).toLowerCase(), fieldPair.get(1).toLowerCase());
                }
            }
        }
    }


    public String getFieldFor(String field) {

        String res = null;
        String outField = fieldsRegistry.get(field);

        if (outField == null) {
            res = field; // By default return the given field name

            if (parent != null) { // If we have a parent pass the call to it since we did not find anything here
                res = parent.getFieldFor(field);
            }

        } else {  // This field mapping is now considered as used
            unusedFields.remove(field);
            res = outField;
        }

        return res;
    }


    public void reportUnused() {
        for (Map.Entry<String, String> unusedPair : unusedFields.entrySet()) {
            context.warn(element, "Custom @Field({\"%s\",\"%s\"}) mapping is never used !", unusedPair.getKey(), unusedPair.getValue());
        }
    }
}
