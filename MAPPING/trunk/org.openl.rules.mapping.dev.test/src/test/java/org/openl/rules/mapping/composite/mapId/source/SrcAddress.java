package org.openl.rules.mapping.composite.mapid.source;

/**
 * @author kkachanovskiy@exigenservices.com
 */
public class SrcAddress {
    private String zipCode;
    private String city;
    private String addrLine1;

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getAddrLine1() {
        return addrLine1;
    }

    public void setAddrLine1(String addrLine1) {
        this.addrLine1 = addrLine1;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
