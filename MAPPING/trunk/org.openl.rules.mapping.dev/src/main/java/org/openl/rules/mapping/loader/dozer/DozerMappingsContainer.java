package org.openl.rules.mapping.loader.dozer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dozer.CollectionItemDiscriminator;
import org.dozer.CustomConverter;
import org.dozer.DozerEventListener;
import org.dozer.FieldMappingCondition;
import org.dozer.loader.api.BeanMappingBuilder;

/**
 * Container that holds processed information about mappings. Intended for
 * internal use only.
 */
public class DozerMappingsContainer {

    private final List<BeanMappingBuilder> mappingBuilders = new ArrayList<BeanMappingBuilder>();
    private final Map<String, CustomConverter> converters = new HashMap<String, CustomConverter>();
    private final Map<String, FieldMappingCondition> conditions = new HashMap<String, FieldMappingCondition>();
    private final Map<String, CollectionItemDiscriminator> collectionItemDiscriminators = new HashMap<String, CollectionItemDiscriminator>();
	private final List<DozerEventListener> eventListeners = new ArrayList<DozerEventListener>();

    public List<BeanMappingBuilder> getMappingBuilders() {
        return mappingBuilders;
    }

    public Map<String, CustomConverter> getConverters() {
        return converters;
    }

    public Map<String, FieldMappingCondition> getConditions() {
        return conditions;
    }

    public Map<String, CollectionItemDiscriminator> getCollectionItemDiscriminators() {
        return collectionItemDiscriminators;
    }

	public List<DozerEventListener> getEventListeners() {
		return eventListeners;
	}
}
