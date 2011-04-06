package org.openl.rules.mapping;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;
import org.openl.rules.mapping.to.B;
import org.openl.rules.mapping.to.C;
import org.openl.rules.mapping.to.inheritance.ChildE;

public class CreateMethodTest {

    @Test
    public void createMethodTest() {

        File source = new File("src/test/resources/org/openl/rules/mapping/createMethod/CreateMethodTest.xlsx");
        Mapper mapper = RulesBeanMapperFactory.createMapperInstance(source);

        C c = new C();
        c.setAString("c-string");

        B b = new B();
        b.setAString("b-string");
        b.setAnInteger(100);

        c.setB(b);

        ChildE e = mapper.map(c, ChildE.class);

        assertEquals("b-custom", e.getB().getAString());
        assertEquals(null, e.getB().getAnInteger());

        C c1 = mapper.map(e, C.class);

        assertEquals("b-custom", c1.getB().getAString());    
        assertEquals(null, c1.getB().getAnInteger());
    }
}
