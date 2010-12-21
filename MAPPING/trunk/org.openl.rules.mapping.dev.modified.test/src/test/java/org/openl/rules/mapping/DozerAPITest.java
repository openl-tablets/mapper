package org.openl.rules.mapping;

import static org.junit.Assert.*;
import org.dozer.DozerBeanMapper;
import org.dozer.loader.api.BeanMappingBuilder;
import org.junit.Before;
import org.junit.Test;
import org.openl.rules.mapping.data.Dest;
import org.openl.rules.mapping.data.Source;

public class DozerAPITest {

    private DozerBeanMapper mapper;

    @Before
    public void setUp() {
        mapper = new DozerBeanMapper();
    }

    @Test
    public void test1() {

        BeanMappingBuilder builder = new BeanMappingBuilder() {
            protected void configure() {
                mapping(
                    Source.class, 
                    Dest.class, 
                    wildcard(false)
                )
                .fields(
                    field("stringField"), 
                    field("stringField").required(true).defaultValue("default value"));
            }
        };
        
        mapper.addMapping(builder);
        
        Source source = new Source(null, 10);
        Dest dest = mapper.map(source, Dest.class);

        assertEquals("default value", dest.getStringField());
        assertEquals(0, dest.getIntField());
    }

}
