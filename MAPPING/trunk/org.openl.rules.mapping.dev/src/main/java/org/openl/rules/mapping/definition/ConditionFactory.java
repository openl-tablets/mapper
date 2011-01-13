package org.openl.rules.mapping.definition;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.reflect.MethodUtils;
import org.dozer.CustomConverter;
import org.dozer.fieldmap.FieldMappingCondition;

public class ConditionFactory {

    /**
     * Creates condition object using condition name and object info where it is
     * defined.
     * 
     * @param condition condition name
     * @param instanceClass class object which defines available methods
     * @param object instance of class which is defined by
     *            <code>instanceClass</code> parameter
     * @return {@link FieldMappingCondition} instance
     */
    public static FieldMappingCondition createCondition(String condition, Class<?> instanceClass, Object instance) {
        return createConditionProxy(condition, instanceClass, instance);
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
    private static FieldMappingCondition createConditionProxy(final String condition, final Class<?> instanceClass,
        final Object instance) {

        if (StringUtils.isNotEmpty(condition)) {
            Class<?>[] interfaces = new Class[] { FieldMappingCondition.class };
            ClassLoader classLoader = ConditionFactory.class.getClassLoader();

            return (FieldMappingCondition) Proxy.newProxyInstance(classLoader, interfaces, new InvocationHandler() {
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                    // Parameters list of FieldMappingCondition.mapField method :
                    // 1) Object sourceFieldValue,
                    // 2) Object destFieldValue,
                    // 3) Class<?> sourceType,
                    // 4) Class<?> destType)
                    //
                    Class<?> srcClass = (Class<?>) args[2];
                    Class<?> destClass = (Class<?>) args[3];
                    Class<?>[] parameterTypes = new Class<?>[] { srcClass, destClass };

                    Method conditionMethod = MethodUtils.getMatchingAccessibleMethod(instanceClass, condition, parameterTypes);

                    Object srcValue = args[0];
                    Object destValue = args[1];
                    Object[] parameterValues = new Object[] { srcValue, destValue };

                    return conditionMethod.invoke(instance, parameterValues);
                }
            });
        }

        return null;
    }
}
