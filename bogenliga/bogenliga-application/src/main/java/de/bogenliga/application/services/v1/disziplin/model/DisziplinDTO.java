package de.bogenliga.application.services.v1.disziplin.model;

import de.bogenliga.application.common.service.types.DataTransferObject;

/**
 * I´m the data transfer object of the Disziplin.
 * I define the payload for the external REST interface of the Disziplin business entity.
 *
 * @author Nico Keilig
 * @see DataTransferObject
 */
public class DisziplinDTO implements DataTransferObject {

    private long disziplinId;
    private String disziplinName;

    public DisziplinDTO(){}

    public DisziplinDTO(final Long disziplinId, final String disziplinName){
        this.disziplinId = disziplinId;
        this.disziplinName = disziplinName;
    }


    public long getDisziplinId() {
        return disziplinId;
    }


    public void setDisziplinId(long disziplinId) {
        this.disziplinId = disziplinId;
    }


    public String getDisziplinName() {
        return disziplinName;
    }


    public void setDisziplinName(String disziplinName) {
        this.disziplinName = disziplinName;
    }
}
