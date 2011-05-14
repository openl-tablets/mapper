package org.openl.rules.mapping.loader.dozer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.dozer.CustomConverter;
import org.dozer.FieldMappingCondition;
import org.dozer.loader.api.BeanMappingBuilder;
import org.dozer.loader.api.FieldDefinition;
import org.dozer.loader.api.FieldsMappingOption;
import org.dozer.loader.api.TypeMappingBuilder;
import org.openl.rules.mapping.definition.BeanMap;
import org.openl.rules.mapping.definition.CollectionItemDiscriminatorDescriptor;
import org.openl.rules.mapping.definition.ConditionDescriptor;
import org.openl.rules.mapping.definition.ConverterDescriptor;
import org.openl.rules.mapping.definition.FieldMap;

/**
 * Builds Dozer's mapping model using rules mapping model.
 */
public class DozerMappingBuilder {

    private DozerMappingsContainer mappingsContainer = new DozerMappingsContainer();
    
    public DozerMappingBuilder mapping(BeanMap beanMapping) {
        addMapping(beanMapping);
        return this;
    }
    
    public DozerMappingBuilder customConvertersWithId(Map<String, CustomConverter> userConverters) {
        if(userConverters != null) {
            mappingsContainer.getConverters().putAll(userConverters);
        }
        return this;
    }
    
    public DozerMappingBuilder conditionsWithId(Map<String, FieldMappingCondition> conditionsWithId) {
        if(conditionsWithId != null) {
            mappingsContainer.getConditions().putAll(conditionsWithId);
        }
        return this;
    }
    
    public DozerMappingsContainer build() {
        return mappingsContainer;
    }
    
