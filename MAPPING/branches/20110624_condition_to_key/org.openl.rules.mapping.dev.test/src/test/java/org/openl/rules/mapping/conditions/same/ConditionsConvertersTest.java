package org.openl.rules.mapping.conditions.same;

import org.dozer.CustomConverter;
import org.dozer.FieldMappingCondition;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openl.rules.mapping.Mapper;
import org.openl.rules.mapping.RulesBeanMapperFactory;
import org.openl.rules.mapping.conditions.same.beans.A;
import org.openl.rules.mapping.conditions.same.beans.B;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ConditionsConvertersTest {

    private static final String CONVERTER_1_VALUE = "value1";

    private static final String CONVERTER_2_VALUE = "value2";

    @Mock
    private FieldMappingCondition condition1;

    @Mock
    private FieldMappingCondition condition2;

    @Mock
    private CustomConverter converter1;

    @Mock
    private CustomConverter converter2;

    private Map<String, FieldMappingCondition> conditions;

    private Map<String, CustomConverter> converters;

    @Before
    public void runBeforeEveryTest() {
        MockitoAnnotations.initMocks(this);
        conditions = new HashMap<String, FieldMappingCondition>();
        conditions.put("condition1", condition1);
        conditions.put("condition2", condition2);
        converters = new HashMap<String, CustomConverter>();
        converters.put("converter1", converter1);
        converters.put("converter2", converter2);
    }

    @Test
    public void conditionsTest() {
        File source = new File("src/test/resources/org/openl/rules/mapping/conditions/same/ConditionsConvertersTest.xlsx");
        Mapper mapper = RulesBeanMapperFactory.createMapperInstance(source, converters, conditions);

        A a = new A();
        a.setFieldA1("a-string");
        B b;

        //both conditions are false -> converters are not called
        mockCondtion(condition1, false);
        mockCondtion(condition2, false);
        b = mapper.map(a, B.class);
        verifyCalled(condition1);
        verifyCalled(condition2);
        verifyCalled(converter1, 0);
        verifyCalled(converter2, 0);
        assertNull(b.getFieldB1());

        resetMocks();

        //condition1 is true, condition2 is false -> converter1 is called, converter2 isn't
        mockCondtion(condition1, true);
        mockCondtion(condition2, false);
        b = mapper.map(a, B.class);
        verifyCalled(condition1);
        verifyCalled(condition2);
        verifyCalled(converter1, 1);
        verifyCalled(converter2, 0);
        assertEquals(CONVERTER_1_VALUE, b.getFieldB1());

        resetMocks();

        //condition1 is false, condition2 is true -> converter2 is called, converter1 isn't
        mockCondtion(condition1, false);
        mockCondtion(condition2, true);
        b = mapper.map(a, B.class);
        verifyCalled(condition1);
        verifyCalled(condition2);
        verifyCalled(converter1, 0);
        verifyCalled(converter2, 1);
        assertEquals(CONVERTER_2_VALUE, b.getFieldB1());

        resetMocks();

        //both conditions are true -> both converters are called, the value of converter2 is expected in destination
        //field, because rule with converter2 is defined after the rule with converter1 (last rule wins)
        mockCondtion(condition1, true);
        mockCondtion(condition2, true);
        b = mapper.map(a, B.class);
        verifyCalled(condition1);
        verifyCalled(condition2);
        verifyCalled(converter1, 1);
        verifyCalled(converter2, 1);
        assertEquals(CONVERTER_2_VALUE, b.getFieldB1());

    }

    private void mockCondtion(FieldMappingCondition condition, boolean value) {
        when(condition.mapField(any(Object.class), any(Object.class),
                any(Class.class), any(Class.class))).thenReturn(value);
    }

    private void verifyCalled(FieldMappingCondition condition) {
        verify(condition, times(1)).mapField(any(Object.class), any(Object.class),
                any(Class.class), any(Class.class));
    }

    private void verifyCalled(CustomConverter converter, int count) {
        verify(converter, times(count)).convert(any(Object.class), any(Object.class),
                any(Class.class), any(Class.class));
    }

    private void resetMocks() {
        reset(condition1, condition2, converter1, converter2);
        when(converter1.convert(any(Object.class), any(Object.class),
                any(Class.class), any(Class.class))).thenReturn(CONVERTER_1_VALUE);
        when(converter2.convert(any(Object.class), any(Object.class),
                any(Class.class), any(Class.class))).thenReturn(CONVERTER_2_VALUE);
    }



}
