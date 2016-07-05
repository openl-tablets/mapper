package org.openl.rules.mapping.composite.mapid.destination;

/**
 * @author kkachanovskiy@exigenservices.com
 *
 */
public class DestPolicy {
    private String namedInsured;
    private DestLocDTO[] locDTOs;

    public String getNamedInsured() {
        return namedInsured;
    }

    public void setNamedInsured(String namedInsured) {
        this.namedInsured = namedInsured;
    }

    public DestLocDTO[] getLocDTOs() {
        return locDTOs;
    }

    public void setLocDTOs(DestLocDTO[] locDTOs) {
        this.locDTOs = locDTOs;
    }
}
