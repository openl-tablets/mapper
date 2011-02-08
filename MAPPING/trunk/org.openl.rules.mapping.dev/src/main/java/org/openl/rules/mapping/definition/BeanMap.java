package org.openl.rules.mapping.definition;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * A bean that holds all required information about one way mapping for a single
 * bean map.
 */
public class BeanMap {

    private Class<?> srcClass;
    private Class<?> destClass;
    private String destBeanFactory;
    private List<FieldMap> fieldMappings = new ArrayList<FieldMap>();
    private BeanMapConfiguration configuration;

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
    
    public BeanMapConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(BeanMapConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("srcClass", srcClass).append(
            "destClass", destClass).append("destBeanFactory", destBeanFactory).append("configuration", configuration)
            .toString();
    }
}
