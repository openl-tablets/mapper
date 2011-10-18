package org.openl.rules.mapping;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.dozer.MappingContext;
import org.dozer.MappingParameters;
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
    
    @Test
    public void mappingParamsAwareBeanFactoryTest() {

        File source = new File("src/test/resources/org/openl/rules/mapping/beanfactory/MappingParamsAwareBeanFactoryTest.xlsx");
        Mapper mapper = RulesBeanMapperFactory.createMapperInstance(source);

        E e = mapper.map(new C(), E.class);

        assertEquals(ChildE.class, e.getClass());
        assertEquals(null, e.getAString());
        assertEquals(null, ((ChildE)e).getB());
        
        MappingContext context = new MappingContext();
        MappingParameters params = new MappingParameters();
        params.put("key", "a-value");
        context.setParams(params);
        
        E e1 = mapper.map(new C(), E.class, context);

        assertEquals(ChildE.class, e1.getClass());
        assertEquals("a-value", e1.getAString());
        assertEquals(null, ((ChildE)e1).getB());

        C c = mapper.map(new E(), C.class);

        assertEquals(C.class, c.getClass());
        assertEquals(null, c.getAString());
        assertEquals(null, c.getB());
        
        C c1 = mapper.map(new E(), C.class, context);

        assertEquals(C.class, c1.getClass());
        assertEquals("a-value", c1.getAString());
        assertEquals(null, c1.getB());

    }

}
