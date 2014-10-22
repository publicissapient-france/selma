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
import fr.xebia.extras.selma.Mapper;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import java.io.IOException;
import java.util.*;

import static javax.lang.model.element.Modifier.*;

/**
 * Created by slemesle on 16/09/2014.
 */
public class CustomMapperWrapper {

    public static final String CUSTOM_MAPPER_FIELD_TPL = "customMapper%s";

    private final AnnotationWrapper annotationWrapper;
    private final MapperGeneratorContext context;
    private final HashMap<InOutType, String> unusedCustomMappers;
    private final Map<InOutType, MappingBuilder> registryMap;
    private final Map<InOutType, MappingBuilder> interceptorMap;


    private final List<TypeElement> customMaperFields;
    private final HashSet<InOutType> unusedInterceptor;
    private final TypeElement element;

    public CustomMapperWrapper(TypeElement element, MapperGeneratorContext context) {
        this.element = element;

        this.annotationWrapper = AnnotationWrapper.buildFor(context, element, Mapper.class);
        this.context = context;
        this.customMaperFields = new LinkedList<TypeElement>();
        this.unusedCustomMappers = new HashMap();
        this.unusedInterceptor = new HashSet<InOutType>();
        this.registryMap = new HashMap<InOutType, MappingBuilder>();
        this.interceptorMap = new HashMap<InOutType, MappingBuilder>();

        collectCustomMappers();
    }


    void emitCustomMappersFields(JavaWriter writer, boolean assign) throws IOException {
        for (TypeElement customMaperField : customMaperFields) {
            final String field = String.format(CUSTOM_MAPPER_FIELD_TPL, customMaperField.getSimpleName().toString());
            if (assign) {
                // assign the customMapper field to a newly created instance passing to it the declared source params
                writer.emitStatement("this.%s = new %s(%s)", field, customMaperField.getQualifiedName().toString(), context.newParams());
            } else {
                writer.emitEmptyLine();
                writer.emitJavadoc("This field is used for custom Mapping");
                writer.emitField(customMaperField.asType().toString(), String.format(CUSTOM_MAPPER_FIELD_TPL, customMaperField.getSimpleName().toString()), EnumSet.of(PRIVATE));

                writer.emitEmptyLine();
                writer.emitJavadoc("Custom Mapper setter for " + field);
                writer.beginMethod("void", "setCustomMapper" + customMaperField.getSimpleName(), EnumSet.of(PUBLIC, FINAL), customMaperField.asType().toString(), "mapper");
                writer.emitStatement("this.%s = mapper", field);
                writer.endMethod();
                writer.emitEmptyLine();
            }
        }
    }


    /**
     * Adds a custom mapping method to the registry for later use at codegen.
     *
     * @param method
     */
    private void pushCustomMapper(final TypeElement element, final MethodWrapper method) {

        String customMapperFieldName = buildMapperFieldName(element);

        InOutType inOutType = method.inOutType();
        String methodCall = String.format("%s.%s", customMapperFieldName, method.getSimpleName());
        MappingBuilder res = MappingBuilder.newCustomMapper(inOutType, methodCall);

        registryMap.put(inOutType, res);
        registryMap.put(new InOutType(inOutType.in(), inOutType.out(), true), res);
        unusedCustomMappers.put(inOutType, String.format("%s.%s", element.getQualifiedName(), method.getSimpleName()));
    }


    private void pushMappingInterceptor(TypeElement element, MethodWrapper method) {

        String customMapperFieldName = buildMapperFieldName(element);
        InOutType inOutType = method.inOutArgs();
        MappingBuilder res = MappingBuilder.newMappingInterceptor(inOutType, String.format("%s.%s", customMapperFieldName, method.getSimpleName()));

        // Push IOType for both mutable and immutable mapping
        interceptorMap.put(inOutType, res);
        interceptorMap.put(new InOutType(inOutType.in(), inOutType.out(), false), res);
        unusedInterceptor.add(inOutType);
    }

