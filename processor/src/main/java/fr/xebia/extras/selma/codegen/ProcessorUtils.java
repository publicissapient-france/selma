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

import fr.xebia.extras.selma.SelmaConstants;

import javax.lang.model.element.Element;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.util.List;

/**
 * Created by slemesle on 17/04/2017.
 */
public class ProcessorUtils {

    public static final String getInVar(TypeMirror inTypeMirror) {

        StringBuilder builder = new StringBuilder(SelmaConstants.IN_VAR);
        if (inTypeMirror.getKind() == TypeKind.DECLARED) {
            List<? extends TypeMirror> typeArguments = ((DeclaredType) inTypeMirror).getTypeArguments();
            Element element = ((DeclaredType) inTypeMirror).asElement();
            String[] types = inTypeMirror.toString().replaceAll("<.*>", "").split("\\.");
            builder.append(types[types.length - 1]);
            for (TypeMirror type : typeArguments) {
                types = type.toString().split("\\.");
                builder.append(types[types.length - 1].replace(">", ""));
            }
        } else {
            String[] types = inTypeMirror.toString().split("\\.");
            builder.append(types[types.length - 1].replace("[]", "").replace(">", ""));
        }

        return builder.toString();
    }
}
