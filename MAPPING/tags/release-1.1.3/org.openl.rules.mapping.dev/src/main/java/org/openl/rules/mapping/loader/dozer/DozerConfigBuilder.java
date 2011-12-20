package org.openl.rules.mapping.loader.dozer;

import org.dozer.loader.api.BeanMappingBuilder;
import org.openl.rules.mapping.definition.ConverterDescriptor;

public class DozerConfigBuilder {

    private DozerConfigContainer configContainer = new DozerConfigContainer();
    private ConfigOptionBuilder configOptionBuilder = new ConfigOptionBuilder();
    
    public DozerConfigContainer build() {
     
        // Create Dozer's mapping model.
        // 
        BeanMappingBuilder beanMappingBuilder = new BeanMappingBuilder() {
            protected void configure() {
                config(configOptionBuilder.build());
            }
        };
        
        // Add mapping model to mappings container
        //
        configContainer.getMappingBuilders().add(beanMappingBuilder);
        
        return configContainer;
    }
    
    public DozerConfigBuilder defaultConverter(ConverterDescriptor defaultConverter) {
        addDefaultConverter(defaultConverter);
        return this;
    }
    
    public DozerConfigBuilder dateFormat(String format) {
        configOptionBuilder.dateFormat(format);
        return this;
    }
    
    public DozerConfigBuilder wildcard(boolean value) {
        configOptionBuilder.wildcard(value);
        return this;
    }

    public DozerConfigBuilder trimStrings(boolean value) {
        configOptionBuilder.trimStrings(value);
        return this;
    }
    
    public DozerConfigBuilder mapNulls(boolean value) {
        configOptionBuilder.mapNulls(value);
        return this;
    }

    public DozerConfigBuilder mapEmptyStrings(boolean value) {
        configOptionBuilder.mapEmptyStrings(value);
        return this;
    }

    public DozerConfigBuilder requiredFields(boolean value) {
        configOptionBuilder.requiredFields(value);
        return this;
    }
    
    private void addDefaultConverter(ConverterDescriptor defaultConverter) {
        configOptionBuilder.defaultConverter(defaultConverter.getInstance(), defaultConverter.getSrcType(),
            defaultConverter.getDestType());
    }
}
