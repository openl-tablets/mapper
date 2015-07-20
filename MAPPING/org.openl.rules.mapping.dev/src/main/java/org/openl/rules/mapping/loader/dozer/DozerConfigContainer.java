package org.openl.rules.mapping.loader.dozer;

import java.util.ArrayList;
import java.util.List;

import org.dozer.loader.api.BeanMappingBuilder;

public class DozerConfigContainer {

    private final List<BeanMappingBuilder> mappingBuilders = new ArrayList<BeanMappingBuilder>();

    public List<BeanMappingBuilder> getMappingBuilders() {
        return mappingBuilders;
    }
    
}
