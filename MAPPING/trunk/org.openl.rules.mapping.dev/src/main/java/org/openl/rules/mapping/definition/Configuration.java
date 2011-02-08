package org.openl.rules.mapping.definition;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.openl.rules.mapping.MappingConstants;

/**
 * A bean that holds information about global configuration.
 */
public class Configuration {

    private boolean mapNulls = MappingConstants.DEFAULT_MAP_NULL_POLICY;
    private boolean mapEmptyStrings = MappingConstants.DEFAULT_MAP_EMPTY_STRING_POLICY;
    private boolean trimStrings = MappingConstants.DEFAULT_TRIM_STRINGS_POLICY;
    private boolean requiredFields = MappingConstants.DEFAULT_FIELD_REQUIRED_POLICY;
    private boolean wildcard = MappingConstants.DEFAULT_WILDCARD_POLICY;
    private String dateFormat;

    public boolean isMapNulls() {
        return mapNulls;
    }

    public void setMapNulls(boolean mapNulls) {
        this.mapNulls = mapNulls;
    }

    public boolean isMapEmptyStrings() {
        return mapEmptyStrings;
    }

    public void setMapEmptyStrings(boolean mapEmptyStrings) {
        this.mapEmptyStrings = mapEmptyStrings;
    }

    public boolean isTrimStrings() {
        return trimStrings;
    }

    public void setTrimStrings(boolean trimStrings) {
        this.trimStrings = trimStrings;
    }

    public boolean isRequiredFields() {
        return requiredFields;
    }

    public void setRequiredFields(boolean requiredFields) {
        this.requiredFields = requiredFields;
    }

    public boolean isWildcard() {
        return wildcard;
    }

    public void setWildcard(boolean wildcard) {
        this.wildcard = wildcard;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("mapNulls", mapNulls).append(
            "mapEmptyStrings", mapEmptyStrings).append("trimStrings", trimStrings).append("requiredFields",
            requiredFields).append("wildcard", wildcard).append("dateFormat", dateFormat).toString();
    }

}
