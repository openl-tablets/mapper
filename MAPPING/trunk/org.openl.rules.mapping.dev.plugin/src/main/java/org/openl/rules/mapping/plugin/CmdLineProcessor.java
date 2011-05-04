package org.openl.rules.mapping.plugin;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class that encapsulate command line processing.
 */
class CmdLineProcessor {

    private static final Log LOG = LogFactory.getLog(CmdLineProcessor.class);

    private static final String JAR_PATH_OPTION_LONG_NAME = "jar-path";
    private static final String JAR_PATH_OPTION_NAME = "jp";

    private static final String OUTPUT_XML_PATH_OPTION_LONG_NAME = "output-xml";
    private static final String OUTPUT_XML_PATH_OPTION_NAME = "o";

    private static final String TYPE_EXPORT_OPTION_LONG_NAME = "export-types";
    private static final String TYPE_EXPORT_OPTION_NAME = "t";

    private static final String MSG_EXPORT_OPTION_LONG_NAME = "export-messages";
    private static final String MSG_EXPORT_OPTION_NAME = "m";

    private static final String HELP_OPTION_LONG_NAME = "help";
    private static final String HELP_OPTION_NAME = "h";

    private Options cmdOptions;

    /**
     * Parses command line parameters.
     * 
     * @param args command line parameters
     * @return command line arguments and options
     */
    public CmdLineArgs parse(String[] args) {

        CmdLineArgs cmdArgs = new CmdLineArgs();

        // Process command line arguments
        try {
            CommandLineParser cmdLineParser = new GnuParser();
            Options cmdOptions = getCmdOptions();

            CommandLine commandLine = cmdLineParser.parse(cmdOptions, args);

            if (commandLine.hasOption(JAR_PATH_OPTION_NAME)) {
                cmdArgs.setJarpath(commandLine.getOptionValue(JAR_PATH_OPTION_NAME));
            }
            if (commandLine.hasOption(OUTPUT_XML_PATH_OPTION_NAME)) {
                cmdArgs.setOutpath(commandLine.getOptionValue(OUTPUT_XML_PATH_OPTION_NAME));
            }

            cmdArgs.setHelp(commandLine.hasOption(HELP_OPTION_NAME));
            cmdArgs.setExportTypes(commandLine.hasOption(TYPE_EXPORT_OPTION_NAME));
            cmdArgs.setExportMessages(commandLine.hasOption(MSG_EXPORT_OPTION_NAME));

            if (commandLine.getArgs().length == 0) {
                throw new RuntimeException("Source file is not defined");
            }

            if (commandLine.getArgs().length > 1) {
                throw new RuntimeException("Cannot resolve source file");
            }

            cmdArgs.setSourcepath(commandLine.getArgs()[0]);
        } catch (ParseException parseException) {
            LOG.error("Encountered exception while parsing command line arguments", parseException);
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug(cmdArgs.toString());
        }

        return cmdArgs;
    }

    @SuppressWarnings("static-access")
    private Options getCmdOptions() {

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

            Option exportTypes = OptionBuilder.withDescription("export types")
                .withLongOpt(TYPE_EXPORT_OPTION_LONG_NAME)
                .create(TYPE_EXPORT_OPTION_NAME);

            Option exportMessages = OptionBuilder.withDescription("export errors and warinings")
                .withLongOpt(MSG_EXPORT_OPTION_LONG_NAME)
                .create(MSG_EXPORT_OPTION_NAME);

            cmdOptions = new Options();
            cmdOptions.addOption(out);
            cmdOptions.addOption(jarpath);
            cmdOptions.addOption(exportTypes);
            cmdOptions.addOption(exportMessages);
            cmdOptions.addOption(help);
        }

        return cmdOptions;
    }

    /**
     * Prints usage topic.
     */
    public void printUsage() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java -jar org.openl.rules.mapping.dev.plugin-<version>.jar <openl_project_source> [options]",
            getCmdOptions());
    }

}
