package org.openl.rules.mapping;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import static org.junit.Assert.*;

import org.openl.rules.mapping.to.A;
import org.openl.rules.mapping.to.B;
import org.openl.rules.mapping.to.C;
import org.openl.rules.mapping.to.D;
import org.openl.rules.mapping.to.F;
import org.openl.rules.mapping.to.containers.ArrayContainer;
import org.openl.rules.mapping.to.containers.ListOfTypeBContainer;
import org.openl.rules.mapping.to.containers.ListOfTypeCContainer;
import org.openl.rules.mapping.to.inheritance.ChildA;
import org.openl.rules.mapping.to.inheritance.ChildE;

public class FieldHintSupportTest {

    @Test
    public void indexedListsWithoutGenericTypeTest() {

        File source = new File("src/test/resources/org/openl/rules/mapping/hints/FieldHintsSupportTest.xlsx");
        Mapper mapper = RulesBeanMapperFactory.createMapperInstance(source);

        C c = new C();
        c.setAString("root");

        List aList = new ArrayList();

        A a = new A();
        a.setAString("a-string");
        a.setAnInteger(100);
        a.setAStringArray(new String[] { "x", "y" });

        aList.add(a);

        ChildA childA = new ChildA();
        childA.setAString("child-a-string");
        childA.setAnInteger(50);

        List childList = new ArrayList();

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
        assertEquals(7, acontainer.getArray().length);
        assertEquals("root", acontainer.getArray()[0]);
        assertEquals("a-string", acontainer.getArray()[1]);
        assertEquals("child-a-string", acontainer.getArray()[2]);
        assertEquals("inner-a-string", acontainer.getArray()[3]);
        assertEquals("10", acontainer.getArray()[4]);
        assertEquals("inner-a-string", acontainer.getArray()[5]);
        assertEquals(Integer.valueOf(10), acontainer.getArray()[6]);

        C c1 = mapper.map(acontainer, C.class);
        
        assertEquals("root", c1.getAString());
        assertEquals(2, c1.getAList().size());
        assertEquals(A.class, c1.getAList().get(0).getClass());
        assertEquals("a-string", ((A)c1.getAList().get(0)).getAString());
        assertEquals(ChildA.class, c1.getAList().get(1).getClass());
        assertEquals("child-a-string", ((ChildA)c1.getAList().get(1)).getAString());
        assertEquals(2, ((ChildA)c1.getAList().get(1)).getAList().size());
        assertEquals(A.class, ((ChildA)c1.getAList().get(1)).getAList().get(0).getClass());
        assertEquals("inner-a-string", ((A)((ChildA)c1.getAList().get(1)).getAList().get(0)).getAString());
        assertEquals(A.class, ((ChildA)c1.getAList().get(1)).getAList().get(1).getClass());
        assertEquals("10", ((A)((ChildA)c1.getAList().get(1)).getAList().get(1)).getAString());
    }
    
    @Test
    public void typeCastTest() {
         
        File source = new File("src/test/resources/org/openl/rules/mapping/hints/TypeCastTest.xlsx");
        Mapper mapper = RulesBeanMapperFactory.createMapperInstance(source);

        ChildA childA = new ChildA();
        childA.setAString("child-a-string");

        List childList = new ArrayList();

        A innerA = new A();
        innerA.setAString("inner-a-string");

        ChildA innerChildA = new ChildA();
        innerChildA.setAString("inner-child-a-string");

        childList.add(innerA);
        childList.add(innerChildA);

        childA.setAList(childList);

        F f = new F();
        f.setA(childA);

        ArrayContainer acontainer = mapper.map(f, ArrayContainer.class);
        assertEquals(2, acontainer.getArray().length);
        assertEquals("inner-a-string", acontainer.getArray()[0]);
        assertEquals("inner-child-a-string", acontainer.getArray()[1]);

        F f1 = mapper.map(acontainer, F.class);
        
        assertEquals(ChildA.class, f1.getA().getClass());
        assertEquals(2, ((ChildA) f1.getA()).getAList().size());
        assertEquals("inner-a-string", ((A)((ChildA) f1.getA()).getAList().get(0)).getAString());
        assertEquals("inner-child-a-string", ((A)((ChildA) f1.getA()).getAList().get(1)).getAString());
    }
    
    @Test
    public void simplifiedHintTest() {
         
        File source = new File("src/test/resources/org/openl/rules/mapping/hints/SimplifiedHintsChainTest.xlsx");
        Mapper mapper = RulesBeanMapperFactory.createMapperInstance(source);

        ChildA childA = new ChildA();
        childA.setAString("child-a-string");

        B b = new B();
        b.setAString("b");
        
        D d = new D();
        d.setAnInt(10);
        
        ChildE e = new ChildE();
        e.setB(b);
        e.setD(d);

        childA.setE(e);
        
        F f = new F();
        f.setA(childA);

        ArrayContainer acontainer = mapper.map(f, ArrayContainer.class);
        assertEquals("b", acontainer.getArray()[0]);
        assertEquals("10", acontainer.getArray()[1]);

        F f1 = mapper.map(acontainer, F.class);
        
        assertEquals(A.class, f1.getA().getClass());
        assertEquals("b", ((ChildE) f1.getA().getE()).getB().getAString());
        assertEquals(10, ((ChildE) f1.getA().getE()).getD().getAnInt());
    }

    @Test
    public void mappingWithTypedListTest() {

        File source = new File("src/test/resources/org/openl/rules/mapping/hints/DeepMappingWithHintsTest.xlsx");
        Mapper mapper = RulesBeanMapperFactory.createMapperInstance(source);

        C c1 = new C();
        c1.setAString("c1");

        B b1 = new B();
        b1.setAString("b1");

        c1.setB(b1);

        C c2 = new C();
        c2.setAString("c2");

        B b2 = new B();
        b2.setAString("b2");

        c2.setB(b2);

        C c3 = new C();
        c3.setAString("c3");

        B b3 = new B();
        b3.setAString("b3");

        c3.setB(b3);

        ListOfTypeCContainer array = new ListOfTypeCContainer();
        array.setList(Arrays.asList(c1, c2, c3));

        ListOfTypeBContainer a = mapper.map(array, ListOfTypeBContainer.class);

        assertEquals(3, a.getList().size());
        assertEquals("b1", a.getList().get(0).getAString());
        assertEquals("b2", a.getList().get(1).getAString());
        assertEquals("b3", a.getList().get(2).getAString());

        ListOfTypeCContainer array1 = mapper.map(a, ListOfTypeCContainer.class);

        assertEquals(3, array1.getList().size());
        assertEquals("b1", array1.getList().get(0).getB().getAString());
        assertEquals("b2", array1.getList().get(1).getB().getAString());
        assertEquals("b3", array1.getList().get(2).getB().getAString());
    }
}
