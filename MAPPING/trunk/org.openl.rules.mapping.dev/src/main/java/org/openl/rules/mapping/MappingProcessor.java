package org.openl.rules.mapping;

import java.util.Collection;

import org.dozer.DozerBeanMapper;
import org.dozer.MappingException;
import org.openl.rules.mapping.definition.BeanMap;
import org.openl.rules.mapping.loader.RulesMappingsLoader;
import org.openl.rules.mapping.loader.dozer.DozerBuilder;

/**
 * Internal class which processes mapping definitions and creates internal
 * mapping model. Current implementation uses OpenL Rules project as mapping
 * definitions source and Dozer bean mapping framework with some modifications
 * as a mapping tool. Not intended for direct use by Application code.
 */
class MappingProcessor {

    private RulesMappingsLoader mappingsLoader;
    private DozerBeanMapper beanMapper;
    private DozerBuilder dozerBuilder = new DozerBuilder();

    /**
     * Creates new instance of mappings processor using OpenL Rules project
     * instance information.
     * 
     * @param instanceClass class definition
     * @param instance instance object
     */
    public MappingProcessor(Class<?> instanceClass, Object instance) {
        this.mappingsLoader = new RulesMappingsLoader(instanceClass, instance);

        init();
    }

    private void init() {
        Collection<BeanMap> mappings = mappingsLoader.loadMappings();

        for (BeanMap mapping : mappings) {
            dozerBuilder.mappingBuilder().mapping(mapping);
        }

        beanMapper = dozerBuilder.buildMapper();
    }

    public void map(Object source, Object destination) {
        try {
            beanMapper.map(source, destination);
        } catch (MappingException e) {
            throw new RulesMappingException(e);
        }
    }

    public <T> T map(Object source, Class<T> destination) {
        try {
            return beanMapper.map(source, destination);
        } catch (MappingException e) {
            throw new RulesMappingException(e);
        }
    }

}