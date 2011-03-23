package org.openl.rules.mapping;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.Map;

import org.junit.Test;
import org.openl.rules.mapping.to.A;
import org.openl.rules.mapping.to.D;
import org.openl.rules.mapping.to.E;
import org.openl.rules.mapping.to.containers.ArrayContainer;
import org.openl.rules.mapping.to.containers.MapContainer;

public class MapSupportTest {

    @Test
    public void mapSupportTest() {

        File source = new File("src/test/resources/org/openl/rules/mapping/map/MapSupportTest.xlsx");
        Mapper mapper = RulesBeanMapperFactory.createMapperInstance(source);

        A a = new A();
        a.setAString("a-string");
        a.setAStringArray(new String[] { "a", "b", "c" });
        a.setAnInteger(100);

        E e = new E();
        e.setAString("e-string");

        D d = new D();
        d.setAnInt(100);

        e.setD(d);
        a.setE(e);

        Map result = mapper.map(a, Map.class);

        assertEquals("a-string", result.get("AString"));
        assertEquals(null, result.get("AStringArray"));
        assertEquals(Integer.valueOf(100), result.get("anInteger"));
        assertEquals("e-string", ((E) result.get("e")).getAString());
        assertEquals(100, ((E) result.get("e")).getD().getAnInt());

        A result1 = mapper.map(result, A.class);

        assertEquals("a-string", result1.getAString());
        assertEquals(Integer.valueOf(100), result1.getAnInteger());
        assertEquals(null, result1.getAStringArray());
        assertEquals("e-string", result1.getE().getAString());
        assertEquals(100, result1.getE().getD().getAnInt());

        ArrayContainer container = new ArrayContainer();
        container.setArray(new A[] { a });

        Map result2 = mapper.map(container, Map.class);

        assertEquals("a-string", ((A)result2.get("array")).getAString());
        assertEquals(3, ((A)result2.get("array")).getAStringArray().length);
        assertEquals(Integer.valueOf(100), ((A)result2.get("array")).getAnInteger());
        assertEquals("e-string", ((A)result2.get("array")).getE().getAString());
        assertEquals(100, ((A)result2.get("array")).getE().getD().getAnInt());

        ArrayContainer result3 = mapper.map(result2, ArrayContainer.class);

        assertEquals("a-string", ((A) result3.getArray()[0]).getAString());
        assertEquals(Integer.valueOf(100), ((A) result3.getArray()[0]).getAnInteger());
        assertEquals(3, ((A) result3.getArray()[0]).getAStringArray().length);
        assertEquals("e-string", ((A) result3.getArray()[0]).getE().getAString());
        assertEquals(100, ((A) result3.getArray()[0]).getE().getD().getAnInt());
    }

    @Test
    public void mapTypeFieldSupportTest() {

        File source = new File("src/test/resources/org/openl/rules/mapping/map/MapTypeFieldSupportTest.xlsx");
        Mapper mapper = RulesBeanMapperFactory.createMapperInstance(source);

        A a = new A();
        a.setAString("a-string");
        a.setAStringArray(new String[] { "a", "b", "c" });
        a.setAnInteger(100);

        E e = new E();
        e.setAString("e-string");

        D d = new D();
        d.setAnInt(100);

        e.setD(d);
        a.setE(e);

        ArrayContainer container = new ArrayContainer();
        container.setArray(new A[] { a });

        MapContainer result = mapper.map(container, MapContainer.class);

        assertEquals("a-string", result.getAMap().get("AString"));
        assertEquals(null, result.getAMap().get("AStringArray"));
        assertEquals(Integer.valueOf(100), result.getAMap().get("anInteger"));
        assertEquals("e-string", ((E) result.getAMap().get("e")).getAString());
        assertEquals(100, ((E) result.getAMap().get("e")).getD().getAnInt());

        ArrayContainer result1 = mapper.map(result, ArrayContainer.class);

        assertEquals("a-string", ((A) result1.getArray()[0]).getAString());
        assertEquals(Integer.valueOf(100), ((A) result1.getArray()[0]).getAnInteger());
        assertEquals(null, ((A) result1.getArray()[0]).getAStringArray());
        assertEquals("e-string", ((A) result1.getArray()[0]).getE().getAString());
        assertEquals(100, ((A) result1.getArray()[0]).getE().getD().getAnInt());
    }

}
