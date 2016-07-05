/*
 * Copyright 2005-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dozer.util;

import static org.dozer.util.DozerConstants.BASE_CLASS;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.lang.StringUtils;
import org.dozer.CustomConverter;
import org.dozer.MappingException;
import org.dozer.cache.Cache;
import org.dozer.cache.CacheKeyFactory;
import org.dozer.classmap.ClassMap;
import org.dozer.classmap.Configuration;
import org.dozer.classmap.CopyByReferenceContainer;
import org.dozer.classmap.DozerClass;
import org.dozer.config.BeanContainer;
import org.dozer.converters.CustomConverterContainer;
import org.dozer.converters.CustomConverterDescription;
import org.dozer.converters.InstanceCustomConverterDescription;
import org.dozer.converters.JavaClassCustomConverterDescription;
import org.dozer.fieldmap.DozerField;
import org.dozer.fieldmap.FieldMap;
import org.dozer.fieldmap.FieldMapUtils;
import org.dozer.fieldmap.MultiFieldsExcludeFieldMap;
import org.dozer.fieldmap.MultiSourceFieldMap;
import org.dozer.propertydescriptor.DozerPropertyDescriptor;

/**
 * Internal class that provides various mapping utilities used throughout the
 * code base. Only intended for internal use.
 * 
 * @author garsombke.franz
 * @author sullins.ben
 * @author tierney.matt
 * 
 */
public final class MappingUtils {

    private MappingUtils() {
    }

    public static String getClassNameWithoutPackage(Class<?> clazz) { // TODO
        // Replace
        // with
        // Apache
        // implementation
        Package pckage = clazz.getPackage();
        int pckageIndex = 0;
        if (pckage != null) {
            pckageIndex = pckage.getName().length() + 1;
        }
        return clazz.getName().substring(pckageIndex);
    }

    public static boolean isSupportedCollection(Class<?> aClass) {
        return CollectionUtils.isCollection(aClass) || CollectionUtils.isArray(aClass);
    }

    public static Class<?> getSupportedCollectionEntryType(Class<?> collectionClass) {
        Class<?> entryType = null;
        if (collectionClass.isArray()) {
            entryType = collectionClass.getComponentType();
        } else if (Collection.class.isAssignableFrom(collectionClass)) {
            Class<?> genericType = ReflectionUtils.determineGenericsType(collectionClass);
            if (genericType != null) {
                entryType = genericType;
            }
        }

        return entryType;
    }

    public static Class<?> getSupportedCollectionEntryType(DozerPropertyDescriptor pd) {
        Class<?> entryType = pd.genericType();
        if (entryType == null) {
            entryType = getSupportedCollectionEntryType(pd.getPropertyType());
        }

        return entryType;
    }

    public static boolean isSupportedMap(Class<?> aClass) {
        return Map.class.isAssignableFrom(aClass);
    }


    public static void throwMappingException(Throwable e) throws MappingException {
        if (e instanceof MappingException) {
            // in this case we do not want to re-wrap an existing mapping
            // exception
            throw (MappingException) e;
        } else if (e instanceof RuntimeException) {
            // feature request 1561837. Dont wrap any runtime exceptions in a
            // MappingException
            throw (RuntimeException) e;
        } else {
            throw new MappingException(e);
        }
    }

    public static void throwMappingException(String msg) throws MappingException {
        throw new MappingException(msg);
    }

    public static void throwMappingException(String msg, Throwable cause) throws MappingException {
        throw new MappingException(msg, cause);
    }

    public static boolean isBlankOrNull(String value) {
        return value == null || value.trim().length() < 1;
    }

    public static Throwable getRootCause(Throwable ex) {
        Throwable rootCause = ex;
        while (rootCause.getCause() != null) {
            rootCause = rootCause.getCause();
        }
        return rootCause;
    }

    public static String getMappedParentFieldKey(Object destObj, FieldMap destFieldMap) {
        StringBuilder buf = new StringBuilder(100);
        // TODO Use IdentityHashMap
        // instead of String
        // concatenation
        buf.append(System.identityHashCode(destObj));
        buf.append(destFieldMap.getDestFieldName());
        if (destFieldMap.getDestFieldKey() != null) {
            buf.append("[").append(destFieldMap.getDestFieldKey()).append("]");
        }
        return buf.toString();
    }

