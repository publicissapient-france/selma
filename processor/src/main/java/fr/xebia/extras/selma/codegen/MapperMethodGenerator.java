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

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static fr.xebia.extras.selma.codegen.MappingSourceNode.*;

/**
 *
 */
public class MapperMethodGenerator {


    private static final Pattern STANDARD_JAVA_PACKAGE = Pattern.compile("^(java|javax)\\.+$");
    private final JavaWriter writer;
    private final MethodWrapper mapperMethod;
    private final MapperGeneratorContext context;
    private final SourceConfiguration configuration;
    private final MapsWrapper maps;
    private final BeanWrapperFactory beanWrapperFactory;
	private final MapperWrapper mapperWrapper;

    public MapperMethodGenerator(JavaWriter writer, MethodWrapper method, MapperWrapper mapperWrapper) {
        this.writer = writer;
        this.mapperMethod = method;
        this.context = mapperWrapper.context();
        this.configuration = mapperWrapper.configuration();
        this.beanWrapperFactory = new BeanWrapperFactory();
        this.maps = new MapsWrapper(method, mapperWrapper);
		this.mapperWrapper = mapperWrapper;
    }

    public void build() throws IOException {

        buildMappingMethod(writer, mapperMethod.inOutType(), mapperMethod.getSimpleName(), true);

        // Do we need to build other methods for this mapping ?
        if (context.hasMappingMethods()) {
            buildMappingMethods(writer);
        }

        // Report unused custom fields mapping
        maps.reportUnused();
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
        if (configuration.isFinalMappers()) {
            methodRoot = mapMethod(inOutType, name, override);
        } else {
            methodRoot = mapMethodNotFinal(inOutType, name, override);
        }

        MappingSourceNode ptr = blank(), blankRoot = ptr;

        MappingSourceNode methodNode = (inOutType.isOutPutAsParam() ? methodRoot.body(blank()) : methodRoot.body(declareOut(inOutType.out())));

        MappingBuilder mappingBuilder = findBuilderFor(inOutType);
		if (mapperWrapper.isUseInstanceCache()) {
			methodNode = methodNode.child(controlInCache("in", inOutType));
		}

        if (mappingBuilder != null) {

            ptr.body(mappingBuilder.build(context, new SourceNodeVars().withInOutType(inOutType).withAssign(true)));
            generateStack(context);
            // set isPrimitiveOrImmutable so we can skip the null check
            isPrimitiveOrImmutable = !inOutType.differs() && mappingBuilder.isNullSafe();

        } else if (inOutType.areDeclared() && isSupported(inOutType.out())) {

            final BeanWrapper outBeanWrapper = getBeanWrapperOrNew(context, inOutType.outAsTypeElement());

			ptr = ptr.body(instantiateOut(mapperWrapper.isUseInstanceCache(), inOutType,
                    (outBeanWrapper.hasMatchingSourcesConstructor() ? context.newParams() : "")));
            context.depth++;
            ptr.child(generate(inOutType));
            context.depth--;
        } else {
            handleNotSupported(inOutType, ptr);
        }

        isPrimitiveOrImmutable = isPrimitiveOrImmutable || inOutType.inIsPrimitive();
        if (!isPrimitiveOrImmutable) {
            methodNode = methodNode.child(controlNotNull("in", inOutType.isOutPutAsParam()));
            methodNode.body(blankRoot.body);
        } else {
            methodNode.child(blankRoot.body);
        }

        // Call the interceptor if it exist
        MappingBuilder interceptor = maps.mappingInterceptor(inOutType);
        if (interceptor != null) {
            methodNode.child(interceptor.build(context, new SourceNodeVars()));
        }

        // Give it a try
        methodRoot.write(writer);
    }

    private BeanWrapper getBeanWrapperOrNew(MapperGeneratorContext context, TypeElement typeElement) {
        return beanWrapperFactory.getBeanWrapperOrNew(context, typeElement);
    }

    /**
     * Method called when encountered a bean not known in registry so considered as
     * custom bean to map.
     *
     * @param out TypeMirror that will be checked
     * @return boolean telling whether this type can be supported by mapping code generation
     */
    private boolean isSupported(TypeMirror out) {
        boolean res = false;
        Matcher matcher = STANDARD_JAVA_PACKAGE.matcher(out.toString());
        if (!matcher.matches()) {
            TypeElement outTypeElement = (TypeElement) context.type.asElement(out);
            BeanWrapper outBean = getBeanWrapperOrNew(context, outTypeElement);
            res = outBean.hasCallableConstructor();
        }

        return res;
    }

