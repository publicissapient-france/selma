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


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVisitor;
import javax.lang.model.util.Elements;
import javax.lang.model.util.SimpleTypeVisitor6;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

import fr.xebia.extras.selma.codegen.compiler.CompilerMessageRegistry;

/**
 * Source code generation context
 */
public class MapperGeneratorContext {


    private final ProcessingEnvironment processingEnv;
    private final CompilerMessageRegistry messageRegistry;
    private boolean ignoreNullValue=false;
    int depth = 0;

    final Elements elements;

    final Types type;

    // Handle nesting on source node
    LinkedList<StackElem> stack;

    // Maintain a registry of known mapping methods
    private HashMap<String, MappingMethod> mappingRegistry;

    // Handle method stack to build all mapping method not already built
    LinkedList<MappingMethod> methodStack;
    private String newParams;
    public final List<TypeElement> sources;

	private MapperWrapper wrapper;

    private final DeclaredType objectType;
    private final TypeVisitor<Boolean, MapperGeneratorContext> isValueTypeVisitor;

    public MapperGeneratorContext(ProcessingEnvironment processingEnvironment) {
        this.elements = processingEnvironment.getElementUtils();
        this.type = processingEnvironment.getTypeUtils();
        this.stack = new LinkedList<StackElem>();
        this.processingEnv = processingEnvironment;
        mappingRegistry = new HashMap<String, MappingMethod>();
        methodStack = new LinkedList<MappingMethod>();
        messageRegistry = new CompilerMessageRegistry();
        sources = new ArrayList<TypeElement>();

        objectType = type.getDeclaredType(elements.getTypeElement("java.lang.Object"));
        isValueTypeVisitor = new SimpleTypeVisitor6<Boolean, MapperGeneratorContext>() {
            private final DeclaredType enumType = getDeclaredType("java.lang.Enum");
            private final DeclaredType numberType = getDeclaredType("java.lang.Number");
            private final DeclaredType stringType = getDeclaredType("java.lang.String");
            private final DeclaredType booleanType = getDeclaredType("java.lang.Boolean");
            private final DeclaredType characterType = getDeclaredType("java.lang.Character");

            @Override
            public Boolean visitDeclared(DeclaredType t, MapperGeneratorContext ctx) {
                return ctx.types().isSubtype(t, enumType) ||
                        ctx.types().isSubtype(t, numberType) ||
                        ctx.types().isSameType(t, stringType) ||
                        ctx.types().isSameType(t, booleanType) ||
                        ctx.types().isSameType(t, characterType);
            }

            @Override
            public Boolean visitPrimitive(PrimitiveType t, MapperGeneratorContext ctx) {
                return true;
            }
        };
    }