    public static CustomConverter findCustomConverterByClass(Class<?> customConverterClass,
            List<CustomConverter> externalConverters) {

        CustomConverter converterInstance = null;
        // search among injected customconverters for a match
        if (externalConverters != null) {
            for (CustomConverter customConverterObject : externalConverters) {
                if (customConverterObject.getClass().isAssignableFrom(customConverterClass)) {
                    // we have a match
                    converterInstance = customConverterObject;
                }
            }
        }
        // if converter object instances were not injected, then create new
        // instance
        // of the converter for each conversion
        // TODO : Should we really create it each time?
        if (converterInstance == null) {
            converterInstance = (CustomConverter) ReflectionUtils.newInstance(customConverterClass);
        }

        return converterInstance;
    }

    /**
     * Finds custom converter for specified classes. The implementation of this
     * method finds converters in the following order:<br/>
     * <ol>
     * <li>looks up converter in cache;</li>
     * <li>looks up converter in defined converters container</li>
     * <li>looks up converter in defined converters container</li>
     * <li>looks up converter among external converters</li>
     * 
     * If appropriate converter not found <code>null</code> will be returned.
     * 
     * @param cache
     * @param externalConverters
     * @param customConverterContainer
     * @param srcClass
     * @param destClass
     * @return
     */
    public static CustomConverter findCustomConverter(Cache cache,
            List<CustomConverter> externalConverters,
            CustomConverterContainer customConverterContainer,
            Class<?> srcClass,
            Class<?> destClass) {

        // check that converters container is defined
        if (customConverterContainer == null) {
            return null;
        }

        // Check cache first
        Object cacheKey = CacheKeyFactory.createKey(destClass, srcClass);
        if (cache.containsKey(cacheKey)) {
            return (CustomConverter) cache.get(cacheKey);
        }

        // check converters container
        CustomConverter converterInstance = null;
        CustomConverterDescription description = customConverterContainer.getCustomConverter(srcClass, destClass);
        if (description != null) {
            if (description instanceof InstanceCustomConverterDescription) {
                converterInstance = ((InstanceCustomConverterDescription) description).getInstance();
            } else {
                Class<?> customConverterClass = ((JavaClassCustomConverterDescription) description).getType();
                converterInstance = findCustomConverterByClass(customConverterClass, externalConverters);
            }
        }

        // put appropriate info into cache
        cache.put(cacheKey, converterInstance);

        return converterInstance;
    }

    public static CustomConverter determineCustomConverter(FieldMap fieldMap,
            Cache converterByDestTypeCache,
            List<CustomConverter> customConverterObjects,
            CustomConverterContainer customConverterContainer,
            Class<?> srcClass,
            Class<?> destClass) {

        // check that converters container is defined
        if (customConverterContainer == null) {
            return null;
        }

        Class<?> destType = destClass;

        // This method is messy. Just trying to isolate the junk into this one
        // method instead of spread across the mapping
        // processor until a better solution can be put into place
        // For indexed mapping, need to use the actual class at index to
        // determine the custom converter.
        if (fieldMap != null && fieldMap.isDestFieldIndexed()) {
            if (destClass.isArray()) {
                destType = destClass.getComponentType();
            } else if (destClass.isAssignableFrom(Collection.class) && fieldMap.getDestHintContainer() != null && !fieldMap.getDestHintContainer()
                .hasMoreThanOneHint()) {
                // use hint when trying to find a custom converter
                destType = fieldMap.getDestHintContainer().getHint();
            }
        }

        return findCustomConverter(converterByDestTypeCache,
            customConverterObjects,
            customConverterContainer,
            srcClass,
            destType);
    }

    public static void reverseFields(FieldMap source, FieldMap reversed) {

        // in case of multi-source field mapping we should use custom
        // implementation of exclude field map
        //
        if (source instanceof MultiSourceFieldMap) {
            ((MultiFieldsExcludeFieldMap) reversed).setDest(FieldMapUtils.getCopy(((MultiSourceFieldMap) source).getSrc()));
            ((MultiFieldsExcludeFieldMap) reversed).setSrc(Arrays.asList(source.getDestFieldCopy()));
        } else {
            DozerField destField = source.getSrcFieldCopy();
            DozerField sourceField = source.getDestFieldCopy();
            reversed.setDestField(destField);
            reversed.setSrcField(sourceField);
            reversed.setSrcHintContainer(source.getDestHintContainer());
            reversed.setDestHintContainer(source.getSrcHintContainer());
            reversed.setSrcDeepIndexHintContainer(source.getDestDeepIndexHintContainer());
            reversed.setDestDeepIndexHintContainer(source.getSrcDeepIndexHintContainer());
        }

        reversed.setCustomConverter(source.getCustomConverter());
        reversed.setCustomConverterId(source.getCustomConverterId());
        reversed.setMapId(source.getMapId());
        reversed.setRelationshipType(source.getRelationshipType());
        reversed.setRemoveOrphans(source.isRemoveOrphans());
    }

