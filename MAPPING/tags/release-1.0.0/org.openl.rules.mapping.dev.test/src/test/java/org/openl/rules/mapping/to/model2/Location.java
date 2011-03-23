package org.openl.rules.mapping.to.model2;

public class Location {

    private long locationId;
    private String country;
    private String city;
    private int zip;
    private Integer protectionClass;
    private String constructionType;
    private Coverage coverageA;
    private Coverage coverageB;
    private Coverage coverageEQ;
    private Integer increasedEnsuingFungiPercent;
    private String paymentBasisForCoverageA;
    private String paymentBasisForCoverageB;
    private String territoryCode;

    public long getLocationId() {
        return locationId;
    }

    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public Integer getProtectionClass() {
        return protectionClass;
    }

    public void setProtectionClass(Integer protectionClass) {
        this.protectionClass = protectionClass;
    }

    public String getConstructionType() {
        return constructionType;
    }

    public void setConstructionType(String constructionType) {
        this.constructionType = constructionType;
    }

    public Coverage getCoverageA() {
        return coverageA;
    }

    public void setCoverageA(Coverage coverageA) {
        this.coverageA = coverageA;
    }

    public Coverage getCoverageB() {
        return coverageB;
    }

    public void setCoverageB(Coverage coverageB) {
        this.coverageB = coverageB;
    }

    public Coverage getCoverageEQ() {
        return coverageEQ;
    }

    public void setCoverageEQ(Coverage coverageEQ) {
        this.coverageEQ = coverageEQ;
    }

    public Integer getIncreasedEnsuingFungiPercent() {
        return increasedEnsuingFungiPercent;
    }

    public void setIncreasedEnsuingFungiPercent(Integer increasedEnsuingFungiPercent) {
        this.increasedEnsuingFungiPercent = increasedEnsuingFungiPercent;
    }

    public String getPaymentBasisForCoverageA() {
        return paymentBasisForCoverageA;
    }

    public void setPaymentBasisForCoverageA(String paymentBasisForCoverageA) {
        this.paymentBasisForCoverageA = paymentBasisForCoverageA;
    }

    public String getPaymentBasisForCoverageB() {
        return paymentBasisForCoverageB;
    }

    public void setPaymentBasisForCoverageB(String paymentBasisForCoverageB) {
        this.paymentBasisForCoverageB = paymentBasisForCoverageB;
    }

    public String getTerritoryCode() {
        return territoryCode;
    }

    public void setTerritoryCode(String territoryCode) {
        this.territoryCode = territoryCode;
    }

}
