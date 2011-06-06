package org.openl.rules.mapping;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.dozer.CustomConverter;
import org.dozer.FieldMappingCondition;
import org.openl.ICompileContext;
import org.openl.message.OpenLMessage;
import org.openl.rules.mapping.exception.RulesMappingException;
import org.openl.rules.mapping.validation.MappingBeanValidator;
import org.openl.rules.mapping.validation.OpenLDataBeanValidator;
import org.openl.rules.runtime.ApiBasedRulesEngineFactory;
import org.openl.runtime.AOpenLEngineFactory;
import org.openl.runtime.ASourceCodeEngineFactory;
import org.openl.syntax.exception.CompositeOpenlException;
import org.openl.syntax.exception.SyntaxNodeException;
import org.openl.validation.IOpenLValidator;

/**
 * The factory class which provides methods to create mapper instance.
 */
public final class RulesBeanMapperFactory {

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
    public static Mapper createMapperInstance(File source,
            Map<String, CustomConverter> customConvertersWithId,
            Map<String, FieldMappingCondition> conditionsWithId) {
        return createMapperInstance(source, customConvertersWithId, conditionsWithId, true);
    }

    /**
     * Creates mapper instance using file with mapping rule definitions.
     * 
     * @param source file with mapping rule definitions
     * @param customConvertersWithId external custom converters
     * @param conditionsWithId external conditions
     * @param executionMode execution mode flag
     * @return mapper instance
     */
    public static Mapper createMapperInstance(File source,
            Map<String, CustomConverter> customConvertersWithId,
            Map<String, FieldMappingCondition> conditionsWithId,
            boolean executionMode) {

        try {
            ApiBasedRulesEngineFactory factory = initEngine(source, executionMode);

            Class<?> instanceClass = factory.getInterfaceClass();
            Object instance = factory.makeInstance();

            // Check that compilation process completed successfully.
            if (factory.getCompiledOpenClass().hasErrors()) {
                // TODO: remove OpenL specific exception
                List<OpenLMessage> messages = factory.getCompiledOpenClass().getMessages();
                throw new CompositeOpenlException("Compilation failed", new SyntaxNodeException[0], messages);
            }

            // Get OpenL configuration object. The OpenL configuration object is
            // created by OpenL engine during compilation process and contains
            // information about imported types. We should use it to obtain
            // required types because if user defined, for example, convert
            // method as an external java static method and didn't use package
            // name (e.g. MyClass.myConvertMethod) we will not have enough
            // information to get convert method.
            //
            TypeResolver typeResolver = null;
            if (executionMode) {
                typeResolver = getTypeResolver(factory);
            } else {
                typeResolver = OpenLReflectionUtils.getTypeResolver(factory.getCompiledOpenClass().getOpenClass());
            }

            return new RulesBeanMapper(instanceClass, instance, typeResolver, customConvertersWithId, conditionsWithId);
        } catch (Exception e) {
            throw new RulesMappingException(String.format("Cannot load mapping definitions from file: %s",
                source.getAbsolutePath()), e);
        }
    }

    /**
     * Initializes OpenL engine.
     * 
     * @param source OpenL project source file
     * @param executionMode execution mode flag
     * @return rules engine instance
     */
    public static ApiBasedRulesEngineFactory initEngine(File source, boolean executionMode) {
        
        ApiBasedRulesEngineFactory factory = new ApiBasedRulesEngineFactory(source);
        factory.setExecutionMode(executionMode);

        ICompileContext compileContext = factory.getOpenL().getCompileContext();
        boolean validationEnabled = compileContext != null && compileContext.isValidationEnabled();

        if (!executionMode && validationEnabled) {
            registerTypeValidator(factory, new MappingBeanValidator());
        }

        return factory;
    }

    private synchronized static void registerTypeValidator(AOpenLEngineFactory factory,
            OpenLDataBeanValidator<?> validator) {

        for (IOpenLValidator regValidator : factory.getOpenL().getCompileContext().getValidators()) {
            if (regValidator.getClass() == validator.getClass()) {
                return;
            }
        }

        factory.getOpenL().getCompileContext().addValidator(validator);
    }

    private static TypeResolver getTypeResolver(ASourceCodeEngineFactory factory) {
        return OpenLReflectionUtils.getTypeResolver(factory.getSourceCode().getUri(0), factory.getUserContext());
    }
}
