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
import javax.lang.model.type.*;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.SimpleTypeVisitor6;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import static fr.xebia.extras.selma.codegen.MappingSourceNode.*;

/**
 *
 */
public abstract class MappingBuilder {

    private static final List<MappingSpecification> mappingSpecificationList = new LinkedList<MappingSpecification>();
    private boolean nullSafe = false;

    private static boolean areMatchingBoxedToPrimitive(InOutType inOutType, MapperGeneratorContext context) {
        boolean res = false;
        if (inOutType.isDeclaredToPrimitive()) {
            PrimitiveType inAsPrimitive = getUnboxedPrimitive(inOutType.inAsDeclaredType(), context);
            res = inAsPrimitive != null && inAsPrimitive.getKind() == inOutType.outKind();
        }
        return res;
    }

    public static final String JAVA_TIME_LOCAL_DATE_CLASS = "java.time.LocalDate";
    public static final String JAVA_TIME_DURATION_CLASS = "java.time.Duration";
    public static final String JAVA_TIME_INSTANT_CLASS = "java.time.Instant";
    public static final String JAVA_TIME_LOCAL_DATE_TIME_CLASS = "java.time.LocalDateTime";

    private static final Set<String> immutableTypes = new HashSet<String>(Arrays.asList(BigInteger.class.getName(),
            BigDecimal.class.getName(), UUID.class.getName(), JAVA_TIME_LOCAL_DATE_CLASS, JAVA_TIME_DURATION_CLASS,
            JAVA_TIME_INSTANT_CLASS, JAVA_TIME_LOCAL_DATE_TIME_CLASS, "java.time.LocalTime", "java.time.MonthDay",
            "java.time.OffsetDateTime", "java.time.OffsetTime", "java.time.Period", "java.time.Year",
            "java.time.YearMonth", "java.time.ZonedDateTime", "java.time.ZoneOffset"));


