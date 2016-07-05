package org.openl.rules.mapping.validation;

/**
 * Provides information about property which doesn't satisfy validation
 * constraints.
 */
public class PropertyConstraintViolation extends ConstraintViolation {

    private String propertyName;
    private Object invalidValue;

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public Object getInvalidValue() {
        return invalidValue;
    }

    public void setInvalidValue(Object invalidValue) {
        this.invalidValue = invalidValue;
    }

}
