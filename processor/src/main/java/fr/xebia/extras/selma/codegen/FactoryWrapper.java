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

import com.squareup.javawriter.JavaWriter;
import fr.xebia.extras.selma.IoC;

import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.util.ElementFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

import static fr.xebia.extras.selma.codegen.MappingSourceNode.callGenericFactoryOut;
import static fr.xebia.extras.selma.codegen.MappingSourceNode.callStaticFactoryOut;
import static javax.lang.model.element.Modifier.*;

/**
 *
 */
public class FactoryWrapper {
    public static final String FACTORY_FIELD_TPL = "factory%s";
    public static final String WITH_FACTORIES = "withFactories";

    private final Element annotatedElement;
    private final AnnotationWrapper annotationWrapper;
    private final MapperGeneratorContext context;
    private final HashMap<Factory, String> unusedFactories;
    private final IoC ioC;
    private final List<TypeElement> factoryFields;
    private final ArrayList<Factory> staticFactories;
    private final ArrayList<Factory> genericFactories;

    public FactoryWrapper(AnnotationWrapper mapperAnnotation, MapperGeneratorContext context) {
        this.annotatedElement = mapperAnnotation.getAnnotatedElement();
        this.annotationWrapper = mapperAnnotation;
        this.context = context;
        this.unusedFactories = new HashMap();
        factoryFields = new ArrayList<TypeElement>();

        staticFactories = new ArrayList<Factory>();
        genericFactories = new ArrayList<Factory>();
        ioC = IoC.valueOf(annotationWrapper.getAsString(MapperWrapper.WITH_IOC));

        collectFactories();

    }

    private void collectFactories() {
        List<String> factoryClasses = annotationWrapper.getAsStrings(WITH_FACTORIES);
        if (factoryClasses.size() > 0) {
            int factoryMethodCount = 0;

            for (String factoryClass : factoryClasses) {

                final TypeElement element = context.elements.getTypeElement(factoryClass.replaceAll("\\.class$", ""));

                factoryMethodCount += collectFactoryMethods(element, false);


                if (factoryMethodCount == 0) {
                    context.error(element, "No valid factory method found in Factory selma class %s\\n " +
                            "A factory method method is public and returns a type not void, it takes a Class object or " +
                            "no parameter at all.", factoryClass);
                } else {

                    TypeConstructorWrapper constructorWrapper = new TypeConstructorWrapper(context, element);
                    if (!constructorWrapper.hasDefaultConstructor && element.getKind() != ElementKind.INTERFACE) {
                        context.error(element, "No default public constructor found in custom mapping class %s\\n" +
                                " Please add one", factoryClass);
                    }

                    // Here we collect the name of the field to create in the Mapper generated class
                    factoryFields.add(element);
                }

            }
        }

    }

    private int collectFactoryMethods(TypeElement element, boolean ignoreAbstract) {
        int factoryMethodCount = 0;
        final List<ExecutableElement> methods = ElementFilter.methodsIn(element.getEnclosedElements());
        for (ExecutableElement method : methods) {
            MethodWrapper methodWrapper = new MethodWrapper(method, (DeclaredType) element.asType(), context);
            // We should ignore abstract methods if parsing an abstract mapper class
            if (ignoreAbstract && methodWrapper.isAbstract()) {
                continue;
            }
            if (isValidFactoryMethod(methodWrapper)) {
                pushFactoryMethod(element, methodWrapper, ignoreAbstract);
                factoryMethodCount++;
            }
        }

        return factoryMethodCount;
    }

    private void pushFactoryMethod(TypeElement element, MethodWrapper method, boolean ignoreAbstract) {
        MappingBuilder res = null;
        String factoryFieldName = ignoreAbstract ? "this" : buildFactoryFieldName(element);

        TypeMirror returnType = method.returnType();
        String methodCall = String.format("%s.%s", factoryFieldName, method.getSimpleName());
        Factory factory = new Factory(this.context, method, methodCall);
        if (method.hasTypeParameter()) {
            genericFactories.add(factory);
        } else {
            staticFactories.add(factory);
        }

        unusedFactories.put(factory,
                String.format("%s.%s", element.getQualifiedName(), method.getSimpleName()));
    }

    private boolean isValidFactoryMethod(MethodWrapper methodWrapper) {
        boolean res = true;

        if (MapperProcessor.exclusions.contains(methodWrapper.getSimpleName())) {
            // We skip excluded methods
            return false;
        }

        if (!methodWrapper.element().getModifiers().contains(javax.lang.model.element.Modifier.PUBLIC)) {
            context.warn(methodWrapper.element(), "Factory method should be *public* " +
                    "(Fix modifiers of the method) on %s", methodWrapper.getSimpleName());
            res = false;
        }

        if (methodWrapper.element().getModifiers().contains(Modifier.STATIC)) {
            context.warn(methodWrapper.element(), "Factory method can not be *static* " +
                    "(Fix modifiers of the method) on %s", methodWrapper.getSimpleName());
            res = false;
        }

        if (!methodWrapper.isFactory()) {
            context.warn(methodWrapper.element(), "Factory method should have a return type and a Class<T> targetType or" +
                    " no parameters" +
                    "(Fix method signature) on %s", methodWrapper.getSimpleName());
            res = false;
        }

        return res;
    }

