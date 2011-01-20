package org.openl.rules.mapping.loader.dozer;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.dozer.loader.api.BeanMappingBuilder;
import org.dozer.loader.api.FieldDefinition;
import org.dozer.loader.api.FieldsMappingOption;
import org.dozer.loader.api.TypeMappingBuilder;
import org.openl.rules.mapping.RulesMappingException;
import org.openl.rules.mapping.definition.BeanMap;
import org.openl.rules.mapping.definition.ConditionDescriptor;
import org.openl.rules.mapping.definition.ConverterDescriptor;
import org.openl.rules.mapping.definition.FieldMap;

public class DozerMappingBuilder {

    private DozerMappingsContainer mappingsContainer = new DozerMappingsContainer();
    
    public DozerMappingBuilder mapping(BeanMap beanMapping) {
        addMapping(beanMapping);
        return this;
    }
    
    public DozerMappingsContainer build() {
        return mappingsContainer;
    }
    
    private void addMapping(BeanMap beanMapping) {

        final TypeDefinitionBuilder srcTypeBuilder = new TypeDefinitionBuilder(beanMapping.getSrcClass());
        final TypeDefinitionBuilder destTypeBuilder = new TypeDefinitionBuilder(beanMapping.getDestClass());

        if (StringUtils.isNotEmpty(beanMapping.getDestBeanFactory())) {
            destTypeBuilder.beanFactory(beanMapping.getDestBeanFactory());
        }

        // Default implementation uses one way mapping.
        //
        final TypeMappingOptionsBuilder typeOptionsBuilder = new TypeMappingOptionsBuilder();
        typeOptionsBuilder.oneWay();
        typeOptionsBuilder.wildcard(false);

        // Convert field mappings to Dozer's model. 
        //
        final List<FieldsMapping> fieldsMappings = new ArrayList<FieldsMapping>(beanMapping.getFieldMappings().size());
        for (FieldMap fieldMapping : beanMapping.getFieldMappings()) {
            fieldsMappings.add(buildFieldMappping(fieldMapping));
        }
        
        // Register all mapping conditions and custom converters 
        //
        for (FieldsMapping fieldsMapping : fieldsMappings) {
            if (fieldsMapping.getCondition() != null) {
                if (mappingsContainer.getConditions().containsKey(fieldsMapping.getCondition().getConditionId())) {
                    throw new RulesMappingException(String.format("Duplicate condition with '%s' ID is found",
                        fieldsMapping.getCondition().getConditionId()));
                }

                mappingsContainer.getConditions().put(fieldsMapping.getCondition().getConditionId(),
                    fieldsMapping.getCondition().getFieldMappingCondition());
            }
            
            if (fieldsMapping.getConverter() != null) {
                if (mappingsContainer.getConverters().containsKey(fieldsMapping.getConverter().getConverterId())) {
                    throw new RulesMappingException(String.format("Duplicate custom converter with '%s' ID is found",
                        fieldsMapping.getConverter().getConverterId()));
                }

                mappingsContainer.getConverters().put(fieldsMapping.getConverter().getConverterId(),
                    fieldsMapping.getConverter().getInstance());
            }
        }

        // Create Dozer's mapping model.
        // 
        BeanMappingBuilder beanMappingBuilder = new BeanMappingBuilder() {
            protected void configure() {
                TypeMappingBuilder mappingBuilder = mapping(srcTypeBuilder.build(), destTypeBuilder.build(),
                    typeOptionsBuilder.build());
                for (FieldsMapping fieldsMapping : fieldsMappings) {
                    mappingBuilder.fields(fieldsMapping.getSrc(), fieldsMapping.getDest(), fieldsMapping.getOptions());
                }
            }
        };
        
        // Add mapping model to mappings container
        //
        mappingsContainer.getMappingBuilders().add(beanMappingBuilder);
    }

