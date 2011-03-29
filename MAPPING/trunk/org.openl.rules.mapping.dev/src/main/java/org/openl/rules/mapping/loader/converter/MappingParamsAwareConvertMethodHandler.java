package org.openl.rules.mapping.loader.converter;

import java.lang.reflect.Method;

import org.dozer.MappingParameters;
import org.dozer.MappingParamsAwareCustomConverter;
import org.openl.rules.mapping.loader.ProxyMethodHandler;

/**
 * Defines handler class which handle
 * {@link MappingParamsAwareCustomConverter#convert(Object, Object, Class, Class)}
 * method invocations.
 * 
 */
class MappingParamsAwareConvertMethodHandler extends ProxyMethodHandler {

    public Object[] getParameterValues(Object[] args) {
        Object mappingParams = args[0];
        Object destValue = args[1];
        Object srcValue = args[2];

        return new Object[] { mappingParams, srcValue, destValue };
    }

    public Class<?>[] getParameterTypes(Object[] args) {
        Class<?> destClass = (Class<?>) args[3];
        Class<?> srcClass = (Class<?>) args[4];

        return new Class<?>[] { MappingParameters.class, srcClass, destClass };
    }

    public boolean canHandle(Method method) {
        // Parameters list of RulesCustomConverter.convert method:
        // 1) MappingParameters mappingParameters,
        // 2) Object existingDestinationFieldValue,
        // 3) Object sourceFieldValue,
        // 4) Class<?> destinationClass,
        // 5) Class<?> sourceClass);
        //
        if (method == null) {
            return false;
        }

        Class<?>[] params = method.getParameterTypes();

        return params.length == 5 && MappingParameters.class == params[0] && Object.class == params[1] && Object.class == params[2] && Class.class == params[3] && Class.class == params[4];
    }

}
