package org.dozer;

/**
 * Extends contract of field mapping condition with mapping parameters. They can be
 * used by implementation to change decision-making flow.
 * 
 * NOTE: Defined contract is not thread-safe. An implementation of current
 * interface has to prevent raise condition problem.
 * 
 * @see BaseMappingParamsAwareFieldMappingCondition
 */
public interface MappingParamsAwareFieldMappingCondition extends FieldMappingCondition {
    void setMappingParams(MappingParameters params);
}
