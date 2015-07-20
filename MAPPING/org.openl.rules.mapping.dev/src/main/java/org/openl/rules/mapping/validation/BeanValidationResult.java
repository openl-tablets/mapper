package org.openl.rules.mapping.validation;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.openl.validation.ValidationResult;
import org.openl.validation.ValidationStatus;

/**
 * Provides information about data bean validation.
 * 
 */
public class BeanValidationResult extends ValidationResult {

    /**
     * Validated bean type.
     */
    private Class<?> beanType;

    /**
     * Set of constraint violations.
     */
    private Set<ConstraintViolation> constraintViolations = new HashSet<ConstraintViolation>();

    public BeanValidationResult(ValidationStatus status, Class<?> beanType) {
        super(status);
        this.beanType = beanType;
    }

    public Class<?> getBeanType() {
        return beanType;
    }

    public Set<ConstraintViolation> getConstraintViolations() {
        return Collections.unmodifiableSet(constraintViolations);
    }

    public void addConstraintViolation(ConstraintViolation constraintViolation) {
        this.constraintViolations.add(constraintViolation);
    }

    public void addAllConstraintViolation(Collection<ConstraintViolation> constraintViolations) {
        this.constraintViolations.addAll(constraintViolations);
    }

}
