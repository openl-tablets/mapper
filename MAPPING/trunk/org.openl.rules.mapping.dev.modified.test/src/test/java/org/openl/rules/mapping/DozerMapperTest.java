package org.openl.rules.mapping;

import static org.junit.Assert.assertEquals;

import org.dozer.MappingException;
import org.junit.Test;
import org.openl.rules.mapping.data.Dest;
import org.openl.rules.mapping.data.Source;
import org.openl.rules.mapping.dev.modified.test.AbstractFunctionalTest;

public class DozerMapperTest extends AbstractFunctionalTest {

    @Override
    public void setUp() throws Exception {
        mapper = getMapper("required-fields-mapping.xml");
    }

    @Test
    public void test1() {
        Source source = new Source(null, 1);

        Dest result = mapper.map(source, Dest.class, "map-wo-required");
        assertEquals(null, result.getStringField());
        assertEquals(1, result.getIntField());
    }

    @Test(expected = MappingException.class)
    public void test2() {
        Source source = new Source(null, 1);

        mapper.map(source, Dest.class, "map-w-required");
    }

    @Test
    public void test3() {
        Source source = new Source("some string", 1);

        Dest result = mapper.map(source, Dest.class, "map-w-required");
        assertEquals("some string", result.getStringField());
        assertEquals(1, result.getIntField());
    }

}
