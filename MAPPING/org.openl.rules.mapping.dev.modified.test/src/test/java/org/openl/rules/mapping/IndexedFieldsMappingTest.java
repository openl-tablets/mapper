package org.openl.rules.mapping;

import static org.junit.Assert.assertEquals;

import org.dozer.Mapper;
import org.junit.Test;
import org.openl.rules.mapping.data.index.IndexDest;
import org.openl.rules.mapping.data.index.IndexSource;
import org.openl.rules.mapping.dev.modified.test.AbstractFunctionalTest;

public class IndexedFieldsMappingTest extends AbstractFunctionalTest {

    @Test
    public void test1() {
        Mapper mapper = getMapper("simple-index-field-mapping.xml");
        IndexSource source = new IndexSource();
        source.setField1("field1");
        source.setField2("field2");
        source.setField3("field3");

        IndexDest result = mapper.map(source, IndexDest.class);
        assertEquals("field1", result.getField1());
        assertEquals(2, result.getArray().length);
        assertEquals("field2", result.getArray()[0]);
        assertEquals("field3", result.getArray()[1]);

        IndexSource result1 = mapper.map(result, IndexSource.class);
        assertEquals("field1", result1.getField1());
        assertEquals("field2", result1.getField2());
        assertEquals("field3", result1.getField3());
    }

}
