package org.openl.rules.mapping.validation.utils;

import java.lang.reflect.Method;

class JavaMethodMetaInfo implements MethodMetaInfo {
    private Method method;

    public JavaMethodMetaInfo(Method method) {
        this.method = method;
    }

    @Override
    public Method getMethod() {
        return method;
    }

    @Override
    public ClassMetaInfo getReturnType() {
        return new JavaClassMetaInfo(method.getReturnType());
    }

}
