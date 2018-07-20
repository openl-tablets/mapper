package org.openl.rules.mapping;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;
import org.openl.rules.mapping.to.A;
import org.openl.rules.mapping.to.B;
import org.openl.rules.mapping.to.C;

public class MapNullsTest {

    @Test
    public void mapNullValueTest() throws Exception {

        File source = new File("src/test/resources/org/openl/rules/mapping/mapnulls/MapNullValueTest.xlsx");
        Mapper mapper = RulesBeanMapperFactory.createMapperInstance(source.toURI().toURL());

        A a = new A();
        B b = new B();
        b.setAString("b-string");
        C c = new C();
        c.setAString("c-string");
        
        mapper.map(a, b);
        assertEquals(null, b.getAString());
        
        mapper.map(a, c);
        assertEquals("c-string", c.getAString());
    }
}
