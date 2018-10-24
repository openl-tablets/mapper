package org.openl.rules.mapping;

import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.dozer.BeanFactory;
import org.dozer.CustomConverter;
import org.dozer.DozerBeanMapper;
import org.dozer.DozerEventListener;
import org.dozer.FieldMappingCondition;
import org.dozer.loader.api.BeanMappingBuilder;
import org.openl.conf.IOpenLConfiguration;
import org.openl.conf.OpenLConfiguration;
import org.openl.rules.mapping.definition.BeanMap;
import org.openl.rules.mapping.definition.BeanMapConfiguration;
import org.openl.rules.mapping.definition.Configuration;
import org.openl.rules.mapping.definition.ConverterDescriptor;
import org.openl.rules.mapping.exception.RulesMappingException;
import org.openl.rules.mapping.loader.RulesMappingsLoader;
import org.openl.rules.mapping.loader.dozer.DozerConfigBuilder;
import org.openl.rules.mapping.loader.dozer.DozerConfigContainer;
import org.openl.rules.mapping.loader.dozer.DozerMappingBuilder;
import org.openl.rules.mapping.loader.dozer.DozerMappingsContainer;
import org.openl.rules.runtime.RulesEngineFactory;

/**
 * The factory class which provides methods to create mapper instance.
 */
public final class RulesBeanMapperFactory {

    private RulesBeanMapperFactory() {
    }

    /**
     * Creates mapper instance using input stream with mapping rule definitions.
     *
     * @param source input stream with mapping rule definitions
     * @return mapper instance
     */
    public static Mapper createMapperInstance(URL source) {
        return createMapperInstance(source, null, null);
    }

    /**
     * Creates mapper instance using file with mapping rule definitions.
     * 
     * @param source input stream with mapping rule definitions
     * @param customConvertersWithId external custom converters
     * @param conditionsWithId external conditions
     * @return mapper instance
     */
    public static Mapper createMapperInstance(URL source,
            Map<String, CustomConverter> customConvertersWithId,
            Map<String, FieldMappingCondition> conditionsWithId) {
        return createMapperInstance(source, customConvertersWithId, conditionsWithId, null, null);
    }

    /**
     * Creates mapper instance using file with mapping rule definitions.
     * 
     * @param source input stream with mapping rule definitions
     * @param customConvertersWithId external custom converters
     * @param conditionsWithId external conditions
     * @param eventListeners dozer event listeners
     * @return mapper instance
     */
    public static Mapper createMapperInstance(URL source,
            Map<String, CustomConverter> customConvertersWithId,
            Map<String, FieldMappingCondition> conditionsWithId,
            List<DozerEventListener> eventListeners) {
        return createMapperInstance(source, customConvertersWithId, conditionsWithId, null, eventListeners);
    }

    /**
     * Creates mapper instance using file with mapping rule definitions.
     *
     * @param source file with mapping rule definitions
     * @param customConvertersWithId external custom converters
     * @param conditionsWithId external conditions
     * @param factories custom bean factories
     * @return mapper instance
     */
    public static Mapper createMapperInstance(URL source,
            Map<String, CustomConverter> customConvertersWithId,
            Map<String, FieldMappingCondition> conditionsWithId,
            Map<String, BeanFactory> factories) {
        return createMapperInstance(source, customConvertersWithId, conditionsWithId, factories, null);
    }

