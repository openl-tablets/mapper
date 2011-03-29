package org.openl.rules.mapping;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.dozer.CustomConverter;
import org.dozer.MappingContext;
import org.dozer.MappingParameters;
import org.junit.Test;
import org.openl.rules.mapping.to.A;
import org.openl.rules.mapping.to.C;
import org.openl.rules.mapping.to.D;
import org.openl.rules.mapping.to.E;
import org.openl.rules.mapping.to.xmlbeans.PurchaseOrderTO;
import org.openuri.easypo.Customer;
import org.openuri.easypo.LineItem;
import org.openuri.easypo.PurchaseOrderDocument.PurchaseOrder;

public class CustomConvertersSupportTest {

    @Test
    public void defaultConverterTest1() {
        File source = new File("src/test/resources/org/openl/rules/mapping/customconverters/DefaultCustomConvertersTest.xlsx");
        Mapper mapper = RulesBeanMapperFactory.createMapperInstance(source);

        PurchaseOrder purchaseOrder = PurchaseOrder.Factory.newInstance();
        
        Calendar currentDate = Calendar.getInstance();
        purchaseOrder.setDate(currentDate);
        
        Customer customer = purchaseOrder.addNewCustomer();
        customer.setName("customer name");
        customer.setAddress("customer address");
        customer.setAge(21);
        
        LineItem item1 = purchaseOrder.addNewLineItem();
        item1.setDescription("line1");
        item1.setPrice(BigDecimal.valueOf(10.5));

        LineItem item2 = purchaseOrder.addNewLineItem();
        item2.setDescription("single");
        item2.setPrice(BigDecimal.valueOf(5.1));

        purchaseOrder.setLineItemArray(new LineItem[]{item1, item2});
        
        PurchaseOrderTO result = mapper.map(purchaseOrder, PurchaseOrderTO.class);
        
        assertEquals(true, result.isHasSingleLineItem());
        
        item2.setDescription("item2");
        result = mapper.map(purchaseOrder, PurchaseOrderTO.class);
        
        assertEquals(false, result.isHasSingleLineItem());
    }
    
    @Test
    public void customConverterTest1() {
        File source = new File("src/test/resources/org/openl/rules/mapping/customconverters/CustomConvertersTest.xlsx");
        Mapper mapper = RulesBeanMapperFactory.createMapperInstance(source);

        PurchaseOrder purchaseOrder = PurchaseOrder.Factory.newInstance();
        
        Calendar currentDate = Calendar.getInstance();
        purchaseOrder.setDate(currentDate);
        
        Customer customer = purchaseOrder.addNewCustomer();
        customer.setName("customer name");
        customer.setAddress("customer address");
        customer.setAge(21);
        
        LineItem item1 = purchaseOrder.addNewLineItem();
        item1.setDescription("line1");
        item1.setPrice(BigDecimal.valueOf(10.5));

        LineItem item2 = purchaseOrder.addNewLineItem();
        item2.setDescription("single");
        item2.setPrice(BigDecimal.valueOf(5.1));

        purchaseOrder.setLineItemArray(new LineItem[]{item1, item2});
        
        PurchaseOrderTO result = mapper.map(purchaseOrder, PurchaseOrderTO.class);
        
        assertEquals(true, result.isHasSingleLineItem());
        
        item2.setDescription("item2");
        result = mapper.map(purchaseOrder, PurchaseOrderTO.class);
        
        assertEquals(false, result.isHasSingleLineItem());
    }
    

    @Test
    public void converterReusageTest() {

        File source = new File("src/test/resources/org/openl/rules/mapping/customconverters/CustomConverterReuseTest.xlsx");
        Mapper mapper = RulesBeanMapperFactory.createMapperInstance(source);

        A a = new A();
        a.setAString("100");
        a.setAnInteger(10);

        C c = mapper.map(a, C.class);
        assertEquals("10", c.getB().getAString());
        assertEquals(Integer.valueOf(100), c.getB().getAnInteger());

        A a1 = mapper.map(c, A.class);
        
        assertEquals("100", a1.getAString());
        assertEquals(Integer.valueOf(10), a1.getAnInteger());
    }
    

