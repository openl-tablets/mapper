package org.openl.rules.mapping.composite.mapid.destination;

/**
 * @author kkachanovskiy@exigenservices.com
 *
 */
public class DestLocDTO {
    private String index;
    private String comment;

    private DestDTO destFields;

    private String voidField;

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public DestDTO getDestFields() {
        return destFields;
    }

    public void setDestFields(DestDTO destFields) {
        this.destFields = destFields;
    }

    public String getVoidField() {
        return voidField;
    }

    public void setVoidField(String voidField) {
        this.voidField = voidField;
    }
}
