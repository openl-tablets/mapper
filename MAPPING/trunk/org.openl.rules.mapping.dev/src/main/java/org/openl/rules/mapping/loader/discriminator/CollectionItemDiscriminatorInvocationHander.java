package org.openl.rules.mapping.loader.discriminator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.openl.rules.mapping.exception.RulesMappingException;
import org.openl.rules.mapping.loader.ProxyMethodHandler;

public class CollectionItemDiscriminatorInvocationHander implements InvocationHandler {

    private static final ProxyMethodHandler[] handlers = new ProxyMethodHandler[] { new MappingParamsAwareCollectionItemDiscriminatorHandler(),
            new SimpleCollectionItemDiscriminatorHandler(),
            new DefaultCollectionItemDiscriminatorHandler() };

    private String discriminator;
    private Class<?> instanceClass;
    private Object instance;

    private ProxyMethodHandler discriminatorHandler;
    private Method discriminatorMethod;

    public CollectionItemDiscriminatorInvocationHander(String discriminator, Class<?> instanceClass, Object instance) {
        this.discriminator = discriminator;
        this.instanceClass = instanceClass;
        this.instance = instance;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        for (ProxyMethodHandler handler : handlers) {
            if (handler.canHandle(method)) {
                if (handler != discriminatorHandler) {
                    Method matchingMethod = handler.findMatchingMethod(instanceClass, discriminator, args);

                    if (matchingMethod != null) {
                        discriminatorHandler = handler;
                        discriminatorMethod = matchingMethod;
                        break;
                    }
                }
            }
        }

        if (discriminatorMethod == null) {
            throw new RulesMappingException(String.format("Cannot find discriminator method with name '%s'",
                discriminator));
        }

        return discriminatorHandler.invoke(instance, discriminatorMethod, args);
    }

}
