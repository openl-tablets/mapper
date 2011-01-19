package org.openl.rules.mapping.definition;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.reflect.MethodUtils;
import org.dozer.CustomConverter;
import org.openl.rules.mapping.RulesMappingException;

public class ConverterFactory {

    /**
     * Creates custom converter object using conversion method name and object
     * info where it is defined.
     * 
     * @param convertMethod conversion method name
     * @param instanceClass class object which defines available methods
     * @param object instance of class which is defined by
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
     * @param object instance of class which is defined by
     *            <code>instanceClass</code> parameter
     * @return {@link CustomConverter} instance
     */
    private static CustomConverter createConverterProxy(final String convertMethodName, final Class<?> instanceClass,
        final Object instance) {

        if (StringUtils.isNotEmpty(convertMethodName)) {
            Class<?>[] interfaces = new Class[] { CustomConverter.class };
            ClassLoader classLoader = ConverterFactory.class.getClassLoader();

            return (CustomConverter) Proxy.newProxyInstance(classLoader, interfaces, new InvocationHandler() {

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

                    Method convertMethod = MethodUtils.getMatchingAccessibleMethod(instanceClass, convertMethodName,
                        parameterTypes);
                    
                    if (convertMethod == null) {
                        throw new RulesMappingException(String.format("Cannot find convert method: \"%s(%s, %s)\"",
                            convertMethodName, srcClass.getName(), destClass.getName()));
                    }

                    Object destValue = args[0];
                    Object srcValue = args[1];
                    Object[] parameterValues = new Object[] { srcValue, destValue };

                    return convertMethod.invoke(instance, parameterValues);
                }
            });
        }

        return null;
    }
}
