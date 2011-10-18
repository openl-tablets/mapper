package org.openl.rules.mapping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.StringUtils;
import org.dozer.CustomConverter;
import org.dozer.DozerBeanMapper;
import org.dozer.FieldMappingCondition;
import org.dozer.MappingContext;
import org.dozer.MappingException;
import org.openl.rules.mapping.definition.BeanMap;
import org.openl.rules.mapping.definition.BeanMapConfiguration;
import org.openl.rules.mapping.definition.Configuration;
import org.openl.rules.mapping.definition.ConverterDescriptor;
import org.openl.rules.mapping.exception.RulesMappingException;
import org.openl.rules.mapping.loader.RulesMappingsLoader;
import org.openl.rules.mapping.loader.dozer.DozerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Internal class which processes mapping definitions and creates internal
 * mapping model. Current implementation uses OpenL Rules project as mapping
 * definitions source and Dozer bean mapping framework with some modifications
 * as a mapping tool. Not intended for direct use by Application code.
 */
class MappingProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(MappingProcessor.class);

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
    public MappingProcessor(Class<?> instanceClass,
            Object instance,
            TypeResolver typeResolver,
            Map<String, CustomConverter> customConvertersWithId,
            Map<String, FieldMappingCondition> conditionsWithId) {
        this.mappingsLoader = new RulesMappingsLoader(instanceClass, instance, typeResolver);
        this.customConvertersWithId = customConvertersWithId;
        this.conditionsWithId = conditionsWithId;

        init();
    }

    @SuppressWarnings("unchecked")
    private void init() {

        dozerBuilder.mappingBuilder().customConvertersWithId(customConvertersWithId);
        dozerBuilder.mappingBuilder().conditionsWithId(conditionsWithId);

        Collection<ConverterDescriptor> defaultConverters = mappingsLoader.loadDefaultConverters();

        for (ConverterDescriptor converter : defaultConverters) {
            dozerBuilder.configBuilder().defaultConverter(converter);
        }

        if (LOG.isDebugEnabled()) {
            Collection<String> defaultConverterLogEntries = CollectionUtils.collect(defaultConverters,
                new Transformer() {

                    @Override
                    public String transform(Object arg) {
                        ConverterDescriptor descriptor = (ConverterDescriptor) arg;
                        return descriptor.getConverterId();
                    }

                });

            LOG.debug("Default converters:\n" + StringUtils.join(defaultConverterLogEntries, "\n"));
            LOG.debug("External converters: " + StringUtils.join(customConvertersWithId == null ? new ArrayList<Object>(0)
                                                                                               : customConvertersWithId.keySet(),
                ", "));
            LOG.debug("External conditions: " + StringUtils.join(conditionsWithId == null ? new ArrayList<Object>(0)
                                                                                         : conditionsWithId.keySet(),
                ", "));
        }

        Configuration globalConfiguration = mappingsLoader.loadConfiguration();

        dozerBuilder.configBuilder().dateFormat(globalConfiguration.getDateFormat());
        dozerBuilder.configBuilder().wildcard(globalConfiguration.isWildcard());
        dozerBuilder.configBuilder().trimStrings(globalConfiguration.isTrimStrings());
        dozerBuilder.configBuilder().mapNulls(globalConfiguration.isMapNulls());
        dozerBuilder.configBuilder().mapEmptyStrings(globalConfiguration.isMapEmptyStrings());
        dozerBuilder.configBuilder().requiredFields(globalConfiguration.isRequiredFields());

        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("Global configuration: dateFormat=%s, wildcard=%s, trimStrings=%s, mapNulls=%s, mapEmptyStrings=%s, requiredFields=%s",
                globalConfiguration.getDateFormat(),
                globalConfiguration.isWildcard(),
                globalConfiguration.isTrimStrings(),
                globalConfiguration.isMapNulls(),
                globalConfiguration.isMapEmptyStrings(),
                globalConfiguration.isRequiredFields()));
        }

        Map<String, BeanMapConfiguration> mappingConfigurations = mappingsLoader.loadBeanMapConfiguraitons(globalConfiguration);

        if (LOG.isDebugEnabled()) {
            Collection<String> beanConfigLogEntries = CollectionUtils.collect(mappingConfigurations.values(),
                new Transformer() {

                    @Override
                    public String transform(Object arg) {
                        BeanMapConfiguration conf = (BeanMapConfiguration) arg;
                        return String.format("[classA=%s, classB=%s, classABeanFactory=%s, classBBeanFactory=%s, mapNulls=%s, mapEmptyStrings=%s, trimStrings=%s, requiredFields=%s, wildcard=%s, dateFormat=%s]",
                            conf.getClassA(),
                            conf.getClassB(),
                            conf.getClassABeanFactory(),
                            conf.getClassBBeanFactory(),
                            conf.isMapNulls(),
                            conf.isMapEmptyStrings(),
                            conf.isTrimStrings(),
                            conf.isRequiredFields(),
                            conf.isWildcard(),
                            conf.getDateFormat());
                    }
                });

            LOG.debug("Bean level configurations:\n" + StringUtils.join(beanConfigLogEntries, "\n"));
        }

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

    public void map(Object source, Object destination, MappingContext context) {
        try {
            beanMapper.map(source, destination, context);
        } catch (MappingException e) {
            throw new RulesMappingException(e);
        }
    }

    public <T> T map(Object source, Class<T> destination, MappingContext context) {
        try {
            return beanMapper.map(source, destination, context);
        } catch (MappingException e) {
            throw new RulesMappingException(e);
        }
    }
}