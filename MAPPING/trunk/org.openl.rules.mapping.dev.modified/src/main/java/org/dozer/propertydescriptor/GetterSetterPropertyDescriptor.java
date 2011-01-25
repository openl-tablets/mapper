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
package org.dozer.propertydescriptor;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Collection;

import org.apache.commons.jxpath.JXPathContext;
import org.dozer.MappingException;
import org.dozer.factory.BeanCreationDirective;
import org.dozer.factory.DestBeanCreator;
import org.dozer.fieldmap.FieldMap;
import org.dozer.fieldmap.HintContainer;
import org.dozer.util.BridgedMethodFinder;
import org.dozer.util.CollectionUtils;
import org.dozer.util.MappingUtils;
import org.dozer.util.ReflectionUtils;
import org.dozer.util.TypeResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Internal class used to read and write values for fields that have a getter
 * and setter method. This class encapsulates underlying dozer specific logic
 * such as index mapping and deep mapping for reading and writing field values.
 * Only intended for internal use.
 * 
 * @author garsombke.franz
 * @author tierney.matt
 * @author dmitry.buzdin
 * 
 */
public abstract class GetterSetterPropertyDescriptor extends AbstractPropertyDescriptor {

    private static final Logger log = LoggerFactory.getLogger(GetterSetterPropertyDescriptor.class);

    private Class<?> propertyType;

    public GetterSetterPropertyDescriptor(Class<?> clazz, String fieldName, boolean isIndexed, String index,
        HintContainer srcDeepIndexHintContainer, HintContainer destDeepIndexHintContainer) {
        super(clazz, fieldName, isIndexed, index, srcDeepIndexHintContainer, destDeepIndexHintContainer);
    }

    public abstract Method getWriteMethod() throws NoSuchMethodException;

    protected abstract Method getReadMethod() throws NoSuchMethodException;

    protected abstract String getSetMethodName() throws NoSuchMethodException;

    protected abstract boolean isCustomSetMethod();

    public Class<?> getPropertyType() {
        if (propertyType == null) {
            propertyType = determinePropertyType();
        }
        return propertyType;
    }

    public Object getPropertyValue(Object bean) {
        Object result;
        if (MappingUtils.isDeepMapping(fieldName)) {
            result = getDeepSrcFieldValue(bean);
        } else {
            result = invokeReadMethod(bean);
            if (isIndexed) {
                if (MappingUtils.isSimpleCollectionIndex(index)) {
                    int collectionIndex = MappingUtils.getCollectionIndex(index);
                    result = MappingUtils.getCollectionIndexedValue(result, collectionIndex);
                } else {
                    String expression = String.format("%s[%s]", fieldName, index);
                    result = MappingUtils.getXPathIndexedValue(bean, expression);
                }
            }
        }
        return result;
    }

    public void setPropertyValue(Object bean, Object value, FieldMap fieldMap) {
        if (MappingUtils.isDeepMapping(fieldName)) {
            writeDeepDestinationValue(bean, value, fieldMap);
        } else {
            if (!getPropertyType().isPrimitive() || value != null) {
                // First check if value is indexed. If it's null, then the new
                // array will be created
                if (isIndexed) {
                    writeIndexedValue(bean, value);
                } else {
                    // Check if dest value is already set and is equal to src
                    // value. If true, no need to rewrite the dest value
                    try {
                        if (getPropertyValue(bean) == value && !isIndexed) {
                            return;
                        }
                    } catch (Exception e) {
                        // if we failed to read the value, assume we must write,
                        // and continue...
                    }
                    invokeWriteMethod(bean, value);
                }
            }
        }
    }

