package de.bogenliga.application.business.veranstaltung.api.types;

import de.bogenliga.application.common.component.types.CommonDataObject;
import de.bogenliga.application.common.component.types.DataObject;

import java.sql.Date;
import java.time.OffsetDateTime;

public class VeranstaltungDO extends CommonDataObject implements DataObject {



    /**
     * business parameter
     */
    private Long id;
    private Long wettkampftypId;
    private String name;
    private Long sportjahr;
    private Date meldeDeadline;
    private Long kampfrichterAnzahl;
    private Long hoehere;
    private Long ligaleiterId;

    /**
     * Constructor with optional parameters
     * @param id
     * @param wettkampftypId
     * @param name
     * @param sportjahr
     * @param meldeDeadline
     * @param kampfrichterAnzahl
     * @param hoehere
     * @param ligaleiterId
     * @param createdAtUtc
     * @param createdByUserId
     * @param lastModifiedAtUtc
     * @param lastModifiedByUserId
     * @param version
     */
    public VeranstaltungDO(final Long id, final long wettkampftypId, final String name,
                           final Long sportjahr, final Date meldeDeadline, final Long kampfrichterAnzahl,
                           final Long hoehere, final Long ligaleiterId, final OffsetDateTime createdAtUtc,
                           final long createdByUserId,final OffsetDateTime lastModifiedAtUtc,
                           final Long lastModifiedByUserId,final Long version){
        this.id =id;
        this.wettkampftypId=wettkampftypId;
        this.name=name;
        this.sportjahr=sportjahr;
        this.meldeDeadline=meldeDeadline;
        this.kampfrichterAnzahl=kampfrichterAnzahl;
        this.hoehere=hoehere;
        this.ligaleiterId=ligaleiterId;
        this.createdAtUtc=createdAtUtc;
        this.createdByUserId=createdByUserId;
        this.lastModifiedAtUtc=lastModifiedAtUtc;
        this.lastModifiedByUserId=lastModifiedByUserId;
        this.version=version;
    }

    /**
     * Constructor with mandatory parameters
     * @param id
     * @param wettkampftypId
     * @param name
     * @param sportjahr
     * @param meldeDeadline
     * @param kampfrichterAnzahl
     * @param hoehere
     * @param ligaleiterId
     * @param createdAtUtc
     * @param createdByUserId
     * @param version
     */
    public VeranstaltungDO(final Long id, final long wettkampftypId, final String name,
                           final Long sportjahr, final Date meldeDeadline, final Long kampfrichterAnzahl,
                           final Long hoehere, final Long ligaleiterId, final OffsetDateTime createdAtUtc,
                           final long createdByUserId,final Long version){
        this.id =id;
        this.wettkampftypId=wettkampftypId;
        this.name=name;
        this.sportjahr=sportjahr;
        this.meldeDeadline=meldeDeadline;
        this.kampfrichterAnzahl=kampfrichterAnzahl;
        this.hoehere=hoehere;
        this.ligaleiterId=ligaleiterId;
        this.createdAtUtc=createdAtUtc;
        this.createdByUserId=createdByUserId;
        this.version=version;
    }

    /**
     * Constructor without technical parameters
     * @param id
     * @param wettkampftypId
     * @param name
     * @param sportjahr
     * @param meldeDeadline
     * @param kampfrichterAnzahl
     * @param hoehere
     * @param ligaleiterId
     */
    public VeranstaltungDO(final Long id,final long wettkampftypId,final String name,
                           final Long sportjahr,final Date meldeDeadline,final Long kampfrichterAnzahl,
                           final Long hoehere,final Long ligaleiterId){
        this.id =id;
        this.wettkampftypId=wettkampftypId;
        this.name=name;
        this.sportjahr=sportjahr;
        this.meldeDeadline=meldeDeadline;
        this.kampfrichterAnzahl=kampfrichterAnzahl;
        this.hoehere=hoehere;
        this.ligaleiterId=ligaleiterId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWettkampftypId() {
        return wettkampftypId;
    }

    public void setWettkampftypId(Long wettkampftypId) {
        this.wettkampftypId = wettkampftypId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSportjahr() {
        return sportjahr;
    }

    public void setSportjahr(Long sportjahr) {
        this.sportjahr = sportjahr;
    }

    public Date getMeldeDeadline() {
        return meldeDeadline;
    }

    public void setMeldeDeadline(Date meldeDeadline) {
        this.meldeDeadline = meldeDeadline;
    }

    public Long getKampfrichterAnzahl() {
        return kampfrichterAnzahl;
    }

    public void setKampfrichterAnzahl(Long kampfrichterAnzahl) {
        this.kampfrichterAnzahl = kampfrichterAnzahl;
    }

    public Long getHoehere() {
        return hoehere;
    }

    public void setHoehere(Long hoehere) {
        this.hoehere = hoehere;
    }

    public Long getLigaleiterId() {
        return ligaleiterId;
    }

    public void setLigaleiterId(Long ligaleiterId) {
        this.ligaleiterId = ligaleiterId;
    }
}
