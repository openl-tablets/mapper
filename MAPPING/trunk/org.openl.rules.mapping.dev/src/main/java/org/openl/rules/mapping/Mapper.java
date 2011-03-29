package org.openl.rules.mapping;

import org.dozer.MappingContext;

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

    /**
     * Performs mapping operation from source object to destination one.
     * 
     * @param source source object
     * @param destination destination object
     * @param params mapping parameters
     */
    void map(Object source, Object destination, MappingContext context);

    /**
     * Creates new instance of destination object and performs mapping operation
     * from source object to destination one.
     * 
     * @param <T> destination class
     * @param source source object
     * @param destination destination object definition
     * @param params mapping parameters
     * @return instance of destination object
     */
    <T> T map(Object source, Class<T> destination, MappingContext context);
}