    private String buildFactoryFieldName(TypeElement element) {
        return String.format(FACTORY_FIELD_TPL, element.getSimpleName());
    }

    /**
     * Generates the code to declare custom factory fields, setter and call default constructor in mapper constructor
     *
     * @param writer
     * @param assign
     * @throws IOException
     */
    public void emitFactoryFields(JavaWriter writer, boolean assign) throws IOException {
        for (TypeElement factoryField : factoryFields) {
            final String field = String.format(FACTORY_FIELD_TPL, factoryField.getSimpleName().toString());
            if (assign) {
                if (factoryField.getKind() != ElementKind.INTERFACE) {
                    TypeConstructorWrapper constructorWrapper = new TypeConstructorWrapper(context, factoryField);
                    // assign the customMapper field to a newly created instance passing to it the declared source params
                    writer.emitStatement("this.%s = new %s(%s)", field, factoryField.getQualifiedName().toString(),
                            constructorWrapper.hasMatchingSourcesConstructor ? context.newParams() : "");
                }
            } else {
                writer.emitEmptyLine();
                writer.emitJavadoc("This field is used for custom Mapping");
                if (ioC == IoC.SPRING) {
                    writer.emitAnnotation("org.springframework.beans.factory.annotation.Autowired");
                }
                writer.emitField(factoryField.asType().toString(), String.format(FACTORY_FIELD_TPL,
                        factoryField.getSimpleName().toString()), EnumSet.of(PRIVATE));

                writer.emitEmptyLine();
                writer.emitJavadoc("Factory setter for " + field);
                writer.beginMethod("void", "setFactory" + factoryField.getSimpleName(),
                        EnumSet.of(PUBLIC, FINAL), factoryField.asType().toString(), "_factory");
                writer.emitStatement("this.%s = _factory", field);
                writer.endMethod();
                writer.emitEmptyLine();
            }
        }
    }

    public void reportUnused() {
        for (String field : unusedFactories.values()) {
            context.warn(annotatedElement, "Factory method \"%s\" is never used", field);
        }
    }


    /**
     * Called to search for a matching factory and return the corresponding MappingSourceNode
     *
     * @param inOutType      the type association we are currently mapping
     * @param outBeanWrapper The Bean wrapper of the bean we want to build
     * @return Null if no factory matches was found or the corresponding MappingSourceNode
     */
    public MappingSourceNode generateNewInstanceSourceNodes(InOutType inOutType, BeanWrapper outBeanWrapper) {

        TypeMirror out = inOutType.out();
        for (Factory factory : staticFactories) {
            if (factory.provide(out)) {
                unusedFactories.remove(factory);
                return factory.buildNewInstanceSourceNode(inOutType);
            }
        }
        Factory found = null;
        for (Factory factory : genericFactories) {
            if (factory.provide(out) && factory.isLowerClass(found)) {
                found = factory;
            }
        }
        if (found != null) {
            unusedFactories.remove(found);
            return found.buildNewInstanceSourceNode(inOutType);
        }
        return null;
    }

    public boolean hasFactory(TypeMirror typeMirror) {
        for (Factory factory : staticFactories) {
            if (factory.provide(typeMirror)) {
                return true;
            }
        }
        for (Factory factory : genericFactories) {
            if (factory.provide(typeMirror)) {
                return true;
            }
        }
        return false;
    }

    private class Factory {

        private MapperGeneratorContext context;
        private final MethodWrapper method;
        private final String methodCall;

        public Factory(MapperGeneratorContext context, MethodWrapper method, String methodCall) {
            this.context = context;
            this.method = method;
            this.methodCall = methodCall;
        }

        @Override public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Factory factory = (Factory) o;

            if (method != null ? !method.equals(factory.method) : factory.method != null) return false;
            return methodCall != null ? methodCall.equals(factory.methodCall) : factory.methodCall == null;

        }

        @Override public int hashCode() {
            int result = method != null ? method.hashCode() : 0;
            result = 31 * result + (methodCall != null ? methodCall.hashCode() : 0);
            return result;
        }

        public boolean provide(TypeMirror out) {
            if (!method.hasTypeParameter()) {
                if (context.type.isSameType(out, method.returnType())) {
                    return true;
                }
            } else if (context.type.isAssignable(out, getUpperBound())) {
                return true;
            }
            return false;
        }

        public MappingSourceNode buildNewInstanceSourceNode(InOutType inOutType) {
            if (!method.hasTypeParameter()) {

                return callStaticFactoryOut(inOutType, methodCall);
            } else {
                return callGenericFactoryOut(inOutType, methodCall);
            }
        }

        public boolean isLowerClass(Factory factory) {
            boolean res;
            if (factory == null) {
                res = true;
            } else {
                TypeMirror upperBound = getUpperBound();
                TypeMirror upperBoundCmp = factory.getUpperBound();
                res = context.type.isAssignable(upperBound, upperBoundCmp);
            }
            return res;
        }

        private TypeMirror getUpperBound() {
            return ((TypeVariable) method.returnType()).getUpperBound();
        }
    }
}

