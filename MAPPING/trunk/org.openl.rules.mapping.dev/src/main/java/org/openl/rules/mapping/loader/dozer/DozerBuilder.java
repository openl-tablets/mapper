package org.openl.rules.mapping.loader.dozer;

import org.dozer.DozerBeanMapper;
import org.dozer.loader.api.BeanMappingBuilder;

/**
 * The helper class which provides methods to build Dozer's bean mapper
 * instance.
 */
public class DozerBuilder {

    private DozerMappingBuilder mappingBuilder = new DozerMappingBuilder();
    private DozerConfigBuilder configBuilder = new DozerConfigBuilder();

    public DozerMappingBuilder mappingBuilder() {
        return mappingBuilder;
    }
    
    public DozerConfigBuilder configBuilder() {
        return configBuilder;
    }

    public DozerBeanMapper buildMapper() {
        DozerBeanMapper mapper = new DozerBeanMapper();

        DozerConfigContainer configContainer = configBuilder.build();

        for (BeanMappingBuilder config : configContainer.getMappingBuilders()) {
            mapper.addMapping(config);
        }

        DozerMappingsContainer mappingsContainer = mappingBuilder.build();

        mapper.setCustomConvertersWithId(mappingsContainer.getConverters());
        mapper.setMappingConditionsWithId(mappingsContainer.getConditions());

        for (BeanMappingBuilder mapping : mappingsContainer.getMappingBuilders()) {
            mapper.addMapping(mapping);
        }

        return mapper;
    }
}
