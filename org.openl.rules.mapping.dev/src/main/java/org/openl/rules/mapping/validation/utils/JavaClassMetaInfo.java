package org.openl.rules.mapping.validation.utils;

class JavaClassMetaInfo implements ClassMetaInfo {
    private Class<?> clazz;

    public JavaClassMetaInfo(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public String getName() {
        return clazz.getName();
    }

    @Override
    public Class<?> getInstanceClass() {
        return clazz;
    }

}
