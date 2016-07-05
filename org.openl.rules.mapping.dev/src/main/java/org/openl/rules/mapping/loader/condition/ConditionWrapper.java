package org.openl.rules.mapping.loader.condition;

import org.dozer.FieldMappingCondition;
import org.openl.rules.mapping.MappingParameters;
import org.dozer.MappingParamsAware;

/**
 * Links Dozer's field mapping condition abstraction with implementations what are
 * supported by rules mapper.
 * 
 * Current wrapper class joins condition abstractions using java
 * inheritance mechanism and defines which method of
 * {@link RulesFieldMappingCondition} instance will be invoked.
 */
class ConditionWrapper implements MappingParamsAware, FieldMappingCondition {

    private RulesFieldMappingCondition conditionProxy;
    private MappingParameters params;

    public ConditionWrapper(RulesFieldMappingCondition conditionProxy) {
        this.conditionProxy = conditionProxy;
    }

    public void setMappingParams(MappingParameters params) {
        this.params = params;
    }

    /**
     * Dispatches "map field" method invocation.
     */
    public boolean mapField(Object sourceFieldValue, Object destFieldValue, Class<?> sourceType, Class<?> destType) {
        if (params != null) {
            return conditionProxy.mapField(params, sourceFieldValue, destFieldValue, sourceType, destType);
        }

        return conditionProxy.mapField(sourceFieldValue, destFieldValue, sourceType, destType);
    }

}
