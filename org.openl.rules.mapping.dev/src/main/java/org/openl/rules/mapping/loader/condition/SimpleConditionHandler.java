package org.openl.rules.mapping.loader.condition;

import java.lang.reflect.Method;

import org.dozer.FieldMappingCondition;
import org.openl.rules.mapping.loader.ProxyMethodHandler;

/**
 * Defines handler class which handle
 * {@link FieldMappingCondition#mapField(Object, Object, Class, Class)} method
 * invocations.
 * 
 */
public class SimpleConditionHandler extends ProxyMethodHandler {

    public Class<?>[] getParameterTypes(Object[] args) {
        Class<?> srcClass = (Class<?>) args[2];
        Class<?> destClass = (Class<?>) args[3];

        return new Class<?>[] { srcClass, destClass };
    }

    public Object[] getParameterValues(Object[] args) {
        Object srcValue = args[0];
        Object destValue = args[1];

        return new Object[] { srcValue, destValue };
    }

    public boolean canHandle(Method method) {
        // Parameters list of FieldMappingCondition.mapField method :
        // 1) Object sourceFieldValue,
        // 2) Object destFieldValue,
        // 3) Class<?> sourceType,
        // 4) Class<?> destType)
        //
        if (method == null) {
            return false;
        }

        Class<?>[] params = method.getParameterTypes();
        return params.length == 4 && Object.class == params[0] && Object.class == params[1] && Class.class == params[2] && Class.class == params[3];
    }

}
