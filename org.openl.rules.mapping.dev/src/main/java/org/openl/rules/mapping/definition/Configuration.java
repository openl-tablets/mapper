package org.openl.rules.mapping.definition;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.openl.rules.mapping.MappingConstants;

/**
 * A bean that holds information about global configuration.
 */
public class Configuration {

    private Boolean mapNulls;
    private Boolean mapEmptyStrings;
    private Boolean trimStrings;
    private Boolean requiredFields;
    private Boolean wildcard;
    private String dateFormat;
	private String beanFactory;
	
    public boolean isMapNulls() {
        if (mapNulls != null) {
            return mapNulls;
        }
        return MappingConstants.DEFAULT_MAP_NULL_POLICY;
    }

    public void setMapNulls(Boolean mapNulls) {
        this.mapNulls = mapNulls;
    }

    public boolean isMapEmptyStrings() {
        if (mapEmptyStrings != null) {
            return mapEmptyStrings;
        }
        return MappingConstants.DEFAULT_MAP_EMPTY_STRING_POLICY;
    }

    public void setMapEmptyStrings(Boolean mapEmptyStrings) {
        this.mapEmptyStrings = mapEmptyStrings;
    }

    public boolean isTrimStrings() {
        if (trimStrings != null) {
            return trimStrings;
        }
        return MappingConstants.DEFAULT_TRIM_STRINGS_POLICY;
    }

    public void setTrimStrings(Boolean trimStrings) {
        this.trimStrings = trimStrings;
    }

    public boolean isRequiredFields() {
        if (requiredFields != null) {
            return requiredFields;
        }
        return MappingConstants.DEFAULT_FIELD_REQUIRED_POLICY;
    }

    public void setRequiredFields(Boolean requiredFields) {
        this.requiredFields = requiredFields;
    }

    public boolean isWildcard() {
        if (wildcard != null) {
            return wildcard;
        }
        return MappingConstants.DEFAULT_WILDCARD_POLICY;
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

	public String getBeanFactory() {
		return beanFactory;
	}

	public void setBeanFactory(String beanFactory) {
		this.beanFactory = beanFactory;
	}

	@Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("mapNulls", mapNulls).append(
		        "mapEmptyStrings", mapEmptyStrings).append("trimStrings", trimStrings).append("requiredFields",
            requiredFields).append("wildcard", wildcard).append("dateFormat", dateFormat).append("beanFactory", beanFactory).toString();
    }

}
