package org.openl.rules.mapping.loader.dozer;

import java.util.ArrayList;
import java.util.List;

import org.dozer.loader.api.MappingOptions;
import org.dozer.loader.api.TypeMappingOption;

public class TypeMappingOptionsBuilder {

    private List<TypeMappingOption> options = new ArrayList<TypeMappingOption>();

    public TypeMappingOptionsBuilder oneWay() {
        options.add(MappingOptions.oneWay());
        return this;
    }
    
    public TypeMappingOptionsBuilder wildcard(boolean value) {
        options.add(MappingOptions.wildcard(value));
        return this;
    }

    public TypeMappingOptionsBuilder trimStrings(boolean value) {
        options.add(MappingOptions.trimStrings(value));
        return this;
    }

    public TypeMappingOptionsBuilder dateFormat(String value) {
        options.add(MappingOptions.dateFormat(value));
        return this;
    }

    
    public TypeMappingOption[] build() {
        return options.toArray(new TypeMappingOption[options.size()]);
    }
}
