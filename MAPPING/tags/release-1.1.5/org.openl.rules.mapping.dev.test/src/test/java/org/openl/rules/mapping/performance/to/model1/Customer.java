package org.openl.rules.mapping.performance.to.model1;

public class Customer extends BaseEntity {

    private java.lang.Integer version;
    private java.lang.String customerNumber;
    private AddressEntity address;
    private java.lang.String emailAddress;
    private java.util.Date birthdate;
    private java.lang.String genderCd;
    private java.lang.String maritalStatusCd;
    private java.util.Date customerSince;
    private java.util.List<CustomerIndividual> individuals;
    public java.lang.Integer getVersion() {
        return version;
    }
    public void setVersion(java.lang.Integer version) {
        this.version = version;
    }
    public java.lang.String getCustomerNumber() {
        return customerNumber;
    }
    public void setCustomerNumber(java.lang.String customerNumber) {
        this.customerNumber = customerNumber;
    }
    public AddressEntity getAddress() {
        return address;
    }
    public void setAddress(AddressEntity address) {
        this.address = address;
    }
    public java.lang.String getEmailAddress() {
        return emailAddress;
    }
    public void setEmailAddress(java.lang.String emailAddress) {
        this.emailAddress = emailAddress;
    }
    public java.util.Date getBirthdate() {
        return birthdate;
    }
    public void setBirthdate(java.util.Date birthdate) {
        this.birthdate = birthdate;
    }
    public java.lang.String getGenderCd() {
        return genderCd;
    }
    public void setGenderCd(java.lang.String genderCd) {
        this.genderCd = genderCd;
    }
    public java.lang.String getMaritalStatusCd() {
        return maritalStatusCd;
    }
    public void setMaritalStatusCd(java.lang.String maritalStatusCd) {
        this.maritalStatusCd = maritalStatusCd;
    }
    public java.util.Date getCustomerSince() {
        return customerSince;
    }
    public void setCustomerSince(java.util.Date customerSince) {
        this.customerSince = customerSince;
    }
    public java.util.List<CustomerIndividual> getIndividuals() {
        return individuals;
    }
    public void setIndividuals(java.util.List<CustomerIndividual> individuals) {
        this.individuals = individuals;
    }
    
    
}
