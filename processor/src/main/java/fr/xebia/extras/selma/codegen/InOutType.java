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

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

/**
 * Created with IntelliJ IDEA.
 * User: slemesle
 * Date: 20/11/2013
 * Time: 02:04
 * To change this template use File | Settings | File Templates.
 */
public class InOutType {

    private final TypeMirror in;
    private final TypeMirror out;
    private final boolean outPutAsParam;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InOutType inOutType = (InOutType) o;

        if (!MapperProcessor.types.isSameType(in, inOutType.in)) return false;
        if (!MapperProcessor.types.isSameType(out, inOutType.out)) return false;

        return inOutType.outPutAsParam == outPutAsParam;
    }

    @Override
    public int hashCode() {
        int result = in != null ? in.hashCode() : 0;
        result = 31 * result + (out != null ? out.hashCode() : 0);
        result = 31 * result + (outPutAsParam ? 1 : 0);
        return result;
    }

    @Override
    public String
    toString() {
        final StringBuilder sb = new StringBuilder("InOutType{");
        sb.append("in=").append(in);
        sb.append(", out=").append(out);
        sb.append(", outPutAsParam=").append(out);
        sb.append('}');
        return sb.toString();
    }

    public InOutType(TypeMirror in, TypeMirror out, boolean outPutAsParam) {
        this.in = in;
        this.out = out;
        this.outPutAsParam = outPutAsParam;

        if (in == null || out == null) {
            throw new IllegalArgumentException(String.format("in type %s and out type %s can not be null", in, out));
        }
    }

    public boolean areSamePrimitive() {
        return in.getKind().isPrimitive() && in.getKind() == out.getKind();
    }

    public boolean areDeclared() {
        return in.getKind() == TypeKind.DECLARED && out.getKind() == TypeKind.DECLARED;
    }

    public DeclaredType inAsDeclaredType() {
        return (DeclaredType) in;
    }

    public TypeElement inAsTypeElement() {
        return (TypeElement) inAsDeclaredType().asElement();
    }

    public boolean areEnums() {
        return inAsTypeElement().getKind() == ElementKind.ENUM && outAsTypeElement().getKind() == ElementKind.ENUM;
    }

    public boolean inIsArray() {
        return in.getKind() == TypeKind.ARRAY;
    }

    public ArrayType inAsArrayType() {
        return (ArrayType) in;
    }

    public boolean isInArrayComponentPrimitive() {
        return inAsArrayType().getComponentType().getKind().isPrimitive();
    }

    public TypeMirror inArrayComponentType() {
        return inAsArrayType().getComponentType();
    }

    public boolean isInArrayComponentDeclared() {
        return inArrayComponentType().getKind() == TypeKind.DECLARED;
    }

    public TypeMirror in() {
        return in;
    }

    public TypeMirror out() {
        return out;
    }

    public TypeMirror outArrayComponentType() {
        return outAsArrayType().getComponentType();
    }

    private ArrayType outAsArrayType() {
        return (ArrayType) out;
    }

    public boolean isInArrayComponentDeclaredOrArray() {
        return isInArrayComponentDeclared() || inArrayComponentType().getKind() == TypeKind.ARRAY;
    }

    public TypeElement outAsTypeElement() {
        return (TypeElement) ((DeclaredType) out).asElement();
    }

    public boolean differs() {
        return !in.toString().equals(out.toString());
    }

    public DeclaredType outAsDeclaredType() {
        return (DeclaredType) out;
    }

    public boolean inIsPrimitive() {
        return in.getKind().isPrimitive();
    }

    public boolean areSameDeclared() {
        return !differs() && areDeclared();
    }

    public boolean outIsDeclared() {
        return out.getKind() == TypeKind.DECLARED;
    }

    public boolean isPrimitiveToDeclared() {
        return inIsPrimitive() && outIsDeclared();
    }

    public TypeKind inKind() {
        return in.getKind();
    }

    public boolean isDeclaredToPrimitive() {
        return outIsPrimitive() && inIsDeclared();
    }

    protected boolean outIsPrimitive() {
        return out().getKind().isPrimitive();
    }

    private boolean inIsDeclared() {
        return in.getKind() == TypeKind.DECLARED;
    }

    public TypeKind outKind() {
        return out.getKind();
    }

    public boolean isOutPutAsParam() {
        return outPutAsParam;
    }
}
