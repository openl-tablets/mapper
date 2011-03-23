package org.openl.rules.mapping.definition;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("conditionId", conditionId).append(
            "condition", condition).toString();
    }

}
