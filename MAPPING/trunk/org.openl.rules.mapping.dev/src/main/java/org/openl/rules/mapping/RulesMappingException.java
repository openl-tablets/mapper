package org.openl.rules.mapping;

/**
 * Class defines base runtime exception which is used by rules mapper.
 */
public class RulesMappingException extends RuntimeException {

    private static final long serialVersionUID = -3000149847274960984L;

    public RulesMappingException(String message) {
        super(message);
    }

    public RulesMappingException(String message, Throwable exception) {
        super(message, exception);
    }

    public RulesMappingException(Throwable cause) {
        super(cause);
    }

}
