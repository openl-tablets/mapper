package org.openl.rules.mapping;

/**
 * The class that holds information about a class pair mapping options.
 */
public class ClassMappingConfiguration {

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

    public Boolean getMapNulls() {
        return mapNulls;
    }

    public void setMapNulls(Boolean mapNulls) {
        this.mapNulls = mapNulls;
    }

    public Boolean getMapEmptyStrings() {
        return mapEmptyStrings;
    }

    public void setMapEmptyStrings(Boolean mapEmptyStrings) {
        this.mapEmptyStrings = mapEmptyStrings;
    }

    public Boolean getTrimStrings() {
        return trimStrings;
    }

    public void setTrimStrings(Boolean trimStrings) {
        this.trimStrings = trimStrings;
    }

    public Boolean getRequiredFields() {
        return requiredFields;
    }

    public void setRequiredFields(Boolean requiredFields) {
        this.requiredFields = requiredFields;
    }

    public Boolean getWildcard() {
        return wildcard;
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

}
