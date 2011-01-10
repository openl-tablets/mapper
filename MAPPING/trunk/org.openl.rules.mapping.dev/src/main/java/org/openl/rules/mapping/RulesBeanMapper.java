package org.openl.rules.mapping;

/**
 * The OpenL Rules Tablets based public implementation of the {@link Mapper}
 * abstraction. Current implementation uses OpenL Rules engine as a mapping
 * definition tool and performs appropriate definitions processing before actual
 * bean mapping will be invoked.
 * 
 */
public class RulesBeanMapper implements Mapper {

    public <T> T map(Object source, Class<T> destination) {
        return null;
    }

    public void map(Object source, Object destination) {

    }

}
