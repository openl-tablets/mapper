package org.openl.rules.mapping;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.dozer.CustomConverter;
import org.dozer.DozerBeanMapper;
import org.dozer.MappingException;
import org.dozer.fieldmap.FieldMappingCondition;
import org.dozer.loader.api.BeanMappingBuilder;
import org.junit.Before;
import org.junit.Test;
import org.openl.rules.mapping.data.Dest;
import org.openl.rules.mapping.data.Source;
import org.openl.rules.mapping.data.ToStringCustomConverter;

public class DozerApiTest {

    private DozerBeanMapper mapper;

    @Before
    public void setUp() {
        mapper = new DozerBeanMapper();
    }

    @Test
    public void test1() {

        BeanMappingBuilder builder = new BeanMappingBuilder() {
            protected void configure() {
                mapping(Source.class, Dest.class, wildcard(false)).fields(field("stringField"),
                    field("stringField").required(true).defaultValue("default value"));
            }
        };

        mapper.addMapping(builder);

        Source source = new Source(null, 10);
        Dest dest = mapper.map(source, Dest.class);

        assertEquals("default value", dest.getStringField());
        assertEquals(0, dest.getIntField());
    }

    @Test
    public void test2() {

        BeanMappingBuilder builder = new BeanMappingBuilder() {
            protected void configure() {
                mapping(Source.class, Dest.class, wildcard(false), oneWay()).fields(field("stringField"),
                    field("stringField").required(true).defaultValue("default value"));
            }
        };

        mapper.addMapping(builder);

        Source source = new Source(null, 10);
        Dest dest = mapper.map(source, Dest.class);

        assertEquals("default value", dest.getStringField());
        assertEquals(0, dest.getIntField());
    }

    @Test
    public void test3() {

        BeanMappingBuilder builder = new BeanMappingBuilder() {
            protected void configure() {
                mapping(Source.class, Dest.class, wildcard(false), oneWay()).fields(
                    multi(field("intField"), field("stringField")),
                    field("stringField").required(true).defaultValue("default value"),
                    customConverterId("testConverter"));
            }
        };

        Map<String, CustomConverter> customConvertersWithId = new HashMap<String, CustomConverter>();
        customConvertersWithId.put("testConverter", new CustomConverter() {

            public Object convert(Object existingDestinationFieldValue, Object sourceFieldValue,
                Class<?> destinationClass, Class<?> sourceClass) {

                if (destinationClass == String.class) {
                    return StringUtils.join((Object[]) sourceFieldValue, ";");
                }

                if (sourceClass == String.class) {
                    String[] values = ((String) sourceFieldValue).split(";");

                    return new Object[] { Integer.valueOf(values[0]), values[1] };
                }

                throw new RuntimeException("Invalid arguments");
            }

        });

        mapper.setCustomConvertersWithId(customConvertersWithId);
        mapper.addMapping(builder);

        Source source = new Source(null, 10);
        Dest dest = mapper.map(source, Dest.class);

        assertEquals("10;", dest.getStringField());
        assertEquals(0, dest.getIntField());
    }

    @Test(expected = MappingException.class)
    public void test4() {

        BeanMappingBuilder builder = new BeanMappingBuilder() {
            protected void configure() {
                mapping(
                    Source.class, 
                    Dest.class, 
                    wildcard(false)
                )
                .fields(
                    multi(field("intField"), field("stringField")),
                    field("stringField").required(true).defaultValue("default value")
                );
            }
        };

        mapper.addMapping(builder);

        Source source = new Source(null, 10);
        mapper.map(source, Dest.class);

    }

    @Test(expected = MappingException.class)
    public void test5() {

        BeanMappingBuilder builder = new BeanMappingBuilder() {
            protected void configure() {
                mapping(
                    Source.class, 
                    Dest.class, 
                    wildcard(false),
                    oneWay()
                )
                .fields(
                    multi(field("intField"), field("stringField")),
                    field("stringField").required(true).defaultValue("default value"));
            }
        };

        mapper.addMapping(builder);

        Source source = new Source(null, 10);
        mapper.map(source, Dest.class);

    }

