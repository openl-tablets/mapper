package org.openl.rules.mapping;

/**
 * The OpenL Rules Tablets based public implementation of the {@link Mapper}
 * abstraction. Current implementation uses OpenL Rules engine as a mapping
 * definition tool and performs appropriate definitions processing before actual
 * bean mapping will be invoked.
 * 
 */
public class RulesBeanMapper implements Mapper {

    private MappingProcessor mappingProcessor;

    public RulesBeanMapper(Class<?> instanceClass, Object instance) {
        this.mappingProcessor = new MappingProcessor(instanceClass, instance);
    }

    public <T> T map(Object source, Class<T> destinationClass) {
        return mappingProcessor.map(source, destinationClass);
    }

    public void map(Object source, Object destination) {
        mappingProcessor.map(source, destination);
    }

}
