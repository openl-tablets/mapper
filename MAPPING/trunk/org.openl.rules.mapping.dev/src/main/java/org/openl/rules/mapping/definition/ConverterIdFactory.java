package org.openl.rules.mapping.definition;

import org.openl.rules.mapping.Converter;

public final class ConverterIdFactory {

    private ConverterIdFactory() {
    }

    public static String createConverterId(Converter converter) {
        StringBuilder result = new StringBuilder();
        result.append("METHOD->");
        result.append(converter.getConvertMethod());
        result.append(" SRC-CLASS->");
        result.append(converter.getClassA().getName());
        result.append(" DST-CLASS->");
        result.append(converter.getClassB().getName());
        
        return result.toString();
    }
    
}
