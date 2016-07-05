package org.openl.rules.mapping.loader.discriminator;

import java.lang.reflect.Proxy;

import org.apache.commons.lang.StringUtils;
import org.dozer.CollectionItemDiscriminator;

public class CollectionItemDiscriminatorFactory {

    private CollectionItemDiscriminatorFactory() {
    }

    public static CollectionItemDiscriminator createDiscriminator(String discriminator, Class<?> instanceClass, Object instance) {
        return new CollectionItemDiscriminatorWrapper(createDiscriminatorProxy(discriminator, instanceClass, instance));
    }

    private static RulesCollectionItemDiscriminator createDiscriminatorProxy(final String discriminator, final Class<?> instanceClass,
        final Object instance) {

        if (StringUtils.isNotEmpty(discriminator)) {
            Class<?>[] interfaces = new Class[] { RulesCollectionItemDiscriminator.class };
            ClassLoader classLoader = instanceClass.getClassLoader();

            return (RulesCollectionItemDiscriminator) Proxy.newProxyInstance(classLoader, interfaces,
                new CollectionItemDiscriminatorInvocationHander(discriminator, instanceClass, instance));
        }

        return null;
    }
}