    @Test(expected = MappingException.class)
    public void test6() {

        BeanMappingBuilder builder = new BeanMappingBuilder() {
            protected void configure() {
                mapping(
                    Source.class, 
                    Dest.class, 
                    wildcard(false)
                )
                .fields(
                    multi(field("intField"), field("stringField")),
                    field("stringField").required(true).defaultValue("default value"),
                    fieldOneWay());
            }
        };

        mapper.addMapping(builder);

        Source source = new Source(null, 10);
        mapper.map(source, Dest.class);

    }

    @Test
    public void test7() {

        BeanMappingBuilder builder = new BeanMappingBuilder() {
            protected void configure() {
                mapping(
                    Source.class, 
                    Dest.class, 
                    wildcard(false)
                )
                .fields(
                    multi(field("intField"), field("stringField")),
                    field("stringField").required(true).defaultValue("default value"),
                    fieldOneWay(),
                    customConverterId("testConverter")
                );
            }
        };

        Map<String, CustomConverter> customConvertersWithId = new HashMap<String, CustomConverter>();
        customConvertersWithId.put("testConverter", new CustomConverter() {

            public Object convert(Object existingDestinationFieldValue, Object sourceFieldValue,
                Class<?> destinationClass, Class<?> sourceClass) {

                if (destinationClass == String.class) {
                    return StringUtils.join((Object[]) sourceFieldValue, ";");
                }

                if (sourceClass == String.class) {
                    String[] values = ((String) sourceFieldValue).split(";");

                    return new Object[] { Integer.valueOf(values[0]), values[1] };
                }

                throw new RuntimeException("Invalid arguments");
            }

        });

        mapper.setCustomConvertersWithId(customConvertersWithId);
        mapper.addMapping(builder);

        Source source = new Source(null, 10);
        Dest dest = mapper.map(source, Dest.class);

        assertEquals("10;", dest.getStringField());
        assertEquals(0, dest.getIntField());
    }
    
    @Test
    public void test8() {

        BeanMappingBuilder builder = new BeanMappingBuilder() {
            protected void configure() {
                mapping(
                    Source.class, 
                    Dest.class, 
                    wildcard(false),
                    oneWay()
                )
                .fields(
                    multi(field("intField"), field("stringField")),
                    field("stringField").required(true).defaultValue("default value"),
                    customConverterId("testConverter")
                );
            }
        };

        Map<String, CustomConverter> customConvertersWithId = new HashMap<String, CustomConverter>();
        customConvertersWithId.put("testConverter", new CustomConverter() {

            public Object convert(Object existingDestinationFieldValue, Object sourceFieldValue,
                Class<?> destinationClass, Class<?> sourceClass) {

                if (destinationClass == String.class) {
                    return StringUtils.join((Object[]) sourceFieldValue, ";");
                }

                if (sourceClass == String.class) {
                    String[] values = ((String) sourceFieldValue).split(";");

                    return new Object[] { Integer.valueOf(values[0]), values[1] };
                }

                throw new RuntimeException("Invalid arguments");
            }

        });

        mapper.setCustomConvertersWithId(customConvertersWithId);
        mapper.addMapping(builder);

        Source source = new Source(null, 10);
        Dest dest = mapper.map(source, Dest.class);

        assertEquals("10;", dest.getStringField());
        assertEquals(0, dest.getIntField());
    }

