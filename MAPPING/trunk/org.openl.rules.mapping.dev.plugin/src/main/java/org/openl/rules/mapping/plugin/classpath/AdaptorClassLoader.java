package org.openl.rules.mapping.plugin.classpath;

import java.net.URL;

public class AdaptorClassLoader extends JarClassLoader {

    public AdaptorClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

}
