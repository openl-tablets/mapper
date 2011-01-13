package org.openl.rules.mapping.definition;

import org.dozer.fieldmap.FieldMappingCondition;

public class ConditionDescriptor {

    private String conditionId;
    private FieldMappingCondition condition;

    public ConditionDescriptor(String conditionId, FieldMappingCondition condition) {
        this.conditionId = conditionId;
        this.condition = condition;
    }

    public String getConditionId() {
        return conditionId;
    }

    public FieldMappingCondition getFieldMappingCondition() {
        return condition;
    }

}
