package org.openl.rules.mapping;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.dozer.MappingContext;
import org.junit.Test;
import org.openl.rules.mapping.to.A;
import org.openl.rules.mapping.to.B;
import org.openl.rules.mapping.to.C;
import org.openl.rules.mapping.to.inheritance.ChildE;

public class MapIdSupportTest {

    @Test
    public void mappingIdTest() throws Exception {

        File source = new File("src/test/resources/org/openl/rules/mapping/mapid/MapIdTest.xlsx");
        Mapper mapper = RulesBeanMapperFactory.createMapperInstance(source.toURI().toURL());

        A a = new A();
        a.setAString("a");
        a.setAnInteger(10);
        
        C c1 = mapper.map(a, C.class);

        assertEquals("a", c1.getB().getAString());
        assertEquals(Integer.valueOf(10), c1.getB().getAnInteger());
        
        MappingContext context = new MappingContext();
        context.setMapId("customMapId");
        C c2 = mapper.map(a, C.class, context);

        assertEquals("a", c2.getB().getAString());
        assertEquals(Integer.valueOf(100), c2.getB().getAnInteger());
    }
    
    @Test
    public void customConverterReusageTest() throws Exception {

        File source = new File("src/test/resources/org/openl/rules/mapping/mapid/UsingCustomConvertersWithMapIdTest.xlsx");
        Mapper mapper = RulesBeanMapperFactory.createMapperInstance(source.toURI().toURL());

        C c = new C();
        B b = new B ();
        b.setAnInteger(10);
        c.setB(b);
        
        A a1 = mapper.map(c, A.class);

        assertEquals(Integer.valueOf(10), a1.getAnInteger());
        assertEquals(100, a1.getE().getD().getAnInt());
        
        MappingContext context = new MappingContext();
        context.setMapId("customMapId");
        A a2 = mapper.map(c, A.class, context);

        assertEquals(Integer.valueOf(100), a2.getAnInteger());
        assertEquals(100, a2.getE().getD().getAnInt());
    }
    
    @Test
    public void supperMappingTest() throws Exception {

        File source = new File("src/test/resources/org/openl/rules/mapping/mapid/SupperMappingWithMapIdTest.xlsx");
        Mapper mapper = RulesBeanMapperFactory.createMapperInstance(source.toURI().toURL());

        A a = new A();
        a.setAString("a");
        a.setAnInteger(10);
        
        ChildE e1 = mapper.map(a, ChildE.class);

        assertEquals(Integer.valueOf(10), e1.getB().getAnInteger());
        assertEquals(10, e1.getD().getAnInt());
        
        MappingContext context = new MappingContext();
        context.setMapId("customMapId");

        ChildE e2 = mapper.map(a, ChildE.class, context);

        assertEquals(Integer.valueOf(10), e2.getB().getAnInteger());
        assertEquals(100, e2.getD().getAnInt());
    }
    
    @Test
    public void supperMappingOverridingTest1() throws Exception {

        File source = new File("src/test/resources/org/openl/rules/mapping/mapid/MappingOverridingWithMapIdTest1.xlsx");
        Mapper mapper = RulesBeanMapperFactory.createMapperInstance(source.toURI().toURL());

        A a = new A();
        a.setAString("a");
        a.setAnInteger(10);
        
        ChildE e1 = mapper.map(a, ChildE.class);

        assertEquals(Integer.valueOf(10), e1.getB().getAnInteger());
        assertEquals(10, e1.getD().getAnInt());
        
        MappingContext context = new MappingContext();
        context.setMapId("customMapId");

        ChildE e2 = mapper.map(a, ChildE.class, context);

        assertEquals(Integer.valueOf(10), e2.getB().getAnInteger());
        assertEquals(10, e2.getD().getAnInt());
    }

    @Test
    public void supperMappingOverridingTest2() throws Exception {

        File source = new File("src/test/resources/org/openl/rules/mapping/mapid/MappingOverridingWithMapIdTest2.xlsx");
        Mapper mapper = RulesBeanMapperFactory.createMapperInstance(source.toURI().toURL());

        A a = new A();
        a.setAString("a");
        a.setAnInteger(10);
        
        ChildE e1 = mapper.map(a, ChildE.class);

        assertEquals(Integer.valueOf(10), e1.getB().getAnInteger());
        assertEquals(10, e1.getD().getAnInt());
        
        MappingContext context = new MappingContext();
        context.setMapId("customMapId");

        ChildE e2 = mapper.map(a, ChildE.class, context);

        assertEquals(Integer.valueOf(10), e2.getB().getAnInteger());
        assertEquals(100, e2.getD().getAnInt());
    }

}
