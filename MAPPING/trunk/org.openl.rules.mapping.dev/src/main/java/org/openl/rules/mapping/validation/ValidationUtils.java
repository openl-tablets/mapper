package org.openl.rules.mapping.validation;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;
import org.dozer.fieldmap.FieldMapUtils;
import org.dozer.fieldmap.HintContainer;
import org.dozer.propertydescriptor.DeepHierarchyElement;
import org.dozer.util.DozerConstants;
import org.dozer.util.MappingUtils;
import org.dozer.util.ReflectionUtils;
import org.openl.rules.mapping.OpenLReflectionUtils;
import org.openl.types.IOpenClass;
import org.openl.types.IOpenMethod;

public class ValidationUtils {

    private ValidationUtils() {
    }

    public static boolean isDeepFieldHierarchy(String fieldPath) {
        return MappingUtils.isDeepMapping(fieldPath);
    }

    public static boolean isSimpleCollectionIndex(String index) {
        return MappingUtils.isSimpleCollectionIndex(index);
    }
    
    public static int getCollectionIndex(String index) {
        return MappingUtils.getCollectionIndex(index);
    }
    
    public static FieldPathHierarchyElement[] getFieldHierarchy(Class<?> parentClass, String field, Class<?>[] hint) {
        HintContainer hintContainer = getHint(hint);
        
        if (isDeepFieldHierarchy(field)) {
            DeepHierarchyElement[] deepHierarchy = ReflectionUtils.getDeepFieldHierarchy(parentClass, field, hintContainer);
            
            FieldPathHierarchyElement[] fieldPathHierarchy = new FieldPathHierarchyElement[deepHierarchy.length];
            for (int i = 0; i < deepHierarchy.length; i++) {
                DeepHierarchyElement element = deepHierarchy[i];
                fieldPathHierarchy[i] = new JavaBeanHierarchyElement(element.getPropDescriptor(), element.getIndex(), element.getHintType());
            }
            
            return fieldPathHierarchy;
        }

        if (DozerConstants.SELF_KEYWORD.equals(field)) {
            return new FieldPathHierarchyElement[] {new ThisHierarchyElement(parentClass)};
        }
        
        String fieldName = field;
        String indexExpression = null;

        if (field.contains("[")) {
            fieldName = field.substring(0, field.indexOf('['));
            indexExpression = field.substring(field.indexOf('[') + 1, field.indexOf(']'));
        }

        PropertyDescriptor propDescriptor = ReflectionUtils.findPropertyDescriptor(parentClass, fieldName, hintContainer);

        if (propDescriptor == null) {
            MappingUtils
                .throwMappingException("Exception occurred determining field hierarchy for Class --> " + parentClass
                    .getName() + ", Field --> " + field);
        }

        return new FieldPathHierarchyElement[] { new JavaBeanHierarchyElement(propDescriptor, indexExpression, null) };
    }

    private static HintContainer getHint(Class<?>... types) {

        if (types == null || types.length == 0) {
            return null;
        }
        
        String[] names = new String[types.length];

        for (int i = 0; i < types.length; i++) {
            if (types[i] == null) {
                names[i] = "";
            } else {
                names[i] = types[i].getName();
            }
        }

        return FieldMapUtils.hint(StringUtils.join(names, ","));
    }
    
    public static Method findMatchingAccessibleMethod(Class<?> clazz, String methodName, Class<?>[] paramTypes) {
        return ReflectionUtils.findMatchingAccessibleMethod(clazz, methodName, paramTypes);
    }

    public static IOpenMethod findMatchingAccessibleMethod(IOpenClass clazz, String methodName, Class<?>[] paramTypes) {
        return OpenLReflectionUtils.findMatchingAccessibleMethod(clazz, methodName, paramTypes);
    }

}
