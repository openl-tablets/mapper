package org.openl.rules.mapping.composite.mapid.source;

import java.util.List;

/**
 * @author kkachanovskiy@exigenservices.com
 *
 */
public class SrcPolicy {
    private String insuredName;
    private List<SrcLocation> srcLocations;

    public String getInsuredName() {
        return insuredName;
    }

    public void setInsuredName(String insuredName) {
        this.insuredName = insuredName;
    }

    public List<SrcLocation> getSrcLocations() {
        return srcLocations;
    }

    public void setSrcLocations(List<SrcLocation> srcLocations) {
        this.srcLocations = srcLocations;
    }
}
