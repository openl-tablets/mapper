package org.openl.rules.mapping.loader;

import java.lang.reflect.Method;

import org.dozer.util.ReflectionUtils;

/**
 * Base abstraction of proxy method handler. Defines methods which should be
 * used to link proxy method with rules method.
 * 
 * Intended for internal use only.
 */
public abstract class ProxyMethodHandler {

    /**
     * Gets parameter types of method to link using arguments what are passed to
     * proxy method.
     * 
     * @param args proxy method arguments
     * @return parameter types
     */
    public abstract Class<?>[] getParameterTypes(Object[] args);

    /**
     * Gets parameter values of method to link using arguments what are passed
     * to proxy method.
     * 
     * @param args proxy method arguments
     * @return parameter values
     */
    public abstract Object[] getParameterValues(Object[] args);

    /**
     * Indicates that proxy method can be handled properly.
     * 
     * @param method proxy method
     * @return <code>true</code> if proxy method can be handle properly;
     *         <code>false</code> - otherwise
     */
    public abstract boolean canHandle(Method method);

    /**
     * Finds method which will be used as real implementation of proxy method.
     * 
     * NOTE: implementation of current class has to process proxy method
     * parameters to get parameters of real method.
     * 
     * @param instanceClass class which provides methods
     * @param methodName method to find
     * @param params proxy method arguments
     * @return {@link Method} if matching method is found; <code>null</code> -
     *         otherwise
     * 
     * 
     */
    public Method findMatchingMethod(Class<?> instanceClass, String methodName, Object[] params) {
        Class<?>[] parameterTypes = getParameterTypes(params);

        // Try to find appropriate method among methods what are provided by
        // instanceClass.
        //
        return ReflectionUtils.findMatchingAccessibleMethod(instanceClass, methodName, parameterTypes);
    }

    /**
     * Invokes given method with specified arguments.
     * 
     * NOTE: implementation of current class has to process proxy method
     * parameters to get values of real method.
     * 
     * @param instance target object
     * @param method method reference
     * @param params proxy method arguments
     * @return result of method invocation
     */
    public Object invoke(Object instance, Method method, Object[] params) {
        Object[] args = getParameterValues(params);

        return ReflectionUtils.invoke(method, instance, args);
    }
}
