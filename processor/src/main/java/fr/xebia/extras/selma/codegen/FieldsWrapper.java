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

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by slemesle on 23/06/2014.
 */
public class FieldsWrapper {

    private MapperGeneratorContext context;
    private Element element;
    private FieldMap fieldsRegistry;
    private FieldMap unusedFields;
    private FieldsWrapper parent = null;
    private List<CustomMapperWrapper> customMapperWrappers = new ArrayList<CustomMapperWrapper>();

    private FieldsWrapper(MapperGeneratorContext context, Element element) {
        this.context = context;
        this.element = element;
        this.fieldsRegistry = new FieldMap(element);
    }

    public FieldsWrapper(MapperGeneratorContext context, MethodWrapper mapperMethod, FieldsWrapper parent, List<AnnotationWrapper> withCustomFields) {
        this(context, mapperMethod.element());

        if (withCustomFields != null && withCustomFields.size() > 0) {
            processFieldList(context, withCustomFields);
        }

        this.unusedFields = new FieldMap(fieldsRegistry);
        this.parent = parent;
    }


    public FieldsWrapper(MapperGeneratorContext context, TypeElement type, AnnotationWrapper mapper) {
        this(context, type);

        processFieldsFromMapper(context, mapper);

        this.unusedFields = new FieldMap(fieldsRegistry);
        this.parent = null;
    }

    private void processFieldsFromMapper(MapperGeneratorContext context, AnnotationWrapper mapper) {
        List<AnnotationWrapper> withCustomFields = mapper.getAsAnnotationWrapper("withCustomFields");
        processFieldList(context, withCustomFields);
    }

    private void processFieldList(MapperGeneratorContext context, List<AnnotationWrapper> fields) {
        for (AnnotationWrapper field : fields) {
            List<String> fieldPair = field.getAsStrings("value");
            String withCustom = field.getAsString("withCustom");
            FieldBuilder fieldBuilder = null;

            if (fieldPair.size() == 1 && !"java.lang.Object".equals(withCustom)) {
                /**
                 * We have only one field with a custom mapper so we use this one as both from and to
                 * @Field(value = "myField", withCustom = MyCustomMapper.class)
                 */
                fieldBuilder = new FieldBuilder().forElement(element)
                                                 .from(fieldPair.get(0).toLowerCase())
                                                 .to(fieldPair.get(0).toLowerCase());

                CustomMapperWrapper customMapperWrapper = new CustomMapperWrapper(field, context);
                fieldBuilder.withCustom(customMapperWrapper);
                customMapperWrappers.add(customMapperWrapper);

            } else if (fieldPair.size() == 2 && !fieldPair.get(0).isEmpty() && !fieldPair.get(1).isEmpty()){
                /**
                 * We have only two fields with a potential custom mapper
                 * @Field(value = {"myFieldFrom", "myFieldTo"}, withCustom = MyCustomMapper.class)
                 */
                fieldBuilder = new FieldBuilder().forElement(element)
                                                 .from(fieldPair.get(0).toLowerCase())
                                                 .to(fieldPair.get(1).toLowerCase());

                if (!"java.lang.Object".equals(withCustom)) {
                    // The custom mapper is only defined if it is not a Object.class
                    CustomMapperWrapper customMapperWrapper = new CustomMapperWrapper(field, context);
                    fieldBuilder.withCustom(customMapperWrapper);
                    customMapperWrappers.add(customMapperWrapper);
                }
            } else if (fieldPair.size() == 2) {
                // Field should have 2 not empty values
                context.error(element, "Invalid @Field signature, empty string for fields are forbidden: " +
                        field.toString());
            } else {
                // Bad Field signature too many fields in value or to few
                context.error(element, "Invalid @Field signature, bad value count in value array: " + field.toString() +
                        " \n" +
                        "One value is supported @Field(value = \"field\", withCustom = MyCustomMapper.class)" +
                        " only when withCustom is defined\n" +
                        "Otherwise you must provide 2 and only 2 values @Field({\"fieldFrom\", \"fieldTo\"}");
            }

            if (fieldBuilder != null){
                fieldsRegistry.push(fieldBuilder.build());
            }
        }
    }


    public List<Field> getFieldFor(String field, DeclaredType sourceType, DeclaredType destinationType) {

        final String sourceFqcn = sourceType.toString().toLowerCase();
        final String sourceSimpleName = sourceType.asElement().getSimpleName().toString().toLowerCase();
        final String destinationFqcn = destinationType.toString().toLowerCase();
        final String destinationSimpleName = destinationType.asElement().getSimpleName().toString().toLowerCase();
        final List<Field> resParent = new ArrayList<Field>();
        final List<Field> res = new ArrayList<Field>();

        if (parent != null) {
            resParent.addAll(parent.getFieldFor(field, sourceType, destinationType));
        }
        res.addAll(fieldsRegistry.getStartingWith(field));
        res.addAll(fieldsRegistry.getStartingWith(sourceFqcn + "." + field));
        res.addAll(fieldsRegistry.getStartingWith(sourceSimpleName + "." + field));

        for (Field re : res) {
            unusedFields.remove(re.to);
            re.removeDestinationPrefix(destinationFqcn, destinationSimpleName);
            re.removeSourcePrefix(sourceFqcn, sourceSimpleName);
            Iterator<Field> parentIterator = resParent.iterator();
            while (parentIterator.hasNext()) {
                Field parentField = parentIterator.next();
                if (parentField.hasOneFieldMatching(re)) {
                    parentIterator.remove();
                }
            }
        }
        res.addAll(resParent);
        return res;
    }


    public void reportUnused() {
        for (Map.Entry<String, Field> unusedPair : unusedFields.entrySet()) {
            context.warn(element, "Custom @Field({\"%s\",\"%s\"}) mapping is never used !", unusedPair.getKey(), unusedPair.getValue().to);
        }
    }

    public List<TypeElement> mapperFields() {
        List<TypeElement> res = new ArrayList<TypeElement>();
        for (CustomMapperWrapper custom : customMapperWrappers) {
            res.addAll(custom.mapperFields());
        }
        return res;
    }
}
