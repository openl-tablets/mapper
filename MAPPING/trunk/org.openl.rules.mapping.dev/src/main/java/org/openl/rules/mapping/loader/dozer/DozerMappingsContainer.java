package org.openl.rules.mapping.loader.dozer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dozer.CustomConverter;
import org.dozer.fieldmap.FieldMappingCondition;
import org.dozer.loader.api.BeanMappingBuilder;

public class DozerMappingsContainer {

    private final List<BeanMappingBuilder> mappingBuilders = new ArrayList<BeanMappingBuilder>();
    private final Map<String, CustomConverter> converters = new HashMap<String, CustomConverter>();
    private final Map<String, FieldMappingCondition> conditions = new HashMap<String, FieldMappingCondition>();

    public List<BeanMappingBuilder> getMappingBuilders() {
        return mappingBuilders;
    }

    public Map<String, CustomConverter> getConverters() {
        return converters;
    }

    public Map<String, FieldMappingCondition> getConditions() {
        return conditions;
    }

}
