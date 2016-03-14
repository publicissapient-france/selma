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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Arrays.asList;

/**
 * Selma is the only class used to gain access to the Mappers implementations generated at compile time.
 * <p/>
 * It offers a Builder API to retrieve the Mapper you want :
 * <code>
 * // Without sources
 * Selma.builder(MapperInterface.class).build();
 * <p/>
 * // With sources
 * Selma.builder(MapperInterface.class).withSources(sourceInstance).build();
 * </code>
 *
 * <code>
 * // Without customMapper
 * Selma.builder(MapperInterface.class).withCustomMappers(customMappers).build();
 * <p/>
 * // With sources
 * Selma.builder(MapperInterface.class).withCustomMappers(customMappers).build();
 * </code>
 * <p/>
 * It also offers two simple static methods getMapper(MapperInterface.class) offering the same service level.
 * <p/>
 * Please notice that Selma holds a cache of the instantiated Mappers, and will maintain them as Singleton.
 * You can bypass the cache using the builder API and specifying disableCache() on it.
 * <p/>
 */
public class Selma {


    private static final Map<String, Object> mappers = new ConcurrentHashMap<String, Object>();
    public static final String SET_CUSTOM_MAPPER = "setCustomMapper";
    public static final String SET_FACTORY = "setFactory";

    /**
     * Return a builder style component to build the Mapper using this builder you can pass the sources, custom mappers and bypass the default cache registry.
     *
     * @param mapperClass The Mapper interface class
     * @return a new MapperBuilder used to instantiate the mapper class
     * @throws IllegalArgumentException If for some reason the Mapper class can not be loaded or instantiated
     */
    public static <T> MapperBuilder<T> builder(Class<T> mapperClass) {

        return new MapperBuilder<T>(mapperClass);
    }

    /**
     * Retrieve the generated Mapper for the corresponding interface in the classpath and instantiate it.
     *
     * @param mapperClass The Mapper interface class
     * @param <T>         The Mapper interface itself
     * @return A new Mapper instance or previously instantiated selma
     * @throws IllegalArgumentException If for some reason the Mapper class can not be loaded or instantiated
     */
    public static <T> T mapper(Class<T> mapperClass) throws IllegalArgumentException {

        return getMapper(mapperClass, null, null);
    }


    /**
     * Mapper Builder DSL for those who like it like that.
     *
     * @param mapperClass The Mapper interface class
     * @param <T>         The Mapper interface itself
     * @return Builder for Mapper
     */
    public static <T, V> T mapper(Class<T> mapperClass, V source) {

        return getMapper(mapperClass, source, null);
    }

    /**
     * Mapper Builder DSL for those who like it like that.
     *
     * @param mapperClass The Mapper interface class
     * @param <T>         The Mapper interface itself
     * @return Builder for Mapper
     */
    public static <T> T getMapper(Class<T> mapperClass) {

        return getMapper(mapperClass, null, null);
    }

    /**
     * Retrieve or build the mapper generated for the given interface
     *
     * @param mapperClass The Mapper interface class
     * @param <T>         The Mapper interface itself
     * @param <V>         The custom mapper instance to be used by this mapper
     * @return Builder for Mapper
     */
    public static <T, V> T getMapperWithCustom(Class<T> mapperClass, V custom) {

        return getMapper(mapperClass, null, custom);
    }

    /**
     * Retrieve the generated Mapper for the corresponding interface in the classpath.
     *
     * @param mapperClass The Mapper interface class
     * @param source      The Source to be passed to bean constructor or null for default
     * @param <T>         The Mapper interface itself
     * @return A new Mapper instance or previously instantiated selma
     * @throws IllegalArgumentException If for some reason the Mapper class can not be loaded or instantiated
     */
    public synchronized static <T, V, U> T getMapper(Class<T> mapperClass, V source, U customMapper) throws IllegalArgumentException {

        return getMapper(mapperClass, source == null ? null : Arrays.asList(source), customMapper == null ? null : Arrays.asList(customMapper));
    }


    private synchronized static <T, V, U> T getMapper(Class<T> mapperClass, List source, List customMappers) throws IllegalArgumentException {
        return getMapper(mapperClass, source, customMappers, true, null);
    }


