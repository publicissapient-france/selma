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

import java.util.Collections;
import java.util.List;

/**
 *
 */
public class SourceConfiguration {


    private final IgnoreFieldsWrapper ignoredFields;
    private boolean ignoreMissingProperties;
    private boolean ignoreNotSupported;
    private List<String> sourceClass;
    private boolean finalMappers;


    private SourceConfiguration(IgnoreFieldsWrapper ignoreFields) {
        this.ignoredFields = ignoreFields;
    }

    public static SourceConfiguration buildFrom(AnnotationWrapper mapper, IgnoreFieldsWrapper ignoreFields) {
        SourceConfiguration res = new SourceConfiguration(ignoreFields);

        res.ignoreMissingProperties(mapper.getAsBoolean("ignoreMissingProperties"));
        res.ignoreNotSupported(mapper.getAsBoolean("ignoreNotSupported"));
        res.finalMappers(mapper.getAsBoolean("withFinalMappers"));
        res.sourceClass(mapper.getAsStrings("withSources"));
/*
        if(ignoreFields != null){
            res.ignoredFields(ignoreFields.getAsStrings("value"));
        } else {
            res.ignoredFields(Collections.<String>emptyList());
        }
*/

        return res;
    }

    private void finalMappers(boolean finalMappers) {
        this.finalMappers = finalMappers;
    }

    public boolean isFinalMappers() {
        return finalMappers;
    }

    private void sourceClass(List<String> source) {
        this.sourceClass = source;
    }

    private SourceConfiguration ignoreNotSupported(boolean b) {
        this.ignoreNotSupported = b;
        return this;
    }

    private SourceConfiguration ignoreMissingProperties(boolean b) {
        this.ignoreMissingProperties = b;
        return this;
    }

    public boolean isIgnoreMissingProperties() {
        return ignoreMissingProperties;
    }

    public boolean isIgnoreNotSupported() {
        return ignoreNotSupported;
    }

    public IgnoreFieldsWrapper ignoredFields() {
        return ignoredFields;
    }

    public List<String> getSourceClass() {
        return sourceClass != null ? sourceClass : Collections.<String>emptyList();
    }
}