    public static void reverseFields(ClassMap source, ClassMap destination) {
        // reverse the fields
//        destination.setSrcClass(new DozerClass(source.getDestClassName(),
//            source.getDestClassToMap(),
//            source.getDestClassBeanFactory(),
//            source.getDestClassBeanFactoryId(),
//            source.getDestClassMapGetMethod(),
//            source.getDestClassMapSetMethod(),
//            source.isDestMapNull(),
//            source.isDestMapEmptyString()));
//        destination.setDestClass(new DozerClass(source.getSrcClassName(),
//            source.getSrcClassToMap(),
//            source.getSrcClassBeanFactory(),
//            source.getSrcClassBeanFactoryId(),
//            source.getSrcClassMapGetMethod(),
//            source.getSrcClassMapSetMethod(),
//            source.isSrcMapNull(),
//            source.isSrcMapEmptyString()));
        destination.setSrcClass(new DozerClass(source.getDestClassName(), source.getDestClassToMap(), source.getDestClassBeanFactory(),
            source.getDestClassBeanFactoryId(), source.getDestClassMapGetMethod(), source.getDestClassMapSetMethod(),
                source.getDestClass().getCreateMethod(),
                source.isDestMapNull(), source.isDestMapEmptyString(), source.getDestClass().isAccesible()));
        destination.setDestClass(new DozerClass(source.getSrcClassName(), source.getSrcClassToMap(), source.getSrcClassBeanFactory(),
            source.getSrcClassBeanFactoryId(), source.getSrcClassMapGetMethod(), source.getSrcClassMapSetMethod(),
                source.getSrcClass().getCreateMethod(),
                source.isSrcMapNull(), source.isSrcMapEmptyString(), source.getSrcClass().isAccesible()));

        destination.setType(source.getType());
        destination.setWildcard(source.isWildcard());
        destination.setTrimStrings(source.isTrimStrings());
        destination.setDateFormat(source.getDateFormat());
        destination.setRelationshipType(source.getRelationshipType());
        destination.setStopOnErrors(source.isStopOnErrors());
        destination.setAllowedExceptions(source.getAllowedExceptions());
        destination.setSrcClassCreateMethod(source.getDestClassCreateMethod());
        destination.setDestClassCreateMethod(source.getSrcClassCreateMethod());
        if (StringUtils.isNotEmpty(source.getMapId())) {
            destination.setMapId(source.getMapId());
        }
    }

    public static void applyGlobalCopyByReference(Configuration globalConfig, FieldMap fieldMap, ClassMap classMap) {
        CopyByReferenceContainer copyByReferenceContainer = globalConfig.getCopyByReferences();
        String destFieldTypeName = null;
        Class<?> clazz = fieldMap.getDestFieldType(classMap.getDestClassToMap());
        if (clazz != null) {
            destFieldTypeName = clazz.getName();
        }
        if (copyByReferenceContainer.contains(destFieldTypeName) && !fieldMap.isCopyByReferenceOveridden()) {
            fieldMap.setCopyByReference(true);
        }
    }

    public static Class<?> loadClass(String name) {
        BeanContainer container = BeanContainer.getInstance();
        DozerClassLoader loader = container.getClassLoader();
        return loader.loadClass(name);
    }

    public static Class<?> getRealClass(Class<?> clazz) {
        BeanContainer container = BeanContainer.getInstance();
        DozerProxyResolver proxyResolver = container.getProxyResolver();
        return proxyResolver.getRealClass(clazz);
    }

    public static <T> T deProxy(T object) {
        BeanContainer container = BeanContainer.getInstance();
        DozerProxyResolver proxyResolver = container.getProxyResolver();
        return proxyResolver.unenhanceObject(object);
    }

    public static boolean isProxy(Class<?> clazz) {
        if (clazz.isInterface()) {
            return false;
        }
        String className = clazz.getName();
        return className.contains(DozerConstants.CGLIB_ID) || className.startsWith(DozerConstants.JAVASSIST_PACKAGE) || className.contains(DozerConstants.JAVASSIST_NAME);
    }

    /**
     * Gets value of object using xpath expression.
     * 
     * @param object source object
     * @param index xpath expression
     * @return obtained value
     */
    public static Object getXPathIndexedValue(Object object, String index) {
        JXPathContext context = JXPathContext.newContext(object);
        context.setLenient(true);
        return context.getValue(index);
    }

