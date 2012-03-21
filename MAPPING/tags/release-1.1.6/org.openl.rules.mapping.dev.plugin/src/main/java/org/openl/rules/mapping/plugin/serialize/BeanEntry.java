package org.openl.rules.mapping.plugin.serialize;

import java.util.List;

public class BeanEntry {

    private String name;
    private Class<?> extendedType;
    private List<FieldEntry> fields;

    public Class<?> getExtendedType() {
        return extendedType;
    }

    public void setExtendedType(Class<?> extendedType) {
        this.extendedType = extendedType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<FieldEntry> getFields() {
        return fields;
    }

    public void setFields(List<FieldEntry> fields) {
        this.fields = fields;
    }

}