    /**
     * Creates mapper instance using file with mapping rule definitions.
     *
     * @param source file with mapping rule definitions
     * @param customConvertersWithId external custom converters
     * @param conditionsWithId external conditions
     * @param factories custom bean factories
     * @param eventListeners dozer event listeners
     * @return mapper instance
     */
    public static Mapper createMapperInstance(URL source,
            Map<String, CustomConverter> customConvertersWithId,
            Map<String, FieldMappingCondition> conditionsWithId,
            Map<String, BeanFactory> factories,
            List<DozerEventListener> eventListeners) {

        try {

            RulesEngineFactory factory = new RulesEngineFactory(source);
            factory.setExecutionMode(true);

            Class<?> instanceClass = factory.getInterfaceClass();
            Object instance = factory.newInstance();

            // Check that compilation process completed successfully.
            factory.getCompiledOpenClass().throwErrorExceptionsIfAny();

            // Get OpenL configuration object. The OpenL configuration object is
            // created by OpenL engine during compilation process and contains
            // information about imported types. We should use it to obtain
            // required types because if user defined, for example, convert
            // method as an external java static method and didn't use package
            // name (e.g. MyClass.myConvertMethod) we will not have enough
            // information to get convert method.
            //
            TypeResolver typeResolver;
            String name = "org.openl.rules.java::" + factory.getSourceCode().getUri();
            IOpenLConfiguration config = OpenLConfiguration.getInstance(name, factory.getUserContext());

            typeResolver = config != null ? new RulesTypeResolver(config) : null;

            org.dozer.Mapper dozerMapper = init(instanceClass,
                instance,
                typeResolver,
                customConvertersWithId,
                conditionsWithId,
                factories,
                eventListeners);
            return new MappingProxy(dozerMapper);
        } catch (Exception e) {
            throw new RulesMappingException("Cannot load mapping definitions from the URL: " + source, e);
        }
    }

    @SuppressWarnings("unchecked")
    private static org.dozer.Mapper init(Class<?> instanceClass,
            Object instance,
            TypeResolver typeResolver,
            Map<String, CustomConverter> customConvertersWithId,
            Map<String, FieldMappingCondition> conditionsWithId,
            Map<String, BeanFactory> factories,
            List<DozerEventListener> eventListeners) {

        RulesMappingsLoader mappingsLoader = new RulesMappingsLoader(instanceClass, instance, typeResolver);
        DozerMappingBuilder mappingBuilder = new DozerMappingBuilder();
        DozerConfigBuilder configBuilder = new DozerConfigBuilder();

        mappingBuilder.customConvertersWithId(customConvertersWithId);
        mappingBuilder.conditionsWithId(conditionsWithId);

        mappingBuilder.eventListeners(eventListeners);

        Collection<ConverterDescriptor> defaultConverters = mappingsLoader.loadDefaultConverters();

        for (ConverterDescriptor converter : defaultConverters) {
            configBuilder.defaultConverter(converter);
        }

        Configuration globalConfiguration = mappingsLoader.loadConfiguration();

        configBuilder.dateFormat(globalConfiguration.getDateFormat());
        configBuilder.wildcard(globalConfiguration.isWildcard());
        configBuilder.trimStrings(globalConfiguration.isTrimStrings());
        configBuilder.mapNulls(globalConfiguration.isMapNulls());
        configBuilder.mapEmptyStrings(globalConfiguration.isMapEmptyStrings());
        configBuilder.requiredFields(globalConfiguration.isRequiredFields());
        configBuilder.beanFactory(globalConfiguration.getBeanFactory());

        Map<String, BeanMapConfiguration> mappingConfigurations = mappingsLoader
            .loadBeanMapConfiguraitons(globalConfiguration);

        Collection<BeanMap> mappings = mappingsLoader.loadMappings(mappingConfigurations, globalConfiguration);

        for (BeanMap mapping : mappings) {
            mappingBuilder.mapping(mapping);
        }

        DozerBeanMapper mapper = new DozerBeanMapper();

        DozerConfigContainer configContainer = configBuilder.build();

        for (BeanMappingBuilder config : configContainer.getMappingBuilders()) {
            mapper.addMapping(config);
        }

        DozerMappingsContainer mappingsContainer = mappingBuilder.build();

        mapper.setCustomConvertersWithId(mappingsContainer.getConverters());
        mapper.setMappingConditionsWithId(mappingsContainer.getConditions());
        mapper.setCollectionItemDiscriminatorsWithId(mappingsContainer.getCollectionItemDiscriminators());
        mapper.setEventListeners(mappingsContainer.getEventListeners());

        for (BeanMappingBuilder mapping : mappingsContainer.getMappingBuilders()) {
            mapper.addMapping(mapping);
        }

        if (factories != null) {
            mapper.setFactories(factories);
        }
        return mapper;
    }

}