    private synchronized static <T, V, U> T getMapper(Class<T> mapperClass,
                                                      List source,
                                                      List customMappers,
                                                      boolean useCache,
                                                      List factories) throws IllegalArgumentException {

        final String mapperKey = String.format("%s-%s-%s", mapperClass.getCanonicalName(), source, customMappers);

        Object mapperInstance = mappers.get(mapperKey);
        if ((mapperInstance == null || !mapperClass.isAssignableFrom(mapperInstance.getClass())) || !useCache) {
            // First look for the context class loader
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

            if (classLoader == null) {
                classLoader = Selma.class.getClassLoader();
            }

            String generatedClassName = mapperClass.getCanonicalName() + SelmaConstants.MAPPER_CLASS_SUFFIX;
            try {

                Class<T> mapperImpl = (Class<T>) classLoader.loadClass(generatedClassName);

                Constructor<T> retainedConstructor = null;
                if (mapperImpl.getConstructors().length != 1) {
                    throw new IllegalArgumentException("Mapper class " + mapperImpl.toString() + " should have 1 constructor !");
                }
                retainedConstructor = (Constructor<T>) mapperImpl.getConstructors()[0];
                if (source != null && retainedConstructor.getParameterTypes().length != source.size()) {
                    throw new IllegalArgumentException("Mapper class " + mapperImpl.toString() + " constructor needs a source " + retainedConstructor.toString());
                }

                if (source != null && source.size() > 0) {
                    mapperInstance = retainedConstructor.newInstance(source.toArray());
                } else {
                    mapperInstance = mapperImpl.newInstance();
                }

                if (factories != null){
                    for (Object factory : factories) {
                        if (factory == null) {
                            continue;
                        }

                        Class<?> factoryClass = factory.getClass();
                        Method method = null;
                        Class<?> classe = factoryClass;

                        while (method == null && !Object.class.equals(classe)) { // Iterate over super classes to find the matching custom mapper in case it is a subclass
                            Class<?>[] interfaces = classe.getInterfaces();
                            Class<?>[] classes = Arrays.copyOf(interfaces, interfaces.length + 1);
                            classes[interfaces.length] = classe;
                            method = getSetFactoryMethodFor(generatedClassName, mapperImpl, factoryClass, classes);
                            classe = classe.getSuperclass();
                        }
                        if (method != null) {
                            method.invoke(mapperInstance, factory);
                        } else {
                            throw new IllegalArgumentException("given a Factory of type " + factoryClass.getSimpleName() + " while setter does not exist, add it to @Mapper interface");
                        }
                    }
                }

                if (customMappers != null) {
                    for (Object customMapper : customMappers) {
                        if (customMapper == null) {
                            continue;
                        }

                        Class<?> customMapperClass = customMapper.getClass();
                        Method method = null;
                        Class<?> classe = customMapperClass;

                        while (method == null && !Object.class.equals(classe)) { // Iterate over super classes to find the matching custom mapper in case it is a subclass

                            Class<?>[] interfaces = classe.getInterfaces();
                            Class<?>[] classes = Arrays.copyOf(interfaces, interfaces.length + 1);
                            classes[interfaces.length] = classe;
                            method = getSetCustomMapperMethodFor(generatedClassName, mapperImpl, customMapperClass, classes);
                            classe = classe.getSuperclass();
                        }
                        if (method != null) {
                            method.invoke(mapperInstance, customMapper);
                        } else {
                            throw new IllegalArgumentException("given a CustomMapper of type " + customMapperClass.getSimpleName() + " while setter does not exist, add it to @Mapper interface");
                        }
                    }


                }
            } catch (InstantiationException e) {
                throw new IllegalArgumentException(String.format("Instantiation of Mapper class %s failed : %s", generatedClassName, e.getMessage()), e);
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException(String.format("Instantiation of Mapper class %s failed : %s", generatedClassName, e.getMessage()), e);
            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException(String.format("Unable to load generated mapper class %s failed : %s", generatedClassName, e.getMessage()), e);
            } catch (InvocationTargetException e) {
                throw new IllegalArgumentException(String.format("Instantiation of Mapper class %s failed (No constructor with %s parameter !) : %s", generatedClassName, source.getClass().getSimpleName(), e.getMessage()), e);
            }

            if (useCache) {
                mappers.put(mapperKey, mapperInstance);
            }
            return (T) mapperInstance;
        }

        return (T) mapperInstance;
    }

    private static <T> Method getSetCustomMapperMethodFor(String generatedClassName, Class<T> mapperImpl, Class<?> customMapperClass, Class<?> ... classes) {
        Method method = null;
        for (Class<?> classe : classes) {
            String setter = SET_CUSTOM_MAPPER + classe.getSimpleName();
            try {
                method = mapperImpl.getMethod(setter, classe);
                break;
            } catch (NoSuchMethodException e) {

            } catch (SecurityException e) {
                throw new SelmaException(e, "Setter for custom mapper %s named %s not accessible in %s", customMapperClass, setter, generatedClassName);
            }
        }
        return method;
    }

    private static <T> Method getSetFactoryMethodFor(String generatedClassName, Class<T> mapperImpl, Class<?> factoryClass, Class<?> ... classes) {
        Method method = null;
        for (Class<?> classe : classes) {
            String setter = SET_FACTORY + classe.getSimpleName();
            try {
                method = mapperImpl.getMethod(setter, classe);
                break;
            } catch (NoSuchMethodException e) {

            } catch (SecurityException e) {
                throw new SelmaException(e, "Setter for custom mapper %s named %s not accessible in %s", factoryClass, setter, generatedClassName);
            }
        }
        return method;
    }

    public static class MapperBuilder<T> {


        private final Class<T> mapperClass;
        private final List customMappers;
        private final List sources;
        private final boolean cache;
        private final List factories;

        private MapperBuilder(Class<T> mapperClass) {

            this.mapperClass = mapperClass;
            customMappers = null;
            sources = null;
            cache = true;
            factories= null;
        }

        private MapperBuilder(Class<T> mapperClass, List sources, List customMappers, boolean cache, List factories) {

            this.mapperClass = mapperClass;
            this.sources = sources;
            this.customMappers = customMappers;
            this.cache = cache;
            this.factories = factories;
        }

        public T build() {
            final T res;
            res = getMapper(mapperClass, sources, customMappers, cache, factories);
            return res;
        }

        public MapperBuilder<T> withCustom(Object... customMapper) {

            return new MapperBuilder<T>(mapperClass, sources, asList(customMapper), cache, factories);
        }

        public MapperBuilder<T> withSources(Object... dataSource) {

            return new MapperBuilder<T>(mapperClass, asList(dataSource), customMappers, cache, factories);
        }

        public MapperBuilder<T> disableCache() {

            return new MapperBuilder<T>(mapperClass, sources, customMappers, false, factories);
        }

        public MapperBuilder<T> withFactories(Object... factories) {
            return new MapperBuilder<T>(mapperClass, sources, customMappers, cache, asList(factories));
        }
    }
}
