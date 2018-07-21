package org.openl.rules.mapping.loader.condition;

import java.lang.reflect.Proxy;

import org.apache.commons.lang3.StringUtils;
import org.dozer.FieldMappingCondition;

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
        return new ConditionWrapper(createConditionProxy(condition, instanceClass, instance));
    }

    /**
     * Creates proxy object for condition instance.
     * 
     * @param condition condition method name
     * @param instanceClass class object which defines available methods
     * @param instance instance of class which is defined by
     *            <code>instanceClass</code> parameter
     * @return {@link RulesFieldMappingCondition} instance
     */
    private static RulesFieldMappingCondition createConditionProxy(final String condition, final Class<?> instanceClass,
        final Object instance) {

        if (StringUtils.isNotEmpty(condition)) {
            Class<?>[] interfaces = new Class[] { RulesFieldMappingCondition.class };
            ClassLoader classLoader = instanceClass.getClassLoader();

            return (RulesFieldMappingCondition) Proxy.newProxyInstance(classLoader, interfaces,
                new ConditionInvocationHander(condition, instanceClass, instance));
        }

        return null;
    }

  
}
