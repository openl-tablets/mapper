package org.openl.rules.mapping;

import java.util.Collection;
import java.util.Map;

import org.dozer.CustomConverter;
import org.dozer.DozerBeanMapper;
import org.dozer.MappingException;
import org.dozer.fieldmap.FieldMappingCondition;
import org.openl.rules.mapping.definition.BeanMap;
import org.openl.rules.mapping.definition.BeanMapConfiguration;
import org.openl.rules.mapping.definition.Configuration;
import org.openl.rules.mapping.definition.ConverterDescriptor;
import org.openl.rules.mapping.exception.RulesMappingException;
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

    private Map<String, CustomConverter> customConvertersWithId;
    private Map<String, FieldMappingCondition> conditionsWithId;
    
    /**
     * Creates new instance of mappings processor using OpenL Rules project
     * instance information.
     * 
     * @param instanceClass class definition
     * @param instance instance object
     * @param typeResolver type resolver
     * @param customConvertersWithId user custom converters
     */
    public MappingProcessor(Class<?> instanceClass, Object instance, TypeResolver typeResolver,
        Map<String, CustomConverter> customConvertersWithId, Map<String, FieldMappingCondition> conditionsWithId) {
        this.mappingsLoader = new RulesMappingsLoader(instanceClass, instance, typeResolver);
        this.customConvertersWithId = customConvertersWithId;
        this.conditionsWithId = conditionsWithId;

        init();
    }

    private void init() {

        dozerBuilder.mappingBuilder().customConvertersWithId(customConvertersWithId);
        dozerBuilder.mappingBuilder().conditionsWithId(conditionsWithId);
        
        Collection<ConverterDescriptor> defaultConverters = mappingsLoader.loadDefaultConverters();

        for (ConverterDescriptor converter : defaultConverters) {
            dozerBuilder.configBuilder().defaultConverter(converter);
        }

        Configuration globalConfiguration = mappingsLoader.loadConfiguration();
        
        dozerBuilder.configBuilder().dateFormat(globalConfiguration.getDateFormat());
        dozerBuilder.configBuilder().wildcard(globalConfiguration.isWildcard());
        dozerBuilder.configBuilder().trimStrings(globalConfiguration.isTrimStrings());
        dozerBuilder.configBuilder().mapNulls(globalConfiguration.isMapNulls());
        dozerBuilder.configBuilder().mapEmptyStrings(globalConfiguration.isMapEmptyStrings());
        dozerBuilder.configBuilder().requiredFields(globalConfiguration.isRequiredFields());
        
        Map<String, BeanMapConfiguration> mappingConfigurations = mappingsLoader
            .loadBeanMapConfiguraitons(globalConfiguration);

        Collection<BeanMap> mappings = mappingsLoader.loadMappings(mappingConfigurations, globalConfiguration);

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