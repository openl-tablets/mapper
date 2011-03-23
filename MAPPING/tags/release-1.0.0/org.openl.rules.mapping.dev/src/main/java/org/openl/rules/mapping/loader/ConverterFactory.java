package org.openl.rules.mapping.loader;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.apache.commons.lang.StringUtils;
import org.dozer.CustomConverter;
import org.dozer.util.ReflectionUtils;
import org.openl.rules.mapping.exception.RulesMappingException;

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
        return createConverterProxy(convertMethod, instanceClass, instance);
    }

    /**
     * Creates proxy object for converter instance. The following steps are used
     * to obtain converter instance:
     * <ol>
     * <li>find method of <code>instanceClass</code> with
     * <code>convertMethodName</code> name and the</li>
     * <li></li>
     * </ol>
     * 
     * @param convertMethodName convert method name
     * @param instanceClass class object which defines available methods
     * @param instance instance of class which is defined by
     *            <code>instanceClass</code> parameter
     * @return {@link CustomConverter} instance
     */
    private static CustomConverter createConverterProxy(final String convertMethodName, final Class<?> instanceClass,
        final Object instance) {

        if (StringUtils.isNotEmpty(convertMethodName)) {
            Class<?>[] interfaces = new Class[] { CustomConverter.class };
            ClassLoader classLoader = instanceClass.getClassLoader();

            return (CustomConverter) Proxy.newProxyInstance(classLoader, interfaces,
                new CustomConverterInvocationHandler(convertMethodName, instanceClass, instance));
        }

        return null;
    }

    /**
     * Intended to internal use only. The invocation handler implementation
     * which used to create proxy object for appropriate convert method.
     */
    private static final class CustomConverterInvocationHandler implements InvocationHandler {
        private final Class<?> instanceClass;
        private final Object instance;
        private final String convertMethodName;

        private CustomConverterInvocationHandler(String convertMethodName, Class<?> instanceClass, Object instance) {
            this.instanceClass = instanceClass;
            this.instance = instance;
            this.convertMethodName = convertMethodName;
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

            // Parameters list of CustomConverter.convert method :
            // 1) Object existingDestinationFieldValue,
            // 2) Object sourceFieldValue,
            // 3) Class<?> destinationClass,
            // 4) Class<?> sourceClass);
            //
            Class<?> destClass = (Class<?>) args[2];
            Class<?> srcClass = (Class<?>) args[3];
            Class<?>[] parameterTypes = new Class<?>[] { srcClass, destClass };

            // Try to find appropriate method among methods what are provided by
            // instanceClass.
            //
            Method convertMethod = ReflectionUtils.findMatchingAccessibleMethod(instanceClass, convertMethodName,
                parameterTypes);

            if (convertMethod == null) {
                throw new RulesMappingException(String.format("Cannot find convert method: \"%s(%s, %s)\"",
                    convertMethodName, srcClass.getName(), destClass.getName()));
            }

            Object destValue = args[0];
            Object srcValue = args[1];
            Object[] parameterValues = new Object[] { srcValue, destValue };

            return ReflectionUtils.invoke(convertMethod, instance, parameterValues);
        }
    }

}
