package de.bogenliga.application.business.competitionclass.api.types;

import java.time.OffsetDateTime;
import de.bogenliga.application.common.component.types.CommonDataObject;
import de.bogenliga.application.common.component.types.DataObject;

/**
 * Contains the values of the class business entity
 *
 * @author Giuseppe Ferrera, giuseppe.ferrera@student.reutlingen-university.de
 */
public class CompetitionClassDO extends CommonDataObject implements DataObject {

    /**
     * business parameter
     */
    private Long id;
    private String klasseName;
    private Long klasseJahrgangMin;
    private Long klasseJahrgangMax;
    private Long klasseNr;

    /**
     * Constructor with mandatory parameters
     */
    public CompetitionClassDO(final Long id, final String klasseName, final Long klasseJahrgangMin,
                              final Long klasseJahrgangMax, final Long klasseNr, final OffsetDateTime createdAtUtc,
                              final Long createdByUserId, final OffsetDateTime lastModifiedUtc, final Long lastModifiedByUserId, final Long version) {
        this.id = id;
        this.klasseName = klasseName;
        this.klasseJahrgangMin = klasseJahrgangMin;
        this.klasseJahrgangMax = klasseJahrgangMax;
        this.klasseNr = klasseNr;

        // set param from CommonDataObject
        this.createdAtUtc = createdAtUtc;
        this.createdByUserId = createdByUserId;
        this.lastModifiedAtUtc = lastModifiedUtc;
        this.lastModifiedByUserId = lastModifiedByUserId;
        this.version = version;
    }


    /**
     * Constructor with optional parameters
     */
    public CompetitionClassDO(final Long id, final String klasseName, final Long klasseJahrgangMin,
                              final Long klasseJahrgangMax, final Long klasseNr) {
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