    @Test
    public void externalMapperTest() {

        File source = new File("src/test/resources/org/openl/rules/mapping/customconverters/ExternalMapperTest.xlsx");
        Mapper mapper = RulesBeanMapperFactory.createMapperInstance(source);

        A a = new A();
        a.setAString("a-string");

        E e = new E();
        e.setAString("e-string");
        
        D d = new D();
        d.setAnInt(100);
        
        e.setD(d);
        a.setE(e);
        
        C c = mapper.map(a, C.class);
        assertEquals("a-string", c.getAString());
        assertEquals("e-string", c.getB().getAString());
        assertEquals(Integer.valueOf(100), c.getB().getAnInteger());
    }

    @Test
    public void externalCustomConverterTest() {
        File source = new File("src/test/resources/org/openl/rules/mapping/customconverters/ExternalStaticMethodCustomConverterTest.xlsx");
        Mapper mapper = RulesBeanMapperFactory.createMapperInstance(source);

        PurchaseOrder purchaseOrder = PurchaseOrder.Factory.newInstance();
        
        Calendar currentDate = Calendar.getInstance();
        purchaseOrder.setDate(currentDate);
        
        Customer customer = purchaseOrder.addNewCustomer();
        customer.setName("customer name");
        customer.setAddress("customer address");
        customer.setAge(21);
        
        LineItem item1 = purchaseOrder.addNewLineItem();
        item1.setDescription("line1");
        item1.setPrice(BigDecimal.valueOf(10.5));

        LineItem item2 = purchaseOrder.addNewLineItem();
        item2.setDescription("single");
        item2.setPrice(BigDecimal.valueOf(5.1));

        purchaseOrder.setLineItemArray(new LineItem[]{item1, item2});
        
        PurchaseOrderTO result = mapper.map(purchaseOrder, PurchaseOrderTO.class);
        
        assertEquals(true, result.isHasSingleLineItem());
        
        item2.setDescription("item2");
        result = mapper.map(purchaseOrder, PurchaseOrderTO.class);
        
        assertEquals(false, result.isHasSingleLineItem());
    }

    @Test
    public void customConverterWithIdTest1() {
        
        Map<String, CustomConverter> converters = new HashMap<String, CustomConverter>();
        converters.put("isExists", new CustomConverter(){
            public Object convert(Object existingDestinationFieldValue, Object sourceFieldValue,
                Class<?> destinationClass, Class<?> sourceClass) {
                return sourceFieldValue != null;
            }
            
        });

        File source = new File("src/test/resources/org/openl/rules/mapping/customconverters/CustomConvertersWithIdTest.xlsx");
        Mapper mapper = RulesBeanMapperFactory.createMapperInstance(source, converters, null);

        PurchaseOrder purchaseOrder = PurchaseOrder.Factory.newInstance();
        
        Calendar currentDate = Calendar.getInstance();
        purchaseOrder.setDate(currentDate);
        
        Customer customer = purchaseOrder.addNewCustomer();
        customer.setName("customer name");
        customer.setAddress("customer address");
        customer.setAge(21);
        
        LineItem item1 = purchaseOrder.addNewLineItem();
        item1.setDescription("line1");
        item1.setPrice(BigDecimal.valueOf(10.5));

        LineItem item2 = purchaseOrder.addNewLineItem();
        item2.setDescription("single");
        item2.setPrice(BigDecimal.valueOf(5.1));

        purchaseOrder.setLineItemArray(new LineItem[]{item1, item2});
        
        PurchaseOrderTO result = mapper.map(purchaseOrder, PurchaseOrderTO.class);
        
        assertEquals(true, result.isHasSingleLineItem());
        
        item2.setDescription("item2");
        result = mapper.map(purchaseOrder, PurchaseOrderTO.class);
        
        assertEquals(false, result.isHasSingleLineItem());
    }

