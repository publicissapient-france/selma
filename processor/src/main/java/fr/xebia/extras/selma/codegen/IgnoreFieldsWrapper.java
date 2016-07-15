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

import fr.xebia.extras.selma.IgnoreFields;

import javax.lang.model.element.Element;
import javax.lang.model.type.DeclaredType;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by slemesle on 22/06/2014.
 */
public class IgnoreFieldsWrapper {

    private final AnnotationWrapper annotationWrapper;
    private final List<String> fields;
    private final TreeSet<String> unusedFields;
    private final MapperGeneratorContext context;
    private final Element mapperMethod;
    private IgnoreFieldsWrapper parent;

    public IgnoreFieldsWrapper(MapperGeneratorContext context, Element mapperMethod, List<String> ignoreFieldsParam) {
        this.context = context;
        this.mapperMethod = mapperMethod;
        annotationWrapper = AnnotationWrapper.buildFor(context, mapperMethod, IgnoreFields.class);

        // TODO : Remove IgnoreFields annotation support once deleted in 1.0 ?
        if (annotationWrapper != null) {
            fields = annotationWrapper.getAsStrings("value");
        } else if (ignoreFieldsParam != null && ignoreFieldsParam.size() > 0) {
            fields = ignoreFieldsParam;
        } else {
            fields = Collections.EMPTY_LIST;
        }
        unusedFields = new TreeSet<String>(fields);
        parent = null;
    }

    public IgnoreFieldsWrapper(MapperGeneratorContext context, Element mapperMethod, IgnoreFieldsWrapper parent, List<String> ignoreFieldsParam) {
        this(context, mapperMethod, ignoreFieldsParam);
        this.parent = parent;
    }


    /**
     * Tells wether the given field can be ignored for the given DeclaredType.
     *
     * @param field
     * @param type
     * @return
     */
    public boolean isIgnoredField(String field, DeclaredType type) {
        boolean res = false;
        String fqcn = type.toString();
        String simpleName = type.asElement().getSimpleName().toString();

        String lastIgnoredField = null;
        for (String ignoredField : fields) {
            lastIgnoredField = ignoredField;
            if (ignoredField.equalsIgnoreCase(field)) {
                res = true;
                break;
            } else if (ignoredField.contains(".")) {
                if ((fqcn + "." + field).equalsIgnoreCase(ignoredField)) {
                    res = true;
                    break;
                } else if ((simpleName + "." + field).equalsIgnoreCase(ignoredField)) {
                    res = true;
                    break;
                }
            }
        }

        if (res) { // remove ignore field from unused
            unusedFields.remove(lastIgnoredField);
        } else if (parent != null) { // Call parent since we have nothing to check here
            res = parent.isIgnoredField(field, type);
        }
        return res;
    }

    public void reportUnusedFields() {
        for (String field : unusedFields) {
            context.warn(mapperMethod, "Ignored field \"%s\" is never used", field);
        }
    }


}
