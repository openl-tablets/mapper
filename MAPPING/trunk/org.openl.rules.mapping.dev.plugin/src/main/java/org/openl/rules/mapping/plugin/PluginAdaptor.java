package org.openl.rules.mapping.plugin;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openl.CompiledOpenClass;
import org.openl.binding.impl.module.ModuleOpenClass;
import org.openl.rules.mapping.RulesBeanMapperFactory;
import org.openl.rules.mapping.plugin.classpath.AdaptorClassLoader;
import org.openl.rules.mapping.plugin.serialize.ClassSerializer;
import org.openl.rules.mapping.plugin.serialize.xml.XmlWriter;
import org.openl.rules.mapping.plugin.util.AdaptorUtils;
import org.openl.rules.runtime.ApiBasedRulesEngineFactory;
import org.openl.types.IOpenClass;
import org.openl.types.java.OpenClassHelper;

// TODO: replace utils method to separate class.
public class PluginAdaptor {

    private static final Log LOG = LogFactory.getLog(PluginAdaptor.class);

    private static final String JAR_PATH_OPTION_LONG_NAME = "jar-path";
    private static final String JAR_PATH_OPTION_NAME = "jp";

    private static final String OUTPUT_XML_PATH_OPTION_LONG_NAME = "output-xml";
    private static final String OUTPUT_XML_PATH_OPTION_NAME = "o";

    private static final String HELP_OPTION_LONG_NAME = "help";
    private static final String HELP_OPTION_NAME = "h";

    private static final String FOLDER_PATH_DELIMITER = ";";
    private static final String JAR_FILE_EXTENSION = "jar";
    private static final String FILE_EXTENSION_DELIMITER = ".";
    private static final String JAR_FILE_SUFFIX = FILE_EXTENSION_DELIMITER + JAR_FILE_EXTENSION;

    private static Options cmdOptions = null;

    private String jarpath;
    private String outFilename;
    private String inputFilename;

    public PluginAdaptor(String jarpath, String outFilename, String inputFilename) {
        this.jarpath = jarpath;
        this.outFilename = outFilename;
        this.inputFilename = inputFilename;
    }

    public void process() {
        if (StringUtils.isBlank(inputFilename)) {
            LOG.error("Input source file has to be provided");
            return;
        }

        File source = new File(inputFilename);

        if (!source.exists() || !source.canRead()) {
            LOG.error(String.format("Input source file '%s' is not exist or cannot be read", source.getName()));
            return;
        }

        String[] foldersToScan = getFoldersToScan(jarpath);
        URL[] jarURLs = scanDirs(foldersToScan);

        ClassLoader originalClassLoader = Thread.currentThread().getContextClassLoader();
        AdaptorClassLoader cl = new AdaptorClassLoader(jarURLs, originalClassLoader);

        Thread.currentThread().setContextClassLoader(cl);

        ApiBasedRulesEngineFactory engine = RulesBeanMapperFactory.initEngine(source, false);
        CompiledOpenClass compiledOpenClass = engine.getCompiledOpenClass();
        ModuleOpenClass openClass = (ModuleOpenClass)compiledOpenClass.getOpenClassWithErrors();
        Collection<IOpenClass> values = openClass.getTypes().values();
        IOpenClass[] internalTypes = values.toArray(new IOpenClass[values.size()]);
        
        List<Class<?>> classes = new ArrayList<Class<?>>();
        classes.addAll(Arrays.asList(OpenClassHelper.getInstanceClasses(internalTypes)));
        classes.addAll(AdaptorUtils.loadClassesFromJars(jarURLs, cl));
        
        OutputStream out = null;
        if (StringUtils.isNotBlank(outFilename)) {
            try {
                out = new FileOutputStream(outFilename);
            } catch (FileNotFoundException e) {
                LOG.error(String.format("File '%s' is not found", outFilename), e);
            }
        } 

        if (out == null) {
            out = System.out;
        }
        
        try {
            new XmlWriter().write(ClassSerializer.serialize(classes), out);
        } catch (IOException e) {
            LOG.error(e);
        }
        
        Thread.currentThread().setContextClassLoader(originalClassLoader);
    }

