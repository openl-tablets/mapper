package org.openl.rules.mapping;

import org.dozer.*;
import org.dozer.Mapping;
import org.junit.Assert;
import org.junit.Test;
import org.openl.rules.mapping.to.*;
import org.openl.rules.mapping.to.xmlbeans.PurchaseOrderTO;
import org.openuri.easypo.Customer;
import org.openuri.easypo.LineItem;
import org.openuri.easypo.PurchaseOrderDocument;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class FieldMappingAccessToParentTest {


    @Test
    public void fieldMapConditionSupportTest() {

        File source = new File("src/test/resources/org/openl/rules/mapping/customconverters/FieldMappingAccessToParentTest.xlsx");

	    Map<String, CustomConverter> converters = new HashMap<String, CustomConverter>();
	    converters.put("ExtConverter", new ParentAccessCustomConverter());


	    Mapper mapper = RulesBeanMapperFactory.createMapperInstance(source, converters, null);

		D d = new D();
	    d.setAnInt(100);
	    E e = new E();
	    e.setD(d);
	    A a = new A();
	    a.setAString("Woohoo IT WORKS");
	    a.setE(e);

	    MappingContext context1 = new MappingContext();
	    MappingParameters params1 = new MappingParameters();
	    params1.put("value", "value1");
	    context1.setParams(params1);

	    C c = mapper.map(a, C.class, context1);
	    
	    Assert.assertEquals("Woohoo IT WORKS", c.getB().getAString());
    }

}
