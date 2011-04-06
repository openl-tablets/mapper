package org.openl.rules.mapping.loader.converter;

import java.lang.reflect.Method;

import org.openl.rules.mapping.loader.ProxyMethodHandler;

/**
 *  Default convert method handler implementation.   
 *  
 *  @see RulesCustomConverter
 */
public class DefaultConvertMethodHandler extends ProxyMethodHandler {

    public Class<?>[] getParameterTypes(Object[] args) {
        int length = args.length;
        Class<?> destClass = (Class<?>) args[length - 2];
        Class<?> srcClass = (Class<?>) args[length - 1];

        return new Class<?>[] { srcClass, destClass };
    }

    public Object[] getParameterValues(Object[] args) {
        int length = args.length;
        Object destValue = args[length - 4];
        Object srcValue = args[length - 3];

        return new Object[] { srcValue, destValue };
    }

    @Override
    public boolean canHandle(Method method) {
        if (method == null) {
            return false;
        }
        
        return true;
    }

}
