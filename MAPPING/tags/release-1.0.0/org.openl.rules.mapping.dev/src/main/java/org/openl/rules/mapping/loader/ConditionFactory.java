package org.openl.rules.mapping.loader;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.apache.commons.lang.StringUtils;
import org.dozer.CustomConverter;
import org.dozer.fieldmap.FieldMappingCondition;
import org.dozer.util.ReflectionUtils;
import org.openl.rules.mapping.exception.RulesMappingException;

/**
 * Provides method to create field mapping condition object.
 */
public final class ConditionFactory {

    private ConditionFactory() {
    }

    /**
     * Creates condition object using condition name and object info where it is
     * defined.
     * 
     * @param condition condition name
     * @param instanceClass class object which defines available methods
     * @param instance instance of class which is defined by
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
     * @param instance instance of class which is defined by
     *            <code>instanceClass</code> parameter
     * @return {@link CustomConverter} instance
     */
    private static FieldMappingCondition createConditionProxy(final String condition, final Class<?> instanceClass,
        final Object instance) {

        if (StringUtils.isNotEmpty(condition)) {
            Class<?>[] interfaces = new Class[] { FieldMappingCondition.class };
            ClassLoader classLoader = instanceClass.getClassLoader();

            return (FieldMappingCondition) Proxy.newProxyInstance(classLoader, interfaces,
                new ConditionInvocationHander(condition, instanceClass, instance));
        }

        return null;
    }

    /**
     * Intended to internal use only. The invocation handler implementation
     * which used to create proxy object for appropriate condition method.
     */
    private static final class ConditionInvocationHander implements InvocationHandler {

        private String condition;
        private Class<?> instanceClass;
        private Object instance;

        public ConditionInvocationHander(String condition, Class<?> instanceClass, Object instance) {
            this.condition = condition;
            this.instanceClass = instanceClass;
            this.instance = instance;
        }

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

            // Try to find appropriate method among methods what are provided by
            // instanceClass.
            //
            Method conditionMethod = ReflectionUtils.findMatchingAccessibleMethod(instanceClass, condition,
                parameterTypes);

            if (conditionMethod == null) {
                throw new RulesMappingException(String.format("Cannot find condition method: \"%s(%s, %s)\"",
                    condition, srcClass.getName(), destClass.getName()));
            }

            Object srcValue = args[0];
            Object destValue = args[1];
            Object[] parameterValues = new Object[] { srcValue, destValue };

            return ReflectionUtils.invoke(conditionMethod, instance, parameterValues);
        }
    }

}
