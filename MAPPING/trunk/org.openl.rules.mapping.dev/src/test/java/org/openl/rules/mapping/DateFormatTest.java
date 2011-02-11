package org.openl.rules.mapping;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.Calendar;

import org.junit.Test;
import org.openl.rules.mapping.to.A;
import org.openl.rules.mapping.to.containers.DateHolder;

public class DateFormatTest {

    @Test
    public void fieldDateFormatSupportTest() {

        File source = new File("src/test/resources/org/openl/rules/mapping/dateformat/FieldDateFormatSupportTest.xlsx");
        RulesBeanMapper mapper = RulesBeanMapperFactory.createMapperInstance(source);

        A a = new A();
        a.setAString("2010-07-12");
        
        DateHolder result = mapper.map(a, DateHolder.class);
        
        Calendar c = Calendar.getInstance();
        c.setTime(result.getDate());
        
        assertEquals(12, c.get(Calendar.DATE));
        assertEquals(6, c.get(Calendar.MONTH));
        assertEquals(2010, c.get(Calendar.YEAR));
        
        A a1 = mapper.map(result, A.class);
        
        assertEquals("2010-07-12", a1.getAString());
    }
    
    @Test
    public void globalDateFormatSupportTest() {

        File source = new File("src/test/resources/org/openl/rules/mapping/dateformat/GlobalDateFormatOptionTest.xlsx");
        RulesBeanMapper mapper = RulesBeanMapperFactory.createMapperInstance(source);

        A a = new A();
        a.setAString("2010-07-12");
        
        DateHolder result = mapper.map(a, DateHolder.class);
        
        Calendar c = Calendar.getInstance();
        c.setTime(result.getDate());
        
        assertEquals(12, c.get(Calendar.DATE));
        assertEquals(6, c.get(Calendar.MONTH));
        assertEquals(2010, c.get(Calendar.YEAR));
        
        A a1 = mapper.map(result, A.class);
        
        assertEquals("2010-07-12", a1.getAString());
    }

}
