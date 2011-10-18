package org.openl.rules.mapping.validation;


public class ThisHierarchyElement implements FieldPathHierarchyElement {

    private Class<?> type;
    
    public ThisHierarchyElement(Class<?> type) {
        this.type = type;
    }

    public String getIndex() {
        return null;
    }

    public String getName() {
        return "this";
    }

    public Class<?> getType() {
        return type;
    }

}