    @Test(expected = MappingException.class)
    public void test9() {

        BeanMappingBuilder builder = new BeanMappingBuilder() {
            protected void configure() {
                mapping(
                    Source.class, 
                    Dest.class, 
                    wildcard(false)
                )
                .fields(
                    multi(field("intField"), field("stringField")),
                    field("stringField").required(true).defaultValue("default value"),
                    customConverterId("testConverter")
                );
            }
        };

        Map<String, CustomConverter> customConvertersWithId = new HashMap<String, CustomConverter>();
        customConvertersWithId.put("testConverter", new CustomConverter() {
            public Object convert(Object existingDestinationFieldValue, Object sourceFieldValue,
                Class<?> destinationClass, Class<?> sourceClass) {
                   throw new RuntimeException("Converter shouldn't be invoked");
            }
        });

        mapper.setCustomConvertersWithId(customConvertersWithId);
        mapper.addMapping(builder);

        Source source = new Source(null, 10);
        mapper.map(source, Dest.class);
    }
    
    @Test
    public void test10() {

        BeanMappingBuilder builder = new BeanMappingBuilder() {
            protected void configure() {
                mapping(
                    Source.class, 
                    Dest.class, 
                    wildcard(false)
                )
                .fields(
                    multi(field("intField"), field("stringField")),
                    field("stringField").required(true),
                    customConverterId("testConverter"),
                    fieldOneWay()
                )
                .fields(
                    field("intField"),
                    field("intField")
                );
            }
        };

        Map<String, CustomConverter> customConvertersWithId = new HashMap<String, CustomConverter>();
        customConvertersWithId.put("testConverter", new CustomConverter() {

            public Object convert(Object existingDestinationFieldValue, Object sourceFieldValue,
                Class<?> destinationClass, Class<?> sourceClass) {

                if (destinationClass == String.class) {
                    return StringUtils.join((Object[]) sourceFieldValue, ";");
                }

                if (sourceClass == String.class) {
                    String[] values = ((String) sourceFieldValue).split(";");

                    return new Object[] { Integer.valueOf(values[0]), values[1] };
                }

                throw new RuntimeException("Invalid arguments");
            }

        });

        mapper.setCustomConvertersWithId(customConvertersWithId);
        mapper.addMapping(builder);

        Source source = new Source("some string", 10);
        Dest dest = mapper.map(source, Dest.class);

        Source source1 = mapper.map(dest, Source.class);
        
        assertEquals(null, source1.getStringField());
        assertEquals(10, source1.getIntField());
    }
    
    @Test
    public void test11() {

        BeanMappingBuilder builder = new BeanMappingBuilder() {
            protected void configure() {
                mapping(
                    Source.class, 
                    Dest.class 
                )
                .fields(
                    multi(field("intField"), field("stringField")),
                    field("stringField").required(true),
                    customConverterId("testConverter"),
                    fieldOneWay()
                );
            }
        };

        Map<String, CustomConverter> customConvertersWithId = new HashMap<String, CustomConverter>();
        customConvertersWithId.put("testConverter", new CustomConverter() {

            public Object convert(Object existingDestinationFieldValue, Object sourceFieldValue,
                Class<?> destinationClass, Class<?> sourceClass) {

                if (destinationClass == String.class) {
                    return StringUtils.join((Object[]) sourceFieldValue, ";");
                }

                if (sourceClass == String.class) {
                    String[] values = ((String) sourceFieldValue).split(";");

                    return new Object[] { Integer.valueOf(values[0]), values[1] };
                }

                throw new RuntimeException("Invalid arguments");
            }

        });

        mapper.setCustomConvertersWithId(customConvertersWithId);
        mapper.addMapping(builder);

        Source source = new Source("some string", 10);
        Dest dest = mapper.map(source, Dest.class);

        assertEquals("10;some string", dest.getStringField());
        assertEquals(10, dest.getIntField());
        
        Source source1 = mapper.map(dest, Source.class);
        
        assertEquals("10;some string", source1.getStringField());
        assertEquals(10, source1.getIntField());
    }
    
