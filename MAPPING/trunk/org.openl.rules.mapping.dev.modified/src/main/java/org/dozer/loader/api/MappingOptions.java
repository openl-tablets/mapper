package org.dozer.loader.api;

import org.dozer.CustomConverter;
import org.dozer.classmap.RelationshipType;

public class MappingOptions {

    public static ConfigurationMappingOption defaultStopOnErrors(final Boolean value) {
        return ConfigurationMappingOptions.stopOnErrors(value);
    }

    public static ConfigurationMappingOption defaultDateFormat(final String format) {
        return ConfigurationMappingOptions.dateFormat(format);
    }

    public static ConfigurationMappingOption defaultWildcard(final Boolean value) {
        return ConfigurationMappingOptions.wildcard(value);
    }

    public static ConfigurationMappingOption defaultTrimStrings(final Boolean value) {
        return ConfigurationMappingOptions.trimStrings(value);
    }

    public static ConfigurationMappingOption defaultRelationshipType(final RelationshipType value) {
        return ConfigurationMappingOptions.relationshipType(value);
    }

    public static ConfigurationMappingOption defaultBeanFactory(final String name) {
        return ConfigurationMappingOptions.beanFactory(name);
    }

    public static ConfigurationMappingOption defaultCustomConverter(Class<? extends CustomConverter> type, Class<?> aClass, Class<?> bClass) {
        return ConfigurationMappingOptions.customConverter(type, aClass, bClass); 
    }
    
    public static ConfigurationMappingOption defaultCustomConverter(CustomConverter converter, Class<?> aClass, Class<?> bClass) {
        return ConfigurationMappingOptions.customConverter(converter, aClass, bClass);
    }

    public static ConfigurationMappingOption defaultCopyByReference(final String typeMask) {
        return ConfigurationMappingOptions.copyByReference(typeMask);
    }

    public static ConfigurationMappingOption allowedException(final String type) {
        return ConfigurationMappingOptions.allowedException(type);
    }

    public static ConfigurationMappingOption allowedException(final Class<? extends Exception> type) {
        return ConfigurationMappingOptions.allowedException(type);
    }

    public static FieldsMappingOption copyByReference() {
        return FieldsMappingOptions.copyByReference();
    }

    public static FieldsMappingOption customConverter(final String type) {
        return FieldsMappingOptions.customConverter(type);
    }

    public static FieldsMappingOption customConverter(final Class<? extends CustomConverter> type) {
        return FieldsMappingOptions.customConverter(type);
    }

    public static FieldsMappingOption customConverter(final Class<? extends CustomConverter> type,
        final String parameter) {
        return FieldsMappingOptions.customConverter(type, parameter);
    }

    public static FieldsMappingOption customConverter(final String type, final String parameter) {
        return FieldsMappingOptions.customConverter(type, parameter);
    }

    public static FieldsMappingOption customConverterId(final String id) {
        return FieldsMappingOptions.customConverterId(id);
    }

    public static FieldsMappingOption condition(final String type) {
        return FieldsMappingOptions.condition(type);
    }

    public static FieldsMappingOption conditionId(String id) {
        return FieldsMappingOptions.conditionId(id);
    }

    public static FieldsMappingOption useMapId(String mapId) {
        return FieldsMappingOptions.useMapId(mapId);
    }

    public static FieldsMappingOption fieldOneWay() {
        return FieldsMappingOptions.fieldOneWay();
    }
    
    public static FieldsMappingOption mapNulls(boolean value) {
        return FieldsMappingOptions.mapNulls(value);
    }

    public static FieldsMappingOption mapEmptyStrings(boolean value) {
        return FieldsMappingOptions.mapEmptyStrings(value);
    }

    public static FieldsMappingOption collectionStrategy(final boolean removeOrphans,
        final RelationshipType relationshipType) {
        return FieldsMappingOptions.collectionStrategy(removeOrphans, relationshipType);
    }

    public static TypeMappingOption mapId(final String mapId) {
        return TypeMappingOptions.mapId(mapId);
    }

    public static TypeMappingOption beanFactory(final String value) {
        return TypeMappingOptions.beanFactory(value);
    }

    public static TypeMappingOption dateFormat(final String value) {
        return TypeMappingOptions.dateFormat(value);
    }

    public static TypeMappingOption mapEmptyString(final boolean value) {
        return TypeMappingOptions.mapEmptyString(value);
    }

    public static TypeMappingOption mapNull(final boolean value) {
        return TypeMappingOptions.mapNull(value);
    }

    public static TypeMappingOption relationshipType(final RelationshipType value) {
        return TypeMappingOptions.relationshipType(value);
    }

    public static TypeMappingOption stopOnErrors(final boolean value) {
        return TypeMappingOptions.stopOnErrors(value);
    }

    public static TypeMappingOption trimStrings(final boolean value) {
        return TypeMappingOptions.trimStrings(value);
    }

    public static TypeMappingOption oneWay() {
        return TypeMappingOptions.oneWay();
    }

    public static TypeMappingOption wildcard(final boolean value) {
        return TypeMappingOptions.wildcard(value);
    }

}