    static {  // init specs here

        /**
         * Mapping Primitives
         */
        mappingSpecificationList.add(new MappingSpecification() {
            @Override MappingBuilder getBuilder(final MapperGeneratorContext context, final InOutType inOutType) {
                return new MappingBuilder(true) {
                    @Override
                    public MappingSourceNode buildNodes(final MapperGeneratorContext context, final SourceNodeVars vars) throws IOException {

                        if (context.depth > 0) {
                            root.body(set(vars.outSetterPath(), vars.inGetter()));
                        } else {
                            root.body(assignOutPrime());
                        }
                        return root.body;
                    }
                };
            }

            @Override boolean match(final MapperGeneratorContext context, final InOutType inOutType) {
                return inOutType.areSamePrimitive() || areMatchingBoxedToPrimitive(inOutType, context);
            }
        });


        /**
         * Different Enum Mapper
         */
        mappingSpecificationList.add(new MappingSpecification() {
            @Override MappingBuilder getBuilder(final MapperGeneratorContext context, final InOutType inOutType) {

                TypeElement typeElement = inOutType.inAsTypeElement();
                final List<String> enumValues = collectEnumValues(typeElement);

                return new MappingBuilder(true) {
                    @Override
                    MappingSourceNode buildNodes(MapperGeneratorContext context, SourceNodeVars vars) throws IOException {
                        // If we are in a nested context we should call an enum mapping method
                        if (context.depth > 0) {
                            String mappingMethod = context.mappingMethod(inOutType);
                            root.body(vars.setOrAssign(String.format("%s(%%s)", mappingMethod)));
                        } else { // Otherwise we should build the real enum selma code
                            MappingSourceNode node = blank();
                            MappingSourceNode valuesBlock = node;
                            for (String value : enumValues) {
                                node = node.child(mapEnumCase(value));
                                node.body(vars.setOrAssign(String.format("%s.%s", inOutType.out(), value)));
                            }
                            root.body(mapEnumBlock(vars.inGetter())).body(valuesBlock.child);
                        }
                        return root.body;
                    }
                };
            }

            @Override boolean match(final MapperGeneratorContext context, final InOutType inOutType) {
                return inOutType.areDeclared() && inOutType.areEnums() && !inOutType.areSameDeclared();
            }
        });

        /**
         * Same Enum Mapper
         */
        mappingSpecificationList.add(new MappingSpecification() {
            @Override MappingBuilder getBuilder(final MapperGeneratorContext context, final InOutType inOutType) {

                TypeElement typeElement = inOutType.inAsTypeElement();
                final List<String> enumValues = collectEnumValues(typeElement);

                return new MappingBuilder(true) {
                    @Override
                    MappingSourceNode buildNodes(MapperGeneratorContext context, SourceNodeVars vars) throws IOException {

                        root.body(vars.setOrAssign("%s"));
                        return root.body;
                    }
                };
            }

            @Override boolean match(final MapperGeneratorContext context, final InOutType inOutType) {
                return inOutType.areDeclared() && inOutType.areEnums() && inOutType.areSameDeclared();
            }
        });

        // Map String or Boxed Primitive
        mappingSpecificationList.add(new MappingSpecification() {
            @Override MappingBuilder getBuilder(final MapperGeneratorContext context, final InOutType inOutType) {
                return new MappingBuilder(true) {
                    @Override
                    MappingSourceNode buildNodes(MapperGeneratorContext context, SourceNodeVars vars) throws IOException {

                        root.body(vars.setOrAssign("%s"));
                        return root.body;
                    }
                };
            }

            @Override boolean match(final MapperGeneratorContext context, final InOutType inOutType) {
                boolean res;
                if (inOutType.areSameDeclared()) {
                    DeclaredType declaredType = inOutType.inAsDeclaredType();
                    res = (isBoxedPrimitive(declaredType, context) || String.class.getName().equals(declaredType.toString()));
                } else {
                    res = isMatchingPrimitiveToBoxed(inOutType, context);
                }
                return res;
            }
        });

        // Map Dates
        mappingSpecificationList.add(new SameDeclaredMappingSpecification() {
            @Override MappingBuilder getBuilder(final MapperGeneratorContext context, final InOutType inOutType) {
                return new MappingBuilder() {
                    @Override
                    MappingSourceNode buildNodes(MapperGeneratorContext context, SourceNodeVars vars) throws IOException {
                        root.body(vars.setOrAssign("new java.util.Date(%s.getTime())"));
                        return root.body;
                    }
                };
            }

            @Override boolean match(final MapperGeneratorContext context, final InOutType inOutType) {
                return super.match(context, inOutType) && Date.class.getName().equals(inOutType.in().toString());
            }
        });

        // Map Immutables
        mappingSpecificationList.add(new SameDeclaredMappingSpecification() {
            @Override MappingBuilder getBuilder(final MapperGeneratorContext context, final InOutType inOutType) {
                return new MappingBuilder(true) {
                    @Override
                    MappingSourceNode buildNodes(MapperGeneratorContext context, SourceNodeVars vars) throws IOException {
                        root.body(vars.setOrAssign("%s"));
                        return root.body;
                    }
                };
            }

            @Override boolean match(final MapperGeneratorContext context, final InOutType inOutType) {
                if (super.match(context, inOutType)) {
                    String inType = inOutType.in().toString();
                    return immutableTypes.contains(inType);
                }
                return false;
            }
        });

        // Map collections
        mappingSpecificationList.add(new MappingSpecification() {
            @Override boolean match(final MapperGeneratorContext context, final InOutType inOutType) {

                return inOutType.areDeclared() && isCollection(inOutType.inAsDeclaredType(), context) && isCollection(inOutType.outAsDeclaredType(), context);
            }

            @Override MappingBuilder getBuilder(final MapperGeneratorContext context, final InOutType inOutType) {

                // We have a collection
                final TypeMirror genericIn = getTypeArgument(context, inOutType.inAsDeclaredType(), 0);
                final TypeMirror genericOut = getTypeArgument(context, inOutType.outAsDeclaredType(), 0);
                // emit writer loop for collection and choose an implementation
                String impl;
                if (inOutType.outAsTypeElement().getKind() == ElementKind.CLASS) {
                    // Use given class for collection
                    impl = inOutType.out().toString();
                } else {
                    impl = CollectionsRegistry.findImplementationForType(inOutType.outAsTypeElement()) + "<" + genericOut.toString() + ">";
                }
                final String implementation = impl;
                TypeElement outElement = context.elements().getTypeElement(impl.replaceAll("<.*>", ""));

                boolean hasSizeCtr = false;
                List<ExecutableElement> constructors = ElementFilter.constructorsIn(context.elements().getAllMembers(outElement));
                for (ExecutableElement constructor : constructors) {
                    if (constructor.getParameters().size() == 1 && constructor.getParameters().get(0).asType().getKind() == TypeKind.INT) {
                        hasSizeCtr = true;
                        break;
                    }
                }
                final boolean hasSizeConstructor = hasSizeCtr;
                return new MappingBuilder() {
                    @Override
                    MappingSourceNode buildNodes(MapperGeneratorContext context, SourceNodeVars vars) throws IOException {

                        final String itemVar = vars.itemVar();
                        final String tmpVar = vars.tmpVar("Collection");
                        final String arg;
                        if (hasSizeConstructor) {
                            arg = String.format("%s.size()", vars.inGetter());
                        } else {
                            arg = "";
                        }
                        MappingSourceNode node;
                        if (vars.useGetterForDestination) {
                            node = root.body(assign(String.format("%s %s", inOutType.inAsTypeElement(), tmpVar), String.format("%s()", vars.outFieldGetter)))
                                       .child(mapCollection(itemVar, genericIn.toString(), vars.inGetter()));
                        } else {
                            node = root.body(assign(String.format("%s %s", implementation, tmpVar), String.format("new %s(%s)", implementation, arg)))
                                       .child(mapCollection(itemVar, genericIn.toString(), vars.inGetter()));
                            node.child(vars.setOrAssign(tmpVar));
                        }
                        context.pushStackForBody(node,
                                new SourceNodeVars().withInOutType(new InOutType(genericIn, genericOut, false))
                                                    .withInField(itemVar)
                                                    .withOutField(String.format("%s.add", tmpVar)).withAssign(false).withIndexPtr(vars.nextPtr()));

                        return root.body;
                    }
                };
            }
        });

        // Map map
        mappingSpecificationList.add(new MappingSpecification() {
            @Override MappingBuilder getBuilder(final MapperGeneratorContext context, final InOutType inOutType) {
                // We have a Map
                final TypeMirror genericInKey = getTypeArgument(context, inOutType.inAsDeclaredType(), 0);
                final TypeMirror genericOutKey = getTypeArgument(context, inOutType.outAsDeclaredType(), 0);
                final TypeMirror genericInValue = getTypeArgument(context, inOutType.inAsDeclaredType(), 1);
                final TypeMirror genericOutValue = getTypeArgument(context, inOutType.outAsDeclaredType(), 1);

                // emit writer loop for collection and choose an implementation
                String impl;
                if (inOutType.outAsTypeElement().getKind() == ElementKind.CLASS) {
                    // Use given class for collection
                    impl = inOutType.out().toString();

                } else {
                    impl = CollectionsRegistry.findImplementationForType(inOutType.outAsTypeElement()) + "<" + genericOutKey.toString() + ", " + genericOutValue.toString() + ">";
                }
                final String implementation = impl;

                return new MappingBuilder() {
                    @Override
                    MappingSourceNode buildNodes(MapperGeneratorContext context, SourceNodeVars vars) throws IOException {

                        final String itemVar = vars.itemEntry();
                        final String tmpVar = vars.tmpVar("Map");
                        final String keyVar = vars.tmpVar("Key");
                        MappingSourceNode node = root.body(assign(String.format("%s %s", implementation, tmpVar), String.format("new %s()", implementation)))
                                                     .child(vars.setOrAssign(tmpVar))
                                                     .child(mapMap(itemVar, genericInKey.toString(), genericInValue.toString(), vars.inGetter()))
                                                     .body(assign(String.format("%s %s", genericOutKey, keyVar), "null"));
                        context.pushStackForChild(node, new SourceNodeVars().withInOutType(new InOutType(genericInValue, genericOutValue, false))
                                                                            .withInField(String.format("%s.getValue()", itemVar)).withInFieldPrefix(String.format("%s,", keyVar))
                                                                            .withOutField(String.format("%s.put", tmpVar))
                                                                            .withAssign(false).withIndexPtr(vars.nextPtr()));
                        context.pushStackForChild(node, new SourceNodeVars().withInOutType(new InOutType(genericInKey, genericOutKey, false))
                                                                            .withInField(String.format("%s.getKey()", itemVar))
                                                                            .withOutField(keyVar)
                                                                            .withAssign(true).withIndexPtr(vars.nextPtr()));
                        return root.body;
                    }
                };
            }

            @Override boolean match(final MapperGeneratorContext context, final InOutType inOutType) {
                return inOutType.areDeclared() && isMap(inOutType.inAsDeclaredType(), context) && isMap(inOutType.outAsDeclaredType(), context);
            }
        });

        // Map array of primitive
        mappingSpecificationList.add(new MappingSpecification() {
            @Override MappingBuilder getBuilder(final MapperGeneratorContext context, final InOutType inOutType) {
                return new MappingBuilder() {
                    @Override
                    MappingSourceNode buildNodes(MapperGeneratorContext context, SourceNodeVars vars) throws IOException {

                        root.body(vars.setOrAssign(String.format("new %s[%%s.length]", inOutType.inArrayComponentType())))
                            .child(arrayCopy(vars.inGetter(), vars.outSetterPath()));

                        return root.body;
                    }
                };
            }

            @Override boolean match(final MapperGeneratorContext context, final InOutType inOutType) {
                return inOutType.inIsArray() && inOutType.isInArrayComponentPrimitive() && !inOutType.differs();
            }
        });

        // Map array of Declared or array of array
        mappingSpecificationList.add(new MappingSpecification() {
            @Override MappingBuilder getBuilder(MapperGeneratorContext context, final InOutType inOutType) {
                return new MappingBuilder() {
                    @Override
                    MappingSourceNode buildNodes(MapperGeneratorContext context, SourceNodeVars vars) throws IOException {
                        MappingSourceNode node;
                        final String indexVar = vars.indexVar();
                        final String tmpVar = vars.tmpVar("Array");

                        Map.Entry<TypeMirror, Integer> dims = getArrayDimensionsAndType(inOutType.inAsArrayType());

                        String totalCountVar = vars.totalCountVar();
                        String totalCountVal = String.format("%s.length", vars.inGetter());
                        String newArray = String.format("new %s[%s.length]", dims.getKey(), vars.inGetter());
                        int i = 1;
                        while (i < dims.getValue()) {
                            newArray += "[]";
                            i++;
                        }
                        node = root.body(assign(String.format("%s %s", inOutType.out().toString(), tmpVar), newArray))
                                   .child(assign("int " + totalCountVar, totalCountVal))
                                   .child(vars.setOrAssign(tmpVar))
                                   .child(mapArrayBis(indexVar, totalCountVar));

                        context.pushStackForBody(node,
                                new SourceNodeVars().withInOutType(new InOutType(inOutType.inArrayComponentType(), inOutType.outArrayComponentType(), false))
                                                    .withInField(String.format("%s[%s]", vars.inGetter(), indexVar))
                                                    .withOutField(String.format("%s[%s]", tmpVar, indexVar)).withAssign(true).withIndexPtr(vars.nextPtr()));

                        return root.body;
                    }
                };
            }

            @Override boolean match(MapperGeneratorContext context, InOutType inOutType) {
                return !inOutType.differs() && inOutType.inIsArray() && inOutType.isInArrayComponentDeclaredOrArray();
            }
        });

    }

