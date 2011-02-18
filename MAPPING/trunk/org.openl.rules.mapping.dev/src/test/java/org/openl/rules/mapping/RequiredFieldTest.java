package org.openl.rules.mapping;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;
import org.openl.rules.mapping.exception.RulesMappingException;
import org.openl.rules.mapping.to.A;
import org.openl.rules.mapping.to.B;

public class RequiredFieldTest {

    @Test
    public void requiredClauseIsPassedTest() {

        File source = new File("src/test/resources/org/openl/rules/mapping/required/RequiredFieldTest.xlsx");
        Mapper mapper = RulesBeanMapperFactory.createMapperInstance(source);

        A a = new A();
        a.setAString("a-string");

        B b = mapper.map(a, B.class);

        assertEquals("a-string", b.getAString());
    }

    @Test(expected = RulesMappingException.class)
    public void requiredClauseIsNotPassedTest() {

        File source = new File("src/test/resources/org/openl/rules/mapping/required/RequiredFieldTest.xlsx");
        Mapper mapper = RulesBeanMapperFactory.createMapperInstance(source);

        A a = new A();
        mapper.map(a, B.class);
    }

}
