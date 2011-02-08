package org.openl.rules.mapping.loader.dozer;

import java.util.ArrayList;
import java.util.List;

import org.dozer.loader.api.FieldsMappingOption;
import org.dozer.loader.api.MappingOptions;

public class FieldMappingOptionsBuilder {

    private List<FieldsMappingOption> options = new ArrayList<FieldsMappingOption>();

    public FieldMappingOptionsBuilder() {
    }

    public FieldMappingOptionsBuilder oneWay() {
        options.add(MappingOptions.fieldOneWay());
        return this;
    }

    public FieldMappingOptionsBuilder mapNulls(boolean value) {
        options.add(MappingOptions.mapNulls(value));
        return this;
    }

    public FieldMappingOptionsBuilder mapEmptyStrings(boolean value) {
        options.add(MappingOptions.mapEmptyStrings(value));
        return this;
    }
    
    public FieldMappingOptionsBuilder customConverterId(String converterId) {
        options.add(MappingOptions.customConverterId(converterId));
        return this;
    }

    public FieldMappingOptionsBuilder conditionId(String conditionId) {
        options.add(MappingOptions.conditionId(conditionId));
        return this;
    }

    public FieldsMappingOption[] build() {
        return options.toArray(new FieldsMappingOption[options.size()]);
    }

}
