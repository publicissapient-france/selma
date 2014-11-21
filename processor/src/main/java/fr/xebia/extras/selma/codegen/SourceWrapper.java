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

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;

/**
 * Created by slemesle on 21/11/14.
 */
public class SourceWrapper {
    public static final String WITH_SOURCES = "withSources";
    private final AnnotationWrapper mapperAnnotation;
    private final MapperGeneratorContext context;
    private final List<String> sources;
    private final String[] args;
    private final List<String> assigns;

    public SourceWrapper(AnnotationWrapper mapperAnnotation, MapperGeneratorContext context) {
        this.mapperAnnotation = mapperAnnotation;
        this.context = context;
        sources = mapperAnnotation.getAsStrings(WITH_SOURCES);


        int i = 0, iArg = 0;
        args = new String[sources.size() * 2];
        assigns = new ArrayList<String>();
        StringBuilder builder = new StringBuilder();
        for (String classe : sources) {
            builder.append(',');
            args[iArg] = classe.replace(".class", "");
            iArg++;
            args[iArg] = "_source" + i;
            iArg++;
            assigns.add(String.format("this.source%s = _source%s", i, i));
            builder.append("this.source").append(i);
            i++;
        }


        if (sources.size() > 0) {
            builder.deleteCharAt(0);
        }

        // newParams hold the parameters we pass to Pojo constructor
        context.setNewParams(builder.toString());
        context.setSourcesCount(sources.size());
    }

    public void emitFields(JavaWriter writer) throws IOException {
        int i = 0;
        for (String classe : sources) {
            writer.emitEmptyLine();
            writer.emitJavadoc("This field is used as source akka given as parameter to the Pojos constructors");
            writer.emitField(classe.replace(".class", ""), "source" + i, EnumSet.of(PRIVATE, FINAL));
            i++;
        }
    }

    public void emitAssigns(JavaWriter writer) throws IOException {
        for (String statement : assigns) {
            writer.emitStatement(statement);
        }
    }

    public String[] sourceConstructorArgs(){
        return args;
    }

}
