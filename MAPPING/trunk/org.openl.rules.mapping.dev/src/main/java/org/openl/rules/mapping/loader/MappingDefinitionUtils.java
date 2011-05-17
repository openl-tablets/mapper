package org.openl.rules.mapping.loader;

import java.util.Arrays;

import org.openl.rules.mapping.Mapping;
import org.openl.rules.mapping.exception.RulesMappingException;

public class MappingDefinitionUtils {

    private MappingDefinitionUtils() {
    }

    /**
     * Helper method that makes several actions with loaded mappings to remove
     * OpenL data loading specific.
     * 
     * @param original mapping object
     */
    public static Mapping normalizeMapping(Mapping original) {
        Mapping mapping = createCopy(original);

        Class<?>[][] fieldAHint = getAHint(mapping.getFieldAHint(), mapping.getFieldA());

        if (fieldAHint != null) {
            for (int i = 0; i < fieldAHint.length; i++) {
                Class<?>[] element = fieldAHint[i];
                if (isEmpty(element)) {
                    fieldAHint[i] = null;
                }
            }
        }

        if (mapping.getFieldA() != null) {
            fieldAHint = resizeHintIfRequired(fieldAHint, mapping.getFieldA().length);
        }

        mapping.setFieldAHint(fieldAHint);

        Class<?>[] fieldBHint = mapping.getFieldBHint();
        if (isEmpty(fieldBHint)) {
            mapping.setFieldBHint(null);
        }

        Class<?>[] fieldAType = mapping.getFieldAType();
        if (isEmpty(fieldAType)) {
            mapping.setFieldAType(null);
        }

        return mapping;
    }

    private static Mapping createCopy(Mapping bean) {
        Mapping copy = new Mapping();

        copy.setClassA(bean.getClassA());
        copy.setClassB(bean.getClassB());

        String[] fieldACopy = null;
        if (bean.getFieldA() != null) {
            fieldACopy = Arrays.copyOf(bean.getFieldA(), bean.getFieldA().length);
        }

        copy.setFieldA(fieldACopy);
        copy.setFieldB(bean.getFieldB());
        copy.setConvertMethodAB(bean.getConvertMethodAB());
        copy.setConvertMethodBA(bean.getConvertMethodBA());
        copy.setConvertMethodABId(bean.getConvertMethodABId());
        copy.setConvertMethodBAId(bean.getConvertMethodBAId());
        copy.setOneWay(bean.getOneWay());
        copy.setMapNulls(bean.getMapNulls());
        copy.setMapEmptyStrings(bean.getMapEmptyStrings());
        copy.setTrimStrings(bean.getTrimStrings());
        copy.setFieldACreateMethod(bean.getFieldACreateMethod());
        copy.setFieldBCreateMethod(bean.getFieldBCreateMethod());
        copy.setFieldADefaultValue(bean.getFieldADefaultValue());
        copy.setFieldBDefaultValue(bean.getFieldBDefaultValue());

        String[] fieldADateFormatCopy = null;
        if (bean.getFieldADateFormat() != null) {
            fieldADateFormatCopy = Arrays.copyOf(bean.getFieldADateFormat(), bean.getFieldADateFormat().length);
        }
        copy.setFieldADateFormat(fieldADateFormatCopy);
        copy.setFieldBDateFormat(bean.getFieldBDateFormat());

        Class<?>[][] fieldAHintCopy = null;
        if (bean.getFieldAHint() != null) {
            fieldAHintCopy = new Class<?>[bean.getFieldAHint().length][];

            for (int i = 0; i < bean.getFieldAHint().length; i++) {
                if (bean.getFieldAHint()[i] != null) {
                    fieldAHintCopy[i] = Arrays.copyOf(bean.getFieldAHint()[i], bean.getFieldAHint()[i].length);
                }
            }
        }

        copy.setFieldAHint(fieldAHintCopy);

        Class<?>[] fieldBHintCopy = null;
        if (bean.getFieldBHint() != null) {
            fieldBHintCopy = Arrays.copyOf(bean.getFieldBHint(), bean.getFieldBHint().length);
        }
        copy.setFieldBHint(fieldBHintCopy);

        Class<?>[] fieldATypeCopy = null;
        if (bean.getFieldAType() != null) {
            fieldATypeCopy = Arrays.copyOf(bean.getFieldAType(), bean.getFieldAType().length);
        }

        copy.setFieldAType(fieldATypeCopy);

        copy.setFieldBType(bean.getFieldBType());
        copy.setFieldARequired(bean.getFieldARequired());
        copy.setFieldBRequired(bean.getFieldBRequired());
        copy.setConditionAB(bean.getConditionAB());
        copy.setConditionBA(bean.getConditionBA());
        copy.setConditionABId(bean.getConditionABId());
        copy.setConditionBAId(bean.getConditionBAId());
        copy.setFieldADiscriminator(bean.getFieldADiscriminator());
        copy.setFieldBDiscriminator(bean.getFieldBDiscriminator());
        copy.setFieldADiscriminatorId(bean.getFieldADiscriminatorId());
        copy.setFieldBDiscriminatorId(bean.getFieldBDiscriminatorId());
        copy.setMapId(bean.getMapId());

        return copy;
    }

