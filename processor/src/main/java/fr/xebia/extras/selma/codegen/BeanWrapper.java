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

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import java.util.*;

/**
 * Wrapper for specific type
 * builds a graph of fields and getter/setter methods for a type.
 */
public class BeanWrapper {


    private static final Set<String> exclusions = new TreeSet<String>(Arrays.asList("getClass"));

    final MapperGeneratorContext context;
    final TypeElement typeElement;
    final Map<String, FieldItem> fieldsGraph;
    private final TypeConstructorWrapper constructors;


    public BeanWrapper(MapperGeneratorContext context, TypeElement typeElement) {
        this.context = context;
        this.typeElement = typeElement;

        fieldsGraph = buildFieldGraph();
        constructors = new TypeConstructorWrapper(context, typeElement);
    }

    private Map<String, FieldItem> buildFieldGraph() {
        final HashMap<String, FieldItem> result = new HashMap<String, FieldItem>();
        final List<? extends Element> elementInList = context.elements.getAllMembers(typeElement);
        final List<ExecutableElement> methods = ElementFilter.methodsIn(elementInList);

        // looping around all methods
        for (Iterator<ExecutableElement> it = methods.iterator(); it.hasNext(); ) {
            ExecutableElement method = it.next();

            if (exclusions.contains(method.getSimpleName().toString())) {
                continue;
            }
            MethodWrapper methodWrapper = new MethodWrapper(method, (DeclaredType) typeElement.asType(), context);

            if (methodWrapper.isGetter()) {
                putGetterField(methodWrapper, result);
            } else if (methodWrapper.isSetter()) {
                putSetterField(methodWrapper, result);
            }

        }

        return result;
    }

    private void putSetterField(MethodWrapper methodWrapper, HashMap<String, FieldItem> result) {

        String field = methodWrapper.getFieldName().toLowerCase();
        FieldItem item = result.get(field);
        if (item != null) {
            item = new FieldItem(field, methodWrapper, item.getter);
        } else {
            item = new FieldItem(field, methodWrapper, null);
        }
        result.put(field, item);
    }

    private void putGetterField(MethodWrapper methodWrapper, HashMap<String, FieldItem> result) {

        String field = methodWrapper.getFieldName().toLowerCase();
        FieldItem item = result.get(field);
        if (item != null) {
            item = new FieldItem(item.field, item.setter, methodWrapper);
        } else {
            item = new FieldItem(field, null, methodWrapper);
        }

        result.put(field, item);
    }


    public Iterable<? extends String> getFields() {
        return fieldsGraph.keySet();
    }

    public boolean hasFieldAndSetter(String field) {
        boolean res = false;
        FieldItem item = fieldsGraph.get(field);

        if (item != null && item.setter != null) {
            res = true;
        }

        return res;
    }

    public boolean hasFieldAndGetter(String field) {
        boolean res = false;
        FieldItem item = fieldsGraph.get(field);

        if (item != null && item.getter != null) {
            res = true;
        }

        return res;
    }

    public String getSetterFor(String field) {
        String res = null;
        FieldItem item = fieldsGraph.get(field);

        if (item != null && item.setter != null) {
            res = item.setter.getSimpleName().toString();
        }
        return res;
    }

    public String getOutSetterPathFor(String field) {
        return String.format("out.%s", getSetterFor(field));
    }

    public String getOutGetterPathFor(String field) {
        return String.format("out.%s", getGetterFor(field));
    }

    public String getGetterFor(String field) {
        String res = null;
        FieldItem item = fieldsGraph.get(field);

        if (item != null && item.getter != null) {
            res = item.getter.getSimpleName().toString();
        }
        return res;
    }


    public String getInGetterFor(String field) {
        return String.format("in.%s()", getGetterFor(field));
    }


    public Set<String> getSetterFields() {
        Set<String> res = new TreeSet<String>();
        for (String s : fieldsGraph.keySet()) {
            if (fieldsGraph.get(s).setter != null) {
                res.add(s);
            }
        }
        return res;
    }

    public Set<String> getGetterFields() {
        Set<String> res = new TreeSet<String>();
        for (String s : fieldsGraph.keySet()) {
            if (fieldsGraph.get(s).getter != null) {
                res.add(s);
            }
        }
        return res;
    }


    public boolean hasMatchingSourcesConstructor() {
        return constructors.hasMatchingSourcesConstructor;
    }

    public boolean hasCallableConstructor() {
        return constructors.hasCallableConstructor();
    }

    public TypeMirror getTypeForGetter(String field) {
        TypeMirror result = null;
        FieldItem item = fieldsGraph.get(field);

        if (item != null && item.getter != null) {
            result = item.getter.returnType();
        }

        return result;
    }

    public TypeMirror getTypeForSetter(String field) {
        TypeMirror result = null;
        FieldItem item = fieldsGraph.get(field);

        if (item != null && item.setter != null) {
            result = item.setter.firstParameterType();
        }

        return result;
    }

    public DeclaredType getDeclaredTypeForGetter(String field) {
        final TypeMirror typeForGetter = getTypeForGetter(field);
        if (typeForGetter.getKind() == TypeKind.DECLARED){
            return (DeclaredType) typeForGetter;
        }
        return null;
    }


    class FieldItem {

        final String field;
        final MethodWrapper setter;
        final MethodWrapper getter;


        FieldItem(String field, MethodWrapper setter, MethodWrapper getter) {
            this.field = field;
            this.setter = setter;
            this.getter = getter;
        }

    }


}
