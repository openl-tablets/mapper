package org.openl.rules.mapping.data.converter;

import org.dozer.CustomConverter;

public class ToStringCustomConverter implements CustomConverter {

    public Object convert(Object existingDestinationFieldValue, Object sourceFieldValue, Class<?> destinationClass,
        Class<?> sourceClass) {

        if (sourceFieldValue != null) {
            return sourceFieldValue.toString();
        }
        
        return null;
    }
    
}
