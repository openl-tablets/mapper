package org.openl.rules.mapping.definition;

import org.openl.rules.mapping.Converter;
import org.openl.rules.mapping.Mapping;

public class ConverterIdFactory {

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
    
    public static String createConverterId(Mapping mapping) {
        StringBuilder result = new StringBuilder();
        result.append("METHOD->");
        result.append(mapping.getConvertMethodAB());
        result.append(" SRC-CLASS->");
        result.append(mapping.getClassA().getName());
        result.append(" DST-CLASS->");
        result.append(mapping.getClassB().getName());
        
        return result.toString();
    }

}
