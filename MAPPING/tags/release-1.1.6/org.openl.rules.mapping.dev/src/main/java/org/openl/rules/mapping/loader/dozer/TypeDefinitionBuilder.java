package org.openl.rules.mapping.loader.dozer;

import org.dozer.loader.api.TypeDefinition;

public class TypeDefinitionBuilder {

    private TypeDefinition typeDefinition;

    public TypeDefinitionBuilder(Class<?> type) {
        typeDefinition = new TypeDefinition(type);
    }

    public TypeDefinitionBuilder beanFactory(String beanFactory) {
        typeDefinition.beanFactory(beanFactory);
        return this;
    }
    
    public TypeDefinition build() {
        return typeDefinition;
    }
}
