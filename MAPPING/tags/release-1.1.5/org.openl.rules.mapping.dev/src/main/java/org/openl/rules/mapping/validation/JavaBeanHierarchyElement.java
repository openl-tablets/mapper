package org.openl.rules.mapping.validation;

import java.beans.PropertyDescriptor;


public class JavaBeanHierarchyElement implements FieldPathHierarchyElement {

    private String index;
    private Class<?> hintType;
    private PropertyDescriptor propDescriptor;

    public JavaBeanHierarchyElement(PropertyDescriptor propDescriptor, String index, Class<?> hintType) {
        this.propDescriptor = propDescriptor;
        this.index = index;
        this.hintType = hintType;
    }

    public String getIndex() {
        return index;
    }

    public String getName() {
        return propDescriptor.getName();
    }

    public Class<?> getType() {
        if (hintType != null) {
            return hintType;
        }
        
        return propDescriptor.getPropertyType();
    }

    
}