    @Test
    public void test12() {
     
        BeanMappingBuilder builder = new BeanMappingBuilder() {
            protected void configure() {
                mapping(
                    Source.class, 
                    Dest.class 
                )
                .fields(
                    field("stringField"),
                    field("stringField").required(true),
                    condition("org.openl.rules.mapping.data.FalseMappingCondition")
                );
            }
        };

        mapper.addMapping(builder);

        Source source = new Source("some string", 10);
        Dest dest = new Dest("another string value", 5);
        
        mapper.map(source, dest);

        assertEquals("another string value", dest.getStringField());
        assertEquals(10, dest.getIntField());
        
        mapper.map(dest, source);

        assertEquals("some string", source.getStringField());
        assertEquals(10, source.getIntField());
    }

    @Test
    public void test13() {
     
        BeanMappingBuilder builder = new BeanMappingBuilder() {
            protected void configure() {
                config(
                )
                .mapping(
                    Source.class, 
                    Dest.class 
                )
                .fields(
                    field("stringField"),
                    field("stringField").required(true),
                    conditionId("false-condition-id")
                );
            }
        };

        Map<String, FieldMappingCondition> mappingConditionsWithId = new HashMap<String, FieldMappingCondition>();
        mappingConditionsWithId.put("false-condition-id", new FieldMappingCondition() {

            public boolean mapField(Object sourceFieldValue, Object destFieldValue, Class<?> sourceType,
                Class<?> destType) {
                return "another string value".equals(sourceFieldValue);
            }
        });

        mapper.setMappingConditionsWithId(mappingConditionsWithId);
        mapper.addMapping(builder);

        Source source = new Source("some string", 10);
        Dest dest = new Dest("another string value", 5);
        
        mapper.map(source, dest);

        assertEquals("another string value", dest.getStringField());
        assertEquals(10, dest.getIntField());
        
        mapper.map(dest, source);

        assertEquals("another string value", source.getStringField());
        assertEquals(10, source.getIntField());
    }
    
    @Test
    public void test14() {

        final CustomConverter customConverter = new CustomConverter() {
            public Object convert(Object existingDestinationFieldValue, Object sourceFieldValue,
                Class<?> destinationClass, Class<?> sourceClass) {
                return "converter's string value";
            }
        };

        BeanMappingBuilder builder = new BeanMappingBuilder() {
            protected void configure() {
                config(
                    defaultCustomConverter(customConverter, 
                    String.class, 
                    String.class)
                )
                .mapping(
                    Source.class, 
                    Dest.class 
                )
                .fields(
                    field("stringField"),
                    field("stringField").required(true)
                );
            }
        };

        mapper.addMapping(builder);

        Source source = new Source("some string", 10);
        Dest dest = new Dest("another string value", 5);
        
        mapper.map(source, dest);

        assertEquals("converter's string value", dest.getStringField());
        assertEquals(10, dest.getIntField());
        
        mapper.map(dest, source);

        assertEquals("converter's string value", source.getStringField());
        assertEquals(10, source.getIntField());
    }

    @Test
    public void test15() {

        final CustomConverter customConverter = new CustomConverter() {
            public Object convert(Object existingDestinationFieldValue, Object sourceFieldValue,
                Class<?> destinationClass, Class<?> sourceClass) {
                return "converter's string value";
            }
        };

        BeanMappingBuilder builder = new BeanMappingBuilder() {
            protected void configure() {
                config(
                    defaultCustomConverter(customConverter, 
                    String.class, 
                    String.class)
                )
                .mapping(
                    Source.class, 
                    Dest.class 
                )
                .fields(
                    field("stringField"),
                    field("stringField").required(true),
                    customConverter(ToStringCustomConverter.class)
                );
            }
        };

        mapper.addMapping(builder);

        Source source = new Source("some string", 10);
        Dest dest = new Dest("another string value", 5);
        
        mapper.map(source, dest);

        assertEquals("some string", dest.getStringField());
        assertEquals(10, dest.getIntField());
    }

}
