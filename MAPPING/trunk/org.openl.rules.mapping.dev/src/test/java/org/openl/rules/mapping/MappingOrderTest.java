package org.openl.rules.mapping;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;
import org.openl.rules.mapping.to.A;
import org.openl.rules.mapping.to.C;

public class MappingOrderTest {

    @Test
    public void mappingOrderTest() {

        File source = new File("src/test/resources/org/openl/rules/mapping/order/MappingOrderTest.xlsx");
        Mapper mapper = RulesBeanMapperFactory.createMapperInstance(source);

        A a = new A();
        a.setAStringArray(new String[] {"a", "b"});
        
        C c = mapper.map(a, C.class);
        
        assertEquals("b", c.getAString());
    }
}
