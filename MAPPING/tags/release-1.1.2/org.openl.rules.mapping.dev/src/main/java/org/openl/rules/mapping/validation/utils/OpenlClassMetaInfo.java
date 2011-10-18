package org.openl.rules.mapping.validation.utils;

import org.openl.types.IOpenClass;

public class OpenlClassMetaInfo implements ClassMetaInfo {

    private IOpenClass clazz;

    public OpenlClassMetaInfo(IOpenClass clazz) {
        this.clazz = clazz;
    }

    @Override
    public String getName() {
        return getInstanceClass().getName();
    }

    @Override
    public Class<?> getInstanceClass() {
        return clazz.getInstanceClass();
    }

}
