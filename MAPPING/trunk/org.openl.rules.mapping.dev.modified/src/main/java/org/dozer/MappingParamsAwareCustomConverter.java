package org.dozer;

public interface MappingParamsAwareCustomConverter extends CustomConverter {
//    not tread safe
    void setMappingParams(MappingParameters params);
}
