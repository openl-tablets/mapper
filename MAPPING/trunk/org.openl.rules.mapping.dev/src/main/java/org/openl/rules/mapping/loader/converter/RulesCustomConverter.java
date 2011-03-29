package org.openl.rules.mapping.loader.converter;

import org.dozer.MappingParameters;

/**
 * Defines contract of supported convert method signatures.
 */
public interface RulesCustomConverter {

    Object convert(Object existingDestinationFieldValue, Object sourceFieldValue,
        Class<?> destinationClass, Class<?> sourceClass);

    Object convert(MappingParameters params, Object existingDestinationFieldValue, Object sourceFieldValue,
        Class<?> destinationClass, Class<?> sourceClass);
}
