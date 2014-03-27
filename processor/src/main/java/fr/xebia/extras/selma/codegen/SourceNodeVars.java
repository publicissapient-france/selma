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

import javax.lang.model.type.TypeMirror;

/**
 * Created with IntelliJ IDEA.
 * User: slemesle
 * Date: 20/11/2013
 * Time: 02:10
 * To change this template use File | Settings | File Templates.
 */
public class SourceNodeVars {

    String field;

    BeanWrapper inBean;

    BeanWrapper outBean;

    String inField;
    String outField;

    InOutType inOutType;

    boolean assign = false;
    private String inFieldPrefix;
    private byte ptr = 'a';

    public SourceNodeVars(String inF, String outF, BeanWrapper inBean, BeanWrapper outBean) {
        this.field = inF;
        this.inBean = inBean;
        this.outBean = outBean;
        inField = (inBean == null ? "in" : inBean.getInGetterFor(inF));
        outField = (outBean == null ? "out" : outBean.getOutSetterPathFor(outF));
        inFieldPrefix = "";
    }

    public SourceNodeVars(String field, BeanWrapper inBean, BeanWrapper outBean) {
        this.field = field;
        this.inBean = inBean;
        this.outBean = outBean;
        inField = (inBean == null ? "in" : inBean.getInGetterFor(field));
        outField = (outBean == null ? "out" : outBean.getOutSetterPathFor(field));
        inFieldPrefix = "";
    }

    public SourceNodeVars() {
        this.field = null;
        this.inBean = null;
        this.outBean = null;
        inField = "in";
        outField = "out";
        inFieldPrefix = "";
    }

    public SourceNodeVars(String inField) {
        this.field = inField;
    }

    public SourceNodeVars withInField(String _inField) {
        inField = _inField;
        return this;
    }

    public SourceNodeVars withOutField(String _outField) {
        outField = _outField;
        return this;
    }

    public SourceNodeVars withInOutType(InOutType inOutType) {
        this.inOutType = inOutType;
        return this;
    }

    public SourceNodeVars withAssign(boolean assign) {
        this.assign = assign;
        return this;
    }

    public String inGetter() {
        return inField;
    }

    public String outType() {
        return outBean.getTypeFor(field).toString();
    }

    public String outSetterPath() {
        return outField;
    }

    public String outSetter() {
        return outField + (inBean == null ? "" : "()");
    }

    public MappingSourceNode setOrAssign(String value) {

        String formattedValue = inFieldPrefix + String.format(value, inGetter());

        return (assign ? MappingSourceNode.assign(outField, formattedValue) : MappingSourceNode.set(outField, formattedValue));
    }

    public String itemVar() {
        return String.format("%s%sItem", (char)ptr, (field == null ? "out" : field));
    }


    public String itemEntry() {
        return String.format("%s%sEntry", (char)ptr, (field == null ? "out" : field));
    }

    public String indexVar() {
        return indexVar((char)ptr);
    }

    public String indexVar(char indexChar) {
        return String.format("%c%sIndex", indexChar, (field == null ? "out" : field));
    }

    public boolean inTypeIsPrime() {
        return inOutType.inIsPrimitive();
    }


    public String tmpVar(String suffix) {
        return String.format("%s%sTmp%s", (char) ptr, (field == null ? "out" : field), suffix);
    }

    public String totalCountVar() {
        return String.format("%s%sTotalCount", (char) ptr, (field == null ? "out" : field));
    }

    public SourceNodeVars withInFieldPrefix(String inFieldPrefix) {
        this.inFieldPrefix = inFieldPrefix;
        return this;
    }

    public boolean isOutPrimitive() {
        return inOutType.outIsPrimitive();
    }

    public char nextPtr() {
        return (char)(ptr+1);
    }

    public SourceNodeVars withIndexPtr(char _ptr) {
        this.ptr = (byte) _ptr;
        return this;
    }
}
