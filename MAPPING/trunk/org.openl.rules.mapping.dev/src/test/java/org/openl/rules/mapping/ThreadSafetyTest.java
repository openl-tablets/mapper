package org.openl.rules.mapping;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dozer.BaseMappingParamsAwareFieldMappingCondition;
import org.dozer.FieldMappingCondition;
import org.dozer.MappingContext;
import org.dozer.MappingParameters;
import org.junit.Test;
import org.openl.rules.mapping.to.A;
import org.openl.rules.mapping.to.B;
import org.openl.rules.mapping.to.C;

public class ThreadSafetyTest {

    @Test
    public void threadSafetyTest() {

        int threadsCount = 5;
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
                        status.getExceptions().add(e);
                    } finally {
                        status.setCounter(status.getCounter() + 1);
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
    
//    @Test
    public void fieldMapConditionWithIdSupportTest() {

        Map<String, FieldMappingCondition> conditions = new HashMap<String, FieldMappingCondition>();
        conditions.put("mapField", new BaseMappingParamsAwareFieldMappingCondition() {
            
            @Override
            public void setMappingParams(MappingParameters params) {
                super.setMappingParams(params);
                try {
                    Thread.sleep(2000);
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

        MappingContext context2 = new MappingContext();
        MappingParameters params2 = new MappingParameters();
        params2.put("mapField", false);
        context2.setParams(params2);
        
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
                    status.getExceptions().add(e);
                } finally {
                    status.setCounter(status.getCounter() + 1);
                }
            }
        });
        
        thread1.start();



        B b2 = mapper.map(a, B.class, context2);
        assertEquals(null, b2.getAString());
    }

    
    private static class ExecutionStatus {
        private int counter;
        private List<Exception> exceptions = new ArrayList<Exception>();

        public int getCounter() {
            return counter;
        }
        public void setCounter(int counter) {
            this.counter = counter;
        }
        public List<Exception> getExceptions() {
            return exceptions;
        }
        public void setExceptions(List<Exception> exceptions) {
            this.exceptions = exceptions;
        }
        
    }
}
