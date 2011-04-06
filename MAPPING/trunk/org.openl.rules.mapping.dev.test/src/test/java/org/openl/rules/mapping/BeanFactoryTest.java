package org.openl.rules.mapping;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;
import org.openl.rules.mapping.to.B;
import org.openl.rules.mapping.to.C;
import org.openl.rules.mapping.to.E;
import org.openl.rules.mapping.to.inheritance.ChildE;

public class BeanFactoryTest {

    @Test
    public void beanFactoryTest() {

        File source = new File("src/test/resources/org/openl/rules/mapping/beanfactory/BeanFactoryTest.xlsx");
        Mapper mapper = RulesBeanMapperFactory.createMapperInstance(source);

        C c = new C();
        c.setAString("c-string");

        B b = new B();
        b.setAString("b-string");
        b.setAnInteger(100);

        c.setB(b);

        E e = mapper.map(c, E.class);

        assertEquals(ChildE.class, e.getClass());
        assertEquals("c-string", e.getAString());
        assertEquals(null, ((ChildE)e).getB());

        C c1 = mapper.map(e, C.class);

        assertEquals(C.class, c1.getClass());
        assertEquals("c-string", c1.getAString());
        assertEquals(null, c1.getB());
    }

}
