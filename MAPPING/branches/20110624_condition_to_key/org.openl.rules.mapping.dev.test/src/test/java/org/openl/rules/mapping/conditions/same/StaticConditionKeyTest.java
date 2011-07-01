package org.openl.rules.mapping.conditions.same;

import org.junit.Before;
import org.junit.Test;
import org.openl.rules.mapping.Mapper;
import org.openl.rules.mapping.RulesBeanMapperFactory;
import org.openl.rules.mapping.conditions.same.beans.A;
import org.openl.rules.mapping.conditions.same.beans.B;
import org.openl.rules.mapping.conditions.same.beans.StaticUtil;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class StaticConditionKeyTest {

    @Before
    public void runBeforeEveryTest() {
        resetConditions();
    }

    @Test
    public void staticConditionBasicTest() {
        File source = new File("src/test/resources/org/openl/rules/mapping/conditions/same/StaticBasicTest.xlsx");
        Mapper mapper = RulesBeanMapperFactory.createMapperInstance(source);

        A a = new A();
        a.setFieldA1("a-string");

        //both condiotions are false
        StaticUtil.setCondition1value(false);
        StaticUtil.setCondition2value(false);
        B b = mapper.map(a, B.class);
        assertTrue(StaticUtil.isCondition1Called());
        assertTrue(StaticUtil.isCondition2Called());
        assertNull(b.getFieldB1());

        //first condition is true
        resetConditions();
        StaticUtil.setCondition1value(true);
        StaticUtil.setCondition2value(false);
        b = mapper.map(a, B.class);
        assertTrue(StaticUtil.isCondition1Called());
        assertTrue(StaticUtil.isCondition2Called());
        assertEquals("a-string", b.getFieldB1());

        //second condition is true
        resetConditions();
        StaticUtil.setCondition1value(false);
        StaticUtil.setCondition2value(true);
        b = mapper.map(a, B.class);
        assertTrue(StaticUtil.isCondition1Called());
        assertTrue(StaticUtil.isCondition2Called());
        assertEquals("a-string", b.getFieldB1());
    }

    private void resetConditions() {
        StaticUtil.resetCondition1();
        StaticUtil.resetCondition2();
    }

}
