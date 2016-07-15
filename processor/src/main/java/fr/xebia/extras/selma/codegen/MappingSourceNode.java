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

import com.squareup.javawriter.JavaWriter;
import fr.xebia.extras.selma.InstanceCache;
import fr.xebia.extras.selma.SelmaConstants;
import fr.xebia.extras.selma.SimpleInstanceCache;

import javax.lang.model.type.TypeMirror;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;

/**
 * Builds the mapping graph
 */
public abstract class MappingSourceNode {


    MappingSourceNode body;

    MappingSourceNode child;

    public static final MappingSourceNode blank() {
        return new MappingSourceNode() {
            @Override void writeNode(JavaWriter writer) throws IOException {
            }
        };

    }

    public static MappingSourceNode mapMethod(final MapperGeneratorContext context, final InOutType inOutType, final String name, final boolean override, final boolean isFinal) {

        return new MappingSourceNode() {
            @Override void writeNode(JavaWriter writer) throws IOException {
                List<String> parameters = new ArrayList<String>();
                parameters.add(inOutType.in().toString());
                parameters.add(SelmaConstants.IN_VAR);
                if (inOutType.isOutPutAsParam()) {
                    parameters.add(inOutType.out().toString());
                    parameters.add(SelmaConstants.OUT_VAR);
                }

                if (override) {
                    writer.emitAnnotation(Override.class);
                }

                if (context.getWrapper().isUseCyclicMapping()) {
                    // Method without instance cache : call the other method with a new InstanceCache as parameter
                    writer.emitJavadoc("Mapping method overridden by Selma");
                    writer.beginMethod(inOutType.out().toString(), name, isFinal ? EnumSet.of(PUBLIC, FINAL) : EnumSet.of(PUBLIC), parameters, null);
                    if (inOutType.isOutPutAsParam()) {
                        writer.emitStatement("return %s(in, out, new %s())", name, SimpleInstanceCache.class.getName());
                    } else {
                        writer.emitStatement("return %s(in, new %s())", name, SimpleInstanceCache.class.getName());
                    }
                    writer.endMethod();
                    writer.emitEmptyLine();


                    parameters.add(InstanceCache.class.getName());
                    parameters.add(SelmaConstants.INSTANCE_CACHE);
                }

                writer.beginMethod(inOutType.out().toString(), name, isFinal ? EnumSet.of(PUBLIC, FINAL) : EnumSet.of(PUBLIC), parameters, null);

                writeBody(writer);

                writer.emitStatement("return out");
                writer.endMethod();
                writer.emitEmptyLine();
            }
        };
    }

    public static MappingSourceNode controlInCache(final String field, final String outType) {
        return new MappingSourceNode() {
            @Override void writeNode(JavaWriter writer) throws IOException {
                writer.emitStatement("%s object = %s.get(%s)", outType, SelmaConstants.INSTANCE_CACHE, field);
                writer.beginControlFlow(String.format("if (object != null)"));
                writer.emitStatement("return object");
                writer.endControlFlow();
            }
        };
    }

    public static MappingSourceNode pushInCache() {
        return new MappingSourceNode() {
            @Override void writeNode(JavaWriter writer) throws IOException {
                writer.emitStatement("%s.push()", SelmaConstants.INSTANCE_CACHE);
            }
        };
    }

    public static MappingSourceNode popFromCache() {
        return new MappingSourceNode() {
            @Override void writeNode(JavaWriter writer) throws IOException {
                writer.emitStatement("%s.pop()", SelmaConstants.INSTANCE_CACHE);
            }
        };
    }

    public static MappingSourceNode controlNotNull(final String field, final boolean outPutAsParam) {
        return new MappingSourceNode() {
            @Override void writeNode(JavaWriter writer) throws IOException {
                writer.beginControlFlow(String.format("if (%s != null)", field));
                // body is Mandatory here
                writeBody(writer);
                if (outPutAsParam) {
                    writer.nextControlFlow("else");
                    writer.emitStatement("out = null");
                }
                writer.endControlFlow();
            }
        };
    }

    public static MappingSourceNode tryBlock() {
        return new MappingSourceNode() {
            @Override void writeNode(JavaWriter writer) throws IOException {
                writer.beginControlFlow("try");
                // body is Mandatory here
                writeBody(writer);
                writer.endControlFlow();
            }
        };
    }

    public static MappingSourceNode finallyBlock() {
        return new MappingSourceNode() {
            @Override void writeNode(JavaWriter writer) throws IOException {
                writer.beginControlFlow("finally");
                // body is Mandatory here
                writeBody(writer);
                writer.endControlFlow();
            }
        };
    }

