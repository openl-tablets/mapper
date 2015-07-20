package org.openl.rules.mapping.performance.to.model1;

import java.util.List;

public class RiskItem extends UniqueBaseEntity {

    private int seqNo;
    private List<Form> forms;
    private Location location;
    private List<Coverage> coverages;
    private PremiumOverrideInfo premiumOverrideInfo;
    private double defaultDeductibleAmount;

    public double getDefaultDeductibleAmount() {
        return defaultDeductibleAmount;
    }

    public void setDefaultDeductibleAmount(double defaultDeductibleAmount) {
        this.defaultDeductibleAmount = defaultDeductibleAmount;
    }

    public List<Coverage> getCoverages() {
        return coverages;
    }

    public void setCoverages(List<Coverage> coverages) {
        this.coverages = coverages;
    }

    public int getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(int seqNo) {
        this.seqNo = seqNo;
    }

    public List<Form> getForms() {
        return forms;
    }

    public void setForms(List<Form> forms) {
        this.forms = forms;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public PremiumOverrideInfo getPremiumOverrideInfo() {
        return premiumOverrideInfo;
    }

    public void setPremiumOverrideInfo(PremiumOverrideInfo premiumOverrideInfo) {
        this.premiumOverrideInfo = premiumOverrideInfo;
    }

}
