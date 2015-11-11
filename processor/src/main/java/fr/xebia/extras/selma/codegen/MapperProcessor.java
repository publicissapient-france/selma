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

import fr.xebia.extras.selma.Mapper;
import fr.xebia.extras.selma.SelmaConstants;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

/**
 * Process the @Mapper and generate the corresponding implementations
 */
@SupportedAnnotationTypes({"fr.xebia.extras.selma.Mapper"})
public final class MapperProcessor extends AbstractProcessor {


    private final HashMap<String, List<ExecutableElement>> remainingMapperTypes = new HashMap<String, List<ExecutableElement>>();

    static Types types;


    protected static final Set<ExecutableElement> exclusions = new HashSet<ExecutableElement>();

    @Override public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        TypeElement objectElement = processingEnv.getElementUtils().getTypeElement(Object.class.getName());
        List<ExecutableElement> objectElementsMethods = ElementFilter.methodsIn(objectElement.getEnclosedElements());
        exclusions.clear();
        exclusions.addAll(objectElementsMethods);
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        boolean res = true;
        if (annotations.size() > 0) {
            types = processingEnv.getTypeUtils();
            populateAllMappers(roundEnv);

            try {
                generateMappingClassses();
            } catch (IOException e) {
                e.printStackTrace();
                StringWriter writer = new StringWriter();
                e.printStackTrace(new PrintWriter(writer));
                error(writer.toString(), null);
            }
            remainingMapperTypes.clear();
        }
        return res;
    }

    private void generateMappingClassses() throws IOException {

        for (String classe : remainingMapperTypes.keySet()) {
            MapperClassGenerator classGenerator = new MapperClassGenerator(classe, remainingMapperTypes.get(classe), processingEnv);
            classGenerator.build();
        }

    }

    private void populateAllMappers(RoundEnvironment roundEnv) {

        for (Element element : roundEnv.getElementsAnnotatedWith(Mapper.class)) {
            boolean abstractClass = isAbstractClass(element);
            if (isSelmaGenerated(element) || !isValidMapperUse(element)) {
                continue;
            } else {
                TypeElement typeElement = (TypeElement) element;
                List<? extends Element> allMembers = processingEnv.getElementUtils().getAllMembers(typeElement);
                List<? extends Element> methods = ElementFilter.methodsIn(allMembers);

                for (Element method : methods) {
                    ExecutableElement executableElement = (ExecutableElement) method;
                    // We should only process abstract method as mapper to implement
                    if (abstractClass && !isAbstractMethod(executableElement)) {
                        continue;
                    }
                    if (isValidMapperMethod(executableElement)) {
                        putMapper(element, executableElement);
                    }
                }

            }
        }
    }

    private boolean isAbstractMethod(ExecutableElement executableElement) {
        return executableElement.getModifiers().contains(Modifier.ABSTRACT);
    }

    private boolean isSelmaGenerated(Element element) {
        return (""+element.getSimpleName()).endsWith(SelmaConstants.MAPPER_CLASS_SUFFIX);
    }

    private boolean isValidMapperMethod(ExecutableElement executableElement) {

        if (exclusions.contains(executableElement)) {

            return false;
        }

        if (executableElement.getParameters().size() < 1) {
            error(executableElement, "@Mapper method %s can not have less than one parameter", executableElement.getSimpleName());
            return false;
        }
        if (executableElement.getParameters().size() > 2) {
            error(executableElement, "@Mapper method %s can not have more than two parameters", executableElement.getSimpleName());
            return false;
        }

        if (executableElement.getReturnType().getKind() == TypeKind.VOID) {
            error(executableElement, "@Mapper method %s can not return void", executableElement.getSimpleName());
            return false;
        }

        if (executableElement.getParameters().size() == 2) {
            TypeMirror returnType = executableElement.getReturnType();
            VariableElement variableElement = executableElement.getParameters().get(1);
            if (!variableElement.asType().toString().equals(returnType.toString())) {
                error(executableElement, "@Mapper method %s second parameter type should be %s as the return type is", executableElement.getSimpleName(), executableElement.getReturnType());
                return false;
            }
        }

        return true;
    }


    private void error(Element element, String templateMessage, Object... args) {

        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, String.format(templateMessage, args), element);
    }

    private void putMapper(Element element, ExecutableElement executableElement) {
        String type = element.asType().toString();
        List<ExecutableElement> elementList;
        if (remainingMapperTypes.containsKey(type)) {
            elementList = remainingMapperTypes.get(type);
        } else {
            elementList = new ArrayList<ExecutableElement>();
            remainingMapperTypes.put(type, elementList);
        }
        elementList.add(executableElement);
    }

    private boolean isValidMapperUse(Element element) {
        boolean res = true;
        if (element.getKind() != ElementKind.INTERFACE && !isAbstractClass(element)) {
            error(element, "@Mapper can only be used on interface or public abstract class");
            res = false;
        }

        return res;
    }

    private boolean isAbstractClass(Element element) {
        boolean res = false;
        if (element.getKind() == ElementKind.CLASS) {
            TypeElement typeElement = (TypeElement) element;
            res = typeElement.getModifiers().contains(Modifier.ABSTRACT) &&
                    typeElement.getModifiers().contains(Modifier.PUBLIC) &&
                    !typeElement.getModifiers().contains(Modifier.FINAL);

        }
        return res;
    }

    private void error(String msg, Element element) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, msg, element);
    }

}