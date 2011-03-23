package org.openl.rules.mapping.loader.dozer;

import java.util.ArrayList;
import java.util.List;

import org.dozer.CustomConverter;
import org.dozer.loader.api.ConfigurationMappingOption;
import org.dozer.loader.api.MappingOptions;

public class ConfigOptionBuilder {

    private List<ConfigurationMappingOption> options = new ArrayList<ConfigurationMappingOption>();

    public ConfigOptionBuilder defaultConverter(CustomConverter converter, Class<?> classA, Class<?> classB) {
        options.add(MappingOptions.defaultCustomConverter(converter, classA, classB));
        return this;
    }
    
    public ConfigOptionBuilder dateFormat(String format) {
        options.add(MappingOptions.defaultDateFormat(format));
        return this;
    }
    
    public ConfigOptionBuilder wildcard(boolean value) {
        options.add(MappingOptions.defaultWildcard(value));
        return this;
    }

    public ConfigOptionBuilder trimStrings(boolean value) {
        options.add(MappingOptions.defaultTrimStrings(value));
        return this;
    }
    
    public ConfigOptionBuilder mapNulls(boolean value) {
        options.add(MappingOptions.defaultMapNulls(value));
        return this;
    }

    public ConfigOptionBuilder mapEmptyStrings(boolean value) {
        options.add(MappingOptions.defaultMapEmptyStrings(value));
        return this;
    }

    public ConfigOptionBuilder requiredFields(boolean value) {
        options.add(MappingOptions.defaultRequiredFields(value));
        return this;
    }
    
    public ConfigurationMappingOption[] build() {
        return options.toArray(new ConfigurationMappingOption[options.size()]);
    }
}
