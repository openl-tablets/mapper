package org.openl.rules.mapping;

import static org.junit.Assert.assertEquals;

import org.dozer.Mapper;
import org.dozer.MappingContext;
import org.junit.Test;
import org.openl.rules.mapping.data.Dest;
import org.openl.rules.mapping.data.Source;
import org.openl.rules.mapping.dev.modified.test.AbstractFunctionalTest;

public class MultiSourceFieldMappingTest extends AbstractFunctionalTest {

    @Test
    public void test1() {
        Mapper mapper = getMapper("multi-source-field-mapping.xml");
        Source source = new Source(null, 1);

        MappingContext context = new MappingContext();
        context.setMapId("w-wildcard");
        Dest result = mapper.map(source, Dest.class, context);
        assertEquals(";1", result.getStringField());
        assertEquals(1, result.getIntField());
        
        Source result1 = mapper.map(result, Source.class, context);
        assertEquals(";1", result1.getStringField());
        assertEquals(1, result1.getIntField());
    }

    @Test
    public void test2() {
        Mapper mapper = getMapper("multi-source-field-mapping.xml");
        Source source = new Source(null, 1);

        MappingContext context = new MappingContext();
        context.setMapId("wo-wildcard");
        Dest result = mapper.map(source, Dest.class, context);
        assertEquals(";1", result.getStringField());
        assertEquals(1, result.getIntField());
        
        Source result1 = mapper.map(result, Source.class, context);
        assertEquals(null, result1.getStringField());
        assertEquals(1, result1.getIntField());
    }

}