    private static boolean isMatchingPrimitiveToBoxed(InOutType inOutType, MapperGeneratorContext context) {
        boolean res = false;

        if (inOutType.isPrimitiveToDeclared()) {
            PrimitiveType outAsPrimitive = getUnboxedPrimitive(inOutType.outAsDeclaredType(), context);
            res = outAsPrimitive != null && outAsPrimitive.getKind() == inOutType.inKind();
        }
        return res;
    }

    MappingSourceNode root;

    private MappingBuilder() {
        this.root = blank();
    }

    private MappingBuilder(boolean nullSafe) {
        this.nullSafe = nullSafe;
        this.root = blank();
    }

    protected void setOrAssignNestedBean(final MapperGeneratorContext context, final SourceNodeVars vars, final InOutType inOutType) {
        String mappingMethod = context.mappingMethod(inOutType);
        if (inOutType.isOutPutAsParam()) {
            this.root.body(vars.setOrAssignWithOutPut(String.format("%s(%%s, %%s)", mappingMethod)));
        } else {
            this.root.body(vars.setOrAssign(String.format("%s(%%s)", mappingMethod)));
        }
    }

    private static Map.Entry<TypeMirror, Integer> getArrayDimensionsAndType(final ArrayType arrayType) {
        int res = 1;
        ArrayType type = arrayType;
        while (type.getComponentType().getKind() == TypeKind.ARRAY) {
            res++;
            type = (ArrayType) type.getComponentType();
        }

        return new AbstractMap.SimpleEntry<TypeMirror, Integer>(type.getComponentType(), res);
    }

