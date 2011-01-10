package org.openl.rules.mapping;

/**
 * Class defines base runtime exception which is used by rules mapper.
 */
public class RulesMappingException extends RuntimeException {

    public RulesMappingException(String message) {
        super(message);
    }

    public RulesMappingException(String message, Throwable exception) {
        super(message, exception);
    }

}
