package org.openl.rules.mapping.loader.discriminator;

import java.lang.reflect.Method;

import org.openl.rules.mapping.loader.ProxyMethodHandler;

public class SimpleCollectionItemDiscriminatorHandler extends ProxyMethodHandler {

    public Class<?>[] getParameterTypes(Object[] args) {
        Class<?> srcClass = (Class<?>) args[0];
        Class<?> destClass = (Class<?>) args[2];

        return new Class<?>[] { srcClass, destClass };
    }

    public Object[] getParameterValues(Object[] args) {
        Object srcValue = args[1];
        Object destValue = args[4];

        return new Object[] { srcValue, destValue };
    }

    public boolean canHandle(Method method) {
        // Parameters list of CollectionItemDiscriminator.discriminate method
        // 1) Class<?> sourceItemType,
        // 2) Object sourceItemValue,
        // 3) Class<?> destCollectionType,
        // 4) Class<?> destItemType,
        // 5) Object destCollection
        //
        if (method == null) {
            return false;
        }

        Class<?>[] params = method.getParameterTypes();

        return params.length == 5 && Class.class == params[0] && Object.class == params[1] && Class.class == params[2] && Class.class == params[3] && Object.class == params[4];
    }

}
