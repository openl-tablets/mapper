package org.openl.rules.mapping.loader.converter;

import java.lang.reflect.Proxy;

import org.apache.commons.lang3.StringUtils;
import org.dozer.CustomConverter;

/**
 * Provides method to create custom converter object.
 */
public final class ConverterFactory {

    private ConverterFactory() {
    }

    /**
     * Creates custom converter object using conversion method name and object
     * info where it is defined.
     * 
     * @param convertMethod conversion method name
     * @param instanceClass class object which defines available methods
     * @param instance instance of class which is defined by
     *            <code>instanceClass</code> parameter
     * @return {@CustomConverter} instance
     */
    public static CustomConverter createConverter(String convertMethod, Class<?> instanceClass, Object instance) {
        return new CustomConverterWrapper(createConverterProxy(convertMethod, instanceClass, instance));
    }

    /**
     * Creates proxy object for converter instance.
     * 
     * @param convertMethodName convert method name
     * @param instanceClass class object which defines available methods
     * @param instance instance of class which is defined by
     *            <code>instanceClass</code> parameter
     * @return {@link CustomConverter} instance
     */
    private static RulesCustomConverter createConverterProxy(final String convertMethodName,
        final Class<?> instanceClass, final Object instance) {

        if (StringUtils.isNotEmpty(convertMethodName)) {
            Class<?>[] interfaces = new Class[] { RulesCustomConverter.class };
            ClassLoader classLoader = instanceClass.getClassLoader();

            return (RulesCustomConverter) Proxy.newProxyInstance(classLoader, interfaces,
                new CustomConverterInvocationHandler(convertMethodName, instanceClass, instance));
        }

        return null;
    }

}
