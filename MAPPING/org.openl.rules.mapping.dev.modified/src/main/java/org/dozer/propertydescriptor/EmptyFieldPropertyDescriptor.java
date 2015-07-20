package org.dozer.propertydescriptor;

import org.dozer.fieldmap.FieldMap;

public class EmptyFieldPropertyDescriptor implements DozerPropertyDescriptor {

    public Class<?> genericType() {
        return Object.class;
    }

    public Class<?> getPropertyType() {
        return Object.class;
    }

    public Object getPropertyValue(Object bean) {
        return null;
    }

    public void setPropertyValue(Object bean, Object value, FieldMap fieldMap) {
        throw new UnsupportedOperationException();
    }

}