    public static MappingSourceNode controlNull(final String field) {
        return new MappingSourceNode() {
            @Override void writeNode(JavaWriter writer) throws IOException {
                writer.beginControlFlow(String.format("if (%s == null)", field));
                // body is Mandatory here
                writeBody(writer);
                writer.endControlFlow();
            }
        };
    }

    public static MappingSourceNode controlNullElse() {
        return new MappingSourceNode() {
            @Override void writeNode(JavaWriter writer) throws IOException {
                writer.beginControlFlow("else");
                // body is Mandatory here
                writeBody(writer);
                writer.endControlFlow();
            }
        };
    }

    public static MappingSourceNode set(final String outField, final String inField) {
        return new MappingSourceNode() {
            @Override void writeNode(JavaWriter writer) throws IOException {
                writer.emitStatement("%s(%s)", outField, inField);
            }
        };
    }

    public static MappingSourceNode assign(final String outField, final String inField) {
        return new MappingSourceNode() {
            @Override void writeNode(JavaWriter writer) throws IOException {
                writer.emitStatement("%s = %s", outField, inField);
            }
        };
    }

    public static MappingSourceNode assignOutPrime() {
        return new MappingSourceNode() {
            @Override void writeNode(JavaWriter writer) throws IOException {
                writer.emitStatement("out = in");
            }
        };
    }

    public static MappingSourceNode assignOutToString() {
        return new MappingSourceNode() {
            @Override void writeNode(JavaWriter writer) throws IOException {
                writer.emitStatement("out = in + \"\"");
            }
        };
    }

    public static MappingSourceNode declareOut(final TypeMirror outType) {
        return new MappingSourceNode() {
            @Override
            void writeNode(JavaWriter writer) throws IOException {  // declaring out should support both primitive and declared default value (null / primitive)
                writer.emitStatement("%s out %s", outType, (outType.getKind().isPrimitive() ? "= fr.xebia.extras.selma.SelmaConstants.DEFAULT_" + outType.getKind() : "= null"));
            }
        };
    }

    public static MappingSourceNode notSupported(final String message) {
        return new MappingSourceNode() {
            @Override
            void writeNode(JavaWriter writer) throws IOException {
                writer.emitJavadoc("Throw UnsupportedOperationException because we failed to generate the mapping code:\n" + message);
                // new lines in message result in uncompilable code.
                writer.emitStatement("throw new UnsupportedOperationException(\"%s\")", message.replace("\n", " "));
            }
        };
    }

    public static MappingSourceNode mapArray(final String indexVar, final String inField) {
        return new MappingSourceNode() {
            @Override void writeNode(JavaWriter writer) throws IOException {
                writer.beginControlFlow(String.format("for (int %s = 0 ; %s < %s.length; %s++)", indexVar, indexVar, inField, indexVar));
                writeBody(writer);
                writer.endControlFlow();
            }
        };
    }

    public static MappingSourceNode mapArrayBis(final String indexVar, final String totalCount) {
        return new MappingSourceNode() {
            @Override void writeNode(JavaWriter writer) throws IOException {
                writer.beginControlFlow(String.format("for (int %s = 0 ; %s < %s; %s++)", indexVar, indexVar, totalCount, indexVar));
                writeBody(writer);
                writer.endControlFlow();
            }
        };
    }

    public static MappingSourceNode mapCollection(final String itemVar, final String in, final String inField) {
        return new MappingSourceNode() {
            @Override void writeNode(JavaWriter writer) throws IOException {
                writer.beginControlFlow(String.format("for (%s %s : %s)", in, itemVar, inField));
                writeBody(writer);
                writer.endControlFlow();
            }
        };
    }

    public static MappingSourceNode mapMap(final String itemVar, final String keyType, final String valueType, final String inField) {
        return new MappingSourceNode() {
            @Override void writeNode(JavaWriter writer) throws IOException {
                //for ( java.util.Map.Entry<java.lang.String,java.lang.String> _inEntry  : inType.entrySet())
                writer.beginControlFlow("for (java.util.Map.Entry<%s,%s> %s : %s.entrySet())", keyType, valueType, itemVar, inField);
                writeBody(writer);
                writer.endControlFlow();
            }
        };
    }

    public static MappingSourceNode mapEnumBlock(final String inField) {
        return new MappingSourceNode() {
            @Override void writeNode(JavaWriter writer) throws IOException {
                //   switch (inType) { ... }
                writer.beginControlFlow(String.format("switch (%s)", inField));
                writeBody(writer);
                writer.endControlFlow();
            }
        };
    }

