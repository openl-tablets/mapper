package org.dozer;

/**
 * @author: SStrukau
 */
public abstract class MapIdConverter extends BaseMappingParamsAwareCustomConverter {

    public abstract boolean canConvert(MappingParameters mappingParameters, Object existingDestinationFieldValue, Object sourceFieldValue, Class<?> destinationClass, Class<?> sourceClass);

    @Override
    public abstract String convert(MappingParameters mappingParameters, Object existingDestinationFieldValue, Object sourceFieldValue, Class<?> destinationClass, Class<?> sourceClass);

}