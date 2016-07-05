package org.openl.rules.mapping.loader.condition;

import org.openl.rules.mapping.MappingParameters;

/**
 * Defines contract of supported condition method signatures.
 * 
 * The following convention is used:
 * <ul>
 * <li>last 4 parameters must be (in the same order): sourceFieldValue,
 * existingDestinationFieldValue, sourceClass, destinationClass</li>
 * </ul>
 */
public interface RulesFieldMappingCondition {

    boolean mapField(Object sourceFieldValue, Object destFieldValue, Class<?> sourceType, Class<?> destType);

    boolean mapField(MappingParameters params, Object sourceFieldValue, Object destFieldValue, Class<?> sourceType,
        Class<?> destType);
}
