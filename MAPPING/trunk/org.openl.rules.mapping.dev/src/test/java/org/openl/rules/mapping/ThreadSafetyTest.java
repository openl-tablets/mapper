package org.openl.rules.mapping;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.openl.rules.mapping.to.A;
import org.openl.rules.mapping.to.B;

public class ThreadSafetyTest {

    @Test
    public void threadSafetyTest() {

        int threadsCount = 100;
        final ExecutionStatus status = new ExecutionStatus();

        File source = new File("src/test/resources/org/openl/rules/mapping/thread/ThreadSafetyTest.xlsx");
        final RulesBeanMapper mapper = RulesBeanMapperFactory.createMapperInstance(source);

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
