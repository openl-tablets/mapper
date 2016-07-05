package org.openl.rules.mapping.plugin.util;

import java.io.File;
import java.io.FileFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.AntPathMatcher;

public class AdaptorUtils {

    private static final Log LOG = LogFactory.getLog(AdaptorUtils.class);

    private static final String FILTER_DELIMITER = "\\|";
    private static final String PATH_DELIMITER = ";";

    private static final String JAR_FILE_EXTENSION = "jar";
    private static final String FILE_EXTENSION_DELIMITER = ".";
    private static final String JAR_FILE_SUFFIX = FILE_EXTENSION_DELIMITER + JAR_FILE_EXTENSION;

    private AdaptorUtils() {
    }

    /**
     * Scans directories to find jar files.
     * 
     * @param pathes directory paths
     * @return array of jar urls
     */
    public static URL[] scanDirs(String[] pathes) {
        List<URL> urls = new ArrayList<URL>();

        for (String path : pathes) {
            String[] pathArgs = path.split(FILTER_DELIMITER);
            String dir = pathArgs[0];

            String filter = null;

            if (pathArgs.length > 1) {
                filter = pathArgs[1];
            }
            if (pathArgs.length > 2) {
                LOG.warn(String.format("Only one filter expression can be applyed to path: %s", path));
            }

            urls.addAll(scanDir(dir, filter));
        }

        return urls.toArray(new URL[urls.size()]);
    }

    /**
     * Scans directory to find jar files.
     * 
     * @param path directory path
     * @param filter filter expression
     * @return list of jar urls
     */
    public static List<URL> scanDir(String path, final String filter) {

        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("Scanning dir: %s, filter: %s", path, StringUtils.defaultString(filter, "null")));
        }

        File file = new File(path);

        // Check that file is exist.
        if (!file.exists()) {
            LOG.error(String.format("File '%s' is not exist", path));
            return new ArrayList<URL>(0);
        }

        // Check that file is directory.
        if (!file.isDirectory()) {
            LOG.error(String.format("File '%s' is not a directory", path));
            return new ArrayList<URL>(0);
        }

        FileFilter dirFilter = new AntPathDirFilter(filter);
        List<File> dirs = FileUtils.listFiles(file, true, dirFilter);

        // Select jar files from folder.
        List<File> jarFiles = new ArrayList<File>();
        FileFilter jarFilter = new JarFileFilter();

        for (File dir : dirs) {
            List<File> jars = FileUtils.listFiles(dir, false, jarFilter);
            jarFiles.addAll(jars);
        }

        List<URL> jarURLs = new ArrayList<URL>(jarFiles.size());

        for (File jar : jarFiles) {
            try {
                URL url = jar.toURI().toURL();
                jarURLs.add(url);
                LOG.debug(String.format("Found file: %s", url));
            } catch (MalformedURLException e) {
                LOG.error(String.format("File '%s' is invalid", file.getName()), e);
            }
        }

        return jarURLs;
    }

    /**
     * Splits paths string into array of paths.
     * 
     * @param paths paths string
     * @return array of paths
     */
    public static String[] getPaths(String paths) {
        if (StringUtils.isNotBlank(paths)) {
            return paths.split(PATH_DELIMITER);
        }

        return new String[0];
    }

    /**
     * The {@link FileFilter} implementation that filters directories using ant
     * matcher.
     * 
     * @see AntPathMatcher
     */
    private static class AntPathDirFilter implements FileFilter {

        private AntPathMatcher matcher = new AntPathMatcher();
        private String filter;

        public AntPathDirFilter(String filter) {
            this.filter = filter;

            matcher.setPathSeparator(System.getProperty("file.separator"));
        }

        @Override
        public boolean accept(File arg) {
            if (arg.isFile()) {
                return false;
            }

            if (StringUtils.isNotBlank(filter)) {
                String path = arg.toURI().getPath();
                return matcher.match(filter, path.substring(0, path.length() - 1));
            }

            return true;
        }

        @Override
        public String toString() {
            return "AntPathDirFilter [filter=" + filter + "]";
        }

    }

    /**
     * The {@link FileFilter} implementation that filters jar files.
     */
    private static class JarFileFilter implements FileFilter {

        @Override
        public boolean accept(File arg) {
            return arg.isFile() && arg.getName().endsWith(JAR_FILE_SUFFIX);
        }

        @Override
        public String toString() {
            return "JarFileFilter [filter=isFile && endWith(\".jar\")]";
        }

    }

}
