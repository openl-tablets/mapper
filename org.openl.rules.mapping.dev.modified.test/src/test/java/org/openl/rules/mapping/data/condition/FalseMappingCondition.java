package org.openl.rules.mapping.data.condition;

import org.dozer.FieldMappingCondition;

public class FalseMappingCondition implements FieldMappingCondition {

    public boolean mapField(Object sourceFieldValue, Object destFieldValue, Class<?> sourceType, Class<?> destType) {
        return false;
    }
    
}
