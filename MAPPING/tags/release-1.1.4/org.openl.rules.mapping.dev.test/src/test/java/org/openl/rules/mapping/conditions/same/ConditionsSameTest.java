package org.openl.rules.mapping.conditions.same;

import org.dozer.FieldMappingCondition;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openl.rules.mapping.Mapper;
import org.openl.rules.mapping.RulesBeanMapperFactory;
import org.openl.rules.mapping.to.A;
import org.openl.rules.mapping.to.B;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ConditionsSameTest {

    @Mock
    private FieldMappingCondition condition1;

    @Mock
    private FieldMappingCondition condition2;

    @Before
    public void runBeforeEveryTest() {
        MockitoAnnotations.initMocks(this);
        when(condition1.mapField(any(Object.class), any(Object.class),
                any(Class.class), any(Class.class))).thenReturn(true);
        when(condition2.mapField(any(Object.class), any(Object.class),
                any(Class.class), any(Class.class))).thenReturn(true);
    }

    @Test
    public void sameConditionsTest() {

        Map<String, FieldMappingCondition> conditions = new HashMap<String, FieldMappingCondition>();
        conditions.put("condition1", condition1);
        conditions.put("condition2", condition2);

        File source = new File("src/test/resources/org/openl/rules/mapping/conditions/SameConditionsTest.xlsx");
        Mapper mapper = RulesBeanMapperFactory.createMapperInstance(source, null, conditions);

        A a = new A();
        a.setAString("a-string");

        B b = mapper.map(a, B.class);

        verify(condition1, times(1)).mapField(any(Object.class), any(Object.class),
                any(Class.class), any(Class.class));

        //TODO should be times(1)
        verify(condition2, times(0)).mapField(any(Object.class), any(Object.class),
                any(Class.class), any(Class.class));
    }

}
