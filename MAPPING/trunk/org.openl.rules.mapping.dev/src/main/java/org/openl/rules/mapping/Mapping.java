package org.openl.rules.mapping;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * The class that holds all information about a single field mapping definition.
 */
public class Mapping {

    private Class<?> classA;
    private Class<?> classB;
    private String[] fieldA;
    private String fieldB;
    private String convertMethodAB;
    private String convertMethodBA;
    private boolean oneWay;
    private boolean mapNulls;
    private String classABeanFactory;
    private String classBBeanFactory;
    private String fieldACreateMethod;
    private String fieldBCreateMethod;
    private String fieldADefaultValue;
    private String fieldBDefaultValue;
    private Class<?>[][] fieldAHint;
    private Class<?>[] fieldBHint;
    private Class<?>[] fieldAType;
    private Class<?> fieldBType;
    private boolean fieldARequired;
    private boolean fieldBRequired;
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

    public boolean isOneWay() {
        return oneWay;
    }

    public void setOneWay(boolean oneWay) {
        this.oneWay = oneWay;
    }

    public boolean isMapNulls() {
        return mapNulls;
    }

    public void setMapNulls(boolean mapNulls) {
        this.mapNulls = mapNulls;
    }

    public String getClassABeanFactory() {
        return classABeanFactory;
    }

    public void setClassABeanFactory(String classABeanFactory) {
        this.classABeanFactory = classABeanFactory;
    }

    public String getClassBBeanFactory() {
        return classBBeanFactory;
    }

    public void setClassBBeanFactory(String classBBeanFactory) {
        this.classBBeanFactory = classBBeanFactory;
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

    public boolean isFieldARequired() {
        return fieldARequired;
    }

    public void setFieldARequired(boolean fieldARequired) {
        this.fieldARequired = fieldARequired;
    }

    public boolean isFieldBRequired() {
        return fieldBRequired;
    }

    public void setFieldBRequired(boolean fieldBRequired) {
        this.fieldBRequired = fieldBRequired;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("classA", classA).append("classB",
            classB).append("fieldA", fieldA).append("fieldB", fieldB).append("convertMethodAB", convertMethodAB)
            .append("convertMethodBA", convertMethodBA).append("oneWay", oneWay).append("mapNulls", mapNulls).append(
                "classABeanFactory", classABeanFactory).append("classBBeanFactory", classBBeanFactory).append(
                "fieldACreateMethod", fieldACreateMethod).append("fieldBCreateMethod", fieldBCreateMethod).append(
                "fieldADefaultValue", fieldADefaultValue).append("fieldBDefaultValue", fieldBDefaultValue).append(
                "fieldAHint", fieldAHint).append("fieldBHint", fieldBHint).append("fieldAType", fieldAType).append(
                "fieldBType", fieldBType).append("fieldARequired", fieldARequired).append("fieldBRequired",
                fieldBRequired).append("conditionAB", conditionAB).append("conditionBA", conditionBA).toString();
    }

}