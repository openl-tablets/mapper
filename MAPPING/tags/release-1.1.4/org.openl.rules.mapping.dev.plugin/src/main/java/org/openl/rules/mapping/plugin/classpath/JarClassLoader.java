package org.openl.rules.mapping.plugin.classpath;

import java.io.IOException;

import java.net.URL;
import java.net.URLClassLoader;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class loader for loading jar files.
 * Adapted from the Java Tutorial.
 *
 * http://java.sun.com/docs/books/tutorial/jar/api/index.html
 */
public class JarClassLoader extends URLClassLoader {

    public JarClassLoader(URL[] urls) {
        super(urls);
    }

    public JarClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    /** 
     * Adds the jar file with the following url into the class loader. This can be 
     * a local or network resource.
     * 
     * @param url The url of the jar file i.e. http://www.xxx.yyy/jarfile.jar
     *            or file:c:\foo\lib\testbeans.jar
     */
    public void addJarFile(URL url)  {
        addURL(url);
    }

    /** 
     * Adds a jar file from the filesystems into the jar loader list.
     * 
     * @param jarfile The full path to the jar file.
     */
    public void addJarFile(String jarfile)  {
        try {
            URL url = new URL("file:" + jarfile);
            addURL(url);
        } catch (IOException ex) {
            Logger.getAnonymousLogger().log(Level.WARNING, "Error adding jar file", ex);
        }
    }
    
}
