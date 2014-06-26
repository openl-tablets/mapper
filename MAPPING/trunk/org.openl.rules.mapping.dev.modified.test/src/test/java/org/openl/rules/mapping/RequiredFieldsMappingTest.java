package org.openl.rules.mapping;

import static org.junit.Assert.assertEquals;

import org.dozer.Mapper;
import org.dozer.MappingContext;
import org.dozer.MappingException;
import org.junit.Test;
import org.openl.rules.mapping.data.Dest;
import org.openl.rules.mapping.data.Source;
import org.openl.rules.mapping.dev.modified.test.AbstractFunctionalTest;

public class RequiredFieldsMappingTest extends AbstractFunctionalTest {

    @Test
    public void test1() {
        Mapper mapper = getMapper("required-fields-mapping.xml");
        Source source = new Source(null, 1);

        MappingContext context = new MappingContext();
        context.setMapId("map-wo-required");
        Dest result = mapper.map(source, Dest.class, context);
        assertEquals(null, result.getStringField());
        assertEquals(1, result.getIntField());
    }

    @Test(expected = MappingException.class)
    public void test2() {
        Mapper mapper = getMapper("required-fields-mapping.xml");
        Source source = new Source(null, 1);
        MappingContext context = new MappingContext();
        context.setMapId("map-w-required");
        mapper.map(source, Dest.class, context);
    }

    @Test
    public void test3() {
        Mapper mapper = getMapper("required-fields-mapping.xml");
        Source source = new Source("some string", 1);

        MappingContext context = new MappingContext();
        context.setMapId("map-w-required");
        Dest result = mapper.map(source, Dest.class, context);
        assertEquals("some string", result.getStringField());
        assertEquals(1, result.getIntField());
    }

}
