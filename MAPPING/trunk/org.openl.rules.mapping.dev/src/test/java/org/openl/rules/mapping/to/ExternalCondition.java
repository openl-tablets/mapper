package org.openl.rules.mapping.to;

public class ExternalCondition {

    public static boolean dontMap(Object src, Object dest) {
        return false;
    }
    
    public static boolean map(Object src, Object dest) {
        return true;
    }

}
