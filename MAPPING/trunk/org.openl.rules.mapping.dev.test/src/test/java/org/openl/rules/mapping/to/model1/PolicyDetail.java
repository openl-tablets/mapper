package org.openl.rules.mapping.to.model1;

import java.util.List;

public class PolicyDetail extends BaseEntity {

    private List<RiskItem> riskItems;

    public List<RiskItem> getRiskItems() {
        return riskItems;
    }

    public void setRiskItems(List<RiskItem> riskItems) {
        this.riskItems = riskItems;
    }
    
}