    private FieldsMapping buildFieldMappping(FieldMap fieldMap) {

        FieldsMapping fieldsMapping = new FieldsMapping();
        String[] src = fieldMap.getSrc();
        
        if (src.length > 1) {
            FieldDefinition[] definitions = new FieldDefinition[src.length];
            for (int i = 0; i < src.length; i++) {
                FieldDefinitionBuilder fieldDefBuilder = new FieldDefinitionBuilder(src[i]);

                if (fieldMap.getSrcHint() != null && fieldMap.getSrcHint()[i] != null) {
                    Class<?>[] fieldHint = fieldMap.getSrcHint()[i];
                    fieldDefBuilder.deepHint(getHint(fieldHint));
                }
                
                if(fieldMap.getSrcType() != null && fieldMap.getSrcType().length != 0 && fieldMap.getSrcType()[i] != null) {
                    fieldDefBuilder.hint(getHint(fieldMap.getSrcType()));
                }
                
                definitions[i] = fieldDefBuilder.build();
            }
            fieldsMapping.setSrc(definitions);
        } else {
            String fieldName = StringUtils.EMPTY;
            if (StringUtils.isNotEmpty(src[0])) {
                fieldName = src[0];
            }
            FieldDefinitionBuilder fieldDefBuilder = new FieldDefinitionBuilder(fieldName);
            if (fieldMap.getSrcHint() != null && fieldMap.getSrcHint()[0] != null) {
                Class<?>[] fieldHint = fieldMap.getSrcHint()[0];
                fieldDefBuilder.deepHint(getHint(fieldHint));
            }
            if (fieldMap.getSrcType() != null && fieldMap.getSrcType()[0] != null) {
                fieldDefBuilder.hint(getHint(fieldMap.getSrcType()[0]));
            }
            
            fieldsMapping.setSrc(new FieldDefinition[] { fieldDefBuilder.build() });
        }

        FieldDefinitionBuilder fieldDefBuilder = new FieldDefinitionBuilder(fieldMap.getDest());
        fieldDefBuilder.required(fieldMap.isRequired());
        fieldDefBuilder.defaultValue(fieldMap.getDefaultValue());
        fieldDefBuilder.createMethod(fieldMap.getCreateMethod());

        if (fieldMap.getDestHint() != null) {
            fieldDefBuilder.deepHint(getHint(fieldMap.getDestHint()));
        }
        if (fieldMap.getDestType() != null) {
            fieldDefBuilder.hint(getHint(fieldMap.getDestType()));
        }

        FieldMappingOptionsBuilder optionsBuilder = new FieldMappingOptionsBuilder();
        if (fieldMap.getConverter() != null) {
            optionsBuilder.customConverterId(fieldMap.getConverter().getConverterId());
            fieldsMapping.setConverter(fieldMap.getConverter());
        }
        if (fieldMap.getCondition() != null) {
            optionsBuilder.conditionId(fieldMap.getCondition().getConditionId());
            fieldsMapping.setCondition(fieldMap.getCondition());
        }
        
        fieldsMapping.setDest(fieldDefBuilder.build());
        fieldsMapping.setOptions(optionsBuilder.build());
        
        return fieldsMapping;
    }
    
    private String getHint(Class<?>... types) {
        String[] names = new String[types.length];
        for (int i = 0; i < types.length; i++) {
            if (types[i] == null) {
                names[i] = "";
            } else {
                names[i] = types[i].getName();
            }
        }

        return StringUtils.join(names, ",");
    }

    /**
     * Intended for internal usage. Holds information about a single field map
     * and used as an intermediate container during mapping models conversion.
     */
    private class FieldsMapping {

        private FieldDefinition[] src;
        private FieldDefinition dest;
        private FieldsMappingOption[] options;
        private ConverterDescriptor converter;
        private ConditionDescriptor condition;

        public FieldDefinition[] getSrc() {
            return src;
        }

        public void setSrc(FieldDefinition[] src) {
            this.src = src;
        }

        public FieldDefinition getDest() {
            return dest;
        }

        public void setDest(FieldDefinition dest) {
            this.dest = dest;
        }

        public FieldsMappingOption[] getOptions() {
            return options;
        }

        public void setOptions(FieldsMappingOption[] options) {
            this.options = options;
        }

        public ConverterDescriptor getConverter() {
            return converter;
        }

        public void setConverter(ConverterDescriptor converter) {
            this.converter = converter;
        }

        public ConditionDescriptor getCondition() {
            return condition;
        }

        public void setCondition(ConditionDescriptor condition) {
            this.condition = condition;
        }
    }

}
