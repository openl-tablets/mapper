package org.openl.rules.mapping.loader.discriminator;

import org.dozer.MappingParameters;

public interface RulesCollectionItemDiscriminator {

    Object discriminate(Class<?> sourceItemType,
            Object sourceItemValue,
            Class<?> destCollectionType,
            Class<?> destItemType,
            Object destCollection);

    Object discriminate(MappingParameters params,
            Class<?> sourceItemType,
            Object sourceItemValue,
            Class<?> destCollectionType,
            Class<?> destItemType,
            Object destCollection);
}
