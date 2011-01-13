package org.openl.rules.mapping.definition;

/**
 * A bean that holds all required information about one way mapping for a single
 * field map.
 */
public class FieldMap {

    private String[] src;
    private String dest;
    private String createMethod;
    private String defaultValue;
    private boolean mapNulls;
    private boolean mapEmptyStrings;
    private boolean required;

    // ????
    private BeanMap beanMap;
    private ConverterDescriptor converter;
    private ConditionDescriptor condition;

    public String[] getSrc() {
        return src;
    }

    public void setSrc(String[] src) {
        this.src = src;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public String getCreateMethod() {
        return createMethod;
    }

    public void setCreateMethod(String createMethod) {
        this.createMethod = createMethod;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public boolean isMapNulls() {
        return mapNulls;
    }

    public void setMapNulls(boolean mapNulls) {
        this.mapNulls = mapNulls;
    }

    public boolean isMapEmptyStrings() {
        return mapEmptyStrings;
    }

    public void setMapEmptyStrings(boolean mapEmptyStrings) {
        this.mapEmptyStrings = mapEmptyStrings;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public BeanMap getBeanMap() {
        return beanMap;
    }

    public void setBeanMap(BeanMap beanMap) {
        this.beanMap = beanMap;
    }

    public ConverterDescriptor getConverter() {
        return converter;
    }

    public void setConverter(ConverterDescriptor converter) {
        this.converter = converter;
    }

    public ConditionDescriptor getCondition() {
        return condition;
    }

    public void setCondition(ConditionDescriptor condition) {
        this.condition = condition;
    }

}
