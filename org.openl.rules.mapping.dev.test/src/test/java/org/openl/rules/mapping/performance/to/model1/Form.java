package org.openl.rules.mapping.performance.to.model1;

public class Form extends UniqueBaseEntity {

    private java.lang.String formCd;
    private java.lang.String formText;

    private java.util.List<FormParameter> parameters;

    public java.lang.String getFormCd() {
        return formCd;
    }

    public void setFormCd(java.lang.String formCd) {
        this.formCd = formCd;
    }

    public java.lang.String getFormText() {
        return formText;
    }

    public void setFormText(java.lang.String formText) {
        this.formText = formText;
    }

    public java.util.List<FormParameter> getParameters() {
        return parameters;
    }

    public void setParameters(java.util.List<FormParameter> parameters) {
        this.parameters = parameters;
    }

}