    private Object getDeepSrcFieldValue(Object srcObj) {

        // follow deep field hierarchy. If any values are null along the way,
        // then return null
        Object parentObj = srcObj;
        Object hierarchyValue = parentObj;
        // get deep hierarchy for current source object
        // 
        DeepHierarchyElement[] hierarchy = getDeepFieldHierarchy(srcObj, srcDeepIndexHintContainer);
        int hierarchyLength = hierarchy.length;

        // Iterate thru each deep element in the hierarchy to obtain field value
        //
        for (int i = 0; i < hierarchyLength; i++) {
            DeepHierarchyElement hierarchyElement = hierarchy[i];
            PropertyDescriptor pd = hierarchyElement.getPropDescriptor();

            // If any fields in the deep hierarchy are indexed, get actual value
            // within the collection at the specified index
            //
            if (!MappingUtils.isBlankOrNull(hierarchyElement.getIndex())) {
                Object bean = ReflectionUtils.invoke(pd.getReadMethod(), hierarchyValue, null);
                // Obtain field value with appropriate method
                // 
                if (MappingUtils.isSimpleCollectionIndex(hierarchyElement.getIndex())) {
                    int collectionIndex = MappingUtils.getCollectionIndex(hierarchyElement.getIndex());
                    hierarchyValue = MappingUtils.getCollectionIndexedValue(bean, collectionIndex);
                } else {
                    // We should provide right xpath's context and expression to
                    // obtain field
                    // value. In current state the right context is parent
                    // object of hierarchy element
                    // and the expression is concatenation of current field name
                    // and hierarchy element index value.
                    //
                    String expression = String.format("%s[%s]", pd.getName(), hierarchyElement.getIndex());
                    hierarchyValue = MappingUtils.getXPathIndexedValue(parentObj, expression);
                }
            } else {
                hierarchyValue = ReflectionUtils.invoke(pd.getReadMethod(), parentObj, null);
            }

            // if one of hierarchy element value is null we stop further
            // field path processing.
            //
            if (hierarchyValue == null) {
                return null;
            }

            // For the last element in hierarchy we skip this step to obtain
            // right
            // parent object for further processing.
            //
            if (i != hierarchyLength - 1) {
                parentObj = hierarchyValue;
            }
        }

        // At current state we processed field path and have the last field
        // value. We should check that field path is indexed ([] operator
        // at the end of field path) or not. If it is true we should evaluate
        // index expression and return value; otherwise - just return the last
        // field value.
        //
        if (isIndexed) {
            if (MappingUtils.isSimpleCollectionIndex(index)) {
                int collectionIndex = MappingUtils.getCollectionIndex(index);
                hierarchyValue = MappingUtils.getCollectionIndexedValue(hierarchyValue, collectionIndex);
            } else {
                String lastFieldName = hierarchy[hierarchyLength - 1].getPropDescriptor().getName();
                String expression = String.format("%s[%s]", lastFieldName, index);
                hierarchyValue = MappingUtils.getXPathIndexedValue(parentObj, expression);
            }
        }

        return hierarchyValue;
    }

