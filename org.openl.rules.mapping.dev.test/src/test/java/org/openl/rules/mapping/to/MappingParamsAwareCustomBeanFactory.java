package org.openl.rules.mapping.to;

import org.dozer.MappingParameters;
import org.dozer.factory.BaseMappingParamsAwareBeanFactory;
import org.openl.rules.mapping.to.inheritance.ChildE;

public class MappingParamsAwareCustomBeanFactory extends BaseMappingParamsAwareBeanFactory {

	
    @Override
    public Object createBean(MappingParameters params, Object source, Class<?> sourceClass, String targetBeanId) {
        if (E.class.getName().equals(targetBeanId)) {
            ChildE e = new ChildE();

            if (params != null && params.get("key") != null) {
                e.setAString((String)params.get("key"));
            }

            return e;
        }

        if (C.class.getName().equals(targetBeanId)) {

            C c = new C();

            if (params != null && params.get("key") != null) {
                c.setAString((String)params.get("key"));
            }

            return c;
        }

        return null;
    }

}
