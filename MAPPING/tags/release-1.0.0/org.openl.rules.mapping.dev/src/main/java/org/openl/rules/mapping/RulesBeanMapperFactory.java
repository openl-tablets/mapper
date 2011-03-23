package org.openl.rules.mapping;

import java.io.File;
import java.util.Map;

import org.dozer.CustomConverter;
import org.dozer.fieldmap.FieldMappingCondition;
import org.openl.conf.IOpenLConfiguration;
import org.openl.conf.OpenLConfiguration;
import org.openl.rules.mapping.exception.RulesMappingException;
import org.openl.rules.runtime.ApiBasedRulesEngineFactory;
import org.openl.runtime.ASourceCodeEngineFactory;
import org.openl.syntax.exception.CompositeOpenlException;
import org.openl.syntax.exception.SyntaxNodeException;

/**
 * The factory class which provides methods to create mapper instance.
 */
public final class RulesBeanMapperFactory {

    private static final String DEFAULT_OPENL_CONFIGURATION_PREFIX = "org.openl.rules.java::";

    private RulesBeanMapperFactory() {
    }

    /**
     * Creates mapper instance using file with mapping rule definitions.
     * 
     * @param source file with mapping rule definitions
     * @return mapper instance
     */
    public static Mapper createMapperInstance(File source) {
        return createMapperInstance(source, null, null);
    }

    /**
     * Creates mapper instance using file with mapping rule definitions.
     * 
     * @param source file with mapping rule definitions
     * @param customConvertersWithId external custom converters
     * @param conditionsWithId external conditions
     * @return mapper instance
     */
    public static Mapper createMapperInstance(File source, Map<String, CustomConverter> customConvertersWithId,
        Map<String, FieldMappingCondition> conditionsWithId) {
        ApiBasedRulesEngineFactory factory = new ApiBasedRulesEngineFactory(source);
        factory.setExecutionMode(true);
        Class<?> instanceClass;
        Object instance;

        try {
            instanceClass = factory.getInterfaceClass();
            instance = factory.makeInstance();

            // Check that compilation process completed successfully.
            if (factory.getCompiledOpenClass().hasErrors()) {
                // TODO: remove OpenL specific exception
                //
                throw new CompositeOpenlException("Compilation failed", new SyntaxNodeException[0], factory
                    .getCompiledOpenClass().getMessages());
            }

            // Get OpenL configuration object. The OpenL configuration object is
            // created by OpenL engine during compilation process and contains
            // information about imported types. We should use it to obtain
            // required types because if user defined, for example, convert
            // method as an external java static method and didn't use package
            // name (e.g. MyClass.myConvertMethod) we will not have enough
            // information to get convert method.
            //
            IOpenLConfiguration openLConfiguration = getOpenLConfiguration(factory);
            TypeResolver typeResolver = new RulesTypeResolver(openLConfiguration);

            return new RulesBeanMapper(instanceClass, instance, typeResolver, customConvertersWithId, conditionsWithId);
        } catch (Exception e) {
            throw new RulesMappingException(String.format("Cannot load mapping definitions from file: %s", source
                .getAbsolutePath()), e);
        }

    }

    private static IOpenLConfiguration getOpenLConfiguration(ASourceCodeEngineFactory factory) {
        String configName = DEFAULT_OPENL_CONFIGURATION_PREFIX + factory.getSourceCode().getUri(0);

        return OpenLConfiguration.getInstance(configName, factory.getUserContext());
    }
}
