package org.openl.rules.mapping;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;
import org.openl.rules.mapping.to.A;
import org.openl.rules.mapping.to.B;
import org.openl.rules.mapping.to.C;
import org.openl.rules.mapping.to.E;
import org.openl.rules.mapping.to.F;

public class OneToOneMappingsTest {

    @Test
    public void fieldToFieldTest1() throws Exception {

        File source = new File("src/test/resources/org/openl/rules/mapping/OneToOneMappingsTest.xlsx");
        Mapper mapper = RulesBeanMapperFactory.createMapperInstance(source.toURI().toURL());

        A a = new A();
        a.setAString("string");
        a.setAnInteger(10);
        a.setAStringArray(new String[] { "x", null, "y" });

        C c = mapper.map(a, C.class);

        A a1 = new A();
        mapper.map(c, a1);

        F f = new F();
        f.setA(a);

        E e = mapper.map(f, E.class);

        assertEquals(10, e.getD().getAnInt());

        B b = new B();
        b.setAString("string");
        A a2 = new A();
        mapper.map(b, a2);

        B b1 = mapper.map(a2, B.class);
        assertEquals("string", b1.getAString());
    }

}