    protected void writeDeepDestinationValue(Object destObj, Object destFieldValue, FieldMap fieldMap) {
        // follow deep field hierarchy. If any values are null along the way,
        // then create a new instance
        DeepHierarchyElement[] hierarchy = getDeepFieldHierarchy(destObj, fieldMap.getDestDeepIndexHintContainer());
        // first, iteratate through hierarchy and instantiate any objects that
        // are null
        Object parentObj = destObj;
        int hierarchyLength = hierarchy.length - 1;

        for (int i = 0; i < hierarchyLength; i++) {
            DeepHierarchyElement hierarchyElement = hierarchy[i];
            PropertyDescriptor pd = hierarchyElement.getPropDescriptor();
            Object value = ReflectionUtils.invoke(pd.getReadMethod(), parentObj, null);
            Class<?> clazz;

            if (value == null) {
                clazz = pd.getPropertyType();
                if (clazz.isInterface() && (i + 1) == hierarchyLength && fieldMap.getDestHintContainer() != null) {
                    // before setting the property on the destination object we
                    // should check for a destination hint. need to know
                    // that we are at the end of the line determine the property
                    // type
                    clazz = fieldMap.getDestHintContainer().getHint();
                }

                Object o = null;

                if (clazz.isArray() || Collection.class.isAssignableFrom(clazz)) {
                    // index value must be not null because hierarchy element is
                    // not at the end.
                    String index = hierarchyElement.getIndex();
                    if (!MappingUtils.isSimpleCollectionIndex(index)) {
                        MappingUtils
                            .throwMappingException("Destination field path should not contain filter expressions");
                    }

                    int collectionIndex = MappingUtils.getCollectionIndex(index);

                    if (clazz.isArray()) {
                        o = MappingUtils.prepareIndexedCollection(clazz, null, DestBeanCreator.create(clazz
                            .getComponentType()), collectionIndex);
                    }

                    if (Collection.class.isAssignableFrom(clazz)) {
                        Class<?> hintType = null;

                        if (fieldMap.getDestDeepIndexHintContainer() != null) {
                            hintType = fieldMap.getDestDeepIndexHintContainer().getHint(i);
                        }
                        
                        Class<?> collectionEntryType = getComponentType(clazz, pd, hintType);

                        o = MappingUtils.prepareIndexedCollection(clazz, null, DestBeanCreator
                            .create(collectionEntryType), collectionIndex);
                    }
                } else {
                    // if user defined another type of property we should use it 
                    if (fieldMap.getDestDeepIndexHintContainer() != null && fieldMap.getDestDeepIndexHintContainer().hasHintType(i)) {
                        clazz = fieldMap.getDestDeepIndexHintContainer().getHint(i);
                    }
                    
                    try {
                        o = DestBeanCreator.create(clazz);
                    } catch (Exception e) {
                        // lets see if they have a factory we can try as a last
                        // ditch. If not...throw the exception:
                        if (fieldMap.getClassMap().getDestClassBeanFactory() != null) {
                            o = DestBeanCreator.create(new BeanCreationDirective(null, fieldMap.getClassMap()
                                .getSrcClassToMap(), clazz, clazz, fieldMap.getClassMap().getDestClassBeanFactory(),
                                fieldMap.getClassMap().getDestClassBeanFactoryId(), null));
                        } else {
                            MappingUtils.throwMappingException(e);
                        }
                    }
                }

                ReflectionUtils.invoke(pd.getWriteMethod(), parentObj, new Object[] { o });
                value = ReflectionUtils.invoke(pd.getReadMethod(), parentObj, null);
            }

            // Check to see if collection needs to be resized
            if (MappingUtils.isSupportedCollection(value.getClass())) {
                int currentSize = CollectionUtils.getLengthOfCollection(value);

                String index = hierarchyElement.getIndex();
                if (!MappingUtils.isSimpleCollectionIndex(index)) {
                    MappingUtils.throwMappingException("Destination field path should not contain filter expressions");
                }

                int collectionIndex = MappingUtils.getCollectionIndex(index);

                if (currentSize < collectionIndex + 1) {
                    Class<?> hintType = null;

                    if (fieldMap.getDestDeepIndexHintContainer() != null) {
                        hintType = fieldMap.getDestDeepIndexHintContainer().getHint(i);
                    }

                    Class<?> componentType = getComponentType(pd.getPropertyType(), pd, hintType);
                    value = MappingUtils.prepareIndexedCollection(pd.getPropertyType(), value, DestBeanCreator
                        .create(componentType), collectionIndex);
                    ReflectionUtils.invoke(pd.getWriteMethod(), parentObj, new Object[] { value });
                    // Re-read value object from parent object to avoid using invalid instance of property value.
                    //
                    value = ReflectionUtils.invoke(pd.getReadMethod(), parentObj, null);
                }
            }

            if (value != null && (value.getClass().isArray() || Collection.class.isAssignableFrom(value.getClass()))) {
                String index = hierarchyElement.getIndex();
                if (!MappingUtils.isSimpleCollectionIndex(index)) {
                    MappingUtils.throwMappingException("Destination field path should not contain filter expressions");
                }
                parentObj = MappingUtils.getCollectionIndexedValue(value, MappingUtils.getCollectionIndex(index));
            } else {
                parentObj = value;
            }
        }
        // second, set the very last field in the deep hierarchy
        PropertyDescriptor pd = hierarchy[hierarchy.length - 1].getPropDescriptor();

        Class<?> type;
        // For one-way mappings there could be no read method
        if (pd.getReadMethod() != null) {
            type = pd.getReadMethod().getReturnType();
        } else {
            type = pd.getWriteMethod().getParameterTypes()[0];
        }

        if (!type.isPrimitive() || destFieldValue != null) {
            if (!isIndexed) {
                Method method = null;
                if (!isCustomSetMethod()) {
                    method = pd.getWriteMethod();
                } else {
                    try {
                        method = ReflectionUtils.findAMethod(parentObj.getClass(), getSetMethodName());
                    } catch (NoSuchMethodException e) {
                        MappingUtils.throwMappingException(e);
                    }
                }

                ReflectionUtils.invoke(method, parentObj, new Object[] { destFieldValue });
            } else {
                writeIndexedValue(parentObj, destFieldValue);
            }
        }
    }

