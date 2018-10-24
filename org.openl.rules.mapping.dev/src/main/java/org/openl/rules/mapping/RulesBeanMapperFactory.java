package org.openl.rules.mapping;

import java.net.URL;
import java.util.List;
import java.util.Map;

import org.dozer.BeanFactory;
import org.dozer.CustomConverter;
import org.dozer.DozerEventListener;
import org.dozer.FieldMappingCondition;
import org.openl.conf.IOpenLConfiguration;
import org.openl.conf.OpenLConfiguration;
import org.openl.rules.mapping.exception.RulesMappingException;
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

            RulesEngineFactory factory1 = new RulesEngineFactory(source);
            factory1.setExecutionMode(true);

            RulesEngineFactory factory = factory1;

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

            return new MappingProcessor(instanceClass, instance, typeResolver, customConvertersWithId, conditionsWithId, factories, eventListeners);
        } catch (Exception e) {
            throw new RulesMappingException("Cannot load mapping definitions from the URL: " + source, e);
        }
    }

}
