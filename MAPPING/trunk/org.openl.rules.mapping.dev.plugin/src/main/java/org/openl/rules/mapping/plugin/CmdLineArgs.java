package org.openl.rules.mapping.plugin;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

class CmdLineArgs {

    private boolean exportTypes;
    private boolean exportMessages;
    private String typesXmlPath;
    private boolean help;
    private String jarpath;
    private String outpath;
    private String sourcepath;
    private boolean quietReflectionErrors;
    private boolean generateTypes;

    public boolean hasHelpOption() {
        return help;
    }

    public void setHelp(boolean help) {
        this.help = help;
    }

    public String getJarpath() {
        return jarpath;
    }

    public void setJarpath(String jarpath) {
        this.jarpath = jarpath;
    }

    public String getOutpath() {
        return outpath;
    }

    public void setOutpath(String outpath) {
        this.outpath = outpath;
    }

    public String getSourcepath() {
        return sourcepath;
    }

    public void setSourcepath(String sourcepath) {
        this.sourcepath = sourcepath;
    }

    public boolean hasExportTypesOption() {
        return exportTypes;
    }

    public void setExportTypes(boolean exportTypes) {
        this.exportTypes = exportTypes;
    }

    public boolean hasExportMessagesOption() {
        return exportMessages;
    }

    public void setExportMessages(boolean exportMessages) {
        this.exportMessages = exportMessages;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("exportTypes", exportTypes)
            .append("exportTypes", exportTypes)
            .append("exportMessages", exportMessages)
            .append("help", help)
            .append("sourcepath", sourcepath)
            .append("outpath", outpath)
            .append("jarpath", jarpath)
            .toString();
    }

    public String getTypesXmlPath() {
        return typesXmlPath;
    }

    public void setTypesXmlPath(String typesXmlPath) {
        this.typesXmlPath = typesXmlPath;
    }

    public boolean isQuietReflectionErrors() {
        return quietReflectionErrors;
    }

    public void setQuietReflectionErrors(boolean quietReflectionErrors) {
        this.quietReflectionErrors = quietReflectionErrors;
    }

    public boolean isGenerateTypes() {
        return generateTypes;
    }

    public void setGenerateTypes(boolean generateTypes) {
        this.generateTypes = generateTypes;
    }
}
