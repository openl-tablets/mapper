package org.dozer.loader.api;

import org.dozer.loader.DozerBuilder;

public interface ConfigurationMappingOption {

    void apply(DozerBuilder.ConfigurationBuilder configBuilder);
}
