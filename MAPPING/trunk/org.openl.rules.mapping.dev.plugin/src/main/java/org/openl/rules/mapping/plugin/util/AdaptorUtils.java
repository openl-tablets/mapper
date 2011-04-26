package org.openl.rules.mapping.plugin.util;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import org.openl.util.Log;

public class AdaptorUtils {

    private AdaptorUtils() {
    }

    public static List<Class<?>> loadClassesFromJars(URL[] jars, ClassLoader cl) {
        List<Class<?>> classes = new ArrayList<Class<?>>();

        for (URL jar : jars) {
            try {
                classes.addAll(loadClassesFromJar(jar, cl));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return classes;
    }

    public static List<Class<?>> loadClassesFromJar(URL jar, ClassLoader cl) {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        JarInputStream jarFile = null;

        try {
            jarFile = new JarInputStream(jar.openStream());
        } catch (IOException e) {
            Log.error(String.format("File '%s' is not found or cannot be open", jar), e);
        }

        JarEntry jarEntry;

        if (jarFile != null) {
            while (true) {
                jarEntry = null;

                try {
                    jarEntry = jarFile.getNextJarEntry();
                } catch (IOException e) {
                    Log.error(String.format("An error occurred while open '%s'", jar), e);
                }

                if (jarEntry == null) {
                    break;
                }

                String entryName = jarEntry.getName();

                if (entryName.endsWith(".class")) {
                    String className = entryName.substring(0, entryName.length() - ".class".length()).replaceAll("/",
                        "\\.");

                    Class<?> clazz;
                    try {
                        clazz = cl.loadClass(className);
                        classes.add(clazz);
                    } catch (ClassNotFoundException e) {
                        Log.error(String.format("Class '%s' is not found", className), e);
                    }
                }
            }
        }

        return classes;
    }

}
