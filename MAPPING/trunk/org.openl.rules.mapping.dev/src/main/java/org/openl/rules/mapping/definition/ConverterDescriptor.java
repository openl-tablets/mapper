package org.openl.rules.mapping.definition;

import org.dozer.CustomConverter;

public class ConverterDescriptor {

    private String converterId;
    private CustomConverter instance;
    private Class<?> srcType;
    private Class<?> destType;

    public ConverterDescriptor(String converterId, CustomConverter instance, Class<?> srcType, Class<?> destType) {
        this.converterId = converterId;
        this.instance = instance;
        this.srcType = srcType;
        this.destType = destType;
    }

    public String getConverterId() {
        return converterId;
    }

    public CustomConverter getInstance() {
        return instance;
    }

    public Class<?> getSrcType() {
        return srcType;
    }

    public Class<?> getDestType() {
        return destType;
    }
    
}
