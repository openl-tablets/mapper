package org.openl.rules.mapping;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.openl.rules.mapping.to.A;
import org.openl.rules.mapping.to.B;
import org.openl.rules.mapping.to.C;
import org.openl.rules.mapping.to.D;
import org.openl.rules.mapping.to.E;
import org.openl.rules.mapping.to.containers.ArrayContainer;
import org.openl.rules.mapping.to.containers.IntArrayContainer;
import org.openl.rules.mapping.to.containers.ListContainer;
import org.openl.rules.mapping.to.containers.ListOfTypeCContainer;
import org.openl.rules.mapping.to.containers.SetContainer;

public class CollectionDiscriminatorTest {
    
    @Test
    public void arrayToListDiscriminatorSupportTest() {
        File source = new File("src/test/resources/org/openl/rules/mapping/discriminator/ListToArrayCollectionDiscriminatorTest.xlsx");
        Mapper mapper = RulesBeanMapperFactory.createMapperInstance(source);

        ArrayContainer array = new ArrayContainer();
        A a1 = new A();
        a1.setAString("x");
        a1.setAnInteger(10);
        
        A a2 = new A();
        a2.setAString("y");
        a1.setAnInteger(10);

        array.setArray(new A[] { a1, a2 });

        ListOfTypeCContainer container1 = new ListOfTypeCContainer();
        C c1 = new C();
        c1.setAString("c");
        List<C> list1 = new ArrayList<C>();
        list1.add(c1);
        container1.setList(list1);
        
        mapper.map(array, container1);
        
        assertEquals(3, container1.getList().size());
        assertEquals("x", container1.getList().get(1).getAString());
        assertEquals("y", container1.getList().get(2).getAString());
        assertEquals("c", container1.getList().get(0).getAString());
        assertEquals(null, container1.getList().get(0).getB());

        ListOfTypeCContainer container2 = new ListOfTypeCContainer();
        C c2 = new C();
        c2.setAString("x");
        List<C> list2 = new ArrayList<C>();
        list2.add(c2);
        container2.setList(list2);
        
        mapper.map(array, container2);

        assertEquals(2, container2.getList().size());
        assertEquals("x", container2.getList().get(0).getAString());
        assertEquals("y", container2.getList().get(1).getAString());
        assertEquals(Integer.valueOf(10), container2.getList().get(0).getB().getAnInteger());
    }
    
    @Test
    public void listToArrayDiscriminatorSupportTest() {
        File source = new File("src/test/resources/org/openl/rules/mapping/discriminator/ListToArrayCollectionDiscriminatorTest.xlsx");
        Mapper mapper = RulesBeanMapperFactory.createMapperInstance(source);

        ArrayContainer array = new ArrayContainer();
        A a1 = new A();
        a1.setAString("x");
        
        array.setArray(new A[] { a1});

        ListOfTypeCContainer container1 = new ListOfTypeCContainer();
        C c1 = new C();
        c1.setAString("c1");
        B b1 = new B();
        b1.setAnInteger(10);
        b1.setAString("b1");
        c1.setB(b1);

        C c2 = new C();
        c2.setAString("c2");
        B b2 = new B();
        b2.setAnInteger(10);
        b2.setAString("b2");
        c2.setB(b2);

        List<C> list1 = new ArrayList<C>();
        list1.add(c1);
        list1.add(c2);
        container1.setList(list1);
        
        mapper.map(container1, array);
        
        List<A> result = (List)Arrays.asList(array.getArray()); 
        assertEquals(3, result.size());
        assertEquals("x", result.get(0).getAString());
        assertEquals("c1", result.get(1).getAString());
        assertEquals("c2", result.get(2).getAString());
        
        ListOfTypeCContainer container2 = new ListOfTypeCContainer();

        c1.setAString("x");
        List<C> list2 = new ArrayList<C>();
        list2.add(c1);
        list2.add(c2);
        container2.setList(list2);

        array.setArray(new A[] { a1});

        mapper.map(container2, array);
        result = (List)Arrays.asList(array.getArray());
        assertEquals(2, result.size());
        assertEquals("x", result.get(0).getAString());
        assertEquals(Integer.valueOf(10), result.get(0).getAnInteger());
        assertEquals("c2", result.get(1).getAString());
    }
    
