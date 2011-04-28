package org.openl.rules.mapping.plugin.classpath;

import java.net.URL;

/**
 * A class loader of plugin adaptor.
 */
public class AdaptorClassLoader extends JarClassLoader {

    public AdaptorClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

}
