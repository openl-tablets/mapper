package org.openl.rules.mapping.data;

import org.apache.commons.lang.StringUtils;
import org.dozer.CustomConverter;

public class ToStringCustomConverter implements CustomConverter {

    public Object convert(Object existingDestinationFieldValue, Object sourceFieldValue, Class<?> destinationClass,
        Class<?> sourceClass) {
        
        if (sourceFieldValue != null) {
            return StringUtils.join((Object[])sourceFieldValue, ";");
        }
        
        return null;
    }

    
}
