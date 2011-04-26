package org.openl.rules.mapping.plugin.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

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

    public static List<Class<?>> loadClassesFromJar(URL jar, ClassLoader cl) throws IOException, ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        JarInputStream jarFile = new JarInputStream(new FileInputStream(jar.getFile()));

        JarEntry jarEntry;

        while (true) {
            jarEntry = jarFile.getNextJarEntry();
            if (jarEntry == null) {
                break;
            }
            if (jarEntry.getName().endsWith(".class")) {
                String className = jarEntry.getName().substring(0, jarEntry.getName().length() - ".class".length())
                    .replaceAll("/", "\\.");
                Class<?> clazz = cl.loadClass(className);
                classes.add(clazz);
            }
        }

        return classes;
    }

}
