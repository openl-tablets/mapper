package org.dozer.loader.api;

import org.dozer.CustomConverter;
import org.dozer.classmap.RelationshipType;
import org.dozer.loader.DozerBuilder.ConfigurationBuilder;
import org.dozer.loader.DozerBuilder.CustomConverterBuilder;

public class ConfigurationMappingOptions {

    public static ConfigurationMappingOption stopOnErrors(final Boolean value) {
        return new ConfigurationMappingOption() {
            public void apply(ConfigurationBuilder configBuilder) {
                configBuilder.stopOnErrors(value);
            }
        };
    }

    public static ConfigurationMappingOption dateFormat(final String format) {
        return new ConfigurationMappingOption() {
            public void apply(ConfigurationBuilder configBuilder) {
                configBuilder.dateFormat(format);
            }
        };
    }

    public static ConfigurationMappingOption wildcard(final Boolean value) {
        return new ConfigurationMappingOption() {
            public void apply(ConfigurationBuilder configBuilder) {
                configBuilder.wildcard(value);
            }
        };
    }

    public static ConfigurationMappingOption trimStrings(final Boolean value) {
        return new ConfigurationMappingOption() {
            public void apply(ConfigurationBuilder configBuilder) {
                configBuilder.trimStrings(value);
            }
        };
    }

    public static ConfigurationMappingOption relationshipType(final RelationshipType value) {
        return new ConfigurationMappingOption() {
            public void apply(ConfigurationBuilder configBuilder) {
                configBuilder.relationshipType(value);
            }
        };
    }

    public static ConfigurationMappingOption beanFactory(final String name) {
        return new ConfigurationMappingOption() {
            public void apply(ConfigurationBuilder configBuilder) {
                configBuilder.beanFactory(name);
            }
        };
    }

    public static ConfigurationMappingOption customConverter(final Class<? extends CustomConverter> type,
        final Class<?> aClass, final Class<?> bClass) {
        return new ConfigurationMappingOption() {
            public void apply(ConfigurationBuilder configBuilder) {
                CustomConverterBuilder customConverterBuilder = configBuilder.customConverter(type);
                customConverterBuilder.classA(aClass);
                customConverterBuilder.classB(bClass);
            }
        };
    }

    public static ConfigurationMappingOption customConverter(final CustomConverter converter, final Class<?> aClass,
        final Class<?> bClass) {
        return new ConfigurationMappingOption() {
            public void apply(ConfigurationBuilder configBuilder) {
                CustomConverterBuilder customConverterBuilder = configBuilder.customConverter(converter);
                customConverterBuilder.classA(aClass);
                customConverterBuilder.classB(bClass);
            }
        };
    }

    public static ConfigurationMappingOption copyByReference(final String typeMask) {
        return new ConfigurationMappingOption() {
            public void apply(ConfigurationBuilder configBuilder) {
                configBuilder.copyByReference(typeMask);
            }
        };
    }

    public static ConfigurationMappingOption allowedException(final String type) {
        return new ConfigurationMappingOption() {
            public void apply(ConfigurationBuilder configBuilder) {
                configBuilder.allowedException(type);
            }
        };
    }

    public static ConfigurationMappingOption allowedException(final Class<? extends Exception> type) {
        return new ConfigurationMappingOption() {
            public void apply(ConfigurationBuilder configBuilder) {
                configBuilder.allowedException(type);
            }
        };
    }

}
