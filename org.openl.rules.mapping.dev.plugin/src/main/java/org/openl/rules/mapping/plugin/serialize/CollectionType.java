package org.openl.rules.mapping.plugin.serialize;

public enum CollectionType {

    ARRAY("array"),
    COLLECTION("collection");
    
    private String type;

    private CollectionType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
    
}
