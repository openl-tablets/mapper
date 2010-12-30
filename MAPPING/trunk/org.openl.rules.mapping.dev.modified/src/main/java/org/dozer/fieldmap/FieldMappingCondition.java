package org.dozer.fieldmap;

/**
 * Public field map condition interface.
 * 
 * Field mapping condition is a expression which is evaluated as a boolean value
 * and defines whether field mapping to be processed.
 * 
 * @author Alexey Gamanovich
 * 
 */
public interface FieldMappingCondition {

    boolean mapField(Object sourceFieldValue, Object destFieldValue, Class<?> sourceType, Class<?> destType);

}