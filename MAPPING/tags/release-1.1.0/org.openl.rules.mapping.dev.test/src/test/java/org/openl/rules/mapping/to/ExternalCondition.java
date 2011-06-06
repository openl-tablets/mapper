package org.openl.rules.mapping.to;

import org.dozer.MappingParameters;

public class ExternalCondition {

    public static boolean dontMap(Object src, Object dest) {
        return false;
    }
    
    public static boolean map(Object src, Object dest) {
        return true;
    }

    public static boolean dontMap(MappingParameters params, Object src, Object dest) {
        return (Boolean)params.get("false");
    }
    
    public static boolean map(MappingParameters params, Object src, Object dest) {
        return (Boolean)params.get("true");
    }

}
