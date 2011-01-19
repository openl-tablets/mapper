package org.openl.rules.mapping;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.math.BigDecimal;
import java.util.Calendar;

import org.junit.Test;
import org.openl.rules.mapping.to.xmlbeans.PurchaseOrderTO;
import org.openuri.easypo.Customer;
import org.openuri.easypo.LineItem;
import org.openuri.easypo.PurchaseOrderDocument.PurchaseOrder;

public class CustomConvertersSupportTest {

    @Test
    public void defaultConverterTest1() {
        File source = new File("src/test/resources/org/openl/rules/mapping/DefaultCustomConvertersTest.xlsx");
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
        File source = new File("src/test/resources/org/openl/rules/mapping/CustomConvertersTest.xlsx");
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