    @Test
    public void arrayToSetDiscriminatorSupportTest() {
        File source = new File("src/test/resources/org/openl/rules/mapping/discriminator/SetToArrayCollectionDiscriminatorTest.xlsx");
        Mapper mapper = RulesBeanMapperFactory.createMapperInstance(source);

        ArrayContainer array = new ArrayContainer();
        A a1 = new A();
        a1.setAString("x");
        a1.setAnInteger(10);
        
        A a2 = new A();
        a2.setAString("y");
        a1.setAnInteger(10);

        array.setArray(new A[] { a1, a2 });

        SetContainer container1 = new SetContainer();
        C c1 = new C();
        c1.setAString("c");
        Set<C> set1 = new LinkedHashSet<C>();
        set1.add(c1);
        container1.setSet(set1);
        
        mapper.map(array, container1);
        List<C> result = new ArrayList<C>(container1.getSet());
        assertEquals(3, result.size());
        assertEquals("x", result.get(1).getAString());
        assertEquals("y", result.get(2).getAString());
        assertEquals("c", result.get(0).getAString());
        assertEquals(null, result.get(0).getB());

        SetContainer container2 = new SetContainer();
        C c2 = new C();
        c2.setAString("x");
        Set<C> set2 = new LinkedHashSet<C>();
        set2.add(c2);
        container2.setSet(set2);
        
        mapper.map(array, container2);
        result = new ArrayList<C>(container2.getSet());
        assertEquals(2, result.size());
        assertEquals("x", result.get(0).getAString());
        assertEquals("y", result.get(1).getAString());
        assertEquals(Integer.valueOf(10), result.get(0).getB().getAnInteger());
    }
    
    @Test
    public void setToArrayDiscriminatorSupportTest() {
        File source = new File("src/test/resources/org/openl/rules/mapping/discriminator/SetToArrayCollectionDiscriminatorTest.xlsx");
        Mapper mapper = RulesBeanMapperFactory.createMapperInstance(source);

        ArrayContainer array = new ArrayContainer();
        A a1 = new A();
        a1.setAString("x");
        
        array.setArray(new A[] { a1});

        SetContainer container1 = new SetContainer();
        C c1 = new C();
        c1.setAString("c1");
        B b1 = new B();
        b1.setAnInteger(10);
        b1.setAString("b1");
        c1.setB(b1);

        C c2 = new C();
        c2.setAString("c2");
        B b2 = new B();
        b2.setAnInteger(10);
        b2.setAString("b2");
        c2.setB(b2);

        Set<C> set1 = new LinkedHashSet<C>();
        set1.add(c1);
        set1.add(c2);
        container1.setSet(set1);
        
        mapper.map(container1, array);
        
        List<A> result = (List)Arrays.asList(array.getArray()); 
        assertEquals(3, result.size());
        assertEquals("x", result.get(0).getAString());
        assertEquals("c1", result.get(1).getAString());
        assertEquals("c2", result.get(2).getAString());
        
        SetContainer container2 = new SetContainer();

        c1.setAString("x");
        Set<C> set2 = new LinkedHashSet<C>();
        set2.add(c1);
        set2.add(c2);
        container2.setSet(set2);

        array.setArray(new A[] { a1});

        mapper.map(container2, array);
        result = (List)Arrays.asList(array.getArray());
        assertEquals(2, result.size());
        assertEquals("x", result.get(0).getAString());
        assertEquals(Integer.valueOf(10), result.get(0).getAnInteger());
        assertEquals("c2", result.get(1).getAString());
    }

