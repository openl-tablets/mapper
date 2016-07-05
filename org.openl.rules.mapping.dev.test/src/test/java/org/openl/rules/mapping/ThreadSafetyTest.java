package org.openl.rules.mapping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dozer.BaseMappingParamsAwareCustomConverter;
import org.dozer.BaseMappingParamsAwareFieldMappingCondition;
import org.dozer.CustomConverter;
import org.dozer.FieldMappingCondition;
import org.dozer.MappingContext;
import org.junit.Test;
import org.openl.rules.mapping.to.A;
import org.openl.rules.mapping.to.B;
import org.openl.rules.mapping.to.C;
import org.openl.rules.mapping.to.E;

public class ThreadSafetyTest {

    @Test
    public void threadSafetyTest() {

        int threadsCount = 50;
        final ExecutionStatus status = new ExecutionStatus();

        File source = new File("src/test/resources/org/openl/rules/mapping/thread/ThreadSafetyTest.xlsx");
        final Mapper mapper = RulesBeanMapperFactory.createMapperInstance(source);

        for (int i = 0; i < threadsCount; i++) {

            Thread thread = new Thread(new Runnable() {
                public void run() {

                    final A a = new A();
                    a.setAnInteger(10);

                    final B b = new B();
                    b.setAnInteger(10);
                    
                    try {
                        mapper.map(a, b);
                        assertEquals(Integer.valueOf(20), b.getAnInteger());
                    } catch (Exception e) {
                        status.appendException(e);
                    } finally {
                        status.increaseCounter();
                    }
                }
            });
            
            thread.start();
        }

        while (status.getCounter() < threadsCount) {
            // wait until all threads are terminated
        }
        
        assertTrue(status.getExceptions().isEmpty());
    }
    
