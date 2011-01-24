package org.openl.rules.mapping;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.openl.rules.mapping.to.A;
import org.openl.rules.mapping.to.B;
import org.openl.rules.mapping.to.C;
import org.openl.rules.mapping.to.containers.ArrayContainer;
import org.openl.rules.mapping.to.inheritance.ChildA;

public class MultiSourceFieldTest {

    @Test
    public void indexedListsWithoutGenericTypeTest() {

        File source = new File("src/test/resources/org/openl/rules/mapping/multisource/MultiSourceFieldTest1.xlsx");
        RulesBeanMapper mapper = RulesBeanMapperFactory.createMapperInstance(source);

        C c = new C();
        c.setAString("root");

        List<A> aList = new ArrayList<A>();

        A a = new A();
        a.setAString("a-string");

        aList.add(a);

        ChildA childA = new ChildA();
        childA.setAString("child-a-string");

        List<A> childList = new ArrayList<A>();

        A innerA = new A();
        innerA.setAString("inner-a-string");

        ChildA innerChildA = new ChildA();
        innerChildA.setAString("10");

        childList.add(innerA);
        childList.add(innerChildA);

        childA.setAList(childList);

        aList.add(childA);

        c.setAList(aList);

        ArrayContainer acontainer = mapper.map(c, ArrayContainer.class);
        assertEquals(5, acontainer.getArray().length);
        assertEquals("root", acontainer.getArray()[0]);
        assertEquals("a-string", acontainer.getArray()[1]);
        assertEquals("child-a-string", acontainer.getArray()[2]);
        assertEquals("inner-a-string", acontainer.getArray()[3]);
        assertEquals("10", acontainer.getArray()[4]);

        C c1 = mapper.map(acontainer, C.class);
        
        assertEquals(null, c1.getAList());
    }
    
    
    @Test
    public void singleDestinationElementTest() {

        File source = new File("src/test/resources/org/openl/rules/mapping/multisource/MultiSourceFieldTest2.xlsx");
        RulesBeanMapper mapper = RulesBeanMapperFactory.createMapperInstance(source);

        C c = new C();
        c.setAString("root");

        B b = new B();
        b.setAString("b-string");
        b.setAnInteger(100);

        c.setB(b);

        ArrayContainer acontainer = mapper.map(c, ArrayContainer.class);
        assertEquals(6, acontainer.getArray().length);
        assertEquals("root", ((String[])acontainer.getArray()[5])[0]);
        assertEquals("b-string", ((String[])acontainer.getArray()[5])[1]);
        assertEquals("100", ((String[])acontainer.getArray()[5])[2]);

        C c1 = mapper.map(acontainer, C.class);
        
        assertEquals(null, c1.getAList());
    }

}
