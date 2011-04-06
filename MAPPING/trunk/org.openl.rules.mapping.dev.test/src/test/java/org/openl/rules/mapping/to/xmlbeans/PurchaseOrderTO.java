package org.openl.rules.mapping.to.xmlbeans;

import java.util.Date;
import java.util.List;

public class PurchaseOrderTO {

    private Date datetime;
    private CustomerTO customer;
    private List<LineItemTO> lineItems;
    private boolean hasSingleLineItem;

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public CustomerTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerTO customer) {
        this.customer = customer;
    }

    public List<LineItemTO> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<LineItemTO> lineItems) {
        this.lineItems = lineItems;
    }

    public boolean isHasSingleLineItem() {
        return hasSingleLineItem;
    }

    public void setHasSingleLineItem(boolean hasSingleLineItem) {
        this.hasSingleLineItem = hasSingleLineItem;
    }
    
}
