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

import fr.xebia.extras.selma.EnumMapper;
import fr.xebia.extras.selma.Fields;
import fr.xebia.extras.selma.IgnoreFields;
import fr.xebia.extras.selma.Mapper;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.*;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class wraps an ExecutableElement representing a Method
 * it gives convenience method to simplify method manipulation
 */
public class MethodWrapper {
    private static final String GETTER_FORMAT = "(get|is)(.*)";
    private static final Pattern GETTER_PATTERN = Pattern.compile(GETTER_FORMAT);
    private static final String SETTER_FORMAT = "set(.*)";
    private static final Pattern SETTER_PATTERN = Pattern.compile(SETTER_FORMAT);
    private final ExecutableElement method;
    private final ExecutableType executableType;
    private final MapperGeneratorContext context;
    boolean ignoreMissingProperties = false;
    private DeclaredType parentType;
    private String fieldName;

    public MethodWrapper(ExecutableElement method, DeclaredType declaredType, MapperGeneratorContext context) {
        this.method = method;
        this.parentType = declaredType;
        this.context = context;
        // Try to resolve any inherited or declared generic type using asMemberOf processed class
        this.executableType = (ExecutableType) context.type.asMemberOf(parentType, method);

        AnnotationWrapper annotationWrapper = AnnotationWrapper.buildFor(context, method, Mapper.class);
        if (annotationWrapper != null) {
            ignoreMissingProperties = annotationWrapper.getAsBoolean("ignoreMissingProperties");
        }
    }

    public TypeMirror firstParameterType() {
        if (method.getParameters().size() > 0) {
            return executableType.getParameterTypes().get(0);
        } else {
            return null;
        }
    }

    public TypeMirror returnType() {
        return executableType.getReturnType();
    }

    public String getSimpleName() {
        return method.getSimpleName().toString();
    }

    public InOutType inOutType() {
        return new InOutType(firstParameterType(), returnType(), parameterCount() == 2);
    }


    public InOutType inOutArgs() {
        return new InOutType(firstParameterType(), secondParameterType(), parameterCount() == 2);
    }

    private TypeMirror secondParameterType() {
        if (method.getParameters().size() > 1) {
            return executableType.getParameterTypes().get(1);
        } else {
            return null;
        }
    }

    public ExecutableElement element() {
        return method;
    }

    public boolean hasOneParameter() {
        return method.getParameters().size() == 1;
    }

    public int parameterCount() {
        return method.getParameters().size();
    }

    public boolean hasReturnType() {

        return method.getReturnType() != null && method.getReturnType().getKind() != TypeKind.VOID;
    }

    /**
     * Determines if the wrapping method represent a getter :
     * public not void method starting with get or is prefix
     *
     * @return
     */
    public boolean isGetter() {
        boolean res = false;
        if (hasNoParameter() && method.getReturnType().getKind() != TypeKind.VOID
                && method.getModifiers().contains(Modifier.PUBLIC)
                && !method.getModifiers().contains(Modifier.STATIC)) {
            Matcher getterMatcher = GETTER_PATTERN.matcher(method.getSimpleName());
            res = getterMatcher.matches();
            if (res) {
                fieldName = getterMatcher.group(2);
            }
        }
        return res;
    }

    /**
     * Determines if the wrapping method is a setter
     *
     * @return
     */
    public boolean isSetter() {
        boolean res = false;
        if (method.getParameters().size() == 1
                && method.getModifiers().contains(Modifier.PUBLIC)
                && !method.getModifiers().contains(Modifier.ABSTRACT)
                && !method.getModifiers().contains(Modifier.STATIC)) {
            boolean validReturnType = method.getReturnType().getKind() == TypeKind.VOID;
            if (!validReturnType) {
                // check method as a member of the actual parentType
                ExecutableType memberMethod = (ExecutableType) context.type.asMemberOf(parentType, method);
                TypeMirror memberReturnType = memberMethod.getReturnType();
                // can be a type variable if the return type is parameterized
                if (memberReturnType.getKind() == TypeKind.TYPEVAR) {
                    memberReturnType = ((TypeVariable) memberReturnType).getUpperBound();
                }
                validReturnType = context.type.isSameType(memberReturnType, parentType);
            }
            if (validReturnType) {
                Matcher setterMatcher = SETTER_PATTERN.matcher(method.getSimpleName());
                res = setterMatcher.matches();
                if (res) {
                    fieldName = setterMatcher.group(1);
                }
            }
        }
        return res;
    }

    public String getFieldName() {
        return fieldName;
    }


    public boolean hasAnnotation(String annotation) {
        boolean res = false;
        for (AnnotationMirror annotationMirror : method.getAnnotationMirrors()) {
            if (annotationMirror.getAnnotationType().toString().equals(annotation)) {
                res = true;
                break;
            }
        }
        return res;

    }

    public boolean hasIgnoreFields() {
        return hasAnnotation(IgnoreFields.class.getCanonicalName());
    }

    public boolean hasEnumMapper() {
        return hasAnnotation(EnumMapper.class.getCanonicalName());
    }

    /**
     * A custom mapper have one parameter and return a type.
     * public Out customMapper(In in)
     *
     * @return
     */
    public boolean isCustomMapper() {
        return (hasReturnType() && hasOneParameter()) ||
                (hasReturnType() && hasTwoParameter() && secondParamIsReturnType());
    }

    private boolean secondParamIsReturnType() {
        boolean res = false;

        TypeMirror secondParameterType = secondParameterType();
        TypeMirror returnType = returnType();
        res = context.type.isSameType(secondParameterType, returnType);
        return res;
    }

    /**
     * A mapping interceptor returns void and have two parameters
     * public void interceptMapping(In in, Out out)
     *
     * @return
     */
    public boolean isMappingInterceptor() {
        return !hasReturnType() && hasTwoParameter();
    }

    private boolean hasTwoParameter() {
        return method.getParameters().size() == 2;
    }

    public boolean hasFields() {
        return hasAnnotation(Fields.class.getCanonicalName());
    }


    public boolean isAbstract() {
        return method.getModifiers().contains(Modifier.ABSTRACT);
    }

    public boolean isFactory() {
        boolean res = false;
        if (hasReturnType()) {
            if (hasNoParameter()) {
                res = true;
            } else if (hasOneParameter()) {
                VariableElement variableElement = method.getParameters().get(0);
                TypeMirror typeMirror = variableElement.asType();
                // TODO Validate is Class<T>
                typeMirror.getKind();
                res = true;
            }
        }
        return hasReturnType() && (hasOneParameter() || hasNoParameter());
    }

    private boolean hasNoParameter() {
        return method.getParameters().size() == 0;
    }

    public boolean hasTypeParameter() {
        return !method.getTypeParameters().isEmpty();
    }
}
