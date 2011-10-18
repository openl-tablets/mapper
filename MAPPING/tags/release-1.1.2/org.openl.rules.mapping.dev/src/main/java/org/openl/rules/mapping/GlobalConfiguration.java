package org.openl.rules.mapping;

/**
 * The class that holds information about mapping options.
 */
public class GlobalConfiguration {

    private Boolean mapNulls;
    private Boolean mapEmptyStrings;
    private Boolean trimStrings;
    private Boolean requiredFields;
    private Boolean wildcard;
    private String dateFormat;

    public Boolean getMapNulls() {
        return mapNulls;
    }

    public void setMapNulls(Boolean mapNulls) {
        this.mapNulls = mapNulls;
    }

    public Boolean getMapEmptyStrings() {
        return mapEmptyStrings;
    }

    public void setMapEmptyStrings(Boolean mapEmptyStrings) {
        this.mapEmptyStrings = mapEmptyStrings;
    }

    public Boolean getTrimStrings() {
        return trimStrings;
    }

    public void setTrimStrings(Boolean trimStrings) {
        this.trimStrings = trimStrings;
    }

    public Boolean getRequiredFields() {
        return requiredFields;
    }

    public void setRequiredFields(Boolean requiredFields) {
        this.requiredFields = requiredFields;
    }

    public Boolean getWildcard() {
        return wildcard;
    }

    public void setWildcard(Boolean wildcard) {
        this.wildcard = wildcard;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

}
