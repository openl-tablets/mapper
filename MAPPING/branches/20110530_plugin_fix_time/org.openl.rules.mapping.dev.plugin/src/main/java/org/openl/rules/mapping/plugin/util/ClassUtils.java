package org.openl.rules.mapping.plugin.util;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The class provides methods to work with classes.
 */
public class ClassUtils {

    private static final Log LOG = LogFactory.getLog(ClassUtils.class);

    private static final String CLASS_FILE_SUFFIX = ".class";

    private ClassUtils() {
    }

    /**
     * Loads classes form specified jars with given class loader. It's a help
     * method which invokes {@link #loadClassesFromJar(URL, ClassLoader)} method
     * to process specified jars.
     * 
     * @param jars urls of jars to process
     * @param cl class loader
     * @return list of loaded classes
     * 
     * @see loadClassesFromJar
     */
    public static List<Class<?>> loadClassesFromJars(URL[] jars, ClassLoader cl, boolean omitLogErrors) {
        List<Class<?>> classes = new ArrayList<Class<?>>();

        for (URL jar : jars) {
            try {
                classes.addAll(loadClassesFromJar(jar, cl, omitLogErrors));
            } catch (Exception e) {
                // ignore exception to load rest of classes
            }
        }

        return classes;
    }

    /**
     * Loads classes of jar with specified class loader.
     * 
     * @param jar jar to load
     * @param cl class loader
     * @return list of loaded classes
     */
    public static List<Class<?>> loadClassesFromJar(URL jar, ClassLoader cl, boolean omitLogErrors) {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        JarInputStream jarFile = null;

        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("Processing %s", jar));
        }

        try {
            jarFile = new JarInputStream(jar.openStream());
        } catch (IOException e) {
            LOG.error(String.format("File '%s' is not found or cannot be open", jar), e);
        }

        JarEntry jarEntry;

        if (jarFile != null) {
            while (true) {
                jarEntry = null;

                try {
                    jarEntry = jarFile.getNextJarEntry();
                } catch (IOException e) {
                    if (!omitLogErrors) {
                        LOG.error(String.format("An error occurred while open '%s'", jar), e);
                    }
                }

                if (jarEntry == null) {
                    break;
                }

                String entryName = jarEntry.getName();

                if (entryName.endsWith(CLASS_FILE_SUFFIX)) {
                    String className = entryName.substring(0, entryName.length() - CLASS_FILE_SUFFIX.length()).replaceAll("/",
                        "\\.");

                    if (LOG.isDebugEnabled()) {
                        LOG.debug(String.format("Loading '%s' class", className));
                    }

                    Class<?> clazz;
                    try {
                        clazz = cl.loadClass(className);
                        classes.add(clazz);
                    } catch (ClassNotFoundException e) {
                        if (!omitLogErrors) {
                            LOG.error(String.format("Class '%s' is not found", className), e);
                        }
                    } catch (Throwable e) {
                        if (!omitLogErrors) {
                            LOG.error(String.format("Class '%s' cannot be loaded", className), e);
                        }
                    }
                }
            }
        }

        return classes;
    }

}
