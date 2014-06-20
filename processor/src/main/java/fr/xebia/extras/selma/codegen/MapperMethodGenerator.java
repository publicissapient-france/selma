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

import com.squareup.javawriter.JavaWriter;
import fr.xebia.extras.selma.Fields;
import fr.xebia.extras.selma.IgnoreFields;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

import static fr.xebia.extras.selma.codegen.MappingSourceNode.*;

/**
 *
 */
public class MapperMethodGenerator {


    private final JavaWriter writer;
    private final MethodWrapper mapperMethod;
    private final MapperGeneratorContext context;
    private final MappingRegistry mappingRegistry;
    private final SourceConfiguration configuration;
    private final Set<String> ignoredFields;

    public MapperMethodGenerator(JavaWriter writer, ExecutableElement mapperMethod, MapperGeneratorContext context, MappingRegistry mappingRegistry, SourceConfiguration configuration) {
        this.writer = writer;
        this.mapperMethod = new MethodWrapper(mapperMethod, context);
        this.context = context;
        this.configuration = configuration;

        this.mappingRegistry = new MappingRegistry(mappingRegistry);
        this.ignoredFields = new TreeSet<String>();
        if (this.mapperMethod.hasIgnoreFields()) { // Add Specific method Ignore Fields
            this.ignoredFields.addAll( AnnotationWrapper.buildFor(context, mapperMethod, IgnoreFields.class).getAsStrings("value"));
        }
        this.ignoredFields.addAll(configuration.getIgnoredFields());

        if (this.mapperMethod.hasFields()){ // Add Specific method Field mapping
            for (AnnotationWrapper field : AnnotationWrapper.buildFor(context, mapperMethod, Fields.class).getAsAnnotationWrapper("value")) {
                this.mappingRegistry.pushFieldMap(mapperMethod, field);
            }
        }
    }

    public static MapperMethodGenerator create(JavaWriter writer, ExecutableElement mapperMethod, MapperGeneratorContext context, MappingRegistry mappingRegistry, SourceConfiguration configuration) {
        return new MapperMethodGenerator(writer, mapperMethod, context, mappingRegistry, configuration);
    }

    public void build() throws IOException {


        buildMappingMethod(writer, mapperMethod.inOutType(), mapperMethod.getSimpleName(), true);

        // Do we need to build other methods for this mapping ?
        if (context.hasMappingMethods()) {
            buildMappingMethods(writer);
        }

    }

    private void buildMappingMethods(JavaWriter writer) throws IOException {

        MapperGeneratorContext.MappingMethod method;
        while ((method = context.popMappingMethod()) != null) {
            buildMappingMethod(writer, method.inOutType(), method.name(), false);
        }

    }

    private void buildMappingMethod(JavaWriter writer, InOutType inOutType, String name, boolean override) throws IOException {

        boolean isPrimitiveOrImmutable = false;
        final MappingSourceNode methodRoot;
        if(configuration.isFinalMappers()){
            methodRoot = mapMethod(inOutType.in().toString(), inOutType.out().toString(), name, override);
        } else {
            methodRoot = mapMethodNotFinal(inOutType.in().toString(), inOutType.out().toString(), name, override);
        }

        MappingSourceNode ptr = blank(), blankRoot = ptr;

        MappingSourceNode methodNode = methodRoot.body(declareOut(inOutType.out()));

        MappingBuilder mappingBuilder = findBuilderFor(inOutType);

        if (mappingBuilder != null) {

            ptr.body(mappingBuilder.build(context, new SourceNodeVars().withInOutType(inOutType).withAssign(true)));
            generateStack(context);
            // set isPrimitiveOrImmutable so we can skip the null check
            isPrimitiveOrImmutable = !inOutType.differs() && mappingBuilder.isNullSafe();
        } else if (inOutType.areDeclared()) {
            ptr = ptr.body(instantiateOut(inOutType.out().toString(), context.newParams()));
            context.depth++;
            ptr.child(generate(inOutType));
            context.depth--;
        } else {
            handleNotSupported(inOutType, ptr);
        }

        isPrimitiveOrImmutable = isPrimitiveOrImmutable || inOutType.inIsPrimitive();
        if (!isPrimitiveOrImmutable) {
            methodNode = methodNode.child(controlNull("in"));
            methodNode.body(blankRoot.body);
        } else {
            methodNode.child(blankRoot.body);
        }

        // Call the interceptor if it exist
        MappingBuilder interceptor = mappingRegistry.mappingInterceptor(inOutType);
        if (interceptor != null){
            methodNode.child(interceptor.build(context, new SourceNodeVars()));
        }

        // Give it a try
        methodRoot.write(writer);
    }

    private void handleNotSupported(InOutType inOutType, MappingSourceNode ptr) {
        final String message = String.format("Failed to generate mapping method for type %s to %s not supported on %s.%s !\n" +
                "--> Add a custom mapper or @IgnoreFields to fix this", inOutType.in(), inOutType.out(), mapperMethod.element().getEnclosingElement(), mapperMethod.element().toString());
        ptr.body(notSupported(message));
        if (configuration.isIgnoreNotSupported()) {
            context.warn(message, mapperMethod.element());
        } else {
            context.error(mapperMethod.element(), message);
        }
    }

