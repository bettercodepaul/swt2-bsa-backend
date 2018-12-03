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
    private Long klasseJahrgangMin;
    private Long klasseJahrgangMax;
    private Long klasseNr;


    /**
     * Constructors
     */
    public CompetitionClassDTO() {
        // empty constructor
    }

    public CompetitionClassDTO(Long id, String klasseName, Long klasseJahrgangMin, Long klasseJahrgangMax, Long klasseNr) {
        this.id = id;
        this.klasseName = klasseName;
        this.klasseJahrgangMin = klasseJahrgangMin;
        this.klasseJahrgangMax = klasseJahrgangMax;
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


    public Long getKlasseJahrgangMin() {
        return klasseJahrgangMin;
    }


    public void setKlasseJahrgangMin(Long klasseJahrgangMin) {
        this.klasseJahrgangMin = klasseJahrgangMin;
    }


    public Long getKlasseJahrgangMax() {
        return klasseJahrgangMax;
    }


    public void setKlasseJahrgangMax(Long klasseJahrgangMax) {
        this.klasseJahrgangMax = klasseJahrgangMax;
    }


    public Long getKlasseNr() {
        return klasseNr;
    }


    public void setKlasseNr(Long klasseNr) {
        this.klasseNr = klasseNr;
    }
}