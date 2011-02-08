package org.openl.rules.mapping;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;
import org.openl.rules.mapping.to.A;
import org.openl.rules.mapping.to.B;

public class EmptySourceFieldTest {

    @Test
    public void emptySourceTest() {

        File source = new File("src/test/resources/org/openl/rules/mapping/emptySource/EmptySourceTest.xlsx");
        RulesBeanMapper mapper = RulesBeanMapperFactory.createMapperInstance(source);

        A a = new A();
        B b = new B();
        b.setAString("b");

        mapper.map(a, b);
        
        assertEquals(null, b.getAString());
    }
    
}
