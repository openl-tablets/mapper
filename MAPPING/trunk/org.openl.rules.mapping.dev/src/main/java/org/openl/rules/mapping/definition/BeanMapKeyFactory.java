package org.openl.rules.mapping.definition;

public class BeanMapKeyFactory {

    public static String createKey(Class<?> srcClass, Class<?> destClass) {

        StringBuilder result = new StringBuilder(140);
        result.append("SRC-CLASS->");
        result.append(srcClass.getName());
        result.append(" DST-CLASS->");
        result.append(destClass.getName());

        return result.toString();
    }

}
