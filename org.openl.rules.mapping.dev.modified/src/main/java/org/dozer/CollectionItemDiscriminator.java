package org.dozer;

public interface CollectionItemDiscriminator {

    /**
     * Discriminates destination element of target collection.
     * 
     * @param sourceItemType
     * @param sourceItemValue
     * @param destCollectionType
     * @param destItemType
     * @param destCollection
     * @return
     */
    Object discriminate(Class<?> sourceItemType,
            Object sourceItemValue,
            Class<?> destCollectionType,
            Class<?> destItemType,
            Object destCollection);
}
