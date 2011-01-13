package org.openl.rules.mapping.loader.dozer;

import java.util.ArrayList;
import java.util.List;

import org.dozer.loader.api.FieldsMappingOption;
import org.dozer.loader.api.FieldsMappingOptions;

public class FieldMappingOptionsBuilder {

    private List<FieldsMappingOption> options = new ArrayList<FieldsMappingOption>();

    public FieldMappingOptionsBuilder() {
    }

    public FieldMappingOptionsBuilder oneWay() {
        options.add(FieldsMappingOptions.fieldOneWay());
        return this;
    }

    public FieldMappingOptionsBuilder customConverterId(String converterId) {
        options.add(FieldsMappingOptions.customConverterId(converterId));
        return this;
    }

    public FieldMappingOptionsBuilder conditionId(String conditionId) {
        options.add(FieldsMappingOptions.conditionId(conditionId));
        return this;
    }

    public FieldsMappingOption[] build() {
        return options.toArray(new FieldsMappingOption[options.size()]);
    }

}