    public static MappingBuilder getBuilderFor(final MapperGeneratorContext context, final InOutType inOutType) {

        MappingBuilder res = null;
        for (MappingSpecification specification : mappingSpecificationList) {
            if (specification.match(context, inOutType)) {
                res = specification.getBuilder(context, inOutType);
                break;
            }
        }

        // Found a nested bean ?
        if (res == null && inOutType.areDeclared() && context.depth > 0) {
            res = new MappingBuilder(true) {
                @Override
                MappingSourceNode buildNodes(MapperGeneratorContext context, SourceNodeVars vars) throws IOException {
                    setOrAssignNestedBean(context, vars, inOutType);
                    return root.body;
                }
            };
        }

        return res;
    }

    public static List<String> collectEnumValues(TypeElement typeElement) {
        List<String> res = new ArrayList<String>();

        final List<? extends Element> elementInList = typeElement.getEnclosedElements();
        for (Element field : ElementFilter.fieldsIn(elementInList)) {
            if (field.getKind() == ElementKind.ENUM_CONSTANT) {
                res.add(field.getSimpleName().toString());
            }
        }
        return res;
    }

    private static boolean isMap(DeclaredType declaredType, MapperGeneratorContext context) {
        TypeElement typeElement1 = context.elements.getTypeElement("java.util.Map");

        DeclaredType declaredType2 = context.type.getDeclaredType(typeElement1, context.type.getWildcardType(null, null), context.type.getWildcardType(null, null));

        return context.type.isAssignable(declaredType, declaredType2);
    }