    @Test
    public void customConverterWithIdTest2() {
        
        Map<String, CustomConverter> converters = new HashMap<String, CustomConverter>();
        converters.put("isExists", new CustomConverter(){
            public Object convert(Object existingDestinationFieldValue, Object sourceFieldValue,
                Class<?> destinationClass, Class<?> sourceClass) {
                return sourceFieldValue != null;
            }
            
        });

        File source = new File("src/test/resources/org/openl/rules/mapping/customconverters/CustomConvertersOrderTest.xlsx");
        Mapper mapper = RulesBeanMapperFactory.createMapperInstance(source, converters, null);

        PurchaseOrder purchaseOrder = PurchaseOrder.Factory.newInstance();
        
        Calendar currentDate = Calendar.getInstance();
        purchaseOrder.setDate(currentDate);
        
        Customer customer = purchaseOrder.addNewCustomer();
        customer.setName("customer name");
        customer.setAddress("customer address");
        customer.setAge(21);
        
        LineItem item1 = purchaseOrder.addNewLineItem();
        item1.setDescription("line1");
        item1.setPrice(BigDecimal.valueOf(10.5));

        LineItem item2 = purchaseOrder.addNewLineItem();
        item2.setDescription("single");
        item2.setPrice(BigDecimal.valueOf(5.1));

        purchaseOrder.setLineItemArray(new LineItem[]{item1, item2});
        
        PurchaseOrderTO result = mapper.map(purchaseOrder, PurchaseOrderTO.class);
        
        assertEquals(true, result.isHasSingleLineItem());
        
        item2.setDescription("item2");
        result = mapper.map(purchaseOrder, PurchaseOrderTO.class);
        
        assertEquals(false, result.isHasSingleLineItem());
    }

    @Test
    public void converterRroxyReusageTest() {

        File source = new File("src/test/resources/org/openl/rules/mapping/customconverters/CustomConverterProxyCacheTest.xlsx");
        Mapper mapper = RulesBeanMapperFactory.createMapperInstance(source);

        A a = new A();
        a.setAString("100");
        a.setAnInteger(10);

        C c = mapper.map(a, C.class);
        assertEquals("10", c.getB().getAString());
        assertEquals(Integer.valueOf(100), c.getB().getAnInteger());

        c = mapper.map(a, C.class);
        assertEquals("10", c.getB().getAString());
        assertEquals(Integer.valueOf(100), c.getB().getAnInteger());

        A a1 = mapper.map(c, A.class);
        
        assertEquals("100", a1.getAString());
        assertEquals(Integer.valueOf(10), a1.getAnInteger());
        
        a1 = mapper.map(c, A.class);
        
        assertEquals("100", a1.getAString());
        assertEquals(Integer.valueOf(10), a1.getAnInteger());
    }

    @Test
    public void mappingParamsAwareConverterTest() {

        File source = new File("src/test/resources/org/openl/rules/mapping/customconverters/MappingParamsAwareCustomConverterTest.xlsx");
        Mapper mapper = RulesBeanMapperFactory.createMapperInstance(source);

        A a_original = new A();
        a_original.setAString("100");
        a_original.setAnInteger(10);

        C c = mapper.map(a_original, C.class);
        assertEquals("10", c.getB().getAString());
        assertEquals(Integer.valueOf(100), c.getB().getAnInteger());

        MappingParameters params = new MappingParameters();
        params.put("string", "1");
        params.put("int", 1000);
        MappingContext context = new MappingContext();
        context.setParams(params);
        
        C c1 = mapper.map(a_original, C.class, context);
        assertEquals("1", c1.getB().getAString());
        assertEquals(Integer.valueOf(1000), c1.getB().getAnInteger());

        A a = mapper.map(c, A.class);
        
        assertEquals("100", a.getAString());
        assertEquals(Integer.valueOf(10), a.getAnInteger());
        
        A a1 = mapper.map(c, A.class, context);
        
        assertEquals("1", a1.getAString());
        assertEquals(Integer.valueOf(1000), a1.getAnInteger());
    }

}
