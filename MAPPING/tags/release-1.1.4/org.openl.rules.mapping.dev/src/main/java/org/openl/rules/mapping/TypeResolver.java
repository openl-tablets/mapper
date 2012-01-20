package org.openl.rules.mapping;

/**
 * Intended for internal use only.
 * 
 * Provides method to resolve type using its name.
 * 
 * In case of using class name with package we can resolve type using
 * appropriate {@link ClassLoader} instance. In case of using class name without
 * package type should be resolved by OpenL engine which has required
 * information about loaded class and can provide appropriate {@link Class}
 * object.
 */
public interface TypeResolver {

    /**
     * Finds class using its name.
     * 
     * @param name class name string
     * @return {@link Class} object if it was found; <code>null</code> -
     *         otherwise
     */
    Class<?> findClass(String name);
}
