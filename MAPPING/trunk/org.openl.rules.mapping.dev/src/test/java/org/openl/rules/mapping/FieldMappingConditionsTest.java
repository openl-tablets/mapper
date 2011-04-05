package org.openl.rules.mapping;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.dozer.BaseMappingParamsAwareFieldMappingCondition;
import org.dozer.FieldMappingCondition;
import org.dozer.MappingContext;
import org.dozer.MappingParameters;
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
    public void mappingParamsAwareFieldMapConditionSupportTest() {

        File source = new File("src/test/resources/org/openl/rules/mapping/conditions/MappingParamsAwareFieldMappingConditionsTest.xlsx");
        Mapper mapper = RulesBeanMapperFactory.createMapperInstance(source);

        MappingContext context = new MappingContext();
        MappingParameters params = new MappingParameters();
        params.put("true", true);
        params.put("false", false);
        context.setParams(params);
        
        A a = new A();
        a.setAString("a-string");
        a.setAnInteger(100);

        C c = mapper.map(a, C.class);
        assertEquals("a-string", c.getAString());
        assertEquals("a-string", c.getB().getAString());
        assertEquals(Integer.valueOf(100), c.getB().getAnInteger());

        C c1 = mapper.map(a, C.class, context);
        assertEquals("a-string", c1.getAString());
        assertEquals("a-string", c1.getB().getAString());
        assertEquals(Integer.valueOf(100), c1.getB().getAnInteger());

        A a1 = mapper.map(c, A.class);

        assertEquals("a-string", a1.getAString());
        assertEquals(null, a1.getAnInteger());
        
        A a2 = mapper.map(c1, A.class, context);

        assertEquals("a-string", a2.getAString());
        assertEquals(null, a2.getAnInteger());
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
    public void mappingParamsAwareExternalFieldMapConditionSupportTest() {

        File source = new File(
            "src/test/resources/org/openl/rules/mapping/conditions/ExternalFieldMappingConditionTest.xlsx");
        Mapper mapper = RulesBeanMapperFactory.createMapperInstance(source);

        MappingContext context = new MappingContext();
        MappingParameters params = new MappingParameters();
        params.put("true", true);
        params.put("false", false);
        context.setParams(params);
        
        A a = new A();
        a.setAString("a-string");
        a.setAnInteger(100);

        C c = mapper.map(a, C.class);
        assertEquals("a-string", c.getAString());
        assertEquals("a-string", c.getB().getAString());
        assertEquals(Integer.valueOf(100), c.getB().getAnInteger());
        
        C c1 = mapper.map(a, C.class, context);
        assertEquals("a-string", c1.getAString());
        assertEquals("a-string", c1.getB().getAString());
        assertEquals(Integer.valueOf(100), c1.getB().getAnInteger());

        A a1 = mapper.map(c, A.class);

        assertEquals("a-string", a1.getAString());
        assertEquals(null, a1.getAnInteger());
        
        A a2 = mapper.map(c, A.class, context);

        assertEquals("a-string", a2.getAString());
        assertEquals(null, a2.getAnInteger());

    }


    @Test
    public void fieldMapConditionWithIdSupportTest() {

        Map<String, FieldMappingCondition> conditions = new HashMap<String, FieldMappingCondition>();
        conditions.put("map", new BaseMappingParamsAwareFieldMappingCondition() {
            
            @Override
            public boolean mapField(MappingParameters params, Object sourceFieldValue, Object destFieldValue,
                Class<?> sourceType, Class<?> destType) {
                if (params != null && params.contains("true")) {
                    return (Boolean) params.get("true");
                }

                return true;
            }        
        });

        conditions.put("dontMap", new BaseMappingParamsAwareFieldMappingCondition() {
            
            @Override
            public boolean mapField(MappingParameters params, Object sourceFieldValue, Object destFieldValue,
                Class<?> sourceType, Class<?> destType) {
                if (params != null && params.contains("false")) {
                    return (Boolean) params.get("false");
                }

                return false;
            }
        });

        File source = new File(
            "src/test/resources/org/openl/rules/mapping/conditions/FieldMappingConditionsWithIdTest.xlsx");
        Mapper mapper = RulesBeanMapperFactory.createMapperInstance(source, null, conditions);

        MappingContext context = new MappingContext();
        MappingParameters params = new MappingParameters();
        params.put("true", true);
        params.put("false", false);
        context.setParams(params);
        
        A a = new A();
        a.setAString("a-string");
        a.setAnInteger(100);

        C c = mapper.map(a, C.class);
        assertEquals("a-string", c.getAString());
        assertEquals("a-string", c.getB().getAString());
        assertEquals(Integer.valueOf(100), c.getB().getAnInteger());

        C c1 = mapper.map(a, C.class, context);
        assertEquals("a-string", c1.getAString());
        assertEquals("a-string", c1.getB().getAString());
        assertEquals(Integer.valueOf(100), c1.getB().getAnInteger());

        A a1 = mapper.map(c, A.class);

        assertEquals("a-string", a1.getAString());
        assertEquals(null, a1.getAnInteger());
        
        A a2 = mapper.map(c, A.class, context);

        assertEquals("a-string", a2.getAString());
        assertEquals(null, a2.getAnInteger());
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
