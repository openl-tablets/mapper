package org.openl.rules.mapping.loader.converter;

import org.dozer.MappingParameters;
import org.dozer.MappingParamsAwareCustomConverter;

/**
 * Links Dozer's custom converter abstraction with implementations what are
 * supported by rules mapper.
 * 
 * Current wrapper class joins custom converter abstractions using java
 * inheritance mechanism and defines which method of
 * {@link RulesCustomConverter} instance will be invoked.
 */
class CustomConverterWrapper implements MappingParamsAwareCustomConverter {

    private RulesCustomConverter customConverterProxy;
    private MappingParameters params;

    public CustomConverterWrapper(RulesCustomConverter customConverterProxy) {
        this.customConverterProxy = customConverterProxy;
    }

    public void setMappingParams(MappingParameters params) {
        this.params = params;
    }

    /**
     * Dispatches convert method invocation.  
     */
    public Object convert(Object existingDestinationFieldValue, Object sourceFieldValue, Class<?> destinationClass,
        Class<?> sourceClass) {

        if (params != null) {
            return customConverterProxy.convert(params, existingDestinationFieldValue, sourceFieldValue,
                destinationClass, sourceClass);
        }

        return customConverterProxy.convert(existingDestinationFieldValue, sourceFieldValue, destinationClass,
            sourceClass);
    }

}
