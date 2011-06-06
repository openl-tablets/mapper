package org.dozer;

/**
 * The class that represents context of mapping process. Intended to satisfy
 * user needs to change mapping business logic at runtime.
 */
public class MappingContext {

    /**
     * Mapping identifier.
     */
    private String mapId;

    /**
     * User defined parameters.
     */
    private MappingParameters userParams;

    
    public String getMapId() {
        return mapId;
    }

    public void setMapId(String mapId) {
        this.mapId = mapId;
    }

    public MappingParameters getParams() {
        return userParams;
    }

    public void setParams(MappingParameters userParams) {
        this.userParams = userParams;
    }

}
