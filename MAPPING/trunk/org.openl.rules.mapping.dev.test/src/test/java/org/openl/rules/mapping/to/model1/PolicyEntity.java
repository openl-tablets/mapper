package org.openl.rules.mapping.to.model1;

import java.util.Map;

public class PolicyEntity extends PolicySummary {

    private Double productVersion;
    private Map<java.lang.String, ExtendedField> additionalFields;
    private Customer customer;

    public Double getProductVersion() {
        return productVersion;
    }

    public void setProductVersion(Double productVersion) {
        this.productVersion = productVersion;
    }

    public Map<java.lang.String, ExtendedField> getAdditionalFields() {
        return additionalFields;
    }

    public void setAdditionalFields(Map<java.lang.String, ExtendedField> additionalFields) {
        this.additionalFields = additionalFields;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

}
