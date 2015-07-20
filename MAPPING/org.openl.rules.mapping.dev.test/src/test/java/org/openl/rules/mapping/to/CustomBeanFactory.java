package org.openl.rules.mapping.to;

import org.dozer.BeanFactory;
import org.openl.rules.mapping.to.inheritance.ChildE;

public class CustomBeanFactory implements BeanFactory {

    public Object createBean(Object source, Class<?> sourceClass, String targetBeanId) {
        if (E.class.getName().equals(targetBeanId)) {
            return new ChildE();
        }
        
        if (C.class.getName().equals(targetBeanId)) {
            return new C();
        }

        return null;
    }
    
}
