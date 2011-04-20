package org.openl.rules.mapping;

import org.openl.conf.IOpenLConfiguration;
import org.openl.types.IOpenClass;

/**
 * Resolves types using OpenL engine which has required information about loaded
 * class and can provide appropriate {@link Class} object.
 * 
 */
public final class RulesTypeResolver implements TypeResolver {

    private static final String DEFAULT_OPENL_NAMESPACE = "org.openl.this";
    
    private IOpenLConfiguration openLConfiguration;

    public RulesTypeResolver(IOpenLConfiguration openLConfiguration) {
        this.openLConfiguration = openLConfiguration;
    }
    
    public Class<?> findClass(String name) {
        IOpenClass openClass = openLConfiguration.getType(DEFAULT_OPENL_NAMESPACE, name);
        
        if (openClass != null) {
            return openClass.getInstanceClass();
        }
        
        return null;
    }
    
}
