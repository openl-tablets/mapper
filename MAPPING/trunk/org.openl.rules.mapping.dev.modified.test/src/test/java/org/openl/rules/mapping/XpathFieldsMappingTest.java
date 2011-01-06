package org.openl.rules.mapping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.dozer.MappingException;
import org.dozer.loader.api.BeanMappingBuilder;
import org.junit.Test;
import org.openl.rules.mapping.data.xpath.InnerInnerObj;
import org.openl.rules.mapping.data.xpath.InnerObj;
import org.openl.rules.mapping.data.xpath.XpathDest;
import org.openl.rules.mapping.data.xpath.XpathSource;
import org.openl.rules.mapping.dev.modified.test.AbstractFunctionalTest;

public class XpathFieldsMappingTest extends AbstractFunctionalTest {

    @Test
    public void test1() {
        Mapper mapper = getMapper("xpath-index-field-mapping.xml");
        XpathSource source = new XpathSource();
        source.setField1("field1");

        InnerObj inner = new InnerObj();
        inner.setField1("test");
        inner.setField2(15);
        inner.setField3(new String[] { "value1", "value2", "value3" });

        InnerInnerObj ininner1 = new InnerInnerObj();
        ininner1.setField1("inner-inner1.field1");
        ininner1.setField2(5);

        InnerInnerObj ininner2 = new InnerInnerObj();
        ininner2.setField1("test");
        ininner2.setField2(15);

        inner.setField4(new InnerInnerObj[] { ininner1, ininner2 });

        source.setField2(inner);

        XpathDest result = mapper.map(source, XpathDest.class, "map1");

        assertNotNull(result);
        assertEquals("test", result.getField1());
        assertEquals("value1", result.getField2());
        assertEquals(2, result.getField3().length);
        assertEquals("inner-inner1.field1", result.getField3()[0].getField1());
        assertEquals(5, result.getField3()[0].getField2());
        assertEquals("test", result.getField3()[1].getField1());
        assertEquals(15, result.getField3()[1].getField2());
        assertEquals("inner-inner1.field1", result.getField4());
        assertEquals(0, result.getField5());
        assertEquals("test", result.getField6().getField1());
        assertEquals(5, result.getField6().getField2());
        
        XpathSource result1 = mapper.map(result, XpathSource.class, "map1");
        
        assertNotNull(result1);
        assertEquals("test", result1.getField2().getField1());
        assertEquals(1, result1.getField2().getField3().length);
        assertEquals("value1", result1.getField2().getField3()[0]);
        assertEquals(1, result1.getField2().getField4().length);
        assertEquals("inner-inner1.field1", result1.getField2().getField4()[0].getField1());
        assertEquals(0, result1.getField2().getField4()[0].getField2());
    }
    
    @Test
    public void test2() {
        Mapper mapper = getMapper("xpath-index-field-mapping.xml");
        XpathSource source = new XpathSource();
        source.setField1("field1");

        InnerObj inner = new InnerObj();
        inner.setField1("test");
        inner.setField2(15);
        inner.setField3(new String[] { "value1", "value2", "value3" });

        InnerInnerObj ininner1 = new InnerInnerObj();
        ininner1.setField1("inner-inner1.field1");
        ininner1.setField2(5);

        InnerInnerObj ininner2 = new InnerInnerObj();
        ininner2.setField1("test");
        ininner2.setField2(15);

        inner.setField4(new InnerInnerObj[] { ininner1, ininner2 });

        source.setField2(inner);

        XpathDest result = mapper.map(source, XpathDest.class, "map2");

        assertNotNull(result);
        assertEquals("inner-inner1.field1", result.getField1());
        assertEquals(1, result.getField3().length);
        assertEquals("inner-inner1.field1", result.getField3()[0].getField1());
        assertEquals(5, result.getField3()[0].getField2());
    }
    
    @Test
    public void test3() {
        Mapper mapper = getMapper("xpath-index-field-mapping.xml");
        XpathSource source = new XpathSource();
        source.setField1("field1");

        InnerObj inner = new InnerObj();
        inner.setField1("test");
        inner.setField2(15);
        inner.setField3(new String[] { "value1", "value2", "value3" });

        InnerInnerObj ininner1 = new InnerInnerObj();
        ininner1.setField1("inner-inner1.field1");
        ininner1.setField2(5);

        InnerInnerObj ininner2 = new InnerInnerObj();
        ininner2.setField1("test");
        ininner2.setField2(15);

        inner.setField4(new InnerInnerObj[] { ininner1, ininner2 });

        source.setField2(inner);

        XpathDest result = mapper.map(source, XpathDest.class, "map3");

        assertNotNull(result);
        assertNull("inner-inner1.field1", result.getField1());
    }
    
    @Test(expected = MappingException.class)
    public void test4() {
        Mapper mapper = getMapper("xpath-index-field-mapping.xml");
        XpathDest source = new XpathDest();
        source.setField1("value");
        mapper.map(source, XpathSource.class, "map4");
    }
    
    @Test
    public void test5() {

        BeanMappingBuilder builder = new BeanMappingBuilder() {
            protected void configure() {
                mapping(XpathSource.class, 
                    XpathDest.class, 
                    wildcard(false)
                )
                .fields(
                    field("field2.field4[@field2 < 10].field2"),
                    field("field6.field2")
                );
            }
        };

        DozerBeanMapper mapper = new DozerBeanMapper();
        mapper.addMapping(builder);

        XpathSource source = new XpathSource();
        source.setField1("field1");

        InnerObj inner = new InnerObj();
        inner.setField1("test");
        inner.setField2(15);
        inner.setField3(new String[] { "value1", "value2", "value3" });

        InnerInnerObj ininner1 = new InnerInnerObj();
        ininner1.setField1("inner-inner1.field1");
        ininner1.setField2(5);

        InnerInnerObj ininner2 = new InnerInnerObj();
        ininner2.setField1("test");
        ininner2.setField2(15);

        inner.setField4(new InnerInnerObj[] { ininner1, ininner2 });

        source.setField2(inner);

        XpathDest result = mapper.map(source, XpathDest.class);
        assertEquals(5, result.getField6().getField2());
    }
    
    @Test
    public void test6() {
        Mapper mapper = getMapper("xpath-index-field-mapping.xml");
        XpathDest source = new XpathDest();
        
        XpathSource result = mapper.map(source, XpathSource.class, "map5");
        
        assertEquals(null, result.getField2().getField4()[0]);
        
        InnerInnerObj field6 = new InnerInnerObj();
        field6.setField1("abc");
        source.setField6(field6);
        
        result = mapper.map(source, XpathSource.class, "map5");
        assertEquals("abc", result.getField2().getField4()[0].getField1());
    }

}