    @Test
    public void arrayToArrayDiscriminatorSupportTest() {
        File source = new File("src/test/resources/org/openl/rules/mapping/discriminator/ArrayToArrayCollectionDiscriminatorTest.xlsx");
        Mapper mapper = RulesBeanMapperFactory.createMapperInstance(source);

        ArrayContainer array = new ArrayContainer();
        A a1 = new A();
        a1.setAString("x");
        a1.setAnInteger(10);
        
        A a2 = new A();
        a2.setAString("y");
        a1.setAnInteger(10);

        array.setArray(new A[] { a1, a2 });

        ArrayContainer container1 = new ArrayContainer();
        C c1 = new C();
        c1.setAString("c");
        container1.setArray(new C[]{c1});
        
        mapper.map(array, container1);
        List<C> result = (List)Arrays.asList(container1.getArray());
        assertEquals(3, result.size());
        assertEquals("x", result.get(1).getAString());
        assertEquals("y", result.get(2).getAString());
        assertEquals("c", result.get(0).getAString());
        assertEquals(null, result.get(0).getB());

        ArrayContainer container2 = new ArrayContainer();
        C c2 = new C();
        c2.setAString("x");
        container2.setArray(new C[]{c2});
        
        mapper.map(array, container2);
        result = (List)Arrays.asList(container2.getArray());
        assertEquals(2, result.size());
        assertEquals("x", result.get(0).getAString());
        assertEquals("y", result.get(1).getAString());
        assertEquals(Integer.valueOf(10), result.get(0).getB().getAnInteger());
    }
    
    @Test
    public void primArrayToPrimArrayDiscriminatorSupportTest() {
        File sourceFile = new File("src/test/resources/org/openl/rules/mapping/discriminator/PrimitiveArrayToPrimitiveArrayCollectionDiscriminatorTest.xlsx");
        Mapper mapper = RulesBeanMapperFactory.createMapperInstance(sourceFile);

        IntArrayContainer source = new IntArrayContainer(); 
        source.setArray(new int[] { 1, 2, 15 });

        IntArrayContainer dest = new IntArrayContainer(); 
        dest.setArray(new int[] { 100, 200, 1500 });
        
        mapper.map(source, dest);
        
        assertEquals(6, dest.getArray().length);
        assertEquals(100, dest.getArray()[0]);
        assertEquals(200, dest.getArray()[1]);
        assertEquals(1500, dest.getArray()[2]);
        assertEquals(1, dest.getArray()[3]);
        assertEquals(2, dest.getArray()[4]);
        assertEquals(15, dest.getArray()[5]);
    }

    @Test
    public void discriminatorSupportTest() {
        File source = new File("src/test/resources/org/openl/rules/mapping/discriminator/CollectionDiscriminatorSupportTest.xlsx");
        Mapper mapper = RulesBeanMapperFactory.createMapperInstance(source);

        A a1 = new A();
        E e1 = new E();
        e1.setAString("x");
        D d1 = new D();
        d1.setAnInt(1000);
        e1.setD(d1);
        a1.setE(e1);

        C c1 = new C();
        c1.setAString("c1");
        B b1 = new B();
        b1.setAnInteger(10);
        b1.setAString("b1");
        c1.setB(b1);

        C c2 = new C();
        c2.setAString("c2");
        B b2 = new B();
        b2.setAnInteger(10);
        b2.setAString("b2");
        c2.setB(b2);

        List<C> list1 = new ArrayList<C>();
        list1.add(c1);
        list1.add(c2);
        ListContainer container1 = new ListContainer();
        container1.setList(list1);
        
        mapper.map(a1, container1);
        List<C> result = (List<C>)container1.getList(); 
        assertEquals(3, result.size());
        assertEquals("c1", result.get(0).getAString());
        assertEquals("c2", result.get(1).getAString());
        assertEquals("x", result.get(2).getAString());
        
        List<C> list2 = new ArrayList<C>();
        list2.add(c1);
        list2.add(c2);
        ListContainer container2 = new ListContainer();
        container2.setList(list2);
        
        e1.setAString("c1");

        mapper.map(a1, container2);
        result = (List<C>)container2.getList(); 
        assertEquals(2, result.size());
        assertEquals("c1", result.get(0).getAString());
        assertEquals("c2", result.get(1).getAString());
        assertEquals(Integer.valueOf(1000), result.get(0).getB().getAnInteger());
        assertEquals(Integer.valueOf(10), result.get(1).getB().getAnInteger());
        
        ListContainer container3 = mapper.map(a1, ListContainer.class);
        result = (List<C>)container3.getList(); 
        assertEquals(1, result.size());
        assertEquals("c1", result.get(0).getAString());
    }

}