    private URL[] scanDirs(String[] pathes) {
        List<URL> urls = new ArrayList<URL>();

        for (String path : pathes) {
            urls.addAll(scanDir(path));
        }

        return urls.toArray(new URL[urls.size()]);
    }

    private List<URL> scanDir(String path) {
        File file = new File(path);

        // Check that file is exist.
        if (!file.exists()) {
            LOG.error(String.format("File '%s' is not exist", file.getName()));
        }

        // Check that file is directory.
        if (!file.isDirectory()) {
            LOG.error(String.format("File '%s' is not a directory", file.getName()));
        }

        // Select jar files from folder.
        File[] jarFiles = file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File arg) {
                return arg.isFile() && arg.getName().endsWith(JAR_FILE_SUFFIX);
            }
        });

        List<URL> jarURLs = new ArrayList<URL>(jarFiles.length);

        for (File jar : jarFiles) {
            try {
                URL url = jar.toURI().toURL();
                jarURLs.add(url);
                LOG.debug(String.format("Loaded file: %s", url));
            } catch (MalformedURLException e) {
                LOG.error(String.format("File '%s' is invalid", file.getName()), e);
            }
        }

        return jarURLs;
    }

    private String[] getFoldersToScan(String folders) {
        if (StringUtils.isNotBlank(folders)) {
            return folders.split(FOLDER_PATH_DELIMITER);
        }

        return new String[0];
    }

    public static void main(String[] args) {
        String jarpath = null;
        String outpath = null;
        String sourcepath = null;

        // Process command line arguments
        //
        try {
            CommandLineParser cmdLineParser = new GnuParser();
            Options cmdOptions = getCmdOptions();

            CommandLine commandLine = cmdLineParser.parse(cmdOptions, args);

            if (commandLine.hasOption(HELP_OPTION_NAME)) {
                printUsage();
                return;
            }

            if (commandLine.hasOption(JAR_PATH_OPTION_NAME)) {
                jarpath = commandLine.getOptionValue(JAR_PATH_OPTION_NAME);
            }
            if (commandLine.hasOption(OUTPUT_XML_PATH_OPTION_NAME)) {
                outpath = commandLine.getOptionValue(OUTPUT_XML_PATH_OPTION_NAME);
            }

            String[] cmdArgs = commandLine.getArgs();

            if (cmdArgs.length != 1) {
                throw new RuntimeException("Source file is not defined");
            }

            sourcepath = cmdArgs[0];
        } catch (ParseException parseException) {
            System.err.println("Encountered exception while parsing command line arguments:\n" + parseException.getMessage());
        }

        // Create new instance of adaptor.
        PluginAdaptor adaptor = new PluginAdaptor(jarpath, outpath, sourcepath);
        adaptor.process();
    }

    @SuppressWarnings("static-access")
    private static Options getCmdOptions() {

        if (cmdOptions == null) {
            Option out = OptionBuilder.withDescription("output xml file path")
                .withArgName("path")
                .hasArg()
                .withLongOpt(OUTPUT_XML_PATH_OPTION_LONG_NAME)
                .create(OUTPUT_XML_PATH_OPTION_NAME);

            Option jarpath = OptionBuilder.withDescription("folders list which will be scanned to find jar files")
                .withArgName("path")
                .hasArg()
                .withLongOpt(JAR_PATH_OPTION_LONG_NAME)
                .create(JAR_PATH_OPTION_NAME);

            Option help = OptionBuilder.withDescription("print current message")
                .withLongOpt(HELP_OPTION_LONG_NAME)
                .create(HELP_OPTION_NAME);

            cmdOptions = new Options();
            cmdOptions.addOption(out);
            cmdOptions.addOption(jarpath);
            cmdOptions.addOption(help);
        }

        return cmdOptions;
    }

    private static void printUsage() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java -jar org.openl.rules.mapping.dev.plugin-<version>.jar <openl_project_source> [options]", getCmdOptions());
    }
}
