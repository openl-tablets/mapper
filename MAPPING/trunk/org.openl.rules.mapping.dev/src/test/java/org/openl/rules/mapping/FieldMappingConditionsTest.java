package org.openl.rules.mapping;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.dozer.fieldmap.FieldMappingCondition;
import org.junit.Test;
import org.openl.rules.mapping.to.A;
import org.openl.rules.mapping.to.C;

public class FieldMappingConditionsTest {

    @Test
    public void fieldMapConditionSupportTest() {

        File source = new File("src/test/resources/org/openl/rules/mapping/conditions/FieldMappingConditionsTest.xlsx");
        Mapper mapper = RulesBeanMapperFactory.createMapperInstance(source);

        A a = new A();
        a.setAString("a-string");
        a.setAnInteger(100);

        C c = mapper.map(a, C.class);
        assertEquals("a-string", c.getAString());
        assertEquals("a-string", c.getB().getAString());
        assertEquals(Integer.valueOf(100), c.getB().getAnInteger());

        A a1 = mapper.map(c, A.class);

        assertEquals("a-string", a1.getAString());
        assertEquals(null, a1.getAnInteger());
    }

    @Test
    public void externalFieldMapConditionSupportTest() {

        File source = new File(
            "src/test/resources/org/openl/rules/mapping/conditions/ExternalFieldMappingConditionTest.xlsx");
        Mapper mapper = RulesBeanMapperFactory.createMapperInstance(source);

        A a = new A();
        a.setAString("a-string");
        a.setAnInteger(100);

        C c = mapper.map(a, C.class);
        assertEquals("a-string", c.getAString());
        assertEquals("a-string", c.getB().getAString());
        assertEquals(Integer.valueOf(100), c.getB().getAnInteger());

        A a1 = mapper.map(c, A.class);

        assertEquals("a-string", a1.getAString());
        assertEquals(null, a1.getAnInteger());
    }

    @Test
    public void fieldMapConditionWithIdSupportTest() {

        Map<String, FieldMappingCondition> conditions = new HashMap<String, FieldMappingCondition>();
        conditions.put("map", new FieldMappingCondition() {

            public boolean mapField(Object sourceFieldValue, Object destFieldValue, Class<?> sourceType,
                Class<?> destType) {
                return true;
            }
        });

        conditions.put("dontMap", new FieldMappingCondition() {

            public boolean mapField(Object sourceFieldValue, Object destFieldValue, Class<?> sourceType,
                Class<?> destType) {
                return false;
            }
        });

        File source = new File(
            "src/test/resources/org/openl/rules/mapping/conditions/FieldMappingConditionsWithIdTest.xlsx");
        Mapper mapper = RulesBeanMapperFactory.createMapperInstance(source, null, conditions);

        A a = new A();
        a.setAString("a-string");
        a.setAnInteger(100);

        C c = mapper.map(a, C.class);
        assertEquals("a-string", c.getAString());
        assertEquals("a-string", c.getB().getAString());
        assertEquals(Integer.valueOf(100), c.getB().getAnInteger());

        A a1 = mapper.map(c, A.class);

        assertEquals("a-string", a1.getAString());
        assertEquals(null, a1.getAnInteger());
    }

    @Test
    public void fieldMapConditionOrderTest() {

        Map<String, FieldMappingCondition> conditions = new HashMap<String, FieldMappingCondition>();
        conditions.put("map", new FieldMappingCondition() {

            public boolean mapField(Object sourceFieldValue, Object destFieldValue, Class<?> sourceType,
                Class<?> destType) {
                return true;
            }
        });

        conditions.put("dontMap", new FieldMappingCondition() {

            public boolean mapField(Object sourceFieldValue, Object destFieldValue, Class<?> sourceType,
                Class<?> destType) {
                return false;
            }
        });

        File source = new File(
            "src/test/resources/org/openl/rules/mapping/conditions/FieldMappingConditionsOrderTest.xlsx");
        Mapper mapper = RulesBeanMapperFactory.createMapperInstance(source, null, conditions);

        A a = new A();
        a.setAString("a-string");
        a.setAnInteger(100);

        C c = mapper.map(a, C.class);
        assertEquals("a-string", c.getAString());
        assertEquals("a-string", c.getB().getAString());
        assertEquals(Integer.valueOf(100), c.getB().getAnInteger());

        A a1 = mapper.map(c, A.class);

        assertEquals("a-string", a1.getAString());
        assertEquals(null, a1.getAnInteger());
    }


}
