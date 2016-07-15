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
package fr.xebia.extras.selma.it.utils;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.matchers.JUnitMatchers;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;
import java.io.File;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 */
@Compile(withPackage = {"fr.xebia.extras.selma.beans"})
public class IntegrationTestBase {
    private static TestCompiler testCompiler;
    private final AtomicBoolean initialized = new AtomicBoolean(false);
    private String SRC_DIR;
    private String OUT_DIR;
    private String GEN_DIR;
    private List<File> classPath;
    private List<File> classes;
    private String TARGET_DIR;
    private DiagnosticCollector<JavaFileObject> diagnostics;
    private boolean compilationResult;

    @BeforeClass
    public static final void beforeClass() {
        testCompiler = new TestCompiler();
    }

    @Before
    public void setup() throws Exception {

        testCompiler.compileFor(getClass());
        testCompiler.compileFor(getClass()).assertCompilation();
    }


    protected boolean compilationSuccess() throws Exception {
        return testCompiler.compileFor(getClass()).compilationSuccess();
    }


    protected DiagnosticCollector<JavaFileObject> getDiagnostics() throws Exception {

        return testCompiler.compileFor(getClass()).diagnostics();
    }

    protected int compilationErrorCount() throws Exception {
        int res = 0;
        DiagnosticCollector<JavaFileObject> diagnosticCollector = getDiagnostics();
        for (Diagnostic<? extends JavaFileObject> diagnostic : diagnosticCollector.getDiagnostics()) {
            if (diagnostic.getKind() == Diagnostic.Kind.ERROR) {
                res++;
            }
        }
        return res;
    }

    /**
     * Count warnings by ignoring -source 6 compiler warning
     *
     * @return
     * @throws Exception
     */
    protected int compilationWarningCount() throws Exception {
        int res = 0;
        DiagnosticCollector<JavaFileObject> diagnosticCollector = getDiagnostics();
        for (Diagnostic<? extends JavaFileObject> diagnostic : diagnosticCollector.getDiagnostics()) {
            if (diagnostic.getKind() == Diagnostic.Kind.WARNING && !diagnostic.toString().endsWith("with -source 1.6")) {
                res++;
            }
        }
        return res;
    }

    protected void assertCompilationError(Class<?> aClass, String signature, String message) throws Exception {
        assertCompilationKind(Diagnostic.Kind.ERROR, aClass, signature, message);
    }

    protected void assertCompilationWarning(Class<?> aClass, String signature, String message) throws Exception {
        assertCompilationKind(Diagnostic.Kind.WARNING, aClass, signature, message);
    }


    protected void assertCompilationKind(Diagnostic.Kind kind, Class<?> aClass, String signature, String message) throws Exception {
        DiagnosticCollector<JavaFileObject> dc = getDiagnostics();

        dc.getDiagnostics();
        Diagnostic<? extends JavaFileObject> res = null;
        for (Diagnostic<? extends JavaFileObject> diagnostic : dc.getDiagnostics()) {

            if (diagnostic.getKind() == kind) {
                String srcLine = diagnostic.toString();
                if (diagnostic.getSource() != null && diagnostic.getSource().getName().contains(aClass.getSimpleName())) {
                    if (srcLine.contains(signature) && srcLine.contains(message)) {
                        res = diagnostic;
                    } else if (srcLine.contains(message)) {
                        res = diagnostic;
                    }
                } else if (diagnostic.getSource() == null && srcLine.contains(message)) {
                    res = diagnostic;
                }
            }
        }

        org.junit.Assert.assertNotNull(res);
        org.junit.Assert.assertThat(res.getMessage(null), JUnitMatchers.containsString(message));

    }

}
