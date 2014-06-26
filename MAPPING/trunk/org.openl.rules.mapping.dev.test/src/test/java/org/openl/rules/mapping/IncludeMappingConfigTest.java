package org.openl.rules.mapping;

import org.dozer.MappingContext;
import org.junit.Test;
import org.openl.rules.mapping.to.A;
import org.openl.rules.mapping.to.B;
import org.openl.rules.mapping.to.D;
import org.openl.rules.mapping.to.E;
import org.openl.rules.mapping.to.inheritance.ChildE;

import java.io.File;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class IncludeMappingConfigTest {

    @Test
    public void superClassMappingTest() {

        File source = new File("src/test/resources/org/openl/rules/mapping/include/ChildMappingConfig.xlsx");
        Mapper mapper = RulesBeanMapperFactory.createMapperInstance(source);

        A a = new A();
        a.setAnInteger(100);
        a.setAString("a-string");

        MappingContext context = new MappingContext();
        context.setMapId("AZ");

        ChildE childE = mapper.map(a, ChildE.class, context);
        
        assertEquals(100, childE.getD().getAnInt());
        assertEquals("a-string", childE.getBase());
        assertNull(childE.getB());

        E e = mapper.map(a, E.class, context);
        
        assertEquals(100, e.getD().getAnInt());

        B b = mapper.map(a, B.class);
        assertEquals(100, (long) b.getAnInteger());

        context.setMapId("IL");
        childE = mapper.map(a, ChildE.class, context);
        assertEquals("a-string", childE.getB().getAString());
        assertEquals("a-string", childE.getBase());
        assertNull(childE.getD());
    }

}
