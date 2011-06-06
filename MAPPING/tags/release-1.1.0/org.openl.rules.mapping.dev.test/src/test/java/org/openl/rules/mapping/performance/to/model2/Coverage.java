package org.openl.rules.mapping.performance.to.model2;

public class Coverage {

    private Integer limit;
    private Integer deductible;
    private Double pctDeductible;
    private Double pctCoverage;

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getDeductible() {
        return deductible;
    }

    public void setDeductible(Integer deductible) {
        this.deductible = deductible;
    }

    public Double getPctDeductible() {
        return pctDeductible;
    }

    public void setPctDeductible(Double pctDeductible) {
        this.pctDeductible = pctDeductible;
    }

    public Double getPctCoverage() {
        return pctCoverage;
    }

    public void setPctCoverage(Double pctCoverage) {
        this.pctCoverage = pctCoverage;
    }

}
