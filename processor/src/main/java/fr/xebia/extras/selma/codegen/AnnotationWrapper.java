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


import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Conveniance annotation wrapper class to retrieve easily annotation parameters
 *
 * User: slemesle
 * Date: 25/11/2013
 * Time: 13:40
 */
public class AnnotationWrapper {
    private final AnnotationMirror annotationMirror;
    private final Element annotatedElement;
    private final HashMap<String, AnnotationValue> map;
    private final MapperGeneratorContext context;


    public AnnotationWrapper(MapperGeneratorContext context, AnnotationMirror annotationMirror, Element annotatedElement) {
        this.annotationMirror = annotationMirror;
        this.annotatedElement = annotatedElement;
        this.map = new HashMap<String, AnnotationValue>();
        this.context = context;

        for (ExecutableElement executableElement : context.elements.getElementValuesWithDefaults(annotationMirror).keySet()) {
            this.map.put(executableElement.getSimpleName().toString(), context.elements.getElementValuesWithDefaults(annotationMirror).get(executableElement));
        }
    }

    public static AnnotationWrapper buildFor(MapperGeneratorContext context, Element method, Class<?> annot) {


        AnnotationMirror annotationMirror = null;

        for (AnnotationMirror mirror : method.getAnnotationMirrors()) {

            if (mirror.getAnnotationType().toString().equals(annot.getCanonicalName())) {
                annotationMirror = mirror;
                break;
            }
        }

        if (annotationMirror != null) {
            return new AnnotationWrapper(context, annotationMirror, method);
        } else {
            return null;
        }

    }

    public List<String> getAsStrings(String parameterName) {

        List<String> res = new ArrayList<String>();
        AnnotationValue myValue = map.get(parameterName);
        if (myValue.getValue() instanceof List) {
            List<? extends AnnotationValue> values = (List<? extends AnnotationValue>) myValue.getValue();
            for (AnnotationValue value : values) {
                if (value.getValue() instanceof String){
                    res.add((String)value.getValue());
                }else {
                    res.add(value.toString());
                }
            }
        }

        return res;
    }

    public List<AnnotationWrapper> getAsAnnotationWrapper(String parameterName) {

        List<AnnotationWrapper> res = new ArrayList<AnnotationWrapper>();
        AnnotationValue myValue = map.get(parameterName);
        if (myValue.getValue() instanceof List) {
            List<? extends AnnotationValue> values = (List<? extends AnnotationValue>) myValue.getValue();
            for (AnnotationValue value : values) {
                if (value.getValue() instanceof AnnotationMirror){
                    res.add(new AnnotationWrapper(context, (AnnotationMirror) value, annotatedElement));
                }
            }
        }

        return res;
    }

    public boolean getAsBoolean(String ignoreMissingProperties) {
        return (Boolean) map.get(ignoreMissingProperties).getValue();

    }

    public TypeMirror getAsTypeMirror(String parameter) {
        String classe =  map.get(parameter).getValue().toString();
        final TypeElement element = context.elements.getTypeElement(classe.replace(".class", ""));

        return element.asType();
    }

    public String getAsString(String parameter) {
        return  map.get(parameter).getValue().toString();
    }

    public <T> T getAs(String parameter) {
        return (T) map.get(parameter).getValue();
    }

    public Element asElement() {
        return this.annotationMirror.getAnnotationType().asElement();
    }

    public Element getAnnotatedElement() {
        return annotatedElement;
    }

}
