package org.openl.rules.mapping;

import org.dozer.MappingContext;
import org.junit.Test;
import org.openl.rules.mapping.composite.mapId.destination.DestPolicy;
import org.openl.rules.mapping.composite.mapId.source.SrcAddress;
import org.openl.rules.mapping.composite.mapId.source.SrcLocation;
import org.openl.rules.mapping.composite.mapId.source.SrcPolicy;
import org.openl.rules.mapping.to.A;
import org.openl.rules.mapping.to.B;
import org.openl.rules.mapping.to.E;
import org.openl.rules.mapping.to.inheritance.ChildE;

import java.io.File;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class CompositeMapIdTest {

    @Test
    public void simpleCompositeMapIdTest() {

        File source = new File("src/test/resources/org/openl/rules/mapping/composite/mapid/LocationMapping.xlsx");
        Mapper mapper = RulesBeanMapperFactory.createMapperInstance(source);

        SrcAddress srcAddress = new SrcAddress();
        srcAddress.setZipCode("60001");
        srcAddress.setCity("Antioch");
        srcAddress.setAddrLine1("PO 1204A");

        SrcLocation srcLocation = new SrcLocation();
        srcLocation.setId(1L);
        srcLocation.setDescription("The First Location");
        srcLocation.setSrcAddress(srcAddress);

        SrcPolicy srcPolicy = new SrcPolicy();
        srcPolicy.setInsuredName("Santuccio Salieri");
        srcPolicy.setSrcLocations(new ArrayList<SrcLocation>());
        srcPolicy.getSrcLocations().add(srcLocation);

        MappingContext context = new MappingContext();
        context.setMapId("IL");

        DestPolicy destPolicy = mapper.map(srcPolicy, DestPolicy.class, context);

        assertNotNull(destPolicy);
        assertEquals("Santuccio Salieri", destPolicy.getNamedInsured());
        assertEquals(1, destPolicy.getLocDTOs().length);

        assertEquals("The First Location", destPolicy.getLocDTOs()[0].getComment());
        assertEquals("1", destPolicy.getLocDTOs()[0].getIndex());

        assertEquals("60001", destPolicy.getLocDTOs()[0].getDestFields().getField1());
        assertEquals("Antioch", destPolicy.getLocDTOs()[0].getDestFields().getField2());
        assertEquals("PO 1204A", destPolicy.getLocDTOs()[0].getDestFields().getField3());


    }

}