    public static MappingSourceNode mapEnumCase(final String value) {
        return new MappingSourceNode() {
            @Override void writeNode(JavaWriter writer) throws IOException {
                   /*
                         case VAL_3 : {
                                ....
                                break;
                         }
                   */
                writer.beginControlFlow(String.format("case %s : ", value));
                writeBody(writer);
                writer.emitStatement("break");
                writer.endControlFlow();
            }
        };
    }

    public static MappingSourceNode mapDefaultCase() {
        return new MappingSourceNode() {
            @Override void writeNode(JavaWriter writer) throws IOException {
                   /*
                         default : {
                                ....
                                break;
                         }
                   */
                writer.beginControlFlow(" default : ");
                writeBody(writer);
                writer.emitStatement("break");
                writer.endControlFlow();
            }
        };
    }

    public static MappingSourceNode statement(final String statement) {
        return new MappingSourceNode() {
            @Override void writeNode(JavaWriter writer) throws IOException {
                   /*
                        statement;
                   */
                writer.emitStatement(statement);
            }
        };
    }

    public static MappingSourceNode instantiateOut(final boolean useCyclicMapping, final InOutType inOutType, final String params) {
        return new MappingSourceNode() {
            @Override void writeNode(JavaWriter writer) throws IOException {
                   /*
                        out = new X();
                   */
                if (inOutType.isOutPutAsParam()) {
                    writer.beginControlFlow("if (out == null)");
                    instantiate(inOutType, params, writer);
                    writer.endControlFlow();
                } else {
                    instantiate(inOutType, params, writer);
                }
            }

            private void instantiate(final InOutType inOutType, final String params, JavaWriter writer)
                    throws IOException {
                writer.emitStatement("%s = new %s(%s)", SelmaConstants.OUT_VAR, inOutType.out().toString(), params);
                if (useCyclicMapping) {
                    writer.emitStatement("%s.put(%s, %s)", SelmaConstants.INSTANCE_CACHE, SelmaConstants.IN_VAR, SelmaConstants.OUT_VAR);
                }
            }
        };
    }

    public static MappingSourceNode callStaticFactoryOut(final InOutType inOutType, final String factoryCall) {
        return new MappingSourceNode() {
            @Override void writeNode(JavaWriter writer) throws IOException {
                   /*
                        out = new X();
                   */
                if (inOutType.isOutPutAsParam()) {
                    writer.beginControlFlow("if (out == null)");
                    writer.emitStatement("out = %s()", factoryCall);
                    writer.endControlFlow();
                } else {
                    writer.emitStatement("out = %s()", factoryCall);
                }
            }
        };
    }

    public static MappingSourceNode callGenericFactoryOut(final InOutType inOutType, final String factoryCall) {
        return new MappingSourceNode() {
            @Override void writeNode(JavaWriter writer) throws IOException {
                   /*
                        out = new X();
                   */
                if (inOutType.isOutPutAsParam()) {
                    writer.beginControlFlow("if (out == null)");
                    writer.emitStatement("out = %s(%s.class)", factoryCall, inOutType.out());
                    writer.endControlFlow();
                } else {
                    writer.emitStatement("out = %s(%s.class)", factoryCall, inOutType.out());
                }
            }
        };
    }

    public static MappingSourceNode put(final String outCollection, final String itemVar) {
        return new MappingSourceNode() {
            @Override void writeNode(JavaWriter writer) throws IOException {
                   /*
                        out.%s().put(%s.getKey(), %s.getValue())
                   */
                writer.emitStatement("%s.put(%s.getKey(), %s.getValue())", outCollection, itemVar, itemVar);
            }
        };
    }

    public static MappingSourceNode arrayCopy(final String inGetterFor, final String outGetterFor) {
        return new MappingSourceNode() {
            @Override void writeNode(JavaWriter writer) throws IOException {
                   /*
                        System.arraycopy(inType.%s(), 0, out.%s(), 0, inType.%s().length)
                   */
                writer.emitStatement("System.arraycopy(%s, 0, %s, 0, %s.length)", inGetterFor, outGetterFor, inGetterFor);
            }
        };

    }

    abstract void writeNode(JavaWriter writer) throws IOException;

    public void write(JavaWriter writer) throws IOException {

        writeNode(writer);

        if (child != null) {
            child.write(writer);
        }
    }

    public void writeBody(JavaWriter writer) throws IOException {

        if (body != null) {
            body.write(writer);
        }

    }

    public MappingSourceNode lastChild() {
        MappingSourceNode ptr = this;
        while (ptr.child != null) {
            ptr = ptr.child;
        }
        return ptr;
    }

    public MappingSourceNode body(MappingSourceNode body) {
        if (this.body != null) {
            return child(body);
        }
        this.body = body;
        return body;
    }

    public MappingSourceNode child(MappingSourceNode child) {
        this.child = child;
        return child;
    }
}
