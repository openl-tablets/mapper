package org.openl.rules.mapping.definition;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * A bean that holds all required information about one way mapping for a single
 * field map.
 */
public class FieldMap {

    private BeanMap beanMap;
    private String[] src;
    private String dest;
    private String createMethod;
    private String defaultValue;
    private Boolean mapNulls;
    private Boolean mapEmptyStrings;
    private Boolean trimStrings;
    private Boolean required;
    private String[] srcDateFormat;
    private String destDateFormat;
    private Class<?>[][] srcHint;
    private Class<?>[] destHint;
    private Class<?>[] srcType;
    private Class<?> destType;
    private ConverterDescriptor converter;
    private ConditionDescriptor condition;
    private CollectionItemDiscriminatorDescriptor collectionItemDiscriminator;
    private String mapId;

    public BeanMap getBeanMap() {
        return beanMap;
    }

    public void setBeanMap(BeanMap beanMap) {
        this.beanMap = beanMap;
    }

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
        if (mapNulls != null) {
            return mapNulls;
        }

        return beanMap.getConfiguration().isMapNulls();
    }

    public void setMapNulls(Boolean mapNulls) {
        this.mapNulls = mapNulls;
    }

    public boolean isMapEmptyStrings() {
        if (mapEmptyStrings != null) {
            return mapEmptyStrings;
        }

        return beanMap.getConfiguration().isMapEmptyStrings();

    }

    public void setMapEmptyStrings(Boolean mapEmptyStrings) {
        this.mapEmptyStrings = mapEmptyStrings;
    }

    public Boolean isTrimStrings() {
        if (trimStrings != null) {
            return trimStrings;
        }

        return beanMap.getConfiguration().isTrimStrings();
    }

    public void setTrimStrings(Boolean trimStrings) {
        this.trimStrings = trimStrings;
    }

    public boolean isRequired() {
        if (required != null) {
            return required;
        }

        return beanMap.getConfiguration().isRequiredFields();
    }

    public void setRequired(Boolean required) {
        this.required = required;
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

    public Class<?>[][] getSrcHint() {
        return srcHint;
    }

    public void setSrcHint(Class<?>[][] srcHint) {
        this.srcHint = srcHint;
    }

    public Class<?>[] getDestHint() {
        return destHint;
    }

    public void setDestHint(Class<?>[] destHint) {
        this.destHint = destHint;
    }

    public Class<?>[] getSrcType() {
        return srcType;
    }

    public void setSrcType(Class<?>[] srcType) {
        this.srcType = srcType;
    }

    public Class<?> getDestType() {
        return destType;
    }

    public void setDestType(Class<?> destType) {
        this.destType = destType;
    }

    public String[] getSrcDateFormat() {
        return srcDateFormat;
    }

    public void setSrcDateFormat(String[] srcDateFormat) {
        this.srcDateFormat = srcDateFormat;
    }

    public String getDestDateFormat() {
        return destDateFormat;
    }

    public void setDestDateFormat(String destDateFormat) {
        this.destDateFormat = destDateFormat;
    }

    public CollectionItemDiscriminatorDescriptor getCollectionItemDiscriminator() {
        return collectionItemDiscriminator;
    }

    public void setCollectionItemDiscriminator(CollectionItemDiscriminatorDescriptor collectionItemDiscriminator) {
        this.collectionItemDiscriminator = collectionItemDiscriminator;
    }

    public String getMapId() {
        return mapId;
    }

    public void setMapId(String mapId) {
        this.mapId = mapId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("src", src)
            .append("dest", dest)
            .append("createMethod", createMethod)
            .append("defaultValue", defaultValue)
            .append("mapNulls", mapNulls)
            .append("mapEmptyStrings", mapEmptyStrings)
            .append("required", required)
            .append("srcHint", srcHint)
            .append("destHint", destHint)
            .append("srcType", srcType)
            .append("destType", destType)
            .append("customConverter", converter)
            .append("mapCondition", condition)
            .append("collectionItemDiscriminator", collectionItemDiscriminator)
            .append("srcDateFormat", srcDateFormat)
            .append("destDateFormat", destDateFormat)
            .append("mapId", mapId)
            .toString();
    }

}
