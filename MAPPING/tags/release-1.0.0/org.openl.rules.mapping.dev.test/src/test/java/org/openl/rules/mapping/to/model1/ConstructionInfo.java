package org.openl.rules.mapping.to.model1;

public class ConstructionInfo extends BaseEntity {

    private String constructionTypeCd;
    private int yearBuilt;

    public String getConstructionTypeCd() {
        return constructionTypeCd;
    }

    public void setConstructionTypeCd(String constructionTypeCd) {
        this.constructionTypeCd = constructionTypeCd;
    }

    public int getYearBuilt() {
        return yearBuilt;
    }

    public void setYearBuilt(int yearBuilt) {
        this.yearBuilt = yearBuilt;
    }

}
