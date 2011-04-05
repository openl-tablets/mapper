package org.dozer;

/**
 * The base implementation of {@link MappingParamsAwareFieldMappingCondition}
 * interface. Current implementation prevents raise condition problem and
 * provide a new one method which should be implemented by user to get
 * implementation of {@link MappingParamsAwareFieldMappingCondition}
 * abstraction.
 */
public abstract class BaseMappingParamsAwareFieldMappingCondition implements MappingParamsAwareFieldMappingCondition {

    /**
     * Thread local variable which holds user defined mapping parameters.
     */
    private ThreadLocal<MappingParameters> parameters = new ThreadLocal<MappingParameters>();

    public void setMappingParams(MappingParameters params) {
        this.parameters.set(params);
    }

    public boolean mapField(Object sourceFieldValue, Object destFieldValue, Class<?> sourceType, Class<?> destType) {
        return mapField(parameters.get(), sourceFieldValue, destFieldValue, sourceType, destType);
    }

    /**
     * Indicates that field map should be processed by mapper.
     * 
     * @param params mapping parameters
     * @param sourceFieldValue source value
     * @param destFieldValue existing destination value
     * @param sourceType source type
     * @param destType destination type
     * @return <code>true</code> if field should be processed by mapper;
     *         <code>false</code> - otherwise
     */
    public abstract boolean mapField(MappingParameters params, Object sourceFieldValue, Object destFieldValue,
        Class<?> sourceType, Class<?> destType);

}
