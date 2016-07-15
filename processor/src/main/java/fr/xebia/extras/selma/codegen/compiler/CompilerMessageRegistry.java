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
package fr.xebia.extras.selma.codegen.compiler;

import javax.lang.model.element.Element;
import javax.tools.Diagnostic;
import java.util.HashSet;

/**
 *
 */
public class CompilerMessageRegistry {

    private final HashSet<KindElementPair> messageSet;

    public CompilerMessageRegistry() {
        messageSet = new HashSet<KindElementPair>();
    }


    public boolean hasMessageFor(Diagnostic.Kind kind, Element element) {
        KindElementPair pair = new KindElementPair(kind, element);

        if (messageSet.contains(pair)) {
            return true;
        } else {
            messageSet.add(pair);
            return false;
        }
    }


    private class KindElementPair {

        final Diagnostic.Kind kind;
        final Element element;

        public KindElementPair(Diagnostic.Kind kind, Element element) {
            this.kind = kind;
            this.element = element;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            KindElementPair that = (KindElementPair) o;

            if (!element.equals(that.element)) return false;
            if (kind != that.kind) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = kind.hashCode();
            result = 31 * result + element.hashCode();
            return result;
        }
    }
}
