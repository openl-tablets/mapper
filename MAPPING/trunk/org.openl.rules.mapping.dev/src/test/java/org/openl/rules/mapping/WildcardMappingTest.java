package org.openl.rules.mapping;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;
import org.openl.rules.mapping.to.B;
import org.openl.rules.mapping.to.C;
import org.openl.rules.mapping.to.inheritance.ChildE;

public class WildcardMappingTest {

    @Test
    public void wildcardSupportTest() {

        File source = new File("src/test/resources/org/openl/rules/mapping/wildcard/WildcardMappingTest.xlsx");
        RulesBeanMapper mapper = RulesBeanMapperFactory.createMapperInstance(source);

        C c = new C();
        c.setAString("c-string");

        B b = new B();
        b.setAString("b-string");
        b.setAnInteger(100);

        c.setB(b);

        ChildE e = mapper.map(c, ChildE.class);

        assertEquals("c-string", e.getAString());
        assertEquals("b-string", e.getB().getAString());
        assertEquals(Integer.valueOf(100), e.getB().getAnInteger());

        C c1 = mapper.map(e, C.class);

        assertEquals("c-string", c1.getAString());
        assertEquals(null, c1.getB());
    }

}