    public static boolean isCollection(DeclaredType declaredType, MapperGeneratorContext context) {

        TypeElement typeElement1 = context.elements.getTypeElement("java.util.Collection");

        DeclaredType declaredType2 = context.type.getDeclaredType(typeElement1, context.type.getWildcardType(null, null));

        return declaredType != null && context.type.isAssignable(declaredType, declaredType2);
    }

    private static boolean isBoxedPrimitive(DeclaredType declaredType, MapperGeneratorContext context) {
        return getUnboxedPrimitive(declaredType, context) != null;
    }

    private static PrimitiveType getUnboxedPrimitive(DeclaredType declaredType, MapperGeneratorContext context) {
        PrimitiveType res = null;
        final PackageElement typePackage = context.elements.getPackageOf(declaredType.asElement());
        final PackageElement javaLang = context.elements.getPackageElement("java.lang");
        if (typePackage.getSimpleName().equals(javaLang.getSimpleName())) {
            try {
                res = context.type.unboxedType(declaredType);
            } catch (IllegalArgumentException ignored) {
            }
        }
        return res;
    }

    private static MappingSourceNode notNullInField(final SourceNodeVars vars) {
        return controlNotNull(vars.inGetter(), false);
    }

    public static MappingBuilder newCustomMapperImmutableForUpdateGraph(final InOutType inOutType, final String name) {
        return new MappingBuilder(true) {
            @Override
            MappingSourceNode buildNodes(MapperGeneratorContext context, SourceNodeVars vars) throws IOException {
                context.mappingMethod(inOutType, name);
                root.body(vars.setOrAssignWithOutPut(String.format("%s(%%s, %%s)", name)));
                return root.body;
            }
        };
    }

    public static MappingBuilder newCustomMapper(final InOutType inOutType, final String name) {
        return new MappingBuilder(true) {
            @Override
            MappingSourceNode buildNodes(MapperGeneratorContext context, SourceNodeVars vars) throws IOException {
                context.mappingMethod(inOutType, name);
                if (inOutType.isOutPutAsParam()) {
                    root.body(vars.setOrAssignWithOutPut(String.format("%s(%%s, %%s)", name)));
                } else {
                    root.body(vars.setOrAssign(String.format("%s(%%s)", name)));
                }
                return root.body;
            }
        };
    }

    public static MappingBuilder newMappingInterceptor(final InOutType inOutType, final String name) {
        return new MappingBuilder() {
            @Override
            MappingSourceNode buildNodes(MapperGeneratorContext context, SourceNodeVars vars) throws IOException {
                context.mappingMethod(inOutType, name);
                root.body(statement(String.format("%s(in,out)", name)));
                return root.body;
            }
        };
    }

    abstract MappingSourceNode buildNodes(final MapperGeneratorContext context, final SourceNodeVars vars) throws IOException;

