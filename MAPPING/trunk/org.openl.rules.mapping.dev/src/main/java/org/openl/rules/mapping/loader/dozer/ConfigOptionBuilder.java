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
    
    public ConfigurationMappingOption[] build() {
        return options.toArray(new ConfigurationMappingOption[options.size()]);
    }
}
