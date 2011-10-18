package org.openl.rules.mapping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.junit.Test;
import org.openl.rules.mapping.to.A;
import org.openl.rules.mapping.to.B;
import org.openl.rules.mapping.to.E;
import org.openl.rules.mapping.to.G;

public class DefaultFieldValueTest {

    @Test
    public void defaultStringValueTest() {

        File source = new File("src/test/resources/org/openl/rules/mapping/defaultValue/DefaultFieldValueTest.xlsx");
        Mapper mapper = RulesBeanMapperFactory.createMapperInstance(source);

        A a = new A();
        B b = new B();

        mapper.map(a, b);
        
        assertEquals("some string", b.getAString());
        assertEquals(Integer.valueOf(10), b.getAnInteger());
    }
    
    @Test
    public void defaultFieldInitializationTest() {

        File source = new File("src/test/resources/org/openl/rules/mapping/defaultValue/DefaultFieldValueTest.xlsx");
        Mapper mapper = RulesBeanMapperFactory.createMapperInstance(source);

        A a = new A();

        E e = mapper.map(a, E.class);
        
        assertNotNull(e.getD());
    }

    @Test
    public void defaultFieldInitializationThruConstructorTest() {

        File source = new File("src/test/resources/org/openl/rules/mapping/defaultValue/DefaultFieldValueTest.xlsx");
        Mapper mapper = RulesBeanMapperFactory.createMapperInstance(source);

        A a = new A();

        G g = mapper.map(a, G.class);
        
        assertEquals(10, g.getIntContainer().getAnInt());
    }

}
