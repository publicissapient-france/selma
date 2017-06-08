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

package fr.xebia.extras.selma;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 *
 */
public class SelmaConstants {

    public static final String MAPPER_CLASS_SUFFIX = "SelmaGeneratedClass";
    public static final String IN_VAR = "in";
    public static final String OUT_VAR = "out";
    public static final String INSTANCE_CACHE = "instanceCache";

    public static final boolean DEFAULT_BOOLEAN = false;
    public static final byte DEFAULT_BYTE = 0;
    public static final short DEFAULT_SHORT = 0;
    public static final int DEFAULT_INT = 0;
    public static final long DEFAULT_LONG = 0;
    public static final char DEFAULT_CHAR = '\0';
    public static final float DEFAULT_FLOAT = 0;
    public static final double DEFAULT_DOUBLE = 0;

    private static final Properties props = new Properties();

    public static final String SELMA_GIT_HASH = loadGitHash();
    public static final String SELMA_GIT_DESC = loadGitDesc();
    public static final String SELMA_VERSION = loadProjectVersion();

    private static String loadProjectVersion() {
        if (props.isEmpty()) {
            loadProperties();
        }

        return props.getProperty("selma.version", "V??.??");
    }

    private static String loadGitDesc() {
        if (props.isEmpty()) {
            loadProperties();
        }

        return props.getProperty("selma.git.desc", "UNKOWN GIT HASH");
    }

    private static String loadGitHash() {
        if (props.isEmpty()) {
            loadProperties();
        }

        return props.getProperty("selma.git.commit-full", "UNKOWN GIT HASH");
    }


    private static final void loadProperties() {

        String propFileName = "selma.properties";

        try {
            InputStream inputStream = SelmaConstants.class.getClassLoader().getResourceAsStream(propFileName);
            if (inputStream != null) {
                props.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }


    }
}