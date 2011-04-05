package org.dozer;

/**
 * Extends contract of custom converter with mapping parameters. They can be
 * used by implementation to change conversion flow.
 * 
 * NOTE: Defined contract is not thread-safe. An implementation of current
 * interface has to prevent raise condition problem.
 * 
 * @see BaseMappingParamsAwareCustomConverter
 */
public interface MappingParamsAwareCustomConverter extends CustomConverter {
    void setMappingParams(MappingParameters params);
}
