package org.dozer;

/**
 * The base implementation of {@link MappingParamsAwareCustomConverter}
 * interface. Current implementation prevents raise condition problem and
 * provide a new one method which should be implemented by user to get
 * implementation of {@link MappingParamsAwareCustomConverter} abstraction.
 */
public abstract class BaseMappingParamsAwareCustomConverter implements MappingParamsAware, CustomConverter {

    /**
     * Thread local variable which holds user defined mapping parameters.
     */
    private ThreadLocal<MappingParameters> parameters = new ThreadLocal<MappingParameters>();

    public void setMappingParams(MappingParameters params) {
        this.parameters.set(params);
    }

    public Object convert(Object existingDestinationFieldValue,
            Object sourceFieldValue,
            Class<?> destinationClass,
            Class<?> sourceClass) {
        return convert(parameters.get(),
            existingDestinationFieldValue,
            sourceFieldValue,
            destinationClass,
            sourceClass);
    }

    /**
     * Converts source value into destination value.
     * 
     * @param mappingParameters user defined mapping parameters
     * @param existingDestinationFieldValue existing destination value
     * @param sourceFieldValue source value
     * @param destinationClass destination class
     * @param sourceClass source class
     * @return converted value
     */
    public abstract Object convert(MappingParameters mappingParameters,
            Object existingDestinationFieldValue,
            Object sourceFieldValue,
            Class<?> destinationClass,
            Class<?> sourceClass);

}
