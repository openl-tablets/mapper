package org.openl.rules.mapping.plugin.serialize;

public class FieldEntry {

    private String name;
    private Class<?> type;
    private CollectionType collectionType;
    private Class<?> collectionItemType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public CollectionType getCollectionType() {
        return collectionType;
    }

    public void setCollectionType(CollectionType collectionType) {
        this.collectionType = collectionType;
    }

    public Class<?> getCollectionItemType() {
        return collectionItemType;
    }

    public void setCollectionItemType(Class<?> collectionItemType) {
        this.collectionItemType = collectionItemType;
    }

}
