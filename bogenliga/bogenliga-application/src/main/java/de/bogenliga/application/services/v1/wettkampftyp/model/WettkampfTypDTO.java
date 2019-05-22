package de.bogenliga.application.services.v1.wettkampftyp.model;

import de.bogenliga.application.common.service.types.DataTransferObject;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class WettkampfTypDTO implements DataTransferObject {
    private Long id;
    private String name;


    public WettkampfTypDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }
}
