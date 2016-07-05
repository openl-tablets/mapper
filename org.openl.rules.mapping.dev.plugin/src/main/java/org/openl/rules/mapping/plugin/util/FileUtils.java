package org.openl.rules.mapping.plugin.util;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    private FileUtils() {
    }

    /**
     * Lists files starting from root. If root file is null an empty list of
     * file will be returned.
     * 
     * @param root file to start
     * @param recursive
     * @param filter file filter
     * @return list of files
     * 
     * @see {@link java.io.File#listFiles(FileFilter)}
     */
    public static List<File> listFiles(File root, boolean recursive, FileFilter filter) {
        List<File> files = new ArrayList<File>();

        if (root == null) {
            return files;
        }
        
        if (filter == null || filter.accept(root)) {
            files.add(root);
        }

        files.addAll(internalListFiles(root, recursive, filter));

        return files;
    }

    /**
     * Internal method which lists files starting from root file.
     * 
     * @param root file to start
     * @param recursive
     * @param filter file filter
     * @return list of files
     */
    private static List<File> internalListFiles(File root, boolean recursive, FileFilter filter) {
        List<File> files = new ArrayList<File>();

        File[] rootFiles = root.listFiles();

        if (rootFiles != null) {
            for (File file : rootFiles) {
                if (filter == null || filter.accept(file)) {
                    files.add(file);
                }

                if (file.isDirectory() && recursive) {
                    files.addAll(internalListFiles(file, recursive, filter));
                }
            }
        }

        return files;
    }
}
