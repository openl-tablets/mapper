package org.openl.rules.mapping.conditions.same;

import org.dozer.FieldMappingCondition;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openl.rules.mapping.Mapper;
import org.openl.rules.mapping.RulesBeanMapperFactory;
import org.openl.rules.mapping.conditions.same.beans.A;
import org.openl.rules.mapping.conditions.same.beans.B;
import org.openl.rules.mapping.conditions.same.beans.ChildClassA;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ConditionsSameTest {

    @Mock
    private FieldMappingCondition condition1;

    @Mock
    private FieldMappingCondition condition2;

    @Mock
    private FieldMappingCondition condition3;

    private Map<String, FieldMappingCondition> conditions;

    @Before
    public void runBeforeEveryTest() {
        MockitoAnnotations.initMocks(this);
        when(condition1.mapField(any(Object.class), any(Object.class),
                any(Class.class), any(Class.class))).thenReturn(true);
        when(condition2.mapField(any(Object.class), any(Object.class),
                any(Class.class), any(Class.class))).thenReturn(true);
        when(condition3.mapField(any(Object.class), any(Object.class),
                any(Class.class), any(Class.class))).thenReturn(true);
        conditions = new HashMap<String, FieldMappingCondition>();
        conditions.put("condition1", condition1);
        conditions.put("condition2", condition2);
        conditions.put("condition3", condition3);
    }

    @Test
    public void sameConditionsTest() {
        File source = new File("src/test/resources/org/openl/rules/mapping/conditions/same/BasicTest.xlsx");
        Mapper mapper = RulesBeanMapperFactory.createMapperInstance(source, null, conditions);

        A a = new A();
        a.setFieldA1("a-string");

        B b = mapper.map(a, B.class);

        verifyTimes(condition1, 1);
        verifyTimes(condition2, 1);
    }

    @Test
    public void overrideParentFieldTest() {

        File source = new File("src/test/resources/org/openl/rules/mapping/conditions/same/OverrideParentFieldTest.xlsx");
        Mapper mapper = RulesBeanMapperFactory.createMapperInstance(source, null, conditions);

        //test direction A -> ChildClassA
        A a = new A();
        a.setFieldA1("a-string");

        ChildClassA child = mapper.map(a, ChildClassA.class);

        verifyTimes(condition1, 0);
        verifyTimes(condition2, 1);
        verifyTimes(condition3, 1);

        //test direction ChildClassA -> A
        reset(condition1, condition2, condition3);

        child = new ChildClassA();
        child.setSuperFieldA1("a-string");

        a = mapper.map(child, A.class);

        verifyTimes(condition1, 0);
        verifyTimes(condition2, 1);
        verifyTimes(condition3, 1);
    }

    @Test
    public void useParentConditionsTest() {

        File source = new File("src/test/resources/org/openl/rules/mapping/conditions/same/UseParentConditionsTest.xlsx");
        Mapper mapper = RulesBeanMapperFactory.createMapperInstance(source, null, conditions);

        //test direction A -> ChildClassA
        A a = new A();
        a.setFieldA1("a-string");

        ChildClassA child = mapper.map(a, ChildClassA.class);

        verifyTimes(condition1, 1);
        verifyTimes(condition2, 1);

        //test direction ChildClassA -> A
        reset(condition1, condition2);

        child = new ChildClassA();
        child.setSuperFieldA1("a-string");

        a = mapper.map(child, A.class);

        verifyTimes(condition1, 1);
        verifyTimes(condition2, 1);
    }

    private void verifyTimes(FieldMappingCondition condition, int count) {
        verify(condition, times(count)).mapField(any(Object.class), any(Object.class),
                any(Class.class), any(Class.class));
    }


}
