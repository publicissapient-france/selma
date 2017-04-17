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
import fr.xebia.extras.selma.InheritMaps;
import fr.xebia.extras.selma.Maps;

import javax.lang.model.element.Element;
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
    public static final String METHOD = "method";


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
    private final AnnotationWrapper inheritMaps;
    private boolean ignoreMissingProperties;
    private MapsWrapper _this = this;


    public MapsWrapper(MethodWrapper method, MapperWrapper mapperWrapper) {

        this.method = method;
        this.mapperWrapper = mapperWrapper;
        this.registry = new MappingRegistry(mapperWrapper.registry());
        this.context = mapperWrapper.context();

        maps = AnnotationWrapper.buildFor(context, method.element(), Maps.class);
        inheritMaps = AnnotationWrapper.buildFor(context, method.element(), InheritMaps.class);


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
        _this.ignoreFields.reportUnusedFields();

        // Report unused custom field to field
        _this.customFields.reportUnused();

        // Report unused custom enum mappers
        _this.enumMappers.reportUnused();

        // Report unused custom mappers
        _this.customMapper.reportUnused();

        // Report unused interceptors
    }

    /**
     * Retrieve the inherited map or fail
     * @param methodGenerators
     */
    public void resolveInheritMaps(List<MapperMethodGenerator> methodGenerators, MethodWrapper mapperMethod) {
        InOutType inOutType = mapperMethod.inOutType();
        MapperMethodGenerator foundTarget = null;
        String methodName = this.getInheritMapsMethod();
        for (MapperMethodGenerator mapperMethodGenerator : methodGenerators){
            // Skip the current running method generator
            if (mapperMethodGenerator.mapperMethod == mapperMethod){
                continue;
            }

            // only consider generator having a Map
            if (mapperMethodGenerator.maps().hasMaps()){
                InOutType ioType = mapperMethodGenerator.mapperMethod.inOutType();
                if (ioType.equalsWithoutOutputAsParam(inOutType) ||
                        ioType.invert().equalsWithoutOutputAsParam(inOutType)){
                    if ((!isEmptyOrNull(methodName) && methodName.equals(mapperMethodGenerator.mapperMethod.getSimpleName()))
                            || isEmptyOrNull(methodName)) {
                        if (foundTarget == null) {
                            foundTarget = mapperMethodGenerator;
                        } else {
                            context.error(this.getInheritMaps(),
                                    "Multiple elligible @Maps method found for @InheritMaps found : %s - %s",
                                    foundTarget.mapperMethod.element(), mapperMethodGenerator.mapperMethod.element());
                            break;
                        }
                    }
                }
            }
        }
        // Do inheritance of the current mapsWrapper from the foundTarget one
        if (foundTarget != null){
            inheritFromMaps(foundTarget.maps());
        }
    }

    private boolean isEmptyOrNull(String in){
        if (in == null || in.isEmpty()){
            return true;
        }
        return false;
    }



    public List<Field> getFieldsFor(String field, DeclaredType sourceType, DeclaredType destinationType) {
        return _this.customFields.getFieldFor(field, sourceType, destinationType);
    }

    public MappingBuilder mappingInterceptor(InOutType inOutType) {
        return _this.registry.mappingInterceptor(inOutType);
    }

    public MappingBuilder findMappingFor(InOutType inOutType) {
        return _this.registry.findMappingFor(inOutType);
    }

    public boolean isIgnoredField(String field, DeclaredType declaredType) {
        return _this.ignoreFields.isIgnoredField(field, declaredType);
    }

    public List<TypeElement> customMapperFields() {
        return _this.customMapper.mapperFields();
    }

    public IgnoreMissing ignoreMissing() {
        return _this.ignoreMissing;
    }

    public boolean allowCollectionGetter() {

        return _this.collectionMappingStrategy == CollectionMappingStrategy.DEFAULT ? mapperWrapper.allowCollectionGetter() : _this.collectionMappingStrategy == CollectionMappingStrategy.ALLOW_GETTER;
    }

    public MappingSourceNode generateNewInstanceSourceNodes(InOutType inOutType, BeanWrapper outBeanWrapper) {
        return mapperWrapper.generateNewInstanceSourceNodes(inOutType, outBeanWrapper);
    }

    public boolean hasFactory(TypeMirror typeMirror) {
        return mapperWrapper.hasFactory(typeMirror);
    }

    public List<TypeElement> customF2FMapperFields() {
        return _this.customFields.mapperFields();
    }

    public boolean hasInheritMaps() {
        return inheritMaps == null ? false : true;
    }

    public boolean hasMaps() {
        return maps == null ? false : true;
    }

    public Element getInheritMaps() {
        return inheritMaps.asElement();
    }

    public void inheritFromMaps(MapsWrapper maps) {
        this._this = maps;
    }

    public String getInheritMapsMethod() {
        return hasInheritMaps() ? inheritMaps.getAsString(METHOD) : null;
    }
}
