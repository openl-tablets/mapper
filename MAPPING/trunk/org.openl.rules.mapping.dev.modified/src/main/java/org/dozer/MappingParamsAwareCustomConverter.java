package org.dozer;

public interface MappingParamsAwareCustomConverter extends CustomConverter {
    void setMappingParams(MappingParameters params);
}
