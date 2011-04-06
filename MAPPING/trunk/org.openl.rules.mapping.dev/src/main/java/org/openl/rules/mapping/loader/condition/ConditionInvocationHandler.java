package org.openl.rules.mapping.loader.condition;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.openl.rules.mapping.exception.RulesMappingException;
import org.openl.rules.mapping.loader.ProxyMethodHandler;

/**
 * Intended to internal use only. The invocation handler implementation which
 * used to create proxy object for appropriate condition method.
 */
final class ConditionInvocationHander implements InvocationHandler {

    private static final ProxyMethodHandler[] handlers = new ProxyMethodHandler[] {
            new MappingParamsAwareConditionHandler(), new SimpleConditionHandler(), new DefaultConditionHandler() };

    private String condition;
    private Class<?> instanceClass;
    private Object instance;

    private ProxyMethodHandler conditionHandler;
    private Method conditionMethod;

    public ConditionInvocationHander(String condition, Class<?> instanceClass, Object instance) {
        this.condition = condition;
        this.instanceClass = instanceClass;
        this.instance = instance;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        for (ProxyMethodHandler handler : handlers) {
            if (handler.canHandle(method)) {
                if (handler != conditionHandler) {
                    Method matchingMethod = handler.findMatchingMethod(instanceClass, condition, args);

                    if (matchingMethod != null) {
                        conditionHandler = handler;
                        conditionMethod = matchingMethod;
                        break;
                    }
                }
            }
        }

        if (conditionMethod == null) {
            throw new RulesMappingException(String.format("Cannot find condition method with name '%s'", condition));
        }

        return conditionHandler.invoke(instance, conditionMethod, args);
    }

}
