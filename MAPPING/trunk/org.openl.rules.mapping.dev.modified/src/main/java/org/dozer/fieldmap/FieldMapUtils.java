package org.dozer.fieldmap;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.dozer.classmap.ClassMap;
import org.dozer.util.MappingUtils;
import org.dozer.util.ReflectionUtils;

public class FieldMapUtils {

    private FieldMapUtils() {
    }
    
    public static String getFieldName(List<DozerField> fields) {
        String name = StringUtils.EMPTY;
        for (DozerField field: fields) {
            name = StringUtils.join(new Object[]{field.getName()}, ",");
        }
        
        return name;
    }

    public static String getFieldKey(List<DozerField> fields) {
        String key = StringUtils.EMPTY;
        for (DozerField field: fields) {
            String fieldKey = field.getKey() != null ? field.getKey() : field.getName();
            key = StringUtils.join(new Object[]{key, fieldKey}, ",");
        }
        
        return key;
    }
    
    public static HintContainer hint(final Class<?>... types) {
        String hintName = ReflectionUtils.mergeTypeNames(types);
        return hint(hintName);
    }

    public static HintContainer hint(String types) {
        if (StringUtils.isNotEmpty(types)) {
            HintContainer hintContainer = new HintContainer();
            hintContainer.setHintName(types);
            return hintContainer;
        }
        
        return null;
    }

    public static Object getFieldsDefaultValues(List<DozerField> fields) {
        Object[] values = new Object[fields.size()];

        for (int i = 0; i < fields.size(); i++) {
            values[i] = fields.get(i).getDefaultValue();
        }

        return values;
    }

    public static String getDateFormat(DozerField field, ClassMap classMap) {
        if (!MappingUtils.isBlankOrNull(field.getDateFormat())) {
            return field.getDateFormat();
        }
          
        return classMap.getDateFormat();
    }
    
    public static List<DozerField> getCopy(List<DozerField> fields) {
        List<DozerField> copy = new ArrayList<DozerField>(fields.size());
        
        for (DozerField field : fields) {
            copy.add(field.copyOf());
        }
        
        return copy;
    }

}