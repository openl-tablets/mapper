package org.openl.rules.mapping;

import java.util.List;
import java.util.Map;

import org.dozer.BeanFactory;
import org.dozer.CustomConverter;
import org.dozer.DozerEventListener;
import org.dozer.FieldMappingCondition;
import org.dozer.MappingContext;

/**
 * The OpenL Rules Tablets based public implementation of the {@link Mapper}
 * abstraction. Current implementation uses OpenL Rules engine as a mapping
 * definition tool and performs appropriate definitions processing before actual
 * bean mapping will be invoked.
 * 
 */
public class RulesBeanMapper implements Mapper {

    private MappingProcessor mappingProcessor;
    private Class<?> instanceClass;
    private Object instance;
    private TypeResolver typeResolver;

    private Map<String, CustomConverter> customConvertersWithId;
    private Map<String, FieldMappingCondition> conditionsWithId;

    private List<DozerEventListener> eventListeners;
    
	private Map<String, BeanFactory> factories;
	
    public RulesBeanMapper(Class<?> instanceClass, Object instance, TypeResolver typeResolver,
        Map<String, CustomConverter> customConvertersWithId, Map<String, FieldMappingCondition> conditionsWithId) {
    	this(instanceClass, instance, typeResolver, customConvertersWithId, conditionsWithId, null, null);
    }

	public RulesBeanMapper(Class<?> instanceClass, Object instance,
			TypeResolver typeResolver,
			Map<String, CustomConverter> customConvertersWithId,
			Map<String, FieldMappingCondition> conditionsWithId,
			Map<String, BeanFactory> factories) {
		this(instanceClass, instance, typeResolver, customConvertersWithId,
				conditionsWithId, factories, null);
	}

	public RulesBeanMapper(Class<?> instanceClass, Object instance,
			TypeResolver typeResolver,
			Map<String, CustomConverter> customConvertersWithId,
			Map<String, FieldMappingCondition> conditionsWithId,
			Map<String, BeanFactory> factories,
			List<DozerEventListener> eventListeners) {
		this.instanceClass = instanceClass;
		this.instance = instance;
		this.typeResolver = typeResolver;
		this.customConvertersWithId = customConvertersWithId;
		this.conditionsWithId = conditionsWithId;
		this.factories = factories;
		this.eventListeners = eventListeners;
	}	

    public <T> T map(Object source, Class<T> destinationClass) {
        return getMappingProcessor().map(source, destinationClass);
    }

    public void map(Object source, Object destination) {
        getMappingProcessor().map(source, destination);
    }
    
    public <T> T map(Object source, Class<T> destinationClass, MappingContext context) {
        return getMappingProcessor().map(source, destinationClass, context);
    }

    public void map(Object source, Object destination, MappingContext context) {
        getMappingProcessor().map(source, destination, context);
    }

    private MappingProcessor getMappingProcessor() {
        if (mappingProcessor == null) {
            mappingProcessor = new MappingProcessor(instanceClass, instance, typeResolver, customConvertersWithId, conditionsWithId, factories, eventListeners);
        }

        return mappingProcessor;
    }

}
