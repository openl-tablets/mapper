package org.openl.rules.mapping;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.Arrays;

import org.junit.Test;
import org.openl.rules.mapping.to.A;
import org.openl.rules.mapping.to.B;
import org.openl.rules.mapping.to.C;
import org.openl.rules.mapping.to.containers.ArrayOfTypeBContainer;
import org.openl.rules.mapping.to.containers.ArrayOfTypeCContainer;
import org.openl.rules.mapping.to.containers.ListOfTypeBContainer;
import org.openl.rules.mapping.to.containers.ListOfTypeCContainer;

public class DeepMappingTest {

    @Test
    public void mappingWithArrayTest() {

        File source = new File("src/test/resources/org/openl/rules/mapping/index/DeepMappingWithArrayTest.xlsx");
        RulesBeanMapper mapper = RulesBeanMapperFactory.createMapperInstance(source);

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

        ArrayOfTypeCContainer array = new ArrayOfTypeCContainer();
        array.setArray(new C[] { c1, c2, c3 });

        ArrayOfTypeBContainer a = mapper.map(array, ArrayOfTypeBContainer.class);

        assertEquals(3, a.getArray().length);
        assertEquals("b1", a.getArray()[0].getAString());
        assertEquals("b2", a.getArray()[1].getAString());
        assertEquals("b3", a.getArray()[2].getAString());

        ArrayOfTypeCContainer array1 = mapper.map(a, ArrayOfTypeCContainer.class);

        assertEquals(3, array1.getArray().length);
        assertEquals("b1", array1.getArray()[0].getB().getAString());
        assertEquals("b2", array1.getArray()[1].getB().getAString());
        assertEquals("b3", array1.getArray()[2].getB().getAString());
    }

    @Test
    public void usingIndexedArrayInCustomConvertersTest() {
        File source = new File(
            "src/test/resources/org/openl/rules/mapping/index/UsingIndexedArrayInCustomConvertersTest.xlsx");
        RulesBeanMapper mapper = RulesBeanMapperFactory.createMapperInstance(source);

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

        ArrayOfTypeCContainer array = new ArrayOfTypeCContainer();
        array.setArray(new C[] { c1, c2, c3 });

        A a = mapper.map(array, A.class);

        assertEquals(3, a.getAStringArray().length);
        assertEquals("b1", a.getAStringArray()[0]);
        assertEquals("b2", a.getAStringArray()[1]);
        assertEquals("b3", a.getAStringArray()[2]);

    }

    @Test
    public void mappingWithTypedListTest() {

        File source = new File("src/test/resources/org/openl/rules/mapping/index/DeepMappingWithTypedListTest.xlsx");
        RulesBeanMapper mapper = RulesBeanMapperFactory.createMapperInstance(source);

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
