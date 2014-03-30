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

import fr.xebia.extras.selma.codegen.MapperProcessor;
import org.junit.Assert;

import javax.tools.*;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * User: slemesle
 * Date: 22/11/2013
 */
public class TestCompiler {


    private final static TestCompiler INSTANCE = new TestCompiler();
    private final String SRC_DIR;
    private final String OUT_DIR;
    private final String GEN_DIR;
    private final String TARGET_DIR;
    private final List<File> classPath;
    private final Map<String, TestCompilerEnv> environments;


    private TestCompiler() {

        String basePath = System.getProperty("user.dir");

        SRC_DIR = basePath + "/src/test/java";
        OUT_DIR = basePath + "/target/it/classes";
        GEN_DIR = basePath + "/target/it/generated-sources/mapping";
        TARGET_DIR = basePath + "/target/dependency";

        classPath = new ArrayList<File>();

        classPath.addAll(findJars());
        environments = new HashMap<String, TestCompilerEnv>();
        createOutputDirs();
    }

    public static TestCompiler getInstance() {
        return INSTANCE;
    }

    public synchronized TestCompilerEnv compileFor(Class<? extends IntegrationTestBase> aClass) throws Exception {
        synchronized (INSTANCE) {
            TestCompilerEnv res;


            if (environments.containsKey(aClass.getCanonicalName())) {
                return environments.get(aClass.getCanonicalName());
            }

            res = new TestCompilerEnv();
            res.init(aClass);
//            Thread.sleep(500);
            environments.put(aClass.getCanonicalName(), res);
            return res;
        }
    }

    private final Collection<? extends File> findJars() {
        List<File> res = new ArrayList<File>();

        File file = new File(TARGET_DIR);

        File[] files = file.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String name) {
                return name.contains(".jar");
            }
        });

        for (File jarFile : files) {
            res.add(jarFile);
        }
        return res;
    }

    private void processCompileAnnotation(Class<?> aClass, List<File> classes) {
        List<Compile> compiles = new ArrayList<Compile>();
        Compile compile = aClass.getAnnotation(Compile.class);
        if (compile != null) {
            compiles.add(compile);
        }
        compile = aClass.getSuperclass().getAnnotation(Compile.class);
        if (compile != null) {
            compiles.add(compile);
        }

        for (Compile res : compiles) {
            String[] packages = res.withPackage();
            for (int i = 0; i < packages.length; i++) {
                String aPackage = packages[i];
                classes.addAll(listFilesIn(new File(String.format("%s/%s", SRC_DIR, aPackage.replace('.', '/')))));
            }

            Class<?>[] classesTab = res.withClasses();
            for (int i = 0; i < classesTab.length; i++) {
                Class<?> zeClass = classesTab[i];
                classes.add((new File(String.format("%s/%s.java", SRC_DIR, zeClass.getCanonicalName().replace('.', '/')))));
            }
        }

    }

    private Collection<? extends File> listFilesIn(File file) {
        Collection<File> res = new ArrayList<File>();

        if (file.exists()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    res.addAll(listFilesIn(files[i]));
                } else {
                    res.add(files[i]);
                }
            }
        }
        return res;
    }

    private boolean compile(DiagnosticCollector<JavaFileObject> diagnostics, List<File> classes) throws IOException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
//        List<String> processorOptions = getProcessorOptions( testMethod );

        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, Charset.forName("UTF-8"));

        Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(classes);

        try {
            fileManager.setLocation(StandardLocation.CLASS_PATH, classPath);
            fileManager.setLocation(StandardLocation.CLASS_OUTPUT, Arrays.asList(new File(OUT_DIR)));
            fileManager.setLocation(StandardLocation.SOURCE_OUTPUT, Arrays.asList(new File(GEN_DIR)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        JavaCompiler.CompilationTask task = compiler.getTask(
                null,
                fileManager,
                diagnostics,
                /*processorOptions*/Arrays.asList("-source", "6", "-g"),
                null,
                compilationUnits
        );
        task.setProcessors(Arrays.asList(new MapperProcessor()));


        boolean res = task.call();

        for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
            System.out.println(diagnostic.toString());
        }

        fileManager.flush();

//        initiateClassLoader(fileManager);

        return res;
    }

    private void initiateClassLoader(StandardJavaFileManager fileManager) {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        if(contextClassLoader == null){
            contextClassLoader = getClass().getClassLoader();
        }
        if (!(contextClassLoader instanceof TestClassLoader)) {
            ClassLoader loader =  new TestClassLoader(contextClassLoader, fileManager.getClassLoader( StandardLocation.CLASS_OUTPUT));
            Thread.currentThread().setContextClassLoader(loader);
        }
    }

    private List<File> getSourceFiles(Class<?>... classes) {
        List<File> liste = new ArrayList<File>();
        for (Class<?> aClass : classes) {
            liste.add(new File(String.format("%s/%s.java", SRC_DIR, aClass.getCanonicalName().replace('.', '/'))));
        }
        return liste;
    }

    private void createOutputDirs() {
        File directory = new File(OUT_DIR);
        deleteDirectory(directory);
        directory.mkdirs();

        directory = new File(GEN_DIR);
        deleteDirectory(directory);
        directory.mkdirs();
    }

    private void deleteDirectory(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        path.delete();
    }

    class TestCompilerEnv {
        final AtomicBoolean initialized = new AtomicBoolean(false);
        DiagnosticCollector<JavaFileObject> diagnostics;
        boolean compilationResult;
        List<File> classes;
        boolean shouldFail;
        Class<?> aClass;

        private synchronized void init(Class<?> aClass) throws Exception {
            if (!initialized.get()) {
                this.aClass = aClass;

                classes = new ArrayList<File>();

                processCompileAnnotation(aClass, classes);

//                createOutputDirs();

                Thread.currentThread().setContextClassLoader(
                        new URLClassLoader(
                                new URL[]{new File(OUT_DIR + "/").toURI().toURL()},
                                Thread.currentThread().getContextClassLoader()
                        )
                );

                diagnostics = new DiagnosticCollector<JavaFileObject>();

                compilationResult = compile(diagnostics, classes);

                Compile compile = aClass.getAnnotation(Compile.class);
                shouldFail = compile.shouldFail();
                initialized.set(true);
            }
        }

        protected boolean compilationSuccess() {
            return compilationResult;
        }

        protected void assertCompilation() {
            Assert.assertTrue(String.format("Compilation of class %s result should be %s", aClass.getSimpleName(), !shouldFail), !shouldFail == compilationResult);
        }

        public DiagnosticCollector<JavaFileObject> diagnostics() {

            return diagnostics;
        }
    }
}
