package org.openl.rules.mapping.performance.to.model1;

import java.util.Date;

public class PolicySummary extends UniqueBaseEntity {

    private String policyNumber;
    private Term contractTerm;
    private Date transactionDate;
    private Date inceptionDate;
    private String LOB;
    private String policyTitle;
    private PolicyStatus policyStatusCd;
    private PolicyDetail policyDetail;

    public PolicyDetail getPolicyDetail() {
        return policyDetail;
    }

    public void setPolicyDetail(PolicyDetail policyDetail) {
        this.policyDetail = policyDetail;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public Term getContractTerm() {
        return contractTerm;
    }

    public void setContractTerm(Term contractTerm) {
        this.contractTerm = contractTerm;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Date getInceptionDate() {
        return inceptionDate;
    }

    public void setInceptionDate(Date inceptionDate) {
        this.inceptionDate = inceptionDate;
    }

    public String getLOB() {
        return LOB;
    }

    public void setLOB(String lob) {
        LOB = lob;
    }

    public String getPolicyTitle() {
        return policyTitle;
    }

    public void setPolicyTitle(String policyTitle) {
        this.policyTitle = policyTitle;
    }

    public PolicyStatus getPolicyStatusCd() {
        return policyStatusCd;
    }

    public void setPolicyStatusCd(PolicyStatus policyStatusCd) {
        this.policyStatusCd = policyStatusCd;
    }

}
