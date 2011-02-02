package org.openl.rules.mapping.definition;

public final class BeanMapKeyFactory {

    private BeanMapKeyFactory() {
    }

    /**
     * Creates string key using classes information.
     * 
     * @param srcClass source class
     * @param destClass destination class
     * @return string key
     */
    public static String createKey(Class<?> srcClass, Class<?> destClass) {

        StringBuilder result = new StringBuilder();
        result.append("SRC-CLASS->");
        result.append(srcClass.getName());
        result.append(" DST-CLASS->");
        result.append(destClass.getName());

        return result.toString();
    }

}
