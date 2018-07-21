package org.openl.rules.mapping.definition;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * A bean that holds all required information about one way mapping for a single
 * bean map.
 */
public class BeanMap {

    private Class<?> srcClass;
    private Class<?> destClass;
    private Map<String, FieldMap> fieldMappings = new LinkedHashMap<String, FieldMap>();
    private BeanMapConfiguration configuration;

    public Class<?> getSrcClass() {
        return srcClass;
    }

    public void setSrcClass(Class<?> srcClass) {
        this.srcClass = srcClass;
    }

    public Class<?> getDestClass() {
        return destClass;
    }

    public void setDestClass(Class<?> destClass) {
        this.destClass = destClass;
    }

    public BeanMapConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(BeanMapConfiguration configuration) {
        this.configuration = configuration;
    }

    @SuppressWarnings("unchecked")
    public Collection<FieldMap> getFieldMappings() {
        return CollectionUtils.unmodifiableCollection(fieldMappings.values());
    }
    
    public void addFieldMap(String key, FieldMap fieldMap) {
        fieldMappings.put(key, fieldMap);
    }
    
    public boolean hasFieldMap(String key) {
        return fieldMappings.containsKey(key);
    }
    
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("srcClass", srcClass).append(
            "destClass", destClass).append("configuration", configuration)
            .toString();
    }
}