    private void collectCustomMappers() {

        if (annotationWrapper.getAsStrings("withCustom").size() > 0) {
            int mappingMethodCount = 0;

            for (String customMapper : annotationWrapper.getAsStrings("withCustom")) {

                final TypeElement element = context.elements.getTypeElement(customMapper.replace(".class", ""));

                final List<ExecutableElement> methods = ElementFilter.methodsIn(element.getEnclosedElements());

                for (ExecutableElement method : methods) {
                    MethodWrapper methodWrapper = new MethodWrapper(method, context);
                    if (isValidCustomMapping(methodWrapper)) {

                        if (methodWrapper.isCustomMapper()) {
                            pushCustomMapper(element, methodWrapper);
                        } else {
                            pushMappingInterceptor(element, methodWrapper);
                        }
                        mappingMethodCount++;
                    }
                }

                if (mappingMethodCount == 0) {
                    context.error(element, "No valid mapping method found in custom selma class %s\\n A custom mapping method is public and returns a type not void, it takes one parameter or more if you specified datasource.", customMapper);
                } else {

                    List<ExecutableElement> constructors = ElementFilter.constructorsIn(element.getEnclosedElements());
                    int defaultConstructorCount = 0;
                    for (ExecutableElement constructor : constructors) {
                        if (constructor.getParameters().size() == 0 && constructor.getModifiers().contains(PUBLIC)) {
                            defaultConstructorCount++;
                        }
                    }
                    if (defaultConstructorCount <= 0) {
                        context.error(element, "No default public constructor found in custom mapping class %s\\n Please add one", customMapper);
                    }

                    // Here we collect the name of the field to create in the Mapper generated class
                    customMaperFields.add(element);
                }
            }
        }

    }

    private String buildMapperFieldName(TypeElement element) {
        return String.format(CUSTOM_MAPPER_FIELD_TPL, element.getSimpleName());
    }

    private boolean isValidCustomMapping(MethodWrapper methodWrapper) {
        boolean res = true;

        if (MapperProcessor.exclusions.contains(methodWrapper.getSimpleName())) {
            // We skip excluded methods
            return false;
        }

        if (!methodWrapper.element().getModifiers().contains(javax.lang.model.element.Modifier.PUBLIC)) {
            context.warn(methodWrapper.element(), "Custom mapping method should be *public* (Fix modifiers of the method) on %s", methodWrapper.getSimpleName());
            res = false;
        }

        if (methodWrapper.element().getModifiers().contains(Modifier.STATIC)) {
            context.warn(methodWrapper.element(), "Custom mapping method can not be *static* (Fix modifiers of the method) on %s", methodWrapper.getSimpleName());
            res = false;
        }

        if (methodWrapper.element().getModifiers().contains(Modifier.ABSTRACT)) {
            context.warn(methodWrapper.element(), "Custom mapping method can not be *abstract* (Fix modifiers of the method) on %s", methodWrapper.getSimpleName());
            res = false;
        }

        if (!methodWrapper.isCustomMapper() && !methodWrapper.isMappingInterceptor()) {
            context.warn(methodWrapper.element(), "Custom mapping method should have a return type and one parameter and interceptor method should be void and have two parameters (Fix method signature) on %s", methodWrapper.getSimpleName());
            res = false;
        }

        return res;
    }


    public MappingBuilder getMapper(InOutType inOutType) {
        MappingBuilder res = registryMap.get(inOutType);
        if (res != null) {
            unusedCustomMappers.remove(inOutType);
        }
        return res;
    }

    public MappingBuilder getMappingInterceptor(InOutType inOutType) {
        MappingBuilder res = interceptorMap.get(inOutType);
        if (res != null) {
            unusedInterceptor.remove(inOutType);
        }
        return res;
    }

    public void reportUnused() {
        for (String field : unusedCustomMappers.values()) {
            context.warn(element, "Custom mapping method \"%s\" is never used", field);
        }

    }
}
