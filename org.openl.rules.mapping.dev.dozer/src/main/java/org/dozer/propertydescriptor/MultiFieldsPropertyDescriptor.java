package org.dozer.propertydescriptor;

import org.apache.commons.lang.ArrayUtils;
import org.dozer.fieldmap.FieldMap;
import org.dozer.util.MappingUtils;
import org.dozer.util.ReflectionUtils;

/**
 * The property descriptor implementation which used by engine to support
 * multi-source field maps.
 * 
 */
public class MultiFieldsPropertyDescriptor implements DozerPropertyDescriptor {

    private DozerPropertyDescriptor[] propertyDescriptors;

    public MultiFieldsPropertyDescriptor(DozerPropertyDescriptor[] propertyDescriptors) {
        this.propertyDescriptors = propertyDescriptors;
    }

    public Class<?> genericType() {
        return Object.class;
    }

    public Class<?> getPropertyType() {
        return Object[].class;
    }

    public Object getPropertyValue(Object bean) {

        Object[] values = new Object[propertyDescriptors.length];

        for (int i = 0; i < propertyDescriptors.length; i++) {
            values[i] = propertyDescriptors[i].getPropertyValue(bean);
        }

        return values;
    }

    public void setPropertyValue(Object bean, Object value, FieldMap fieldMap) {

        if (!ReflectionUtils.isArray(value) || ArrayUtils.getLength(value) != propertyDescriptors.length) {
            MappingUtils.throwMappingException(String.format("Cannot set value to property '%s'", fieldMap));
        }

        Object[] values = (Object[]) value;

        for (int i = 0; i < propertyDescriptors.length; i++) {
            DozerPropertyDescriptor propertyDescriptor = propertyDescriptors[i];
            propertyDescriptor.setPropertyValue(bean, values[i], fieldMap);
        }
    }

}
