package org.openl.rules.mapping;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.math.BigDecimal;
import java.util.Calendar;

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
        RulesBeanMapper mapper = RulesBeanMapperFactory.createMapperInstance(source);

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
        RulesBeanMapper mapper = RulesBeanMapperFactory.createMapperInstance(source);

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
        RulesBeanMapper mapper = RulesBeanMapperFactory.createMapperInstance(source);

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

        File source = new File("src/test/resources/org/openl/rules/mapping/customconverters/ExternalCustomConvertersTest.xlsx");
        RulesBeanMapper mapper = RulesBeanMapperFactory.createMapperInstance(source);

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
        RulesBeanMapper mapper = RulesBeanMapperFactory.createMapperInstance(source);

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

}