    private void handleNotSupported(InOutType inOutType, MappingSourceNode ptr) {
        final String message = String.format("Failed to generate mapping method for type %s to %s not supported on %s.%s !\n" +
                "--> Add a custom mapper or 'withIgnoreFields' on @Mapper or @Maps to fix this ! If you think this a Bug in Selma please report issue here [https://github.com/xebia-france/selma/issues].", inOutType.in(), inOutType.out(), mapperMethod.element().getEnclosingElement(), mapperMethod.element().toString());
        ptr.body(notSupported(message));
        if (configuration.isIgnoreNotSupported()) {
            context.warn(message, mapperMethod.element());
        } else {
            context.error(mapperMethod.element(), message);
        }
    }

    /**
     * @param context
     * @return
     * @throws IOException
     */
    private MappingSourceNode generateStack(MapperGeneratorContext context) throws IOException {

        MappingSourceNode root = blank();
        MapperGeneratorContext.StackElem stackElem;
        while ((stackElem = context.popStack()) != null) {
            InOutType inOutType = stackElem.sourceNodeVars().inOutType;
            context.depth++;
            MappingBuilder mappingBuilder = findBuilderFor(inOutType);
            if (stackElem.child) {

                stackElem.lastNode.lastChild().child(mappingBuilder.build(context, stackElem.sourceNodeVars()));
            } else {
                stackElem.lastNode.body(mappingBuilder.build(context, stackElem.sourceNodeVars()));
            }
            context.depth--;
        }

        return root;
    }

    MappingBuilder findBuilderFor(InOutType inOutType) {
        return maps.findMappingFor(inOutType);
    }

    private MappingSourceNode generate(InOutType inOutType) throws IOException {

        MappingSourceNode root = blank();
        MappingSourceNode ptr = root;


        TypeElement outTypeElement = (TypeElement) context.type.asElement(inOutType.out());


        BeanWrapper outBean = getBeanWrapperOrNew(context, outTypeElement);
        BeanWrapper inBean = getBeanWrapperOrNew(context, (TypeElement) context.type.asElement(inOutType.in()));

        Set<String> outFields = outBean.getSetterFields();
        List<Field> customFields = new ArrayList<Field>();
        for (String field : inBean.getGetterFields()) {

            String outFieldName = field;
            List<Field> customFieldsFor = maps.getFieldsFor(field, inOutType.inAsDeclaredType(), inOutType.outAsDeclaredType());
            if (!customFieldsFor.isEmpty()) {
                boolean hasEmbedded = false;
                for (Field customField : customFieldsFor) {
                    if (customField.hasEmbedded()) { // We are trying to map an embedded field let's map it with if
                        outFields.remove(customField.to);
                        hasEmbedded = true;
                    }
                }
                if (hasEmbedded) {
                    customFields.addAll(customFieldsFor);
                    continue;
                } else {
                    // We can only have one field here, if not embedded because fields names are matched with equals
                    outFieldName = customFieldsFor.get(0).to;
                }
            }

            TypeMirror typeForInField = inBean.getTypeForGetter(field);
            TypeMirror typeForOutField = outBean.getTypeForSetter(outFieldName);

            boolean isMissingInDestination = !outBean.hasFieldAndSetter(outFieldName);
            boolean useGetterForDestination = false;
            if (isMissingInDestination && maps.allowCollectionGetter() && outBean.hasFieldAndGetter(outFieldName)) {
                final DeclaredType declaredTypeForGetter = outBean.getDeclaredTypeForGetter(outFieldName);
                isMissingInDestination = !MappingBuilder.isCollection(declaredTypeForGetter, context);
                typeForOutField = declaredTypeForGetter;
                useGetterForDestination = true;

            }


            if (maps.isIgnoredField(field, inOutType.inAsDeclaredType())) {
                continue; // Skip ignored in field
            }

            if (isMissingInDestination && maps.ignoreMissing().isIgnoreDestination()) {
                continue;  // Skip ignored missing properties in destination bean
            }


            if (isMissingInDestination) {
                if (!customFieldsFor.isEmpty()){
                    context.error(mapperMethod.element(), String.format("Mapping custom field %s from source bean %s, setter for" +
                                    " field %s is missing in destination bean %s !\n" +
                                    " --> Check your custom field %s, you can add simple class name or FQCN to the longest field path " +
                                    " or add missing getter",
                            field, inOutType.in(), outFieldName, inOutType.out(), customFieldsFor.get(0)));
                } else {
                    context.error(mapperMethod.element(), String.format("Mapping field %s from source bean %s, setter for" +
                                    " field %s is missing in destination bean %s !\n" +
                                    " --> Add @Mapper(withIgnoreFields=\"%s.%s\") / @Maps(withIgnoreFields=\"%s.%s\")" +
                                    " to mapper interface / method or add missing getter or specify corresponding @Field " +
                                    "to customize field to field mapping",
                            field, inOutType.in(), outFieldName, inOutType.out(), inOutType.in(), field, inOutType.in(), field));
                }
                outFields.remove(field);
                continue;
            }


            try {
                InOutType inOutTypeForField = new InOutType(typeForInField, typeForOutField, inOutType.isOutPutAsParam());
                MappingBuilder mappingBuilder = findBuilderFor(inOutTypeForField);
                if (mappingBuilder != null) {
                    ptr = ptr.child(mappingBuilder.build(context, new SourceNodeVars(field, outFieldName, inBean, outBean)
                            .withInOutType(inOutTypeForField).withAssign(false).withUseGetterForDestination(useGetterForDestination)));

                    generateStack(context);
                } else {
                    handleNotSupported(inOutTypeForField, ptr);
                }
            } catch (Exception e) {
                System.out.printf("Error while searching builder for field %s on %s mapper", field, inOutType.toString());
                e.printStackTrace();
            }


            ptr = ptr.lastChild();

            outFields.remove(outFieldName);
        }

        // Build custom embedded field to field
        for (Field customField : customFields) {
            ptr = buildEmbeddedMapping(customField, inBean, outBean, ptr, outFields, inOutType.isOutPutAsParam());
            ptr = ptr.lastChild();
        }

        if (!maps.ignoreMissing().isIgnoreSource()) { // Report destination bean fields not mapped
            for (String outField : outFields) {

                if (!maps.isIgnoredField(outField, inOutType.outAsDeclaredType())) {
                    context.error(mapperMethod.element(), "setter for field %s from destination bean %s has no getter in source bean %s !\n" +
                            " --> Add @Mapper(withIgnoreFields=\"%s.%s\") / @Maps(withIgnoreFields=\"%s.%s\") to mapper interface / method or add missing setter or specify corresponding @Field to customize field to field mapping", outField, inOutType.out(), inOutType.in(), inOutType.out(), outField, inOutType.out(), outField);
                }
            }
        }
        return root.child;
    }

