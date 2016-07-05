package org.openl.rules.mapping.loader.discriminator;

import org.dozer.CollectionItemDiscriminator;
import org.openl.rules.mapping.MappingParameters;
import org.dozer.MappingParamsAware;

public class CollectionItemDiscriminatorWrapper implements MappingParamsAware, CollectionItemDiscriminator {

    private RulesCollectionItemDiscriminator collectionItemDiscriminatorProxy;
    private MappingParameters params;

    public CollectionItemDiscriminatorWrapper(RulesCollectionItemDiscriminator collectionItemDiscriminatorProxy) {
        this.collectionItemDiscriminatorProxy = collectionItemDiscriminatorProxy;
    }

    public void setMappingParams(MappingParameters params) {
        this.params = params;
    }

    @Override
    public Object discriminate(Class<?> sourceItemType,
            Object sourceItemValue,
            Class<?> destCollectionType,
            Class<?> destItemType,
            Object destCollection) {
        
        if (params != null) {
            return collectionItemDiscriminatorProxy.discriminate(params, sourceItemType, sourceItemValue, destCollectionType, destItemType, destCollection);
        }

        return collectionItemDiscriminatorProxy.discriminate(sourceItemType, sourceItemValue, destCollectionType, destItemType, destCollection);
    }

}
