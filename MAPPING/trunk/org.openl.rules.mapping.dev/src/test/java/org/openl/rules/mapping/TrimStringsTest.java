package org.openl.rules.mapping;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;
import org.openl.rules.mapping.to.A;
import org.openl.rules.mapping.to.B;
import org.openl.rules.mapping.to.C;

public class TrimStringsTest {

    @Test
    public void trimStringsTest() {

        File source = new File("src/test/resources/org/openl/rules/mapping/trimstrings/TrimStringsTest.xlsx");
        RulesBeanMapper mapper = RulesBeanMapperFactory.createMapperInstance(source);

        A a = new A();
        a.setAString(" a-string ");

        B b = mapper.map(a, B.class);
        assertEquals(" a-string ", b.getAString());
        
        C c = mapper.map(a, C.class);
        assertEquals("a-string", c.getAString());
        
        a.setAString("a-string");
        
        mapper.map(a, b);
        assertEquals("a-string", b.getAString());
        
        mapper.map(a, c);
        assertEquals("a-string", c.getAString());
    }
}
