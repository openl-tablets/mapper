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

import java.util.List;

import org.dozer.classmap.ClassMap;
import org.dozer.fieldmap.DozerField;
import org.dozer.fieldmap.HintContainer;
import org.dozer.util.DozerConstants;
import org.dozer.util.MappingUtils;

/**
 * Internal factory responsible for determining which property descriptor should
 * be used. Only intended for internal use.
 * 
 * @author garsombke.franz
 */
public final class PropertyDescriptorFactory {

    private PropertyDescriptorFactory() {
    }

    public static DozerPropertyDescriptor getPropertyDescriptor(Class<?> clazz, ClassMap classMap, List<DozerField> srcFields, DozerField dest) {
        DozerPropertyDescriptor[] propertyDescriptors = new DozerPropertyDescriptor[srcFields.size()];

        for (int i = 0; i < srcFields.size(); i++) {
            propertyDescriptors[i] = getPropertyDescriptor(clazz, classMap, srcFields.get(i), dest);
        }

        return new MultiFieldsPropertyDescriptor(propertyDescriptors);
    }

    public static DozerPropertyDescriptor getPropertyDescriptor(Class<?> clazz, ClassMap classMap, DozerField src, DozerField dest) {

        if (MappingUtils.isBlankOrNull(src.getName())) {
            return new EmptyFieldPropertyDescriptor();
        }
        
        return getPropertyDescriptor(clazz, src.getTheGetMethod(), src.getTheSetMethod(), src.getMapGetMethod(),
            src.getMapSetMethod(), src.isAccessible(), src.isIndexed(), src.getIndex(), src.getName(), src
                .getKey(), src.isSelfReferenced(), src.getName(),  src.getDeepIndexHintContainer(),
                classMap.getDestClassBeanFactory());
    }

    /**
     * Creates appropriate property descriptor using given information.
     * 
     * @param clazz target class
     * @param theGetMethod property get method name
     * @param theSetMethod property set method name
     * @param mapGetMethod property get method name in case of property is map
     * @param mapSetMethod property set method name in case of property is map
     * @param isAccessible indicates that property can be accessed through
     *            reflection directly
     * @param isIndexed indicates that property has index
     * @param index index value
     * @param name property name
     * @param key key value in case of using mapping into map
     * @param isSelfReferencing indicates that property should be copied by
     *            reference
     * @param oppositeFieldName alternative name of field
     * @param deepIndexHintContainer deep index hint container
     * @param beanFactory bean factory
     * @return property descriptor instance
     */
    public static DozerPropertyDescriptor getPropertyDescriptor(Class<?> clazz, String theGetMethod,
        String theSetMethod, String mapGetMethod, String mapSetMethod, boolean isAccessible, boolean isIndexed,
        String index, String name, String key, boolean isSelfReferencing, String oppositeFieldName,
        HintContainer deepIndexHintContainer, String beanFactory) {

        DozerPropertyDescriptor desc;

        // Raw Map types or custom map-get-method/set specified
        boolean isMapProperty = MappingUtils.isSupportedMap(clazz);

        if (name.equals(DozerConstants.SELF_KEYWORD) && (mapSetMethod != null || mapGetMethod != null || isMapProperty)) {
            String setMethod = isMapProperty ? "put" : mapSetMethod;
            String getMethod = isMapProperty ? "get" : mapGetMethod;

            desc = new MapPropertyDescriptor(clazz, name, isIndexed, index, setMethod, getMethod,
                key != null ? key : oppositeFieldName, deepIndexHintContainer);

            // Copy by reference(Not mapped backed properties which also use
            // 'this'
            // identifier for a different purpose)
        } else if (isSelfReferencing) {
            desc = new SelfPropertyDescriptor(clazz);

            // Access field directly and bypass getter/setters
        } else if (isAccessible) {
            desc = new FieldPropertyDescriptor(clazz, name, isIndexed, index, deepIndexHintContainer);

            // Custom get-method/set specified
        } else if (theSetMethod != null || theGetMethod != null) {
            desc = new CustomGetSetPropertyDescriptor(clazz, name, isIndexed, index, theSetMethod, theGetMethod,
                deepIndexHintContainer);

            // If this object is an XML Bean - then use the
            // XmlBeanPropertyDescriptor
        } else if (beanFactory != null && beanFactory.equals(DozerConstants.XML_BEAN_FACTORY)) {
            desc = new XmlBeanPropertyDescriptor(clazz, name, isIndexed, index, deepIndexHintContainer);

            // Everything else. It must be a normal bean with normal custom
            // get/set
            // methods
        } else {
            desc = new JavaBeanPropertyDescriptor(clazz, name, isIndexed, index, deepIndexHintContainer);
        }
        return desc;
    }

}