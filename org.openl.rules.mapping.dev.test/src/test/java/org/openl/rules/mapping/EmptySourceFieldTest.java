package org.openl.rules.mapping;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;
import org.openl.rules.mapping.to.A;
import org.openl.rules.mapping.to.B;

public class EmptySourceFieldTest {

    @Test
    public void emptySourceTest() throws Exception {

        File source = new File("src/test/resources/org/openl/rules/mapping/emptysource/EmptySourceTest.xlsx");
        Mapper mapper = RulesBeanMapperFactory.createMapperInstance(source.toURI().toURL());

        A a = new A();
        B b = new B();
        b.setAString("b");

        mapper.map(a, b);
        
        assertEquals(null, b.getAString());
    }
    
}
