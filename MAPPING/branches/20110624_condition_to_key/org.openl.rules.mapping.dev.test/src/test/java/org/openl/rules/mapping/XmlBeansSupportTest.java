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

public class XmlBeansSupportTest {

    @Test
    public void testBeanFactory() {
        File source = new File("src/test/resources/org/openl/rules/mapping/xmlbeans/XmlBeansSupportTest.xlsx");
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
        item2.setDescription("line2");
        item2.setPrice(BigDecimal.valueOf(5.1));

        purchaseOrder.setLineItemArray(new LineItem[]{item1, item2});
        
        PurchaseOrderTO result = mapper.map(purchaseOrder, PurchaseOrderTO.class);
        
        assertEquals(currentDate.getTime(), result.getDatetime());
        assertEquals("customer name", result.getCustomer().getName());
        assertEquals("customer address", result.getCustomer().getAddress());
        assertEquals(21, result.getCustomer().getAge());
        
        assertEquals(2, result.getLineItems().size());
        assertEquals("line1", result.getLineItems().get(0).getDescription());
        assertEquals(BigDecimal.valueOf(10.5), result.getLineItems().get(0).getPrice());
        
        assertEquals("line2", result.getLineItems().get(1).getDescription());
        assertEquals(BigDecimal.valueOf(5.1), result.getLineItems().get(1).getPrice());

        PurchaseOrder result1 = PurchaseOrder.Factory.newInstance();
        result1.addNewCustomer();

        mapper.map(result, result1);

        assertEquals(currentDate.getTime(), result1.getDate().getTime());
        assertEquals("customer name", result1.getCustomer().getName());
        assertEquals("customer address", result1.getCustomer().getAddress());
        assertEquals(21, result1.getCustomer().getAge());

        
        assertEquals(2, result1.getLineItemArray().length);
        assertEquals("line1", result1.getLineItemArray()[0].getDescription());
        assertEquals(BigDecimal.valueOf(10.5), result1.getLineItemArray()[0].getPrice());
        
        assertEquals("line2", result1.getLineItemArray()[1].getDescription());
        assertEquals(BigDecimal.valueOf(5.1), result1.getLineItemArray()[1].getPrice());
    }
    
}
