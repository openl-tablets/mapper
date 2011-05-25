package org.openl.rules.mapping.validation.utils;

import org.openl.types.IOpenMethod;

public class OpenlMethodMetaInfo implements MethodMetaInfo {
    private IOpenMethod method;

    public OpenlMethodMetaInfo(IOpenMethod method) {
        this.method = method;
    }

    @Override
    public IOpenMethod getMethod() {
        return method;
    }

    @Override
    public ClassMetaInfo getReturnType() {
        return new OpenlClassMetaInfo(method.getType());
    }

}