    private Class<?> getComponentType(Class<?> container, PropertyDescriptor pd, Class<?> hintType) {

        Class<?> componentType = null;
        if (container.isArray()) {
            componentType = container.getComponentType();
        } else if (Collection.class.isAssignableFrom(container)) {
            Class<?> genericType = ReflectionUtils.determineGenericsType(pd);
            if (genericType != null) {
                componentType = genericType;
            } else {
                componentType = hintType;
            }
        }

        return componentType;
    }

    protected Object invokeReadMethod(Object target) {
        Object result = null;
        try {
            result = ReflectionUtils.invoke(getReadMethod(), target, null);
        } catch (NoSuchMethodException e) {
            MappingUtils.throwMappingException(e);
        }
        return result;
    }

    protected void invokeWriteMethod(Object target, Object value) {
        try {
            ReflectionUtils.invoke(getWriteMethod(), target, new Object[] { value });
        } catch (NoSuchMethodException e) {
            MappingUtils.throwMappingException(e);
        }
    }

    private DeepHierarchyElement[] getDeepFieldHierarchy(Object obj, HintContainer deepIndexHintContainer) {
        return ReflectionUtils.getDeepFieldHierarchy(obj.getClass(), fieldName, deepIndexHintContainer);
    }

    private void writeIndexedValue(Object destObj, Object destFieldValue) {
        Object existingValue = invokeReadMethod(destObj);
        Object indexedValue = null;

        boolean isSimpleCollectionIndex = MappingUtils.isSimpleCollectionIndex(index);

        if (!isSimpleCollectionIndex) {
            MappingUtils.throwMappingException("Destinaiton field path should not contain filter expressions");
        }

        int collectionIndex = MappingUtils.getCollectionIndex(index);

        if (isSimpleCollectionIndex) {
            indexedValue = MappingUtils.prepareIndexedCollection(getPropertyType(), existingValue, destFieldValue,
                collectionIndex);
        } else {
            // use index expression as a xpath's expression to obtain value
            indexedValue = MappingUtils.prepareIndexedCollection(getPropertyType(), existingValue, null, 0);
            JXPathContext context = JXPathContext.newContext(indexedValue);
            context.setValue(index, destFieldValue);
        }

        invokeWriteMethod(destObj, indexedValue);
    }

    private Class determinePropertyType() {
        Method readMethod = getBridgedReadMethod();
        Method writeMethod = getBridgedWriteMethod();

        Class returnType = null;

        try {
            returnType = TypeResolver.resolvePropertyType(clazz, readMethod, writeMethod);
        } catch (Exception ignore) {
        }

        if (returnType != null) {
            return returnType;
        }

        if (readMethod == null && writeMethod == null) {
            throw new MappingException(
                "No read or write method found for field (" + fieldName + ") in class (" + clazz + ")");
        }

        if (readMethod == null) {
            return determineByWriteMethod(writeMethod);
        } else {
            try {
                return readMethod.getReturnType();
            } catch (Exception e) {
                // let us try the set method - the field might have inacessible
                // 'get' method
                return determineByWriteMethod(writeMethod);
            }
        }
    }

    private Class determineByWriteMethod(Method writeMethod) {
        try {
            return writeMethod.getParameterTypes()[0];
        } catch (Exception e) {
            throw new MappingException(e);
        }
    }

    private Method getBridgedReadMethod() {
        try {
            return BridgedMethodFinder.findMethod(getReadMethod(), clazz);
        } catch (Exception ignore) {
        }
        return null;
    }

    private Method getBridgedWriteMethod() {
        try {
            return BridgedMethodFinder.findMethod(getWriteMethod(), clazz);
        } catch (Exception ignore) {
        }
        return null;
    }

    public Class<?> genericType() {
        Class<?> genericType = null;
        try {
            Method method = getWriteMethod();
            genericType = ReflectionUtils.determineGenericsType(method, false);
        } catch (NoSuchMethodException e) {
            log.warn("The destination object: {} does not have a write method for property : {}", e);
        }

        return genericType;
    }

}