    private MappingSourceNode generateStack(MapperGeneratorContext context) throws IOException {

        MappingSourceNode root = blank();
        MapperGeneratorContext.StackElem stackElem;
        while ((stackElem = context.popStack()) != null) {
            InOutType inOutType = stackElem.sourceNodeVars().inOutType;
            context.depth++;
            MappingBuilder mappingBuilder = findBuilderFor(inOutType);
            if (stackElem.child) {

                lastChild(stackElem.lastNode).child(mappingBuilder.build(context, stackElem.sourceNodeVars()));
            } else {
                stackElem.lastNode.body(mappingBuilder.build(context, stackElem.sourceNodeVars()));
            }
            context.depth--;
        }

        return root;
    }

    MappingBuilder findBuilderFor(InOutType inOutType) {
        return mappingRegistry.findMappingFor(inOutType);
    }

    private MappingSourceNode generate(InOutType inOutType) throws IOException {

        MappingSourceNode root = blank();
        MappingSourceNode ptr = root;


        TypeElement outTypeElement = (TypeElement) context.type.asElement(inOutType.out());


        BeanWrapper outBean = new BeanWrapper(context, outTypeElement);
        BeanWrapper inBean = new BeanWrapper(context, (TypeElement) context.type.asElement(inOutType.in()));

        Set<String> outFields = outBean.getSetterFields();
        for (String field : inBean.getFields()) {

            String outField = mappingRegistry.getFieldFor(field);
            if (outField == null){
                outField = field;
            }

            boolean isMissingInDestination = !outBean.hasFieldAndSetter(outField);

            if (isIgnoredField(field, inOutType)) {
//                context.warn(inBean.getFieldElement(field), "Field %s from in bean will be ignored as @IgnoredFields require", field);
                continue;
            }

            if (isMissingInDestination && canBeIgnored(field, inOutType)) {
                continue;
            } else {


                if (isMissingInDestination ) {
                    context.error(inBean.getFieldElement(field), String.format("getter for field %s from in bean %s is missing in destination bean %s using field %s !\n" +
                            " --> Add @IgnoreField to mapper interface / method or add missing getter", field, inOutType.in(), inOutType.out(), outField));
                    continue;
                }

                try {
                    MappingBuilder mappingBuilder = findBuilderFor(new InOutType(inBean.getTypeFor(field), outBean.getTypeFor(outField)));
                    if (mappingBuilder != null) {
                        ptr = ptr.child(mappingBuilder.build(context, new SourceNodeVars(field, outField , inBean, outBean)
                                .withInOutType(new InOutType(inBean.getTypeFor(field), outBean.getTypeFor(outField))).withAssign(false)));

                        generateStack(context);
                    } else {
                        handleNotSupported(inOutType, ptr);
                    }
                } catch (Exception e) {
                    System.out.printf("Error while searching builder for field %s on %s mapper", field, inOutType.toString());
                    e.printStackTrace();
                }


                ptr = lastChild(ptr);
            }
            outFields.remove(outField);
        }

        if (!configuration.isIgnoreMissingProperties()){
            for (String outField : outFields) {

                if (!isIgnoredField(outField, inOutType)) {
                    context.error(outBean.getSetterElement(outField), String.format("setter for field %s from out bean %s has no getter in in bean %s !\n" +
                            "-->  Add @IgnoreField to mapper interface / method or add missing setter\"", outField, inOutType.out(), inOutType.in()));
                } else {
                    context.warn(outBean.getSetterElement(outField), "Field %s not populated in destination bean will be ignored as @IgnoredFields require", outField);
                }
            }
        }
        return root.child;
    }

    private boolean canBeIgnored(String field, InOutType inOutType) {

        return configuration.isIgnoreMissingProperties() || isIgnoredField(field, inOutType);
    }

    private MappingSourceNode lastChild(MappingSourceNode ptr) {
        while (ptr.child != null) {
            ptr = ptr.child;
        }
        return ptr;
    }


    public boolean isIgnoredField(String field, InOutType inOutType) {
        boolean res = false;
        DeclaredType inType = inOutType.inAsDeclaredType();
        String fqcn = inType.toString();
        String simpleName = inType.asElement().getSimpleName().toString();

        for (String ignoredField : ignoredFields) {

            if(ignoredField.equalsIgnoreCase(field)){
                res = true;
                break;
            } else if (ignoredField.contains("::")) {
                if ((fqcn + "::" + field).equalsIgnoreCase(ignoredField)) {
                    res = true;
                    break;
                } else if ((simpleName + "::" + field).equalsIgnoreCase(ignoredField)) {
                    res = true;
                    break;
                }

            }
        }
        return res;
    }


}