    private void addMapping(BeanMap beanMapping) {

        final TypeDefinitionBuilder srcTypeBuilder = new TypeDefinitionBuilder(beanMapping.getSrcClass());
        final TypeDefinitionBuilder destTypeBuilder = new TypeDefinitionBuilder(beanMapping.getDestClass());

        if (beanMapping.getConfiguration().getClassBBeanFactory() != null) {
            // Use factory class name instead of Class object because Dozer has
            // pre-processing stuff. See ConstructionStrategies.ByFactory class
            // for more details.
            //
            destTypeBuilder.beanFactory(beanMapping.getConfiguration().getClassBBeanFactory().getName());
        }

        // Default implementation uses one way mapping.
        //
        final TypeMappingOptionsBuilder typeOptionsBuilder = new TypeMappingOptionsBuilder();
        // The bean map level options.
        typeOptionsBuilder.oneWay();
        typeOptionsBuilder.wildcard(beanMapping.getConfiguration().isWildcard());
        typeOptionsBuilder.trimStrings(beanMapping.getConfiguration().isTrimStrings());
        typeOptionsBuilder.mapEmptyStrings(beanMapping.getConfiguration().isMapEmptyStrings());
        typeOptionsBuilder.mapNulls(beanMapping.getConfiguration().isMapNulls());
        typeOptionsBuilder.dateFormat(beanMapping.getConfiguration().getDateFormat());
        typeOptionsBuilder.requiredFields(beanMapping.getConfiguration().isRequiredFields());
        
        // Convert field mappings to Dozer's model. 
        //
        final List<FieldsMapping> fieldsMappings = new ArrayList<FieldsMapping>(beanMapping.getFieldMappings().size());
        for (FieldMap fieldMapping : beanMapping.getFieldMappings()) {
            fieldsMappings.add(buildFieldMappping(fieldMapping));
        }
        
        // Register all mapping conditions and custom converters 
        //
        for (FieldsMapping fieldsMapping : fieldsMappings) {
            if (fieldsMapping.getCondition() != null && !mappingsContainer.getConditions().containsKey(
                fieldsMapping.getCondition().getConditionId())) {
                mappingsContainer.getConditions().put(fieldsMapping.getCondition().getConditionId(),
                    fieldsMapping.getCondition().getFieldMappingCondition());
            }
            
            if (fieldsMapping.getConverter() != null && !mappingsContainer.getConverters().containsKey(
                fieldsMapping.getConverter().getConverterId())) {
                mappingsContainer.getConverters().put(fieldsMapping.getConverter().getConverterId(),
                    fieldsMapping.getConverter().getInstance());
            }
            
            if (fieldsMapping.getCollectionItemDiscriminator() != null && !mappingsContainer.getCollectionItemDiscriminators().containsKey(
                fieldsMapping.getCollectionItemDiscriminator().getDiscriminatorId())) {
                mappingsContainer.getCollectionItemDiscriminators().put(fieldsMapping.getCollectionItemDiscriminator().getDiscriminatorId(),
                    fieldsMapping.getCollectionItemDiscriminator().getDiscriminator());
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
        
        if (src != null && src.length > 1) {
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
                
                if(fieldMap.getSrcDateFormat() != null && fieldMap.getSrcDateFormat().length != 0 && fieldMap.getSrcDateFormat()[i] != null) {
                    fieldDefBuilder.dateFormat(fieldMap.getSrcDateFormat()[i]);
                }

                
                definitions[i] = fieldDefBuilder.build();
            }
            fieldsMapping.setSrc(definitions);
        } else {
            String fieldName = StringUtils.EMPTY;
            if (src != null && src.length > 0 && StringUtils.isNotEmpty(src[0])) {
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

            if (fieldMap.getSrcDateFormat() != null && fieldMap.getSrcDateFormat()[0] != null) {
                fieldDefBuilder.dateFormat(fieldMap.getSrcDateFormat()[0]);
            }

            fieldsMapping.setSrc(new FieldDefinition[] { fieldDefBuilder.build() });
        }

        FieldDefinitionBuilder fieldDefBuilder = new FieldDefinitionBuilder(fieldMap.getDest());
        fieldDefBuilder.required(fieldMap.isRequired());
        fieldDefBuilder.defaultValue(fieldMap.getDefaultValue());
        fieldDefBuilder.createMethod(fieldMap.getCreateMethod());
        
        if (fieldMap.getDestDateFormat() != null) {
            fieldDefBuilder.dateFormat(fieldMap.getDestDateFormat());
        }
        if (fieldMap.getDestHint() != null) {
            fieldDefBuilder.deepHint(getHint(fieldMap.getDestHint()));
        }
        if (fieldMap.getDestType() != null) {
            fieldDefBuilder.hint(getHint(fieldMap.getDestType()));
        }

        FieldMappingOptionsBuilder optionsBuilder = new FieldMappingOptionsBuilder();
        optionsBuilder.mapNulls(fieldMap.isMapNulls());
        optionsBuilder.mapEmptyStrings(fieldMap.isMapEmptyStrings());
        optionsBuilder.trimStrings(fieldMap.isTrimStrings());

        if (fieldMap.getConverter() != null) {
            optionsBuilder.customConverterId(fieldMap.getConverter().getConverterId());
            fieldsMapping.setConverter(fieldMap.getConverter());
        }
        if (fieldMap.getCondition() != null) {
            optionsBuilder.conditionId(fieldMap.getCondition().getConditionId());
            fieldsMapping.setCondition(fieldMap.getCondition());
        }
        
        if (fieldMap.getCollectionItemDiscriminator() != null) {
            optionsBuilder.collectionItemDiscriminatorId(fieldMap.getCollectionItemDiscriminator().getDiscriminatorId());
            fieldsMapping.setCollectionItemDiscriminator(fieldMap.getCollectionItemDiscriminator());
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
    private static class FieldsMapping {

        private FieldDefinition[] src;
        private FieldDefinition dest;
        private FieldsMappingOption[] options;
        private ConverterDescriptor converter;
        private ConditionDescriptor condition;
        private CollectionItemDiscriminatorDescriptor collectionItemDiscriminator;

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

        public CollectionItemDiscriminatorDescriptor getCollectionItemDiscriminator() {
            return collectionItemDiscriminator;
        }

        public void setCollectionItemDiscriminator(CollectionItemDiscriminatorDescriptor collectionItemDiscriminator) {
            this.collectionItemDiscriminator = collectionItemDiscriminator;
        }
        
    }

}