    public void error(Element element, String message, Object... args) {

        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, String.format(message, args), messageRegistry.hasMessageFor(Diagnostic.Kind.ERROR, element) ? null : element);
    }

    public void pushStackForBody(MappingSourceNode node, SourceNodeVars vars) {
        this.stack.push(new StackElem(node, vars));
    }

    public void pushStackForChild(MappingSourceNode node, SourceNodeVars vars) {
        this.stack.push(new StackElem(node, vars).withChild(true));
    }

    public StackElem popStack() {
        if (stack.size() > 0) {
            return stack.pop();
        }
        return null;
    }

    public void warn(String s, ExecutableElement element) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, s, messageRegistry.hasMessageFor(Diagnostic.Kind.WARNING, element) ? null : element);
    }

    public boolean isIgnoreNullValue() {
        return ignoreNullValue;
    }

    public void setIgnoreNullValue(boolean ignoreNullValue) {
        this.ignoreNullValue = ignoreNullValue;
    }

    /**
     * Push a custom mapping to the registry for later use
     *
     * @param inOutType
     * @param name
     */
    public void mappingMethod(InOutType inOutType, String name) {

        getMappingMethod(inOutType, new MappingMethod(inOutType, name));
    }


    /**
     * return mappingMethod name and push it inType registry if not already done
     *
     * @param inOutType
     * @return
     */
    public String mappingMethod(InOutType inOutType) {

        MappingMethod method;
        method = getMappingMethod(inOutType, new MappingMethod(inOutType));

        return method.name();
    }

    private MappingMethod getMappingMethod(InOutType inOutType, MappingMethod mappingMethod) {
        InOutType key = inOutType;
        MappingMethod method = mappingMethod;
        if (mappingRegistry.containsKey(key.toString())) {
            return mappingRegistry.get(key.toString());
        }
        // Default enum mapper should always be considered immutable
        if (inOutType.areDeclared() && inOutType.areEnums() && inOutType.isOutPutAsParam()){
            key = new InOutType(inOutType, false);
            if (mappingRegistry.containsKey(key.toString())){
                return mappingRegistry.get(key.toString());
            }
            mappingMethod = new MappingMethod(mappingMethod, key);
        }

        // This is a new mapping method we should ensure it will be built and present in registry for later use
        methodStack.push(mappingMethod);
        mappingRegistry.put(key.toString(), mappingMethod);

        return mappingMethod;
    }

    public boolean hasMappingMethods() {
        return mappingRegistry.size() > 0;
    }

    public MappingMethod popMappingMethod() {
        MappingMethod mappingMethod = null;
        if (methodStack.size() > 0) {

            mappingMethod = methodStack.pop();

            while (methodStack.size() > 0 && mappingMethod.built) {
                mappingMethod = methodStack.pop();
            }
            if (mappingMethod.built) {
                mappingMethod = null;
            }
        }
        return mappingMethod;
    }

    public Elements elements() {
        return elements;
    }

    public Types types() {
        return type;
    }

    public void warn(Element element, String templateMessage, Object... args) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, String.format(templateMessage, args), messageRegistry.hasMessageFor(Diagnostic.Kind.WARNING, element) ? null : element);
    }

    public void setNewParams(String newParams) {
        this.newParams = newParams;
    }

    public String newParams() {
        return newParams;
    }

    public int getSourcesCount() {
        return sources.size();
    }

    public TypeElement getTypeElement(String classe) {
        return elements.getTypeElement(classe);
    }

    public DeclaredType getDeclaredType(String classe) {
        return type.getDeclaredType(getTypeElement(classe));
    }

    public boolean isValueType(TypeMirror typeMirror) {
        return typeMirror.accept(isValueTypeVisitor, this);
    }

    public TypeMirror getObjectType() {
        return objectType;
    }

    public void setSources(List<TypeElement> _sources) {
        sources.addAll(_sources);
    }

    public List<TypeElement> sources() {
        return sources;
    }

    class StackElem {
        final MappingSourceNode lastNode;
        private final SourceNodeVars vars;
        boolean child = false;

        private StackElem(MappingSourceNode lastNode, SourceNodeVars vars) {
            this.lastNode = lastNode;
            this.vars = vars;
        }

        public SourceNodeVars sourceNodeVars() {
            return vars;
        }

        public StackElem withChild(boolean isChild) {
            this.child = isChild;
            return this;
        }
    }

    public class MappingMethod {


        private final InOutType inOutType;
        private final String name;
        private boolean built;

        public MappingMethod(InOutType inOutType) {
            this.inOutType = inOutType;

            this.name = String.format("as%s", inOutType.outAsTypeElement().getSimpleName());
            this.built = false;
        }

        public MappingMethod(InOutType inOutType, String name) {
            this.inOutType = inOutType;
            this.name = name;
            built = true;
        }

        public MappingMethod(MappingMethod mappingMethod, InOutType key) {
            this.name = mappingMethod.name();
            this.built = mappingMethod.built;
            this.inOutType = key;
        }

        public String name() {
            return name;
        }

        public boolean isBuilt() {
            return built;
        }

        public void build() {
            built = true;
        }

        public String inType() {
            return inOutType.in().toString();
        }

        public String outType() {
            return inOutType.out().toString();
        }

        public TypeMirror in() {
            return inOutType.in();
        }

        public InOutType inOutType() {
            return inOutType;
        }
    }

	public TypeElement getBoxedClass(PrimitiveType type) {
		return processingEnv.getTypeUtils().boxedClass(type);
	}

	public MapperWrapper getWrapper() {
		return wrapper;
	}

	public void setWrapper(MapperWrapper wrapper) {
		this.wrapper = wrapper;
	}

}