    /**
     * Gets element from collection by index. If collection is <code>null</code>
     * - <code>null</code> will be returned, if index value is <code>-1</code> -
     * element with
     * <code>CollectionUtils.getLengthOfCollection(collection)</code> index
     * value will be returned.
     * 
     * @param collection collection object
     * @param index index value
     * @return collection element
     */
    public static Object getCollectionIndexedValue(Object collection, int index) {
        if (collection == null) {
            return null;
        }

        Object result = null;
        int collectionIndex = index;

        if (collectionIndex == -1) {
            collectionIndex = CollectionUtils.getLengthOfCollection(collection);
        }

        if (collection instanceof Object[]) {
            Object[] x = (Object[]) collection;
            if (collectionIndex < x.length) {
                return x[collectionIndex];
            }
        } else if (collection instanceof Collection) {
            Collection<?> x = (Collection<?>) collection;
            if (collectionIndex < x.size()) {
                Iterator<?> iter = x.iterator();
                for (int i = 0; i < collectionIndex; i++) {
                    iter.next();
                }
                result = iter.next();
            }
        }
        return result;
    }

    /**
     * Checks that input string is integer value.
     * 
     * @param index string value
     * @return <code>true</code> if input string is integer value;
     *         <code>false</code> - otherwise
     */
    public static boolean isSimpleCollectionIndex(String index) {
        try {
            Integer.parseInt(index);
            return true;
        } catch (NumberFormatException e) {
            // ignore
        }

        return false;
    }

    /**
     * Parses index string into integer value.
     * 
     * @param index index string
     * @return integer value
     */
    public static int getCollectionIndex(String index) {
        int intValue = Integer.parseInt(index);

        if (intValue < 1) {
            return -1;
        }

        return intValue - 1;
    }

    public static Object prepareIndexedCollection(Class<?> collectionType,
            Object existingCollection,
            Object collectionEntry,
            int index) {
        Object result = null;
        if (collectionType.isArray()) {
            result = prepareIndexedArray(collectionType, existingCollection, collectionEntry, index);
        } else if (Collection.class.isAssignableFrom(collectionType)) {
            result = prepareIndexedCollectionType(collectionType, existingCollection, collectionEntry, index);
        } else {
            throwMappingException("Only types java.lang.Object[] and java.util.Collection are supported for indexed properties.");
        }

        return result;
    }

    public static boolean isDeepMapping(String mapping) {
        return mapping != null && mapping.contains(DozerConstants.DEEP_FIELD_DELIMITER);
    }

    @SuppressWarnings("unchecked")
    private static <T> T[] prepareIndexedArray(Class<T> collectionType,
            Object existingCollection,
            Object collectionEntry,
            int index) {
        T[] result;

        if (existingCollection == null) {
            result = (T[]) Array.newInstance(collectionType.getComponentType(), index + 1);
        } else {
            int originalLenth = ((Object[]) existingCollection).length;
            result = (T[]) Array.newInstance(collectionType.getComponentType(), Math.max(index + 1, originalLenth));
            System.arraycopy(existingCollection, 0, result, 0, originalLenth);
        }
        result[index] = (T) collectionEntry;
        return result;
    }