    /**
     * Build mapping source node tree for custom field to field with embedded property
     *
     * @param customField
     * @param inBean
     * @param outBean
     * @param outFields
     * @param outPutAsParam
     * @return
     */
    private MappingSourceNode buildEmbeddedMapping(Field customField, BeanWrapper inBean, BeanWrapper outBean, MappingSourceNode root, Set<String> outFields, boolean outPutAsParam) {
        MappingSourceNode ptr = blank();
        MappingSourceNode ptrRoot = ptr;
        String lastVisitedField;

        if (customField.hasEmbeddedSourceAndDestination()) {
            context.error(customField.element, "Bad custom field to field mapping: both source and destination can not be embedded !\n" +
                    "-->  Fix @Field({\"%s\",\"%s\"})", customField.originalFrom, customField.originalTo);
            return root;
        }
        final boolean sourceEmbedded = customField.sourceEmbedded();
        BeanWrapper beanPtr = sourceEmbedded ? inBean : outBean;

        boolean useGetterForDestination = false;
        if (sourceEmbedded && !outBean.hasFieldAndSetter(customField.to)) { // Break compilation if field has no setter in out bean and no collection getter

            if (maps.allowCollectionGetter() && outBean.hasFieldAndGetter(customField.to)) {
                final DeclaredType declaredTypeForGetter = outBean.getDeclaredTypeForGetter(customField.to);
                useGetterForDestination = MappingBuilder.isCollection(declaredTypeForGetter, context);
            }

            if (!useGetterForDestination) {
                context.error(customField.element, "Bad custom field to field mapping: setter for field %s is missing in destination bean %s !\n" +
                        " --> Fix @Field({\"%s\",\"%s\"})", customField.to, outBean.typeElement, customField.originalFrom, customField.originalTo);
                return root;
            }
        }
        if (!sourceEmbedded && !inBean.hasFieldAndGetter(customField.from)) {
            context.error(customField.element, "Bad custom field to field mapping: getter for field %s is missing in source bean %s !\n" +
                    " --> Fix @Field({\"%s\",\"%s\"})", customField.from, inBean.typeElement, customField.originalFrom, customField.originalTo);
            return root;
        }
        String[] fields = sourceEmbedded ? customField.fromFields() : customField.toFields();
        String previousFieldPath = sourceEmbedded ? "in" : "out";
        StringBuilder field = new StringBuilder(previousFieldPath);
        for (int id = 0; id < fields.length - 1; id++) {
            lastVisitedField = fields[id];
            if (id == 0 && !sourceEmbedded) {
                outFields.remove(lastVisitedField);
            }

            if (sourceEmbedded && !beanPtr.hasFieldAndGetter(lastVisitedField)) {
                context.error(customField.element, "Bad custom field to field mapping: field %s.%s from source bean %s has no getter !\n" +
                        "-->  Fix @Field({\"%s\",\"%s\"})", field, lastVisitedField, inBean.typeElement, customField.originalFrom, customField.originalTo);
                return root;
            }
            if (!sourceEmbedded && !beanPtr.hasFieldAndGetter(lastVisitedField)) {
                context.error(customField.element, "Bad custom field to field mapping: field %s.%s from destination bean %s has no getter !\n" +
                        "-->  Fix @Field({\"%s\",\"%s\"})", field, lastVisitedField, outBean.typeElement, customField.originalFrom, customField.originalTo);
                return root;
            }
            field.append('.').append(beanPtr.getGetterFor(lastVisitedField)).append("()");

            if (sourceEmbedded) {
                ptr = ptr.body(controlNotNull(field.toString(), false));
            } else {
                ptr = ptr.child(controlNull(field.toString()));
                ptr.body(set(previousFieldPath + '.' + beanPtr.getSetterFor(lastVisitedField), "new " + beanPtr.getTypeForGetter(lastVisitedField) + "(" + context.newParams() + ")"));
            }
            beanPtr = getBeanWrapperOrNew(context, (TypeElement) context.type.asElement(beanPtr.getTypeForGetter(lastVisitedField)));
            previousFieldPath = field.toString();
        }
        lastVisitedField = fields[fields.length - 1];

        if (sourceEmbedded && !beanPtr.hasFieldAndGetter(lastVisitedField)) {
            context.error(customField.element, "Bad custom field to field mapping: field %s.%s from source bean %s has no getter !\n" +
                    "-->  Fix @Field({\"%s\",\"%s\"})", field, lastVisitedField, inBean.typeElement, customField.originalFrom, customField.originalTo);
            return root;
        }

        if (!sourceEmbedded && !beanPtr.hasFieldAndSetter(lastVisitedField)) {
            if (maps.allowCollectionGetter() && beanPtr.hasFieldAndSetter(lastVisitedField)) {
                final DeclaredType declaredTypeForGetter = beanPtr.getDeclaredTypeForGetter(lastVisitedField);
                useGetterForDestination = MappingBuilder.isCollection(declaredTypeForGetter, context);
            }
            if (!useGetterForDestination) {
                context.error(customField.element, "Bad custom field to field mapping: field %s.%s from destination bean %s has no setter !\n" +
                        "-->  Fix @Field({\"%s\",\"%s\"})", field, lastVisitedField, outBean.typeElement, customField.originalFrom, customField.originalTo);
                return root;
            }
        }
        if (sourceEmbedded) {
            field.append('.').append(beanPtr.getGetterFor(lastVisitedField)).append("()");
        } else {
            field.append('.').append(beanPtr.getSetterFor(lastVisitedField));
        }
        InOutType inOutType;
        if (sourceEmbedded) {
            inOutType = new InOutType(beanPtr.getTypeForGetter(lastVisitedField), outBean.getTypeForSetter(customField.to), outPutAsParam);
        } else {
            inOutType = new InOutType(inBean.getTypeForGetter(customField.from), beanPtr.getTypeForSetter(lastVisitedField), outPutAsParam);
        }
        try {
            MappingBuilder mappingBuilder = findBuilderFor(inOutType);
            if (mappingBuilder != null) {

                ptr = sourceEmbedded ?
                        ptr.body(mappingBuilder.build(context, new SourceNodeVars(field.toString(), customField.to, outBean).withInOutType(inOutType).withAssign(false).withUseGetterForDestination(useGetterForDestination))) :
                        ptr.child(mappingBuilder.build(context, new SourceNodeVars("in." + inBean.getGetterFor(customField.from) + "()", field.toString()).withInOutType(inOutType).withAssign(false).withUseGetterForDestination(useGetterForDestination)));

                generateStack(context);
            } else {
                handleNotSupported(inOutType, ptr);
            }
        } catch (Exception e) {
            System.out.printf("Error while searching builder for field %s on %s mapper", field, inOutType.toString());
            e.printStackTrace();
        }

        if (!sourceEmbedded && inBean.getTypeForGetter(customField.from).getKind() == TypeKind.DECLARED) { // Ensure we do not map if source is null
            MappingSourceNode ifNode = controlNotNull("in." + inBean.getGetterFor(customField.from) + "()", false);
            ifNode.body(ptrRoot.child);
            ptrRoot.child = ifNode;
        }


        return root.child(sourceEmbedded ? ptrRoot.body : ptrRoot.child);
    }


    public MapsWrapper maps() {
        return maps;
    }
}
