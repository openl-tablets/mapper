package org.openl.rules.mapping.plugin;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openl.CompiledOpenClass;
import org.openl.binding.impl.module.ModuleOpenClass;
import org.openl.message.OpenLMessage;
import org.openl.rules.mapping.RulesBeanMapperFactory;
import org.openl.rules.mapping.plugin.classpath.AdaptorClassLoader;
import org.openl.rules.mapping.plugin.serialize.BeanEntry;
import org.openl.rules.mapping.plugin.serialize.ClassSerializer;
import org.openl.rules.mapping.plugin.serialize.MessageEntry;
import org.openl.rules.mapping.plugin.serialize.MessageSerializer;
import org.openl.rules.mapping.plugin.serialize.xml.XmlWriter;
import org.openl.rules.mapping.plugin.util.AdaptorUtils;
import org.openl.rules.mapping.plugin.util.ClassUtils;
import org.openl.rules.runtime.ApiBasedRulesEngineFactory;
import org.openl.types.IOpenClass;
import org.openl.types.java.OpenClassHelper;

/**
 * Provides methods to export OpenL specific information about mapping project.
 */
public class PluginAdaptor {

    private static final Log LOG = LogFactory.getLog(PluginAdaptor.class);

    private String jarpath;
    private String outFilename;
    private String inputFilename;
    private boolean exportTypes;
    private boolean exportMessages;
    private String typesXmlPath;
    private boolean quietReflectionErrors;
    private boolean generateTypes;

    private PluginAdaptor(String jarpath,
            String outFilename,
            String inputFilename,
            boolean exportTypes,
            boolean exportMessages,
            String typesXmlPath,
            boolean quietReflectionErrors,
            boolean generateTypes) {
        this.jarpath = jarpath;
        this.outFilename = outFilename;
        this.inputFilename = inputFilename;
        this.exportTypes = exportTypes;
        this.exportMessages = exportMessages;
        this.typesXmlPath = typesXmlPath;
        this.quietReflectionErrors = quietReflectionErrors;
        this.generateTypes = generateTypes;
    }

    /**
     * Processes mapping project.
     */
    public void process() {
        if (StringUtils.isBlank(inputFilename)) {
            throw new RuntimeException("Input source file has to be provided");
        }

        File source = new File(inputFilename);

        if (!source.exists() || !source.canRead()) {
            throw new RuntimeException(String.format("Input source file '%s' is not exist or cannot be read",
                source.getName()));
        }

        String[] foldersToScan = AdaptorUtils.getPaths(jarpath);
        URL[] jarURLs = AdaptorUtils.scanDirs(foldersToScan);

        // Replace current class loader with new one which has extended
        // classpath.
        ClassLoader originalClassLoader = Thread.currentThread().getContextClassLoader();
        AdaptorClassLoader cl = new AdaptorClassLoader(jarURLs, originalClassLoader);
        Thread.currentThread().setContextClassLoader(cl);
        OutputStream out = null;

        try {
            // Compile OpenL project.
            ApiBasedRulesEngineFactory engine = RulesBeanMapperFactory.initEngine(source, false);
            CompiledOpenClass compiledOpenClass = engine.getCompiledOpenClass();

            // Get info about types and compilation messages.
            if (generateTypes) {
                out = getOutputStream();
                writeTypes(out, compiledOpenClass, jarURLs, cl);
            } else if (exportTypes) {
                out = getOutputStream();
                if (typesXmlPath != null) {
                    copyPreparedXml(out);
                } else {
                    writeTypes(out, compiledOpenClass, jarURLs, cl);
                }
            } else if (exportMessages) {
                List<MessageEntry> messages = exportMessages(compiledOpenClass.getMessages());
                out = getOutputStream();
                write(out, null, messages);
            }

        } finally {
            // Return back original class loader.
            Thread.currentThread().setContextClassLoader(originalClassLoader);
            
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    LOG.error(e);
                }
            }
        }
    }

    private void writeTypes(OutputStream out, CompiledOpenClass compiledOpenClass, URL[] jarURLs, AdaptorClassLoader cl) {
        ModuleOpenClass openClass = (ModuleOpenClass) compiledOpenClass.getOpenClassWithErrors();
        List<BeanEntry> types = exportTypes(openClass, jarURLs, cl);
        write(out, types, null);
    }

    private void copyPreparedXml(OutputStream out) {
        try {
            IOUtils.copy(new FileInputStream(new File(typesXmlPath)), out);
        } catch (IOException e) {
            e.printStackTrace();
        };
    }

    private void write(OutputStream out, List<BeanEntry> types, List<MessageEntry> messages) {
        try {
            new XmlWriter().write(types, messages, out);
        } catch (IOException e) {
            LOG.error(e);
        }
    }

    private OutputStream getOutputStream() {
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

        return out;
    }

    /**
     * Exports types from compiled project into internal model.
     * 
     * @param openClass module open class
     * @param jarURLs jar files
     * @param cl class loader
     * @return bean entries
     */
    private List<BeanEntry> exportTypes(ModuleOpenClass openClass, URL[] jarURLs, ClassLoader cl) {
        // Get types what are defined in project in declarative way.
        Collection<IOpenClass> values = openClass.getTypes().values();
        IOpenClass[] internalTypes = values.toArray(new IOpenClass[values.size()]);

        // Composite required types.
        List<Class<?>> classes = new ArrayList<Class<?>>();
        classes.addAll(Arrays.asList(OpenClassHelper.getInstanceClasses(internalTypes)));
        classes.addAll(ClassUtils.loadClassesFromJars(jarURLs, cl, quietReflectionErrors));

        return ClassSerializer.serialize(classes, quietReflectionErrors);
    }

    /**
     * Exports compilation messages.
     * 
     * @param messages messages
     * @return message entries
     */
    private List<MessageEntry> exportMessages(List<OpenLMessage> messages) {
        return MessageSerializer.serialize(messages);
    }

    /**
     * Console application entry point.
     * 
     * @param args cmd args
     */
    public static void main(String[] args) {

        CmdLineProcessor cmdLineProcessor = new CmdLineProcessor();
        CmdLineArgs cmdLineArgs = cmdLineProcessor.parse(args);

        if (cmdLineArgs.hasHelpOption()) {
            cmdLineProcessor.printUsage();
        } else {
            // Create new instance of adaptor.
            PluginAdaptor adaptor = new PluginAdaptor(cmdLineArgs.getJarpath(),
                cmdLineArgs.getOutpath(),
                cmdLineArgs.getSourcepath(),
                cmdLineArgs.hasExportTypesOption(),
                cmdLineArgs.hasExportMessagesOption(),
                cmdLineArgs.getTypesXmlPath(),
                cmdLineArgs.isQuietReflectionErrors(),
                cmdLineArgs.isGenerateTypes());
            // Start process
            adaptor.process();
        }
    }

}
