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
import fr.xebia.extras.selma.CollectionMappingStrategy;
import fr.xebia.extras.selma.IgnoreMissing;
import fr.xebia.extras.selma.IoC;
import fr.xebia.extras.selma.Mapper;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import java.io.IOException;
import java.util.List;

import static fr.xebia.extras.selma.IgnoreMissing.*;
import static fr.xebia.extras.selma.codegen.MappingSourceNode.instantiateOut;

/**
 * Class used to wrap the Mapper Annotation
 */
public class MapperWrapper {
    public static final String WITH_IGNORE_FIELDS = "withIgnoreFields";
    public static final String WITH_ENUMS = "withEnums";
    public static final String WITH_IGNORE_MISSING = "withIgnoreMissing";
    public static final String WITH_IOC = "withIoC";
    public static final String WITH_COLLECTION_STRATEGY = "withCollectionStrategy";
    public static final String WITH_IOC_SERVICE_NAME = "withIoCServiceName";
    public static final String WITH_CYCLIC_MAPPING = "withCyclicMapping";
    public static final String WITH_IGNORE_NULL_VALUE = "withIgnoreNullValue";
    final IoC ioC;
    final String ioCServiceName;
    private final FieldsWrapper fields;
    private final SourceConfiguration configuration;
    private final IgnoreFieldsWrapper ignoreFieldsWrapper;
    private final MappingRegistry mappingRegistry;
    private final CustomMapperWrapper customMappers;
    private final ImmutableTypesWrapper immutablesMapper;
    private final AnnotationWrapper mapper;
    private final EnumMappersWrapper enumMappers;
    private final MapperGeneratorContext context;
    private final TypeElement mapperInterface;
    private final SourceWrapper source;
    private final IgnoreMissing ignoreMissing;
    private final CollectionMappingStrategy collectionMappingStrategy;
    private final boolean abstractClass;
    private final FactoryWrapper factory;
    private final boolean useCyclicMapping;
    private boolean ignoreNullValue;

    public MapperWrapper(MapperGeneratorContext context, TypeElement mapperInterface) {
        this.context = context;
        this.mapperInterface = mapperInterface;

        mappingRegistry = new MappingRegistry(context);

        mapper = AnnotationWrapper.buildFor(context, mapperInterface, Mapper.class);

        List<String> ignoreFieldsParam = mapper.getAsStrings(WITH_IGNORE_FIELDS);

        ignoreFieldsWrapper = new IgnoreFieldsWrapper(context, mapperInterface, ignoreFieldsParam);
        configuration = SourceConfiguration.buildFrom(mapper, ignoreFieldsWrapper);


        IgnoreMissing missing = IgnoreMissing.valueOf(mapper.getAsString(WITH_IGNORE_MISSING));
        if (missing == DEFAULT) {
            if (configuration.isIgnoreMissingProperties()) {
                ignoreMissing = ALL;
            } else {
                ignoreMissing = NONE;
            }
        } else {
            ignoreMissing = missing;
        }

        ioC = IoC.valueOf(mapper.getAsString(WITH_IOC));
        ioCServiceName = mapper.getAsString(WITH_IOC_SERVICE_NAME);
        collectionMappingStrategy = CollectionMappingStrategy.valueOf(mapper.getAsString(WITH_COLLECTION_STRATEGY));

        fields = new FieldsWrapper(context, mapperInterface, mapper);
        mappingRegistry.fields(fields);
        ignoreNullValue = mapper.getAsBoolean(WITH_IGNORE_NULL_VALUE);
        context.setIgnoreNullValue(ignoreNullValue);
        // Here we collect custom mappers
        customMappers = new CustomMapperWrapper(mapper, context);
        mappingRegistry.customMappers(customMappers);
        if (mapperInterface.getModifiers().contains(Modifier.ABSTRACT)) {
            customMappers.addMappersElementMethods(mapperInterface);
        }

        enumMappers = new EnumMappersWrapper(withEnums(), context, mapperInterface);
        mappingRegistry.enumMappers(enumMappers);

        immutablesMapper = new ImmutableTypesWrapper(mapper, context);
        mappingRegistry.immutableTypes(immutablesMapper);

        source = new SourceWrapper(mapper, context);

        factory = new FactoryWrapper(mapper, context);

        abstractClass = mapperInterface.getModifiers().contains(Modifier.ABSTRACT) &&
                mapperInterface.getKind() == ElementKind.CLASS;

        useCyclicMapping = mapper.getAsBoolean(WITH_CYCLIC_MAPPING);
    }


    public List<AnnotationWrapper> withEnums() {
        return mapper.getAsAnnotationWrapper(WITH_ENUMS);
    }

    public void buildEnumForMethod(MethodWrapper methodWrapper) {
        enumMappers.buildForMethod(methodWrapper);
    }

    public boolean isFinalMappers() {

        return configuration.isFinalMappers();
    }

    public MappingRegistry registry() {
        return mappingRegistry;
    }

    public SourceConfiguration configuration() {
        return configuration;
    }

    public void reportUnused() {

        // Report unused customMappers
        customMappers.reportUnused();

        // Report unused ignore fields
        ignoreFieldsWrapper.reportUnusedFields();

        // Report unused custom fields mapping
        fields.reportUnused();

        // Report unused enumMapper
        enumMappers.reportUnused();
        immutablesMapper.reportUnused();
        factory.reportUnused();
    }

    public void emitCustomMappersFields(JavaWriter writer, boolean assign) throws IOException {
        customMappers.emitCustomMappersFields(writer, assign);
    }

    public MapperGeneratorContext context() {
        return context;
    }

    public IgnoreFieldsWrapper ignoredFields() {
        return ignoreFieldsWrapper;
    }

    public FieldsWrapper fields() {
        return fields;
    }

    public boolean isIgnoreMissingProperties() {
        return configuration.isIgnoreMissingProperties();
    }

    public CustomMapperWrapper customMappers() {
        return customMappers;
    }

    /**
     * Method used to collect dependencies from mapping methods that need fields and constructor init
     *
     * @param maps maps annotation we want to collect
     */
    public void collectMaps(MapsWrapper maps) {
        customMappers.addFields(maps.customMapperFields());

    }

    public void emitSourceFields(JavaWriter writer) throws IOException {
        source.emitFields(writer);
    }

    public String[] sourceConstructorArgs() {
        return source.sourceConstructorArgs();
    }

    public void emitSourceAssigns(JavaWriter writer) throws IOException {
        source.emitAssigns(writer);
    }

    public IgnoreMissing ignoreMissing() {
        return ignoreMissing;
    }

    public boolean allowCollectionGetter() {
        return collectionMappingStrategy == CollectionMappingStrategy.ALLOW_GETTER;
    }

    public boolean isAbstractClass() {
        return abstractClass;
    }

    public void emitFactoryFields(JavaWriter writer, boolean assign) throws IOException {
        factory.emitFactoryFields(writer, assign);
    }

    public MappingSourceNode generateNewInstanceSourceNodes(InOutType inOutType, BeanWrapper outBeanWrapper) {
        MappingSourceNode res = factory.generateNewInstanceSourceNodes(inOutType, outBeanWrapper);
        if (res == null) {
            res = instantiateOut(useCyclicMapping, inOutType,
                    (outBeanWrapper.hasMatchingSourcesConstructor() ? context.newParams() : ""));
        }
        return res;
    }

    public boolean hasFactory(TypeMirror typeMirror) {
        return factory.hasFactory(typeMirror);
    }

    public boolean isUseCyclicMapping() {
        return useCyclicMapping;
    }
}
