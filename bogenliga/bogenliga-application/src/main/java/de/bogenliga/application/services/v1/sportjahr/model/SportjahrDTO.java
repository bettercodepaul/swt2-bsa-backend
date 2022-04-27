package de.bogenliga.application.services.v1.sportjahr;

import de.bogenliga.application.common.service.types.DataTransferObject;
/**
 * Erstellt ein SportjahrDTO Objekt.
 *
 * @author Philipp Schmidt
 */
public class SportjahrDTO implements DataTransferObject {

    private Long id;
    private Long sportjahr;
    private Long version;


    public SportjahrDTO() {
        //empty
    }


    public SportjahrDTO(final Long id, final Long sportjahr, final Long version) {
        this.id = id;
        this.sportjahr = sportjahr;
        this.version = version;
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public Long getSportjahr() {
        return sportjahr;
    }


    public void setSportjahr(Long sportjahr) {
        this.sportjahr = sportjahr;
    }


    public Long getVersion() {
        return version;
    }


    public void setVersion(Long version) {
        this.version = version;
    }
}

