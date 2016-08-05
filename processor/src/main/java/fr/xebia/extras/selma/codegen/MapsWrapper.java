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

import fr.xebia.extras.selma.CollectionMappingStrategy;
import fr.xebia.extras.selma.IgnoreMissing;
import fr.xebia.extras.selma.InherittMaps;
import fr.xebia.extras.selma.Maps;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.List;

import static fr.xebia.extras.selma.IgnoreMissing.DEFAULT;

/**
 * Created by slemesle on 10/11/14.
 */
public class MapsWrapper {

    public static final String WITH_CUSTOM_FIELDS = "withCustomFields";
    public static final String WITH_IGNORE_FIELDS = "withIgnoreFields";
    public static final String WITH_ENUMS = "withEnums";
    public static final String WITH_IGNORE_MISSING = "withIgnoreMissing";
    public static final String WITH_COLLECTION_STRATEGY = "withCollectionStrategy";


    private final FieldsWrapper customFields;
    private final AnnotationWrapper maps;
    private final MappingRegistry registry;
    private final IgnoreFieldsWrapper ignoreFields;
    private final EnumMappersWrapper enumMappers;
    private final MethodWrapper method;
    private final MapperWrapper mapperWrapper;
    private final MapperGeneratorContext context;
    private final CustomMapperWrapper customMapper;
    private final IgnoreMissing ignoreMissing;
    private final CollectionMappingStrategy collectionMappingStrategy;
    private boolean ignoreMissingProperties;


    public MapsWrapper(MethodWrapper method, MapperWrapper mapperWrapper) {

        this.method = method;
        this.mapperWrapper = mapperWrapper;
        this.registry = new MappingRegistry(mapperWrapper.registry());
        this.context = mapperWrapper.context();

        maps = AnnotationWrapper.buildFor(context, method.element(), Maps.class);
        AnnotationWrapper inheritMaps = AnnotationWrapper.buildFor(context, method.element(), InherittMaps.class);


        ignoreFields = new IgnoreFieldsWrapper(context, method.element(), mapperWrapper.ignoredFields(), maps == null ? null : maps.getAsStrings(WITH_IGNORE_FIELDS));

        customFields = new FieldsWrapper(context, method, mapperWrapper.fields(), maps == null ? null : maps.getAsAnnotationWrapper(WITH_CUSTOM_FIELDS));

        enumMappers = new EnumMappersWrapper(registry.getEnumMappers(), maps == null ? null : maps.getAsAnnotationWrapper(WITH_ENUMS), method.element());
        registry.enumMappers(enumMappers);

        customMapper = new CustomMapperWrapper(mapperWrapper.customMappers(), maps, context);
        registry.customMappers(customMapper);

        //TODO remove this code in 0.11
        ignoreMissingProperties = mapperWrapper.isIgnoreMissingProperties();

        collectionMappingStrategy = (maps == null ? CollectionMappingStrategy.DEFAULT : CollectionMappingStrategy.valueOf(maps.getAsString(WITH_COLLECTION_STRATEGY)));


        IgnoreMissing missing = (maps == null ? DEFAULT : IgnoreMissing.valueOf(maps.getAsString(WITH_IGNORE_MISSING)));
        if (missing == DEFAULT) {
            ignoreMissing = mapperWrapper.ignoreMissing();
        } else {
            ignoreMissing = missing;
        }

    }

    public void reportUnused() {
        // Report unused ignore fields
        ignoreFields.reportUnusedFields();

        // Report unused custom field to field
        customFields.reportUnused();

        // Report unused custom enum mappers
        enumMappers.reportUnused();

        // Report unused custom mappers
        customMapper.reportUnused();

        // Report unused interceptors
    }

    public List<Field> getFieldsFor(String field, DeclaredType sourceType, DeclaredType destinationType) {
        return customFields.getFieldFor(field, sourceType, destinationType);
    }

    public MappingBuilder mappingInterceptor(InOutType inOutType) {
        return registry.mappingInterceptor(inOutType);
    }

    public MappingBuilder findMappingFor(InOutType inOutType) {
        return registry.findMappingFor(inOutType);
    }

    public boolean isIgnoredField(String field, DeclaredType declaredType) {
        return ignoreFields.isIgnoredField(field, declaredType);
    }

    public boolean isIgnoreMissingProperties() {
        return ignoreMissingProperties;
    }

    public List<TypeElement> customMapperFields() {
        return customMapper.mapperFields();
    }

    public IgnoreMissing ignoreMissing() {
        return ignoreMissing;
    }

    public boolean allowCollectionGetter() {

        return collectionMappingStrategy == CollectionMappingStrategy.DEFAULT ? mapperWrapper.allowCollectionGetter() : collectionMappingStrategy == CollectionMappingStrategy.ALLOW_GETTER;
    }

    public MappingSourceNode generateNewInstanceSourceNodes(InOutType inOutType, BeanWrapper outBeanWrapper) {
        return mapperWrapper.generateNewInstanceSourceNodes(inOutType, outBeanWrapper);
    }

    public boolean hasFactory(TypeMirror typeMirror) {
        return mapperWrapper.hasFactory(typeMirror);
    }

    public List<TypeElement> customF2FMapperFields() {
        return customFields.mapperFields();
    }
}
