package org.dozer;

/**
 * Public field map condition interface.
 * 
 * Field mapping condition is an expression which is evaluated as a boolean
 * value and defines whether field mapping to be processed.
 */
public interface FieldMappingCondition {

    /**
     * Indicates that field map should be processed by mapper.
     * 
     * @param sourceFieldValue source value
     * @param destFieldValue existing destination value
     * @param sourceType source type
     * @param destType destination type
     * @return <code>true</code> if field should be processed by mapper;
     *         <code>false</code> - otherwise
     */
    boolean mapField(Object sourceFieldValue, Object destFieldValue, Class<?> sourceType, Class<?> destType);

}