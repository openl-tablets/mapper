package org.openl.rules.mapping;

import static org.junit.Assert.assertEquals;

import org.dozer.Mapper;
import org.junit.Test;
import org.openl.rules.mapping.data.Dest;
import org.openl.rules.mapping.data.Source;
import org.openl.rules.mapping.dev.modified.test.AbstractFunctionalTest;

public class MultiSourceFieldMappingTest extends AbstractFunctionalTest {

    @Test
    public void test1() {
        Mapper mapper = getMapper("multi-source-field-mapping.xml");
        Source source = new Source(null, 1);

        Dest result = mapper.map(source, Dest.class, "w-wildcard");
        assertEquals(";1", result.getStringField());
        assertEquals(1, result.getIntField());
        
        Source result1 = mapper.map(result, Source.class, "w-wildcard");
        assertEquals(";1", result1.getStringField());
        assertEquals(1, result1.getIntField());
    }

    @Test
    public void test2() {
        Mapper mapper = getMapper("multi-source-field-mapping.xml");
        Source source = new Source(null, 1);

        Dest result = mapper.map(source, Dest.class, "wo-wildcard");
        assertEquals(";1", result.getStringField());
        assertEquals(1, result.getIntField());
        
        Source result1 = mapper.map(result, Source.class, "wo-wildcard");
        assertEquals(null, result1.getStringField());
        assertEquals(1, result1.getIntField());
    }

}
