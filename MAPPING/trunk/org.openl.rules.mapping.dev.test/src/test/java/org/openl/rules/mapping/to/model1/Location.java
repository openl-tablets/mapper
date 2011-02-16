package org.openl.rules.mapping.to.model1;

public class Location extends UniqueBaseEntity {

    private java.lang.String locationName;
    private AddressEntity addressEntity;
    private java.lang.String riskLocationCd;
    private java.lang.String description;

    public java.lang.String getLocationName() {
        return locationName;
    }

    public void setLocationName(java.lang.String locationName) {
        this.locationName = locationName;
    }

    public AddressEntity getAddressEntity() {
        return addressEntity;
    }

    public void setAddressEntity(AddressEntity addressEntity) {
        this.addressEntity = addressEntity;
    }

    public java.lang.String getRiskLocationCd() {
        return riskLocationCd;
    }

    public void setRiskLocationCd(java.lang.String riskLocationCd) {
        this.riskLocationCd = riskLocationCd;
    }

    public java.lang.String getDescription() {
        return description;
    }

    public void setDescription(java.lang.String description) {
        this.description = description;
    }

}