    public MappingSourceNode build(final MapperGeneratorContext context, final SourceNodeVars vars) throws IOException {
        root = blank();
        MappingSourceNode ptr = buildNodes(context, vars);
        if (context.depth > 0 && !nullSafe) {
            // working inside a bean
            root = notNullInField(vars);
            root.body(ptr);

            // Do not set null to primitive type
            if (!vars.isOutPrimitive() && !vars.useGetterForDestination) {
                root.child(controlNullElse()).body(vars.setOrAssign("null"));
            }
            return root;
        } else {
            if (context.isIgnoreNullValue() && !vars.isOutPrimitive()){
                root = notNullInField(vars);
                root.body(ptr);
                return root;
            }else {
                return buildNodes(context, vars);
            }
        }
    }


    /**
     * Builds a method that match identical items  and use default value otherwise
     *
     * @param inOutType    in enum to out enum type relation
     * @param defaultValue The default value used as out
     * @return A builder that generates a custom enum using a switch
     */
    public static MappingBuilder newCustomEnumMapper(final InOutType inOutType, final String defaultValue) {
        TypeElement typeElement = inOutType.inAsTypeElement();
        final List<String> enumInValues = collectEnumValues(typeElement);
        final List<String> enumOutValues = collectEnumValues(inOutType.outAsTypeElement());

        final List<String> valuesIntersection = intersection(enumInValues, enumOutValues);

        return new MappingBuilder() {
            @Override
            MappingSourceNode buildNodes(MapperGeneratorContext context, SourceNodeVars vars) throws IOException {
                // If we are in a nested context we should call an enum mapping method
                if (context.depth > 0) {
                    String mappingMethod = context.mappingMethod(inOutType);
                    root.body(vars.setOrAssign(String.format("%s(%%s)", mappingMethod)));
                } else { // Otherwise we should build the real enum selma code
                    MappingSourceNode node = blank();
                    MappingSourceNode valuesBlock = node;
                    for (String value : valuesIntersection) {
                        node = node.child(mapEnumCase(value));
                        node.body(vars.setOrAssign(String.format("%s.%s", inOutType.out(), value)));
                    }
                    node = node.child(mapDefaultCase());
                    if ("".equals(defaultValue)) {
                        node.body(vars.setOrAssign("null"));
                    } else {
                        node.body(vars.setOrAssign(String.format("%s.%s", inOutType.out(), defaultValue)));
                    }
                    root.body(mapEnumBlock(vars.inGetter())).body(valuesBlock.child);
                }
                return root.body;
            }
        };
    }

    /**
     * Retrieve type arguments for Collections and Maps
     * @return
     */
    private static TypeMirror getTypeArgument(final MapperGeneratorContext context, TypeMirror type, final int index) {
        TypeMirror param = type.accept(new SimpleTypeVisitor6<TypeMirror, Void>() {
            @Override
            public TypeMirror visitDeclared(DeclaredType t, Void p) {
                if (t.getTypeArguments().size() > 0) {
                    return t.getTypeArguments().get(index);
                }
                TypeMirror superclass = ((TypeElement) t.asElement()).getSuperclass();
                return getTypeArgument(context, superclass, index);
            }
        }, null);

        if (param != null) {
            return param;
        }

        return null;
    }

    static public <T> List<T> intersection(List<T> list1, List<T> list2) {
        List<T> list = new ArrayList<T>();

        for (T t : list1) {
            if (list2.contains(t)) {
                list.add(t);
            }
        }

        return list;
    }

    public boolean isNullSafe() {
        return nullSafe;
    }

    public static MappingBuilder newImmutable() {
        return new MappingBuilder(true) {
            @Override
            MappingSourceNode buildNodes(MapperGeneratorContext context, SourceNodeVars vars) throws IOException {
                root.body(vars.setOrAssign("%s"));
                return root.body;
            }
        };
    }


    static abstract class MappingSpecification {

        abstract MappingBuilder getBuilder(final MapperGeneratorContext context, final InOutType inOutType);

        abstract boolean match(final MapperGeneratorContext context, final InOutType inOutType);

    }

    static abstract class SameDeclaredMappingSpecification extends MappingSpecification {

        @Override boolean match(final MapperGeneratorContext context, final InOutType inOutType) {
            return !inOutType.differs() && inOutType.areDeclared();
        }
    }
}