    @SuppressWarnings("unchecked")
    private static Collection<?> prepareIndexedCollectionType(Class<?> collectionType,
            Object existingCollection,
            Object collectionEntry,
            int index) {
        Collection result = null;
        // Instantiation of the new Collection: can be interface or
        // implementation class
        if (collectionType.isInterface()) {
            if (collectionType.equals(Set.class)) {
                result = new HashSet();
            } else if (collectionType.equals(List.class)) {
                result = new ArrayList();
            } else {
                throwMappingException("Only interface types java.util.Set and java.util.List are supported for java.util.Collection type indexed properties.");
            }
        } else {
            // It is an implementation class of Collection
            try {
                result = (Collection) collectionType.newInstance();
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        // Fill in old values in new Collection
        if (existingCollection != null) {
            result.addAll((Collection) existingCollection);
        }

        // Add the new value:
        // For an ordered Collection
        if (result instanceof List) {
            while (result.size() < index + 1) {
                result.add(null);
            }
            ((List) result).set(index, collectionEntry);
        }
        // for an unordered Collection (index has no use here)
        else {
            result.add(collectionEntry);
        }
        return result;
    }

    /**
     * Used to test if both {@code srcFieldClass} and {@code destFieldType} are
     * enum.
     * 
     * @param srcFieldClass the source field to be tested.
     * @param destFieldType the destination field to be tested.
     * @return {@code true} if and only if current running JRE is 1.5 or above,
     *         and both {@code srcFieldClass} and {@code destFieldType} are
     *         enum; otherwise return {@code false}.
     */
    public static boolean isEnumType(Class<?> srcFieldClass, Class<?> destFieldType) {
        if (srcFieldClass.isAnonymousClass()) {
            // If srcFieldClass is anonymous class, replace srcFieldClass with
            // its enclosing class.
            // This is used to ensure Dozer can get correct Enum type.
            srcFieldClass = srcFieldClass.getEnclosingClass();
        }
        if (destFieldType.isAnonymousClass()) {
            // Just like srcFieldClass, if destFieldType is anonymous class,
            // replace destFieldType with
            // its enclosing class. This is used to ensure Dozer can get correct
            // Enum type.
            destFieldType = destFieldType.getEnclosingClass();
        }
        return srcFieldClass.isEnum() && destFieldType.isEnum();
    }

    public static List<Class<?>> getSuperClassesAndInterfaces(Class<?> srcClass) {
        List<Class<?>> superClasses = new ArrayList<Class<?>>();
        Class<?> realClass = getRealClass(srcClass);

        // Add all super classes first
        Class<?> superClass = getRealClass(realClass).getSuperclass();
        while (!isBaseClass(superClass)) {
            superClasses.add(superClass);
            superClass = superClass.getSuperclass();
        }

        // Now add all interfaces of the passed in class and all it's super
        // classes

        // Linked hash set so duplicated are not added but insertion order is
        // kept
        LinkedHashSet<Class<?>> interfaces = new LinkedHashSet<Class<?>>();

        interfaces.addAll(getInterfaceHierarchy(realClass));
        for (Class<?> clazz : superClasses) {
            interfaces.addAll(getInterfaceHierarchy(clazz));
        }

        superClasses.addAll(interfaces);
        return superClasses;
    }

    @SuppressWarnings("unchecked")
    public static List<Class<?>> getInterfaceHierarchy(Class<?> srcClass) {
        final List<Class<?>> result = new LinkedList<Class<?>>();
        Class<?> realClass = getRealClass(srcClass);

        final LinkedList<Class> interfacesToProcess = new LinkedList<Class>();

        Class[] interfaces = realClass.getInterfaces();

        interfacesToProcess.addAll(Arrays.asList(interfaces));

        while (!interfacesToProcess.isEmpty()) {
            Class<?> iface = interfacesToProcess.remove();
            if (!result.contains(iface)) {
                result.add(iface);
                for (Class subiface : iface.getInterfaces()) {
                    // if we haven't processed this interface yet then add it to
                    // be processed
                    if (!result.contains(subiface)) {
                        interfacesToProcess.add(subiface);
                    }
                }
            }
        }

        return result;

    }

    public static String fieldMapKey(FieldMap fieldMap) {
        StringBuilder builder = new StringBuilder();
        if (isBlankOrNull(fieldMap.getSrcFieldName())) {
            builder.append("<noname>");
        } else {
            builder.append(fieldMap.getSrcFieldName());
        }
        if (!(fieldMap instanceof MultiSourceFieldMap) && !(fieldMap instanceof MultiFieldsExcludeFieldMap) && !isBlankOrNull(fieldMap.getSrcFieldIndex())) {
            builder.append("[" + fieldMap.getSrcFieldIndex() + "]");
        }
        if (!(fieldMap instanceof MultiSourceFieldMap) && !(fieldMap instanceof MultiFieldsExcludeFieldMap) && !isBlankOrNull(fieldMap.getSrcFieldKey())) {
            builder.append("{" + fieldMap.getSrcFieldKey() + "}");
        }
        builder.append("-->");
        builder.append(fieldMap.getDestFieldName());
        if (!(fieldMap instanceof MultiFieldsExcludeFieldMap) && !isBlankOrNull(fieldMap.getDestFieldIndex())) {
            builder.append("[" + fieldMap.getDestFieldIndex() + "]");
        }
        if (!(fieldMap instanceof MultiFieldsExcludeFieldMap) && !isBlankOrNull(fieldMap.getDestFieldKey())) {
            builder.append("{" + fieldMap.getDestFieldKey() + "}");
        }

        String mappingConditionId = fieldMap.getMappingConditionId();
        if (!isBlankOrNull(mappingConditionId)) {
            builder.append(" (conditionId: " + mappingConditionId + ")");
        }

        String mappingCondition = fieldMap.getMappingCondition();
        if (!isBlankOrNull(mappingCondition)) {
            builder.append(" (condition: " + mappingCondition + ")");
        }

        return builder.toString();
    }

    private static boolean isBaseClass(Class<?> clazz) {
        return clazz == null || BASE_CLASS.equals(clazz.getName());
    }

}