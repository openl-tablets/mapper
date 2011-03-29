package org.openl.rules.mapping.loader.converter;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.openl.rules.mapping.exception.RulesMappingException;
import org.openl.rules.mapping.loader.ProxyMethodHandler;

/**
 * Intended to internal use only. The invocation handler implementation which
 * used to create proxy object for appropriate convert method.
 */
final class CustomConverterInvocationHandler implements InvocationHandler {

    private static final ProxyMethodHandler[] handlers = new ProxyMethodHandler[] {
            new MappingParamsAwareConvertMethodHandler(), new SimpleConvertMethodHandler() };

    private final Class<?> instanceClass;
    private final Object instance;
    private final String convertMethodName;

    private ProxyMethodHandler convertMethodHandler;
    private Method convertMethod;

    public CustomConverterInvocationHandler(String convertMethodName, Class<?> instanceClass, Object instance) {
        this.instanceClass = instanceClass;
        this.instance = instance;
        this.convertMethodName = convertMethodName;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        for (ProxyMethodHandler handler : handlers) {
            if (handler.canHandle(method)) {
                if (handler != convertMethodHandler) {
                    Method matchingMethod = handler.findMatchingMethod(instanceClass, convertMethodName, args);

                    if (matchingMethod != null) {
                        convertMethodHandler = handler;
                        convertMethod = matchingMethod;
                        break;
                    }
                }
            }
        }

        if (convertMethod == null) {
            throw new RulesMappingException(String.format("Cannot find convert method with name '%s'",
                convertMethodName));
        }

        return convertMethodHandler.invoke(instance, convertMethod, args);
    }

}