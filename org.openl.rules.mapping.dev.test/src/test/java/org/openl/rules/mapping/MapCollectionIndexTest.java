package org.openl.rules.mapping;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.Arrays;

import org.junit.Test;
import org.openl.rules.mapping.to.B;
import org.openl.rules.mapping.to.C;
import org.openl.rules.mapping.to.containers.ArrayOfTypeBContainer;
import org.openl.rules.mapping.to.containers.ArrayOfTypeCContainer;
import org.openl.rules.mapping.to.containers.ListContainer;
import org.openl.rules.mapping.to.containers.ListOfTypeBContainer;
import org.openl.rules.mapping.to.containers.ListOfTypeCContainer;

public class MapCollectionIndexTest {

    @Test
    public void mappingWithArrayTest() throws Exception {

        File source = new File("src/test/resources/org/openl/rules/mapping/index/MapToArrayWithoutIndexTest1.xlsx");
        Mapper mapper = RulesBeanMapperFactory.createMapperInstance(source.toURI().toURL());

        B b1 = new B();
        b1.setAString("b1");

        B b2 = new B();
        b2.setAString("b2");

        ArrayOfTypeBContainer array = new ArrayOfTypeBContainer();
        array.setArray(new B[] { b1, b2 });

        ArrayOfTypeBContainer result = mapper.map(array, ArrayOfTypeBContainer.class);

        assertEquals(2, result.getArray().length);
        assertEquals("b1", result.getArray()[0].getAString());
        assertEquals("b2", result.getArray()[1].getAString());
    }
    
    @Test
    public void mappingWithTypedListTest() throws Exception {

        File source = new File("src/test/resources/org/openl/rules/mapping/index/MapToTypedListWithoutIndexTest1.xlsx");
        Mapper mapper = RulesBeanMapperFactory.createMapperInstance(source.toURI().toURL());

        B b1 = new B();
        b1.setAString("b1");

        B b2 = new B();
        b2.setAString("b2");

        ArrayOfTypeBContainer array = new ArrayOfTypeBContainer();
        array.setArray(new B[] { b1, b2 });

        ListOfTypeBContainer result = mapper.map(array, ListOfTypeBContainer.class);

        assertEquals(2, result.getList().size());
        assertEquals("b1", result.getList().get(0).getAString());
        assertEquals("b2", result.getList().get(1).getAString());
    }
    
    @Test
    public void mappingWithUntypedListTest() throws Exception {

        File source = new File("src/test/resources/org/openl/rules/mapping/index/MapToUntypedListWithoutIndexTest1.xlsx");
        Mapper mapper = RulesBeanMapperFactory.createMapperInstance(source.toURI().toURL());

        B b1 = new B();
        b1.setAString("b1");

        B b2 = new B();
        b2.setAString("b2");

        ArrayOfTypeBContainer array = new ArrayOfTypeBContainer();
        array.setArray(new B[] { b1, b2 });

        ListContainer result = mapper.map(array, ListContainer.class);

        assertEquals(2, result.getList().size());
        assertEquals("b1", ((B) result.getList().get(0)).getAString());
        assertEquals("b2", ((B) result.getList().get(1)).getAString());
    }
    
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void mappingWithArrayAndLastElementIndexValueTest() throws Exception {

        File source = new File("src/test/resources/org/openl/rules/mapping/index/MapToArrayWithoutIndexTest2.xlsx");
        Mapper mapper = RulesBeanMapperFactory.createMapperInstance(source.toURI().toURL());
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
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void mappingWithTypedListAndLastElementIndexTest() throws Exception {

        File source = new File("src/test/resources/org/openl/rules/mapping/index/MapToTypedListWithoutIndexTest2.xlsx");
        Mapper mapper = RulesBeanMapperFactory.createMapperInstance(source.toURI().toURL());

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

    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void mappingWithUntypedListAndLastElementIndexTest() throws Exception {

        File source = new File("src/test/resources/org/openl/rules/mapping/index/MapToUntypedListWithoutIndexTest2.xlsx");
        Mapper mapper = RulesBeanMapperFactory.createMapperInstance(source.toURI().toURL());

        B b1 = new B();
        b1.setAString("b1");

        B b2 = new B();
        b2.setAString("b2");

        ArrayOfTypeBContainer array = new ArrayOfTypeBContainer();
        array.setArray(new B[] { b1, b2 });

        ListContainer result = mapper.map(array, ListContainer.class);
    }
    
    @Test
    public void mappingIntoLastElementTest1() throws Exception {

        File source = new File("src/test/resources/org/openl/rules/mapping/index/MappingIntoLastElementTest1.xlsx");
        Mapper mapper = RulesBeanMapperFactory.createMapperInstance(source.toURI().toURL());

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
    }

    @Test
    public void mappingIntoLastElementTest2() throws Exception {

        File source = new File("src/test/resources/org/openl/rules/mapping/index/MappingIntoLastElementTest2.xlsx");
        Mapper mapper = RulesBeanMapperFactory.createMapperInstance(source.toURI().toURL());

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

        ListOfTypeCContainer list = new ListOfTypeCContainer();
        list.setList(Arrays.asList(c1, c2, c3));

        ListOfTypeBContainer a = mapper.map(list, ListOfTypeBContainer.class);

        assertEquals(3, a.getList().size());
        assertEquals("b1", a.getList().get(0).getAString());
        assertEquals("b2", a.getList().get(1).getAString());
        assertEquals("b3", a.getList().get(2).getAString());
    }

    @Test
    public void mappingIntoLastElementTest3() throws Exception {

        File source = new File("src/test/resources/org/openl/rules/mapping/index/MappingIntoLastElementTest1.xlsx");
        Mapper mapper = RulesBeanMapperFactory.createMapperInstance(source.toURI().toURL());

        B b1 = new B();
        b1.setAString("b1");

        B b2 = new B();
        b2.setAString("b2");

        B b3 = new B();
        b3.setAString("b3");

        ArrayOfTypeBContainer array = new ArrayOfTypeBContainer();
        array.setArray(new B[] { b1, b2, b3 });

        ArrayOfTypeCContainer a = mapper.map(array, ArrayOfTypeCContainer.class);

        assertEquals(3, a.getArray().length);
    }

}
