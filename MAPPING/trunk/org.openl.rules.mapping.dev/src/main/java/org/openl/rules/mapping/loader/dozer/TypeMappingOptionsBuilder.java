package org.openl.rules.mapping.loader.dozer;

import java.util.ArrayList;
import java.util.List;

import org.dozer.loader.api.TypeMappingOption;
import org.dozer.loader.api.TypeMappingOptions;

public class TypeMappingOptionsBuilder {

    private List<TypeMappingOption> options = new ArrayList<TypeMappingOption>();

    public TypeMappingOptionsBuilder() {
    }
    
    public TypeMappingOptionsBuilder oneWay() {
        options.add(TypeMappingOptions.oneWay());
        return this;
    }
    
    public TypeMappingOptionsBuilder wildcard(boolean value) {
        options.add(TypeMappingOptions.wildcard(value));
        return this;
    }
    
    public TypeMappingOption[] build() {
        return options.toArray(new TypeMappingOption[options.size()]);
    }
}
