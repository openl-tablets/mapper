package org.openl.rules.mapping;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * The class that holds all information about a single field mapping definition.
 * 
 * TODO: replace current bean class with bean fields of which are groped by
 * other beans.
 */
public class Mapping {

    private Class<?> classA;
    private Class<?> classB;
    private String[] fieldA;
    private String fieldB;
    private String convertMethodAB;
    private String convertMethodBA;
    private Boolean oneWay;
    private Boolean mapNulls;
    private Boolean mapEmptyStrings;
    private Boolean trimStrings;
    private String fieldACreateMethod;
    private String fieldBCreateMethod;
    private String fieldADefaultValue;
    private String fieldBDefaultValue;
    // private String fieldADateFormat;
    // private String fieldBDateFormat;
    private Class<?>[][] fieldAHint;
    private Class<?>[] fieldBHint;
    private Class<?>[] fieldAType;
    private Class<?> fieldBType;
    private Boolean fieldARequired;
    private Boolean fieldBRequired;
    private String conditionAB;
    private String conditionBA;

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

    public String[] getFieldA() {
        return fieldA;
    }

    public void setFieldA(String[] fieldA) {
        this.fieldA = fieldA;
    }

    public String getFieldB() {
        return fieldB;
    }

    public void setFieldB(String fieldB) {
        this.fieldB = fieldB;
    }

    public String getConvertMethodAB() {
        return convertMethodAB;
    }

    public void setConvertMethodAB(String convertMethodAB) {
        this.convertMethodAB = convertMethodAB;
    }

    public String getConvertMethodBA() {
        return convertMethodBA;
    }

    public void setConvertMethodBA(String convertMethodBA) {
        this.convertMethodBA = convertMethodBA;
    }

    public String getFieldACreateMethod() {
        return fieldACreateMethod;
    }

    public void setFieldACreateMethod(String fieldACreateMethod) {
        this.fieldACreateMethod = fieldACreateMethod;
    }

    public String getFieldBCreateMethod() {
        return fieldBCreateMethod;
    }

    public void setFieldBCreateMethod(String fieldBCreateMethod) {
        this.fieldBCreateMethod = fieldBCreateMethod;
    }

    public String getFieldADefaultValue() {
        return fieldADefaultValue;
    }

    public void setFieldADefaultValue(String fieldADefaultValue) {
        this.fieldADefaultValue = fieldADefaultValue;
    }

    public String getFieldBDefaultValue() {
        return fieldBDefaultValue;
    }

    public void setFieldBDefaultValue(String fieldBDefaultValue) {
        this.fieldBDefaultValue = fieldBDefaultValue;
    }

    public String getConditionAB() {
        return conditionAB;
    }

    public void setConditionAB(String conditionAB) {
        this.conditionAB = conditionAB;
    }

    public String getConditionBA() {
        return conditionBA;
    }

    public void setConditionBA(String conditionBA) {
        this.conditionBA = conditionBA;
    }

    public Class<?>[][] getFieldAHint() {
        return fieldAHint;
    }

    public void setFieldAHint(Class<?>[][] fieldAHint) {
        this.fieldAHint = fieldAHint;
    }

    public Class<?>[] getFieldBHint() {
        return fieldBHint;
    }

    public void setFieldBHint(Class<?>[] fieldBHint) {
        this.fieldBHint = fieldBHint;
    }

    public Class<?>[] getFieldAType() {
        return fieldAType;
    }

    public void setFieldAType(Class<?>[] fieldAType) {
        this.fieldAType = fieldAType;
    }

    public Class<?> getFieldBType() {
        return fieldBType;
    }

    public void setFieldBType(Class<?> fieldBType) {
        this.fieldBType = fieldBType;
    }

    public Boolean getOneWay() {
        return oneWay;
    }

    public void setOneWay(Boolean oneWay) {
        this.oneWay = oneWay;
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

    public Boolean getFieldARequired() {
        return fieldARequired;
    }

    public void setFieldARequired(Boolean fieldARequired) {
        this.fieldARequired = fieldARequired;
    }

    public Boolean getFieldBRequired() {
        return fieldBRequired;
    }

    public void setFieldBRequired(Boolean fieldBRequired) {
        this.fieldBRequired = fieldBRequired;
    }

    // public String getFieldADateFormat() {
    // return fieldADateFormat;
    // }
    //
    // public void setFieldADateFormat(String fieldADateFormat) {
    // this.fieldADateFormat = fieldADateFormat;
    // }
    //
    // public String getFieldBDateFormat() {
    // return fieldBDateFormat;
    // }
    //
    // public void setFieldBDateFormat(String fieldBDateFormat) {
    // this.fieldBDateFormat = fieldBDateFormat;
    // }

    public Boolean getTrimStrings() {
        return trimStrings;
    }

    public void setTrimStrings(Boolean trimStrings) {
        this.trimStrings = trimStrings;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("classA", classA).append("classB",
            classB).append("fieldA", fieldA).append("fieldB", fieldB).append("convertMethodAB", convertMethodAB)
            .append("convertMethodBA", convertMethodBA).append("oneWay", oneWay).append("mapNulls", mapNulls).append(
                "mapEmptyStrings", mapEmptyStrings).append("trimStrings", trimStrings).append("fieldACreateMethod",
                fieldACreateMethod).append("fieldBCreateMethod", fieldBCreateMethod).append("fieldADefaultValue",
                fieldADefaultValue).append("fieldBDefaultValue", fieldBDefaultValue)
            /*
             * .append("fieldADateFormat", fieldADateFormat).append(
             * "fieldBDateFormat", fieldBDateFormat)
             */.append("fieldAHint", fieldAHint).append("fieldBHint", fieldBHint).append("fieldAType", fieldAType)
            .append("fieldBType", fieldBType).append("fieldARequired", fieldARequired).append("fieldBRequired",
                fieldBRequired).append("conditionAB", conditionAB).append("conditionBA", conditionBA).toString();
    }

}