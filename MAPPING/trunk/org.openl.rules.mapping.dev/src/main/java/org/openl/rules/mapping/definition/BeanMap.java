package org.openl.rules.mapping.definition;

import java.util.ArrayList;
import java.util.List;

public class BeanMap {

    private Class<?> classA;
    private Class<?> classB;
    private List<FieldMap> fieldMappings = new ArrayList<FieldMap>();

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

    public List<FieldMap> getFieldMappings() {
        return fieldMappings;
    }

    public void setFieldMappings(List<FieldMap> fieldMappings) {
        this.fieldMappings = fieldMappings;
    }

}
