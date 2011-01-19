package org.openl.rules.mapping;

public class Converter {

    private Class<?> classA;
    private Class<?> classB;
    private String convertMethod;

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

    public String getConvertMethod() {
        return convertMethod;
    }

    public void setConvertMethod(String convertMethod) {
        this.convertMethod = convertMethod;
    }

}
