package org.dozer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: SStrukau
 */
public class MapIdConverterAggregator extends MapIdConverter {

    private List<MapIdConverter> mapIdConverters = new ArrayList<MapIdConverter>();

    @Override
    public boolean canConvert(MappingParameters mappingParameters, Object existingDestinationFieldValue, Object sourceFieldValue, Class<?> destinationClass, Class<?> sourceClass) {
        return getConverter(mappingParameters, existingDestinationFieldValue, sourceFieldValue, destinationClass, sourceClass) != null;
    }

    @Override
    public String convert(MappingParameters mappingParameters, Object existingDestinationFieldValue, Object sourceFieldValue, Class<?> destinationClass, Class<?> sourceClass) {
        return getConverter(mappingParameters, existingDestinationFieldValue, sourceFieldValue, destinationClass, sourceClass).convert(mappingParameters, existingDestinationFieldValue, sourceFieldValue, destinationClass, sourceClass);
    }

    private MapIdConverter getConverter(MappingParameters mappingParameters, Object existingDestinationFieldValue, Object sourceFieldValue, Class<?> destinationClass, Class<?> sourceClass) {
        for (MapIdConverter mapIdConverter : mapIdConverters) {
            if (mapIdConverter.canConvert(mappingParameters, existingDestinationFieldValue, sourceFieldValue, destinationClass, sourceClass)) {
                return mapIdConverter;
            }
        }

        return null;
    }

    public void setMapIdConverters(List<MapIdConverter> mapIdConverters) {
        this.mapIdConverters = mapIdConverters;
    }
}
