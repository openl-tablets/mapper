package org.openl.rules.mapping.definition;

import java.util.ArrayList;
import java.util.List;

/**
 * A bean that holds all required information about one way mapping for a single
 * bean map.
 */
public class BeanMap {

    private Class<?> srcClass;
    private Class<?> destClass;
    private String destBeanFactory;
    private List<FieldMap> fieldMappings = new ArrayList<FieldMap>();

    public Class<?> getSrcClass() {
        return srcClass;
    }

    public void setSrcClass(Class<?> srcClass) {
        this.srcClass = srcClass;
    }

    public Class<?> getDestClass() {
        return destClass;
    }

    public void setDestClass(Class<?> destClass) {
        this.destClass = destClass;
    }
    
    public String getDestBeanFactory() {
        return destBeanFactory;
    }

    public void setDestBeanFactory(String destBeanFactory) {
        this.destBeanFactory = destBeanFactory;
    }

    public List<FieldMap> getFieldMappings() {
        return fieldMappings;
    }

    public void setFieldMappings(List<FieldMap> fieldMappings) {
        this.fieldMappings = fieldMappings;
    }

}
