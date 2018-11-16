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
    private Long klasseAlterMin;
    private Long klasseAlterMax;
    private Long klasseNr;

    /**
     * Constructor with mandatory parameters
     */
    public CompetitionClassDO(final Long id, final String klasseName, final Long klasseAlterMin,
                              final Long klasseAlterMax, final Long klasseNr, final OffsetDateTime createdAtUtc,
                              final Long createdByUserId, final OffsetDateTime lastModifiedUtc, final Long version) {
        this.id = id;
        this.klasseName = klasseName;
        this.klasseAlterMin = klasseAlterMin;
        this.klasseAlterMax = klasseAlterMax;
        this.klasseNr = klasseNr;

        // set param from CommonDataObject
        this.createdAtUtc = createdAtUtc;
        this.createdByUserId = createdByUserId;
        this.lastModifiedAtUtc = lastModifiedUtc;
        this.version = version;
    }


    /**
     * Constructor with optional parameters
     */
    public CompetitionClassDO(final Long id, final String klasseName, final Long klasseAlterMin,
                              final Long klasseAlterMax, final Long klasseNr) {
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