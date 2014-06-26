package org.openl.rules.mapping;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.dozer.*;
import org.dozer.classmap.ClassMap;
import org.junit.Test;
import org.openl.rules.mapping.composite.mapId.destination.DestPolicy;
import org.openl.rules.mapping.composite.mapId.source.SrcAddress;
import org.openl.rules.mapping.composite.mapId.source.SrcLocation;
import org.openl.rules.mapping.composite.mapId.source.SrcPolicy;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CompositeMapIdMultipleStatesTest {

    @Test
    public void simpleCompositeMapIdTest() {

        MappingContext context = new MappingContext();
        context.setMapId("");

        Map<String, FieldMappingCondition> conditions = new HashMap<String, FieldMappingCondition>();
        Map<String, CustomConverter> converters = new HashMap<String, CustomConverter>();
        MapIdConverterAggregator mapIdConverterAggregator = new MapIdConverterAggregator();
        List<MapIdConverter> mapIdConverters = new ArrayList<MapIdConverter>();
        mapIdConverters.add(new MapIdConverter() {
            @Override
            public boolean canConvert(MappingParameters mappingParameters, Object existingDestinationFieldValue, Object sourceFieldValue, Class<?> destinationClass, Class<?> sourceClass) {
                if (sourceFieldValue instanceof SrcLocation) {
                    return true;
                }

                return false;
            }

            @Override
            public String convert(MappingParameters mappingParameters, Object existingDestinationFieldValue, Object sourceFieldValue, Class<?> destinationClass, Class<?> sourceClass) {
                if (sourceFieldValue != null) {
                    return ((SrcLocation)sourceFieldValue).getStateCode();
                }

                return null;
            }
        });
        mapIdConverterAggregator.setMapIdConverters(mapIdConverters);
        converters.put("mapIdConverterAggregator", mapIdConverterAggregator);


        File source = new File("src/test/resources/org/openl/rules/mapping/composite/mapid/BasePolicyMS.xlsx");
        Mapper mapper = RulesBeanMapperFactory.createMapperInstance(source, converters, conditions);
        MappingParameters parameters = new MappingParameters();
        parameters.put("context", context);

        context.setParams(parameters);
        SrcPolicy srcPolicy = populatePolicy();
        DestPolicy destPolicy = mapper.map(srcPolicy, DestPolicy.class, context);

        assertNotNull(destPolicy);
        assertEquals("Santuccio Salieri", destPolicy.getNamedInsured());
        assertEquals(2, destPolicy.getLocDTOs().length);

        assertEquals("The First Location", destPolicy.getLocDTOs()[0].getComment());
        assertEquals("1", destPolicy.getLocDTOs()[0].getIndex());

        assertEquals("The Second Location", destPolicy.getLocDTOs()[1].getComment());
        assertEquals("2", destPolicy.getLocDTOs()[1].getIndex());

        assertEquals("60001", destPolicy.getLocDTOs()[0].getDestFields().getField3());
        assertEquals("Antioch", destPolicy.getLocDTOs()[0].getDestFields().getField4());
        assertEquals("PO 1204A", destPolicy.getLocDTOs()[0].getDestFields().getField5());

        assertEquals("85532", destPolicy.getLocDTOs()[1].getDestFields().getField1());
        assertEquals("TheEdgeCity", destPolicy.getLocDTOs()[1].getDestFields().getField2());
        assertEquals("PO A34", destPolicy.getLocDTOs()[1].getDestFields().getField3());
    }

    private SrcPolicy populatePolicy() {

        SrcAddress srcAddress1 = new SrcAddress();
        srcAddress1.setZipCode("60001");
        srcAddress1.setCity("Antioch");
        srcAddress1.setAddrLine1("PO 1204A");

        SrcAddress srcAddress2 = new SrcAddress();
        srcAddress2.setZipCode("85532");
        srcAddress2.setCity("TheEdgeCity");
        srcAddress2.setAddrLine1("PO A34");

        SrcLocation srcLocation1 = new SrcLocation();
        srcLocation1.setId(1L);
        srcLocation1.setDescription("The First Location");
        srcLocation1.setSrcAddress(srcAddress1);
        srcLocation1.setStateCode("IL");

        SrcLocation srcLocation2 = new SrcLocation();
        srcLocation2.setId(2L);
        srcLocation2.setDescription("The Second Location");
        srcLocation2.setSrcAddress(srcAddress2);
        srcLocation2.setStateCode("AZ");


        SrcPolicy srcPolicy = new SrcPolicy();
        srcPolicy.setInsuredName("Santuccio Salieri");
        srcPolicy.setSrcLocations(new ArrayList<SrcLocation>());
        srcPolicy.getSrcLocations().add(srcLocation1);
        srcPolicy.getSrcLocations().add(srcLocation2);

        return srcPolicy;
    }

    public static String serialize(Object obj) {
        final XStream xStream = new XStream(new DomDriver());
        return xStream.toXML(obj);
    }

    public void test1() throws Exception {
        final XStream xStream = new XStream(new DomDriver());
        ConcurrentHashMap<String, ClassMap> classMap = (ConcurrentHashMap) xStream.fromXML(new FileReader("/home/const/tmp/20130819/classmap1.xml"));

        for (String key: classMap.keySet()) {
            System.out.println(key);
        }

    }
}
