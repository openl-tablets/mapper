package org.openl.rules.mapping.definition;

import org.apache.commons.lang.StringUtils;
import org.openl.rules.mapping.Mapping;

public class MappingIdFactory {

    /**
     * Creates string key using mapping object info.
     * 
     * @param mapping mapping object
     * @return string key
     */
    public static String createMappingId(Mapping mapping) {
        StringBuilder result = new StringBuilder();
        result.append(mapping.getClassA().getName());
        result.append(".");
        
        String[] fieldA = mapping.getFieldA();
        if (fieldA == null || fieldA.length == 0) {
            result.append("<noname>");
        } else if (fieldA.length == 1) {
            result.append(fieldA[0]);
        } else {
            String fieldName = StringUtils.join(fieldA, ",");
            result.append("[" + fieldName + "]");
        }
        
        result.append("->");
        result.append(mapping.getClassB().getName());
        result.append(".");
        
        if (StringUtils.isEmpty(mapping.getFieldB())) {
            result.append("<noname>");
        } else {
            result.append(mapping.getFieldB());
        }

        return result.toString();
    }
}