    @Test
    public void fieldMapConditionWithIdSupportTest() {

        Map<String, FieldMappingCondition> conditions = new HashMap<String, FieldMappingCondition>();
        conditions.put("mapField", new BaseMappingParamsAwareFieldMappingCondition() {
            
            @Override
            public void setMappingParams(MappingParameters params) {
                super.setMappingParams(params);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public boolean mapField(MappingParameters params, Object sourceFieldValue, Object destFieldValue,
                Class<?> sourceType, Class<?> destType) {
                return (Boolean)params.get("mapField");
            }        
        });

        File source = new File(
            "src/test/resources/org/openl/rules/mapping/thread/MappingParamsAwareFieldMappingConditionThreadSafetyTest.xlsx");
        final Mapper mapper = RulesBeanMapperFactory.createMapperInstance(source, null, conditions);

        final ExecutionStatus status = new ExecutionStatus();
        
        final A a = new A();
        a.setAString("a-string");

        Thread thread1 = new Thread(new Runnable() {
            public void run() {
                MappingContext context1 = new MappingContext();
                MappingParameters params1 = new MappingParameters();
                params1.put("mapField", true);
                context1.setParams(params1);
                
                try {
                    B b1 = mapper.map(a, B.class, context1);
                    assertEquals("a-string", b1.getAString());
                } catch (Exception e) {
                    status.appendException(e);
                } finally {
                    status.increaseCounter();
                }
            }
        });
        
        Thread thread2 = new Thread(new Runnable() {
            public void run() {
                MappingContext context2 = new MappingContext();
                MappingParameters params2 = new MappingParameters();
                params2.put("mapField", false);
                context2.setParams(params2);
                
                try {
                    B b2 = mapper.map(a, B.class, context2);
                    assertEquals(null, b2.getAString());
                } catch (Exception e) {
                    status.appendException(e);
                } finally {
                    status.increaseCounter();
                }
            }
        });

        thread1.start();
        thread2.start();
        
        while (status.getCounter() < 2) {
            // wait until all threads are terminated
        }
        
        assertTrue(status.getExceptions().isEmpty());
    }

    @Test
    public void convertersWithIdSupportTest() {

        Map<String, CustomConverter> convertersMap = new HashMap<String, CustomConverter>();
        convertersMap.put("convert", new BaseMappingParamsAwareCustomConverter() {
            
            @Override
            public Object convert(MappingParameters params, Object existingDestinationFieldValue,
                Object sourceFieldValue, Class<?> destinationClass, Class<?> sourceClass) {
                return params.get("value");
            }
            
            @Override
            public void setMappingParams(MappingParameters params) {
                super.setMappingParams(params);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        File source = new File(
            "src/test/resources/org/openl/rules/mapping/thread/MappingParamsAwareCustomConverterThreadSafetyTest.xlsx");
        final Mapper mapper = RulesBeanMapperFactory.createMapperInstance(source, convertersMap, null);

        final ExecutionStatus status = new ExecutionStatus();
        
        final A a = new A();
        a.setAString("a-string");

        Thread thread1 = new Thread(new Runnable() {
            public void run() {
                MappingContext context1 = new MappingContext();
                MappingParameters params1 = new MappingParameters();
                params1.put("value", "value1");
                context1.setParams(params1);
                
                try {
                    B b1 = mapper.map(a, B.class, context1);
                    assertEquals("value1", b1.getAString());
                } catch (Exception e) {
                    status.appendException(e);
                } finally {
                    status.increaseCounter();
                }
            }
        });
        
        Thread thread2 = new Thread(new Runnable() {
            public void run() {
                MappingContext context2 = new MappingContext();
                MappingParameters params2 = new MappingParameters();
                params2.put("value", "value2");
                context2.setParams(params2);
                
                try {
                    B b2 = mapper.map(a, B.class, context2);
                    assertEquals("value2", b2.getAString());
                } catch (Exception e) {
                    status.appendException(e);
                } finally {
                    status.increaseCounter();
                }
            }
        });

        thread1.start();
        thread2.start();
        
        while (status.getCounter() < 2) {
            // wait until all threads are terminated
        }
        
        assertTrue(status.getExceptions().isEmpty());
    }
    
    @Test
    public void beanFactoryTest() {

        File source = new File(
            "src/test/resources/org/openl/rules/mapping/thread/MappingParamsAwareBeanFactoryThreadSafetyTest.xlsx");
        final Mapper mapper = RulesBeanMapperFactory.createMapperInstance(source);

        final ExecutionStatus status = new ExecutionStatus();
        
        Thread thread1 = new Thread(new Runnable() {
            public void run() {
                MappingContext context1 = new MappingContext();
                MappingParameters params1 = new MappingParameters();
                params1.put("key", "value1");
                context1.setParams(params1);
                
                try {
                    E e1 = mapper.map(new C(), E.class, context1);
                    assertEquals("value1", e1.getAString());
                } catch (Exception e) {
                    status.appendException(e);
                } finally {
                    status.increaseCounter();
                }
            }
        });
        
        Thread thread2 = new Thread(new Runnable() {
            public void run() {
                MappingContext context2 = new MappingContext();
                MappingParameters params2 = new MappingParameters();
                params2.put("key", "value2");
                context2.setParams(params2);
                
                try {
                    E e2 = mapper.map(new C(), E.class, context2);
                    assertEquals("value2", e2.getAString());
                } catch (Exception e) {
                    status.appendException(e);
                } finally {
                    status.increaseCounter();
                }
            }
        });

        thread1.start();
        thread2.start();
        
        while (status.getCounter() < 2) {
            // wait until all threads are terminated
        }
        
        assertTrue(status.getExceptions().isEmpty());
    }
    
    private static class ExecutionStatus {
        private int counter;
        private List<Exception> exceptions = new ArrayList<Exception>();

        public synchronized int getCounter() {
            return counter;
        }
        public List<Exception> getExceptions() {
            return Collections.unmodifiableList(exceptions);
        }
        
        public synchronized void increaseCounter() {
            counter += 1;
        }
        
        public synchronized void appendException(Exception ex) {
            exceptions.add(ex);
        }
    }
}
