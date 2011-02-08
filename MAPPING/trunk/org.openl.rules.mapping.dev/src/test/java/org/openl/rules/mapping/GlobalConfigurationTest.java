package org.openl.rules.mapping;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;
import org.openl.rules.mapping.to.A;
import org.openl.rules.mapping.to.B;
import org.openl.rules.mapping.to.C;

public class GlobalConfigurationTest {

    @Test
    public void mapEmptyStringValueTest() {

        File source = new File("src/test/resources/org/openl/rules/mapping/config/GlobalConfigurationTest.xlsx");
        RulesBeanMapper mapper = RulesBeanMapperFactory.createMapperInstance(source);

        A a = new A();
        B b = new B();
        b.setAString("b-string");
        C c = new C();
        c.setAString("c-string");

        mapper.map(a, b);
        assertEquals("b-string", b.getAString());
        
        mapper.map(a, c);
        assertEquals("c-string", c.getAString());

        
        a.setAString("");
        b.setAString("b-string");
        c.setAString("c-string");
        
        mapper.map(a, b);
        assertEquals("", b.getAString());
        
        mapper.map(a, c);
        assertEquals("c-string", c.getAString());

    }

}
