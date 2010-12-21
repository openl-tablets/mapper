package org.openl.rules.mapping.data;

public class Source {

    private String stringField;
    private int intField;

    public Source(String stringField, int intField) {
        this.stringField = stringField;
        this.intField = intField;
    }

    public String getStringField() {
        return stringField;
    }

    public void setStringField(String stringField) {
        this.stringField = stringField;
    }

    public int getIntField() {
        return intField;
    }

    public void setIntField(int intField) {
        this.intField = intField;
    }

}
