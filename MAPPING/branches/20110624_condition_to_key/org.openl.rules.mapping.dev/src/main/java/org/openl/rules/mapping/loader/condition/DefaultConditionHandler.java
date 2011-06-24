package org.openl.rules.mapping.loader.condition;

import java.lang.reflect.Method;

import org.openl.rules.mapping.loader.ProxyMethodHandler;

/**
 *  Default condition method handler implementation.   
 *  
 *  @see RulesFieldMappingCondition
 */
public class DefaultConditionHandler extends ProxyMethodHandler {

    public Class<?>[] getParameterTypes(Object[] args) {
        int length = args.length;
        Class<?> srcClass = (Class<?>) args[length - 2];
        Class<?> destClass = (Class<?>) args[length - 1];

        return new Class<?>[] { srcClass, destClass };
    }

    public Object[] getParameterValues(Object[] args) {
        int length = args.length;
        Object srcValue = args[length - 4];
        Object destValue = args[length - 3];

        return new Object[] { srcValue, destValue };
    }

    public boolean canHandle(Method method) {
        if (method == null) {
            return false;
        }
        
        return true;
    }

}
