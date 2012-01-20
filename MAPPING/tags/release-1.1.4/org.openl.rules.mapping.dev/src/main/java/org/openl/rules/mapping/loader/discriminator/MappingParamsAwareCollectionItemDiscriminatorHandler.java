package org.openl.rules.mapping.loader.discriminator;

import java.lang.reflect.Method;

import org.dozer.MappingParameters;
import org.openl.rules.mapping.loader.ProxyMethodHandler;

class MappingParamsAwareCollectionItemDiscriminatorHandler extends ProxyMethodHandler {

    public Object[] getParameterValues(Object[] args) {
        Object mappingParams = args[0];
        Object srcValue = args[2];
        Object destValue = args[5];

        return new Object[] { mappingParams, srcValue, destValue };
    }

    public Class<?>[] getParameterTypes(Object[] args) {
        Class<?> srcClass = (Class<?>) args[1];
        Class<?> destClass = (Class<?>) args[3];

        return new Class<?>[] { MappingParameters.class, srcClass, destClass };
    }

    public boolean canHandle(Method method) {
        // Parameters list of CollectionItemDiscriminator.discriminate method
        // 1) Class<?> sourceItemType,
        // 2) Object sourceItemValue,
        // 3) Class<?> destCollectionType,
        // 4) Class<?> destItemType,
        // 5) Object destCollection
        //
        if (method == null) {
            return false;
        }

        Class<?>[] params = method.getParameterTypes();

        return params.length == 6 
                    && MappingParameters.class == params[0] 
                    && Class.class == params[1] 
                    && Object.class == params[2] 
                    && Class.class == params[3] 
                    && Class.class == params[4]
                    && Object.class == params[5];
    }

}