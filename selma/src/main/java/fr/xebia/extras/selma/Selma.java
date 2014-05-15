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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Selma is the only class used to gain access to the Mappers implementations generated at compile time.
 *
 * It offers a Builder API to retrieve the Mapper you want :
 * <code>
 *      // Without factory
 *     Selma.mapper(MapperInterface.class).build();
 *
 *     // With factory
 *     Selma.mapper(MapperInterface.class).withFactory(factoryInstance).build();
 * </code>
 *
 * It also offers two simple static methods getMapper(MapperInterface.class) offering the same service level.
 *
 * Please notice that Selma holds a cache of the instantiated Mappers, and will maintain them as Singleton.
 *
 * TODO Provide a nicer way of building maooer
 *
 */
public class Selma {


    private static final Map<String, Object> mappers = new ConcurrentHashMap<String, Object>();

    /**
     * Retrieve the generated Mapper for the corresponding interface in the classpath and instantiate it with default factory.
     *
     * @param mapperClass   The Mapper interface class
     * @param <T>           The Mapper interface itself
     * @return              A new Mapper instance or previously instantiated selma
     *
     * @throws IllegalArgumentException If for some reason the Mapper class can not be loaded or instantiated
     */
    public static <T> T mapper(Class<T> mapperClass) throws IllegalArgumentException {
         return getMapper(mapperClass, null, null);
    }

    /**
     * Mapper Builder DSL for those who like it like that.
     * @param mapperClass   The Mapper interface class
     * @param <T>           The Mapper interface itself
     * @return              Builder for Mapper
     */
    public static <T,V> T mapper(Class<T> mapperClass, V source) {

        return getMapper(mapperClass,source, null);
    }

    /**
     * Mapper Builder DSL for those who like it like that.
     * @param mapperClass   The Mapper interface class
     * @param <T>           The Mapper interface itself
     * @return              Builder for Mapper
     */
    public static <T> T getMapper(Class<T> mapperClass) {

        return getMapper(mapperClass,null, null);
    }

    /**
     * Retrieve or build the mapper generated for the given interface
     * @param mapperClass   The Mapper interface class
     * @param <T>           The Mapper interface itself
     * @param <V>           The custom mapper instance to be used by this mapper
     * @return              Builder for Mapper
     */
    public static <T,V> T getMapperWithCustom(Class<T> mapperClass, V custom) {

        return getMapper(mapperClass,null, custom);
    }

    /**
     * Retrieve the generated Mapper for the corresponding interface in the classpath.
     *
     * @param mapperClass   The Mapper interface class
     * @param source        The Source to be passed to bean constructor or null for default
     * @param <T>           The Mapper interface itself
     * @return              A new Mapper instance or previously instantiated selma
     *
     * @throws IllegalArgumentException If for some reason the Mapper class can not be loaded or instantiated
     */
    public synchronized static <T,V,U> T getMapper(Class<T> mapperClass, V source, U customMapper ) throws IllegalArgumentException {

        final String mapperKey = String.format("%s-%s-%s", mapperClass.getCanonicalName(), source, customMapper);

                    if (!mappers.containsKey(mapperKey)) {
                        // First look for the context class loader
                        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

                        if ( classLoader == null ) {
                            classLoader = Selma.class.getClassLoader();
                        }

                        @SuppressWarnings("unchecked")
                        T mapperInstance = null;
                        try {

                            Class<T> mapperImpl  = (Class<T>) classLoader.loadClass(mapperClass.getCanonicalName() + SelmaConstants.MAPPER_CLASS_SUFFIX);
                            if(source != null){
                                mapperInstance =  mapperImpl.getDeclaredConstructor(source.getClass()).newInstance(source);
                            } else {
                                mapperInstance = mapperImpl.newInstance();
                             }

                            if (customMapper != null) {
                                Class<?> customMapperClass = customMapper.getClass();
                                Method method = null;
                                try {
                                    method = mapperImpl.getMethod("setCustomMapper" + customMapperClass.getSimpleName(), customMapperClass);
                                } catch (NoSuchMethodException e) {
                                    e.printStackTrace();
                                } catch (SecurityException e) {
                                    e.printStackTrace();
                                }
                                Class<?> classe = customMapperClass;

                                while (method == null && !Object.class.equals(classe)){ // Iterate over super classes to find the matching custom mapper in case it is a subclass
                                    classe = classe.getSuperclass();

                                    try {
                                        method = mapperImpl.getMethod("setCustomMapper" + classe.getSimpleName(), classe);
                                    } catch (NoSuchMethodException e) {
                                        e.printStackTrace();
                                    } catch (SecurityException e) {
                                        e.printStackTrace();
                                    }
                                }
                                if (method != null){
                                    method.invoke(mapperInstance, customMapper);
                                } else {
                                    throw new IllegalArgumentException("given a CustomMapper of type " + customMapperClass.getSimpleName()+ " while setter does not exist please fix it");
                                }
                            }
            } catch (InstantiationException e) {
                throw new IllegalArgumentException(String.format("Instantiation of Mapper class %s failed : %s", mapperClass.getName(), e.getMessage()), e);
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException(String.format("Instantiation of Mapper class %s failed : %s", mapperClass.getName(), e.getMessage()), e);
            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException(String.format("Instantiation of Mapper class %s failed : %s", mapperClass.getName(), e.toString()), e);
            } catch (NoSuchMethodException e) {
                throw new IllegalArgumentException(String.format("Instantiation of Mapper class %s failed (No constructor with %s parameter !) : %s", mapperClass.getName(), source.getClass().getSimpleName() ,e.getMessage()), e);
            } catch (InvocationTargetException e) {
                throw new IllegalArgumentException(String.format("Instantiation of Mapper class %s failed (No constructor with %s parameter !) : %s", mapperClass.getName(), source.getClass().getSimpleName(), e.getMessage()), e);
            }

            mappers.put(mapperKey, mapperInstance);
            return mapperInstance;
        }

        return (T) mappers.get(mapperKey);
    }

}
