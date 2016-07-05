package org.dozer.factory;

import org.dozer.BeanFactory;
import org.dozer.MappingParameters;
import org.dozer.MappingParamsAware;

public abstract class BaseMappingParamsAwareBeanFactory implements MappingParamsAware, BeanFactory {

    /**
     * Thread local variable which holds user defined mapping parameters.
     */
    private ThreadLocal<MappingParameters> parameters = new ThreadLocal<MappingParameters>();

    public void setMappingParams(MappingParameters params) {
        this.parameters.set(params);
    }

    @Override
    public Object createBean(Object source, Class<?> sourceClass, String targetBeanId) {
        return createBean(parameters.get(), source, sourceClass, targetBeanId);
    }

    public abstract Object createBean(MappingParameters params,
            Object source,
            Class<?> sourceClass,
            String targetBeanId);

}
