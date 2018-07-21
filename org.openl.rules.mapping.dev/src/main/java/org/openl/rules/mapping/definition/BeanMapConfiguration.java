package org.openl.rules.mapping.definition;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * A bean that holds information about a bean mapping options.
 */
public class BeanMapConfiguration {

    private Configuration globalConfiguration;
    private Class<?> classA;
    private Class<?> classB;
    private Class<?> classABeanFactory;
    private Class<?> classBBeanFactory;
    private Boolean mapNulls;
    private Boolean mapEmptyStrings;
    private Boolean trimStrings;
    private Boolean requiredFields;
    private Boolean wildcard;
    private String dateFormat;

    public Configuration getGlobalConfiguration() {
        return globalConfiguration;
    }

    public void setGlobalConfiguration(Configuration globalConfiguration) {
        this.globalConfiguration = globalConfiguration;
    }

    public Class<?> getClassA() {
        return classA;
    }

    public void setClassA(Class<?> classA) {
        this.classA = classA;
    }

    public Class<?> getClassB() {
        return classB;
    }

    public void setClassB(Class<?> classB) {
        this.classB = classB;
    }

    public Class<?> getClassABeanFactory() {
        return classABeanFactory;
    }

    public void setClassABeanFactory(Class<?> classABeanFactory) {
        this.classABeanFactory = classABeanFactory;
    }

    public Class<?> getClassBBeanFactory() {
        return classBBeanFactory;
    }

    public void setClassBBeanFactory(Class<?> classBBeanFactory) {
        this.classBBeanFactory = classBBeanFactory;
    }

    public boolean isMapNulls() {
        if (mapNulls != null) {
            return mapNulls;
        }

        return globalConfiguration.isMapNulls();
    }

    public void setMapNulls(Boolean mapNulls) {
        this.mapNulls = mapNulls;
    }

    public boolean isMapEmptyStrings() {
        if (mapEmptyStrings != null) {
            return mapEmptyStrings;
        }

        return globalConfiguration.isMapEmptyStrings();
    }

    public void setMapEmptyStrings(Boolean mapEmptyStrings) {
        this.mapEmptyStrings = mapEmptyStrings;
    }

    public boolean isTrimStrings() {
        if (trimStrings != null) {
            return trimStrings;
        }

        return globalConfiguration.isTrimStrings();
    }

    public void setTrimStrings(Boolean trimStrings) {
        this.trimStrings = trimStrings;
    }

    public boolean isRequiredFields() {
        if (requiredFields != null) {
            return requiredFields;
        }

        return globalConfiguration.isRequiredFields();
    }

    public void setRequiredFields(Boolean requiredFields) {
        this.requiredFields = requiredFields;
    }

    public boolean isWildcard() {
        if (wildcard != null) {
            return wildcard;
        }

        return globalConfiguration.isWildcard();
    }

    public void setWildcard(Boolean wildcard) {
        this.wildcard = wildcard;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("classA", classA).append("classB",
            classB).append("classABeanFactory", classABeanFactory).append("classABeanFactory", classABeanFactory)
            .append("mapNulls", mapNulls).append("mapEmptyStrings", mapEmptyStrings).append("trimStrings", trimStrings)
            .append("requiredFields", requiredFields).append("wildcard", wildcard).append("dateFormat", dateFormat)
            .append("globalConfiguration", globalConfiguration).toString();
    }

}
