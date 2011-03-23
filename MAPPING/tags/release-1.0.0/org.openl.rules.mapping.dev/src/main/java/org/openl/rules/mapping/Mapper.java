package org.openl.rules.mapping;

/**
 * The base interface which defines mapper abstraction.
 */
public interface Mapper {

    /**
     * Performs mapping operation from source object to destination one.
     * 
     * @param source source object
     * @param destination destination object
     */
    void map(Object source, Object destination);

    /**
     * Creates new instance of destination object and performs mapping operation
     * from source object to destination one.
     * 
     * @param <T> destination class
     * @param source source object
     * @param destination destination object definition
     * @return instance of destination object
     */
    <T> T map(Object source, Class<T> destination);

}
