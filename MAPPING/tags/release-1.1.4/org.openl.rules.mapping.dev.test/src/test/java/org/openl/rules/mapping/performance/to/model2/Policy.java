package org.openl.rules.mapping.performance.to.model2;

import java.util.Date;

public class Policy {

    private long correlationId;
    private Date effectiveDate;
    private Date originalDate;
    private int effectiveYear;
    private int customerPremium;
    private int insuredAmount;
    private int consecutiveYearsInsured;
    private int paidClaimsNum;
    private Boolean hasExclSpecEndorsement;
    private Boolean useHurricaneDeduction;
    private Boolean houseOrOtherPermanentStructuresEndorsmentExist;
    private Boolean isFineArtsExclusionCreditApplicable;
    private Boolean isLimitedAdditionalCoverageCreditApplicable;
    private Boolean isMiscClubAssessmentCreditApplicable;
    private Boolean isRefrigeratedFoodSpoilageSelected;
    private Boolean isResidenceRentalTheftExclusionSelected;
    private Boolean offPremisesTheftExclusion;
    private Boolean isSinkholeCollapseSelected;
    private Boolean businessPursuitEndorsement;
    private Boolean isLimitedResidenceLiabilitySelected;
    private Boolean isPremisesLiabilityLimitationSelected;
    private String policyType;
    private Location[] locations;
    private String commissions;
    private Boolean hasLiabilityPremiumFlatFactorAmount;
    private Boolean hasBasePremiumFlatFactorAmount;
    private double basePremiumMod;
    private double liabilityPremiumMod;
    public long getCorrelationId() {
        return correlationId;
    }
    public void setCorrelationId(long correlationId) {
        this.correlationId = correlationId;
    }
    public Date getEffectiveDate() {
        return effectiveDate;
    }
    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }
    public Date getOriginalDate() {
        return originalDate;
    }
    public void setOriginalDate(Date originalDate) {
        this.originalDate = originalDate;
    }
    public int getEffectiveYear() {
        return effectiveYear;
    }
    public void setEffectiveYear(int effectiveYear) {
        this.effectiveYear = effectiveYear;
    }
    public int getCustomerPremium() {
        return customerPremium;
    }
    public void setCustomerPremium(int customerPremium) {
        this.customerPremium = customerPremium;
    }
    public int getInsuredAmount() {
        return insuredAmount;
    }
    public void setInsuredAmount(int insuredAmount) {
        this.insuredAmount = insuredAmount;
    }
    public int getConsecutiveYearsInsured() {
        return consecutiveYearsInsured;
    }
    public void setConsecutiveYearsInsured(int consecutiveYearsInsured) {
        this.consecutiveYearsInsured = consecutiveYearsInsured;
    }
    public int getPaidClaimsNum() {
        return paidClaimsNum;
    }
    public void setPaidClaimsNum(int paidClaimsNum) {
        this.paidClaimsNum = paidClaimsNum;
    }
    public Boolean getHasExclSpecEndorsement() {
        return hasExclSpecEndorsement;
    }
    public void setHasExclSpecEndorsement(Boolean hasExclSpecEndorsement) {
        this.hasExclSpecEndorsement = hasExclSpecEndorsement;
    }
    public Boolean getUseHurricaneDeduction() {
        return useHurricaneDeduction;
    }
    public void setUseHurricaneDeduction(Boolean useHurricaneDeduction) {
        this.useHurricaneDeduction = useHurricaneDeduction;
    }
    public Boolean getHouseOrOtherPermanentStructuresEndorsmentExist() {
        return houseOrOtherPermanentStructuresEndorsmentExist;
    }
    public void setHouseOrOtherPermanentStructuresEndorsmentExist(Boolean houseOrOtherPermanentStructuresEndorsmentExist) {
        this.houseOrOtherPermanentStructuresEndorsmentExist = houseOrOtherPermanentStructuresEndorsmentExist;
    }
    public Boolean getIsFineArtsExclusionCreditApplicable() {
        return isFineArtsExclusionCreditApplicable;
    }
    public void setIsFineArtsExclusionCreditApplicable(Boolean isFineArtsExclusionCreditApplicable) {
        this.isFineArtsExclusionCreditApplicable = isFineArtsExclusionCreditApplicable;
    }
    public Boolean getIsLimitedAdditionalCoverageCreditApplicable() {
        return isLimitedAdditionalCoverageCreditApplicable;
    }
    public void setIsLimitedAdditionalCoverageCreditApplicable(Boolean isLimitedAdditionalCoverageCreditApplicable) {
        this.isLimitedAdditionalCoverageCreditApplicable = isLimitedAdditionalCoverageCreditApplicable;
    }
    public Boolean getIsMiscClubAssessmentCreditApplicable() {
        return isMiscClubAssessmentCreditApplicable;
    }
    public void setIsMiscClubAssessmentCreditApplicable(Boolean isMiscClubAssessmentCreditApplicable) {
        this.isMiscClubAssessmentCreditApplicable = isMiscClubAssessmentCreditApplicable;
    }
    public Boolean getIsRefrigeratedFoodSpoilageSelected() {
        return isRefrigeratedFoodSpoilageSelected;
    }
    public void setIsRefrigeratedFoodSpoilageSelected(Boolean isRefrigeratedFoodSpoilageSelected) {
        this.isRefrigeratedFoodSpoilageSelected = isRefrigeratedFoodSpoilageSelected;
    }
    public Boolean getIsResidenceRentalTheftExclusionSelected() {
        return isResidenceRentalTheftExclusionSelected;
    }
    public void setIsResidenceRentalTheftExclusionSelected(Boolean isResidenceRentalTheftExclusionSelected) {
        this.isResidenceRentalTheftExclusionSelected = isResidenceRentalTheftExclusionSelected;
    }
    public Boolean getOffPremisesTheftExclusion() {
        return offPremisesTheftExclusion;
    }
    public void setOffPremisesTheftExclusion(Boolean offPremisesTheftExclusion) {
        this.offPremisesTheftExclusion = offPremisesTheftExclusion;
    }
    public Boolean getIsSinkholeCollapseSelected() {
        return isSinkholeCollapseSelected;
    }
    public void setIsSinkholeCollapseSelected(Boolean isSinkholeCollapseSelected) {
        this.isSinkholeCollapseSelected = isSinkholeCollapseSelected;
    }
    public Boolean getBusinessPursuitEndorsement() {
        return businessPursuitEndorsement;
    }
    public void setBusinessPursuitEndorsement(Boolean businessPursuitEndorsement) {
        this.businessPursuitEndorsement = businessPursuitEndorsement;
    }
    public Boolean getIsLimitedResidenceLiabilitySelected() {
        return isLimitedResidenceLiabilitySelected;
    }
    public void setIsLimitedResidenceLiabilitySelected(Boolean isLimitedResidenceLiabilitySelected) {
        this.isLimitedResidenceLiabilitySelected = isLimitedResidenceLiabilitySelected;
    }
    public Boolean getIsPremisesLiabilityLimitationSelected() {
        return isPremisesLiabilityLimitationSelected;
    }
    public void setIsPremisesLiabilityLimitationSelected(Boolean isPremisesLiabilityLimitationSelected) {
        this.isPremisesLiabilityLimitationSelected = isPremisesLiabilityLimitationSelected;
    }
    public String getPolicyType() {
        return policyType;
    }
    public void setPolicyType(String policyType) {
        this.policyType = policyType;
    }
    public Location[] getLocations() {
        return locations;
    }
    public void setLocations(Location[] locations) {
        this.locations = locations;
    }
    public String getCommissions() {
        return commissions;
    }
    public void setCommissions(String commissions) {
        this.commissions = commissions;
    }
    public Boolean getHasLiabilityPremiumFlatFactorAmount() {
        return hasLiabilityPremiumFlatFactorAmount;
    }
    public void setHasLiabilityPremiumFlatFactorAmount(Boolean hasLiabilityPremiumFlatFactorAmount) {
        this.hasLiabilityPremiumFlatFactorAmount = hasLiabilityPremiumFlatFactorAmount;
    }
    public Boolean getHasBasePremiumFlatFactorAmount() {
        return hasBasePremiumFlatFactorAmount;
    }
    public void setHasBasePremiumFlatFactorAmount(Boolean hasBasePremiumFlatFactorAmount) {
        this.hasBasePremiumFlatFactorAmount = hasBasePremiumFlatFactorAmount;
    }
    public double getBasePremiumMod() {
        return basePremiumMod;
    }
    public void setBasePremiumMod(double basePremiumMod) {
        this.basePremiumMod = basePremiumMod;
    }
    public double getLiabilityPremiumMod() {
        return liabilityPremiumMod;
    }
    public void setLiabilityPremiumMod(double liabilityPremiumMod) {
        this.liabilityPremiumMod = liabilityPremiumMod;
    }
    
    
}
