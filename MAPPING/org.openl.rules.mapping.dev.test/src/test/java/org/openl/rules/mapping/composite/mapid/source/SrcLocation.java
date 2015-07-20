package org.openl.rules.mapping.composite.mapid.source;

/**
 * @author kkachanovskiy@exigenservices.com
 */
public class SrcLocation {
    private Long id;
    private String description;
    private SrcAddress srcAddress;
    private String stateCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SrcAddress getSrcAddress() {
        return srcAddress;
    }

    public void setSrcAddress(SrcAddress srcAddress) {
        this.srcAddress = srcAddress;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }
}
