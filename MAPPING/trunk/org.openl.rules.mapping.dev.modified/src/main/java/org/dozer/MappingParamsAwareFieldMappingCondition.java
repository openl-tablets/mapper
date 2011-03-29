package org.dozer;

public interface MappingParamsAwareFieldMappingCondition extends FieldMappingCondition {
    void setMappingParams(MappingParameters params);
}
