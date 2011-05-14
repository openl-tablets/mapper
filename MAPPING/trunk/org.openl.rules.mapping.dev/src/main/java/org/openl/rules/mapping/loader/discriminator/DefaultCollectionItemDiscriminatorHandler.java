package org.openl.rules.mapping.loader.discriminator;

import java.lang.reflect.Method;

import org.openl.rules.mapping.loader.ProxyMethodHandler;

public class DefaultCollectionItemDiscriminatorHandler extends ProxyMethodHandler {

    public Class<?>[] getParameterTypes(Object[] args) {
        // Parameters list of CollectionItemDiscriminator.discriminate method
        // 1) Class<?> sourceItemType,
        // 2) Object sourceItemValue,
        // 3) Class<?> destCollectionType,
        // 4) Class<?> destItemType,
        // 5) Object destCollection
        //
        int length = args.length;
        Class<?> srcClass = (Class<?>) args[length - 5];
        Class<?> destClass = (Class<?>) args[length - 3];

        return new Class<?>[] { srcClass, destClass };
    }

    public Object[] getParameterValues(Object[] args) {
        int length = args.length;
        Object srcValue = args[length - 4];
        Object destValue = args[length - 1];

        return new Object[] { srcValue, destValue };
    }

    public boolean canHandle(Method method) {
        if (method == null) {
            return false;
        }

        return true;
    }
}
