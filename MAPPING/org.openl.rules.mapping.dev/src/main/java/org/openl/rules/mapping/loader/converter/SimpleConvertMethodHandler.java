package org.openl.rules.mapping.loader.converter;

import java.lang.reflect.Method;

import org.dozer.CustomConverter;
import org.openl.rules.mapping.loader.ProxyMethodHandler;

/**
 * Defines handler class which handle
 * {@link CustomConverter#convert(Object, Object, Class, Class)} method
 * invocations.
 * 
 */
public class SimpleConvertMethodHandler extends ProxyMethodHandler {

    public Class<?>[] getParameterTypes(Object[] args) {
        Class<?> destClass = (Class<?>) args[2];
        Class<?> srcClass = (Class<?>) args[3];

        return new Class<?>[] { srcClass, destClass };
    }

    public Object[] getParameterValues(Object[] args) {
        Object destValue = args[0];
        Object srcValue = args[1];

        return new Object[] { srcValue, destValue };
    }

    public boolean canHandle(Method method) {
        // Parameters list of CustomConverter.convert method:
        // 1) Object existingDestinationFieldValue,
        // 2) Object sourceFieldValue,
        // 3) Class<?> destinationClass,
        // 4) Class<?> sourceClass);
        //
        if (method == null) {
            return false;
        }

        Class<?>[] params = method.getParameterTypes();
        return params.length == 4 && Object.class == params[0] && Object.class == params[1] && Class.class == params[2] && Class.class == params[3];
    }

}
