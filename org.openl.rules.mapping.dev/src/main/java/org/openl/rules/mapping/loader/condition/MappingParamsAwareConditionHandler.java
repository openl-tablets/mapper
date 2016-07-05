package org.openl.rules.mapping.loader.condition;

import java.lang.reflect.Method;

import org.openl.rules.mapping.MappingParameters;
import org.openl.rules.mapping.loader.ProxyMethodHandler;

/**
 * Defines handler class which handle
 * {@link MappingParamsAwareFieldMappingCondition#mapField(Object, Object, Class, Class)}
 * method invocations.
 * 
 */
class MappingParamsAwareConditionHandler extends ProxyMethodHandler {

    public Object[] getParameterValues(Object[] args) {
        Object mappingParams = args[0];
        Object srcValue = args[1];
        Object destValue = args[2];

        return new Object[] { mappingParams, srcValue, destValue };
    }

    public Class<?>[] getParameterTypes(Object[] args) {
        Class<?> srcClass = (Class<?>) args[3];
        Class<?> destClass = (Class<?>) args[4];

        return new Class<?>[] { MappingParameters.class, srcClass, destClass };
    }

    public boolean canHandle(Method method) {
        // Parameters list of FieldMappingCondition.mapField method :
        // 1) MappingParameters params,
        // 2) Object sourceFieldValue,
        // 3) Object destFieldValue,
        // 4) Class<?> sourceType,
        // 5) Class<?> destType)
        //
        if (method == null) {
            return false;
        }

        Class<?>[] params = method.getParameterTypes();

        return params.length == 5 && MappingParameters.class == params[0] && Object.class == params[1] && Object.class == params[2] && Class.class == params[3] && Class.class == params[4];
    }

}
