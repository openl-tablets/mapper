package org.openl.rules.mapping.loader.dozer;

import java.util.ArrayList;
import java.util.List;

import org.dozer.loader.api.FieldsMappingOption;
import org.dozer.loader.api.MappingOptions;

public class FieldMappingOptionsBuilder {

    private List<FieldsMappingOption> options = new ArrayList<FieldsMappingOption>();

    public FieldMappingOptionsBuilder() {
    }

    public FieldMappingOptionsBuilder mapNulls(boolean value) {
        options.add(MappingOptions.fieldMapNull(value));
        return this;
    }

    public FieldMappingOptionsBuilder mapEmptyStrings(boolean value) {
        options.add(MappingOptions.fieldMapEmptyString(value));
        return this;
    }
    
    public FieldMappingOptionsBuilder trimStrings(boolean value) {
        options.add(MappingOptions.fieldTrimStrings(value));
        return this;
    }
    
    public FieldMappingOptionsBuilder customConverterId(String converterId) {
        options.add(MappingOptions.customConverterId(converterId));
        return this;
    }

    public FieldMappingOptionsBuilder collectionItemDiscriminatorId(String collectionItemDiscriminatorId) {
        options.add(MappingOptions.collectionItemDiscriminatorId(collectionItemDiscriminatorId));
        return this;
    }
    
    public FieldMappingOptionsBuilder conditionId(String conditionId) {
        options.add(MappingOptions.conditionId(conditionId));
        return this;
    }
    
    public FieldMappingOptionsBuilder mapId(String mapId) {
        options.add(MappingOptions.useMapId(mapId));
        return this;
    }

    public FieldsMappingOption[] build() {
        return options.toArray(new FieldsMappingOption[options.size()]);
    }

}
