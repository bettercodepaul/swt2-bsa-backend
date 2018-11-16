package de.bogenliga.application.services.v1.competitionclass.model;

import de.bogenliga.application.common.service.types.DataTransferObject;

/**
 * I'm the data transfer object of the competition class
 *
 * I define the payload for the external REST interface of the competition class business entity.
 *
 * @author Giuseppe Ferrera, giuseppe.ferrera@student.reutlingen-university.de
 */
public class CompetitionClassDTO implements DataTransferObject {
    private Long id;
    private String klasseName;
    private Long klasseAlterMin;
    private Long klasseAlterMax;
    private Long klasseNr;


    /**
     * Constructors
     */
    public CompetitionClassDTO() {
        // empty constructor
    }

    public CompetitionClassDTO(Long id, String klasseName, Long klasseAlterMin, Long klasseAlterMax, Long klasseNr) {
        this.id = id;
        this.klasseName = klasseName;
        this.klasseAlterMin = klasseAlterMin;
        this.klasseAlterMax = klasseAlterMax;
        this.klasseNr = klasseNr;
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getKlasseName() {
        return klasseName;
    }


    public void setKlasseName(String klasseName) {
        this.klasseName = klasseName;
    }


    public Long getKlasseAlterMin() {
        return klasseAlterMin;
    }


    public void setKlasseAlterMin(Long klasseAlterMin) {
        this.klasseAlterMin = klasseAlterMin;
    }


    public Long getKlasseAlterMax() {
        return klasseAlterMax;
    }


    public void setKlasseAlterMax(Long klasseAlterMax) {
        this.klasseAlterMax = klasseAlterMax;
    }


    public Long getKlasseNr() {
        return klasseNr;
    }


    public void setKlasseNr(Long klasseNr) {
        this.klasseNr = klasseNr;
    }
}