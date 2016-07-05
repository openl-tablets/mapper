package org.openl.rules.mapping.loader.converter;

import org.openl.rules.mapping.MappingParameters;

/**
 * Defines contract of supported convert method signatures.
 * 
 * The following convention is used:
 * <ul>
 * <li>last 4 parameters must be (in the same order):
 * existingDestinationFieldValue, sourceFieldValue, destinationClass,
 * sourceClass</li>
 * </ul>
 */
public interface RulesCustomConverter {

    Object convert(Object existingDestinationFieldValue, Object sourceFieldValue, Class<?> destinationClass,
        Class<?> sourceClass);

    Object convert(MappingParameters params, Object existingDestinationFieldValue, Object sourceFieldValue,
        Class<?> destinationClass, Class<?> sourceClass);
}