    private static Class<?>[][] resizeHintIfRequired(Class<?>[][] existedHint, int size) {
        if (existedHint == null || size < 1) {
            return null;
        }

        if (existedHint.length != size) {
            Class<?>[][] newHint = new Class<?>[size][];
            System.arraycopy(existedHint, 0, newHint, 0, existedHint.length);

            return newHint;
        }

        return existedHint;
    }

    private static boolean isEmpty(Class<?>[] array) {
        if (array == null || array.length == 0) {
            return true;
        }

        for (Class<?> element : array) {
            if (element != null) {
                return false;
            }
        }

        return true;
    }

    private static Class<?>[][] getAHint(Class<?>[][] fieldAHint, String[] field) {

        if (field == null || field.length == 0 || fieldAHint == null) {
            return null;
        }

        if (field.length > 1) {
            return fieldAHint;
        }

        Class<?>[][] hint = new Class<?>[1][fieldAHint.length];

        for (int i = 0; i < fieldAHint.length; i++) {
            if (fieldAHint[i] != null && fieldAHint[i].length > 0) {
                hint[0][i] = fieldAHint[i][0];
            } else {
                hint[0][i] = null;
            }

        }

        return hint;
    }

    /**
     * Reverses {@link Mapping} object.
     * 
     * @param mapping prime mapping
     * @return revered mapping
     */
    public static Mapping reverseMapping(Mapping mapping) {
        if (mapping == null) {
            throw new RulesMappingException("An empty field mapping is found");
        }

        String[] fieldA = mapping.getFieldA();
        if (fieldA == null || fieldA.length == 0) {
            throw new RulesMappingException("Empty source mapping should be one way");
        }

        if (fieldA.length > 1) {
            throw new RulesMappingException("Multi source mapping should be one way");
        }

        Mapping reverseMapping = new Mapping();
        reverseMapping.setClassA(mapping.getClassB());
        reverseMapping.setClassB(mapping.getClassA());
        reverseMapping.setFieldA(new String[] { mapping.getFieldB() });
        reverseMapping.setFieldB(fieldA[0]);
        reverseMapping.setFieldACreateMethod(mapping.getFieldBCreateMethod());
        reverseMapping.setFieldBCreateMethod(mapping.getFieldACreateMethod());
        reverseMapping.setFieldADefaultValue(mapping.getFieldBDefaultValue());
        reverseMapping.setFieldBDefaultValue(mapping.getFieldADefaultValue());
        reverseMapping.setFieldARequired(mapping.getFieldBRequired());
        reverseMapping.setFieldBRequired(mapping.getFieldARequired());
        reverseMapping.setMapNulls(mapping.getMapNulls());
        reverseMapping.setOneWay(mapping.getOneWay());
        reverseMapping.setConvertMethodAB(mapping.getConvertMethodBA());
        reverseMapping.setConvertMethodBA(mapping.getConvertMethodAB());
        reverseMapping.setConditionAB(mapping.getConditionBA());
        reverseMapping.setConditionBA(mapping.getConditionAB());
        reverseMapping.setConvertMethodABId(mapping.getConvertMethodBAId());
        reverseMapping.setConvertMethodBAId(mapping.getConvertMethodABId());
        reverseMapping.setConditionABId(mapping.getConditionBAId());
        reverseMapping.setConditionBAId(mapping.getConditionABId());
        reverseMapping.setFieldADiscriminator(mapping.getFieldBDiscriminator());
        reverseMapping.setFieldBDiscriminator(mapping.getFieldADiscriminator());
        reverseMapping.setFieldADiscriminatorId(mapping.getFieldBDiscriminatorId());
        reverseMapping.setFieldBDiscriminatorId(mapping.getFieldADiscriminatorId());
        reverseMapping.setMapId(mapping.getMapId());

        if (mapping.getFieldBType() != null) {
            reverseMapping.setFieldAType(new Class<?>[] { mapping.getFieldBType() });
        }
        if (mapping.getFieldAType() != null && mapping.getFieldAType()[0] != null) {
            reverseMapping.setFieldBType(mapping.getFieldAType()[0]);
        }
        if (mapping.getFieldBHint() != null) {
            reverseMapping.setFieldAHint(new Class<?>[][] { mapping.getFieldBHint() });
        }
        if (mapping.getFieldAHint() != null && mapping.getFieldAHint()[0] != null) {
            reverseMapping.setFieldBHint(mapping.getFieldAHint()[0]);
        }
        if (mapping.getFieldADateFormat() != null && mapping.getFieldADateFormat()[0] != null) {
            reverseMapping.setFieldBDateFormat(mapping.getFieldADateFormat()[0]);
        }
        if (mapping.getFieldBDateFormat() != null) {
            reverseMapping.setFieldADateFormat(new String[] { mapping.getFieldBDateFormat() });
        }

        return reverseMapping;
    }
    
    public static String getMethodName(String methodName) {
        if (!methodName.contains(".")) {
            return methodName;
        }

        return methodName.substring(methodName.lastIndexOf('.') + 1);
    }

    public static String getTypeName(String methodName) {
        if (!methodName.contains(".")) {
            return null;
        }

        return methodName.substring(0, methodName.lastIndexOf('.'));
    }


}
