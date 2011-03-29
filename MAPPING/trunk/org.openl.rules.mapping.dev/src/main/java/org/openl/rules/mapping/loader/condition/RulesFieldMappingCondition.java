package org.openl.rules.mapping.loader.condition;

import org.dozer.MappingParameters;

/**
 * Defines contract of supported condition method signatures.
 */
public interface RulesFieldMappingCondition {

    boolean mapField(Object sourceFieldValue, Object destFieldValue, Class<?> sourceType, Class<?> destType);

    boolean mapField(MappingParameters params, Object sourceFieldValue, Object destFieldValue, Class<?> sourceType,
        Class<?> destType);
}
