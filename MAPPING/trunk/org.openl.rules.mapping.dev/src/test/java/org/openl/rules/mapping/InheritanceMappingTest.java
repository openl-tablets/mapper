package org.openl.rules.mapping;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;
import org.openl.rules.mapping.to.A;
import org.openl.rules.mapping.to.B;
import org.openl.rules.mapping.to.D;
import org.openl.rules.mapping.to.E;
import org.openl.rules.mapping.to.inheritance.ChildE;

public class InheritanceMappingTest {

    @Test
    public void superClassMappingTest() {

        File source = new File("src/test/resources/org/openl/rules/mapping/inheritance/SuperClassMappingTest.xlsx");
        RulesBeanMapper mapper = RulesBeanMapperFactory.createMapperInstance(source);

        A a = new A();
        a.setAnInteger(100);
        a.setAString("a-string");
        
        ChildE childE = mapper.map(a, ChildE.class);
        
        assertEquals(100, childE.getD().getAnInt());
        assertEquals("a-string", childE.getB().getAString());
        
        E e = mapper.map(a, E.class);
        
        assertEquals(100, e.getD().getAnInt());
    }
    
    @Test
    public void superMappingsOrderTest() {

        File source = new File("src/test/resources/org/openl/rules/mapping/inheritance/SuperMappingsOrderTest.xlsx");
        RulesBeanMapper mapper = RulesBeanMapperFactory.createMapperInstance(source);

        ChildE childE = new ChildE();
        B b = new B();
        b.setAnInteger(100);
        childE.setB(b);
        
        D d = new D();
        d.setAnInt(500);
        childE.setD(d);
        
        A a = mapper.map(childE, A.class);
        
        assertEquals(Integer.valueOf(500), a.getAnInteger());
    }

//    @Test
    public void superMappingOverrideTest() {

        File source = new File("src/test/resources/org/openl/rules/mapping/inheritance/SuperClassMappingOverrideTest.xlsx");
        RulesBeanMapper mapper = RulesBeanMapperFactory.createMapperInstance(source);

        A a = new A();
        a.setAnInteger(100);
        
        ChildE childE = mapper.map(a, ChildE.class);
        
        assertEquals(1000, childE.getD().getAnInt());
        
        E e = mapper.map(a, E.class);
        
        assertEquals(100, e.getD().getAnInt());
    }
}
