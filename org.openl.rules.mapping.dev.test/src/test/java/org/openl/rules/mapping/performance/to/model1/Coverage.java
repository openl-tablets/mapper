package org.openl.rules.mapping.performance.to.model1;

import java.math.BigDecimal;

public class Coverage extends BaseEntity {

    private BigDecimal limitAmount;
    private String limitValuationCd;

    public BigDecimal getLimitAmount() {
        return limitAmount;
    }

    public void setLimitAmount(BigDecimal limitAmount) {
        this.limitAmount = limitAmount;
    }

    public String getLimitValuationCd() {
        return limitValuationCd;
    }

    public void setLimitValuationCd(String limitValuationCd) {
        this.limitValuationCd = limitValuationCd;
    }

}
