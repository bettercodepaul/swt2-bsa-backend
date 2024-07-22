package de.bogenliga.application.business.dsbmannschaft.api.types;

import de.bogenliga.application.common.component.types.CommonDataObject;
import de.bogenliga.application.common.component.types.DataObject;


import java.time.OffsetDateTime;
import java.util.Objects;

public class DsbMannschaftDO extends CommonDataObject implements DataObject {

    /**
     * business parameter
     */

    private Long id;
    private String name;
    private Long vereinId;
    private Long nummer;
    private Long benutzerId;
    private Long veranstaltungId;
    private Long sortierung;
    private String veranstaltungName;
    private String wettkampfTag;
    private String wettkampfOrtsname;
    private String vereinName;
    private Long mannschaftNummer;


    /**
     * Constructor with optional parameters
     * @param id
     * @param name
     * @param vereinId
     * @param nummer
     * @param benutzerId
     * @param veranstaltungId
     * @param sortierung
     * @param createdAtUtc
     * @param createdByUserId
     * @param lastModifiedAtUtc
     * @param lastModifiedByUserId
     * @param version
     */

    public DsbMannschaftDO(final Long id, final String name, final long vereinId, final long nummer, final long benutzerId,final Long veranstaltungId,
                           final Long sortierung, final OffsetDateTime createdAtUtc, final Long createdByUserId,
                           final OffsetDateTime lastModifiedAtUtc, final Long lastModifiedByUserId, final Long version) {
        this.id = id;
        this.name = name;
        this.vereinId=vereinId;
        this.nummer=nummer;
        this.benutzerId=benutzerId;
        this.veranstaltungId=veranstaltungId;
        this.sortierung = sortierung;
        this.createdAtUtc = createdAtUtc;
        this.createdByUserId = createdByUserId;
        this.lastModifiedAtUtc = lastModifiedAtUtc;
        this.lastModifiedByUserId = lastModifiedByUserId;
        this.version = version;
    }
    /**
     * Constructor with mandatory parameters
     * @param veranstaltungName
     * @param wettkampfTag
     * @param wettkampfOrtsname
     * @param vereinName
     * @param mannschaftNummer
     */
    public DsbMannschaftDO(final String veranstaltungName,final String wettkampfTag, final String wettkampfOrtsname, final String vereinName,final long mannschaftNummer) {
        this.veranstaltungName = veranstaltungName;
        this.wettkampfTag = wettkampfTag;
        this.wettkampfOrtsname = wettkampfOrtsname;
        this.vereinName = vereinName;
        this.mannschaftNummer = mannschaftNummer;
    }


    /**
     * Constructor with mandatory parameters
     * @param id
     * @param name
     * @param vereinId
     * @param nummer
     * @param benutzerId
     * @param veranstaltungId
     * @param sortierung
     * @param createdAtUtc
     * @param createdByUserId
     * @param version
     */
    public DsbMannschaftDO(final Long id, final String name, final long vereinId, final long nummer, final long benutzerId,final Long veranstaltungId,
                           final Long sortierung, final OffsetDateTime createdAtUtc, final Long createdByUserId,final Long version) {
        this.id = id;
        this.name = name;
        this.vereinId=vereinId;
        this.nummer=nummer;
        this.benutzerId=benutzerId;
        this.veranstaltungId=veranstaltungId;
        this.sortierung = sortierung;
        this.createdAtUtc = createdAtUtc;
        this.createdByUserId = createdByUserId;
        this.version = version;
    }


    /**
     * Constructor without technical parameters
     * @param id
     * @param vereinId
     * @param nummer
     * @param benutzerId
     * @param veranstaltungId

     */
    public DsbMannschaftDO(final Long id, final String name, final long vereinId, final long nummer,
                           final long benutzerId,final Long veranstaltungId, final Long sortierung) {
        this.id = id;
        this.name = name;
        this.vereinId=vereinId;
        this.nummer=nummer;
        this.benutzerId=benutzerId;
        this.veranstaltungId=veranstaltungId;
        this.sortierung = sortierung;

    }

    /**
     * individuel constructor
     * @param id
     * @param vereinId
     */
    public DsbMannschaftDO(final Long id, final long vereinId) {
        this.id = id;
        this.vereinId = vereinId;
    }

    /**
     * Constructor with id for deleting existing entries
     * @param id
     */
    public DsbMannschaftDO(final Long id) {
        this.id = id;
    }


    public DsbMannschaftDO() {

    }


    public Long getId(){ return id; }

    public void setId(final long id){this.id=id;}


    public String getName(){ return name; }

    public void setName(final String name){this.name=name;}


    public Long getVereinId(){return vereinId;}

    public void setVereinId(final long vereinId){this.vereinId=vereinId;}


    public Long getNummer(){return nummer;}

    public void setNummer(final long nummer){this.nummer=nummer;}


    public Long getBenutzerId(){return benutzerId;}

    public void setBenutzerId(final long benutzerId){this.benutzerId=benutzerId;}


    public Long getVeranstaltungId(){return veranstaltungId;}

    public void setVeranstaltungId(final Long veranstaltungId){this.veranstaltungId=veranstaltungId;}


    public Long getSortierung(){return sortierung;}

    public void setSortierung(final long sortierung){this.sortierung=sortierung;}
    public String getVeranstaltungName() {
        return veranstaltungName;
    }

    public void setVeranstaltungName(final String veranstaltungName) {
        this.veranstaltungName = veranstaltungName;
    }

    public String getWettkampfTag() {
        return wettkampfTag;
    }

    public void setWettkampfTag(final String wettkampfTag) {
        this.wettkampfTag = wettkampfTag;
    }

    public String getWettkampfOrtsname() {
        return wettkampfOrtsname;
    }

    public void setWettkampfOrtsname(final String wettkampfOrtsname) {
        this.wettkampfOrtsname = wettkampfOrtsname;
    }

    public String getVereinName() {
        return vereinName;
    }

    public void setVereinName(final String vereinName) {
        this.vereinName = vereinName;
    }
    public Long getMannschaftNummer() {
        return mannschaftNummer;
    }
    public void setMannschaftNummer(Long mannschaftNummer) {
        this.mannschaftNummer = mannschaftNummer;
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, vereinId,nummer,benutzerId,veranstaltungId,sortierung,
                createdByUserId, lastModifiedAtUtc,
                lastModifiedByUserId, version,id, veranstaltungName, wettkampfTag, wettkampfOrtsname, vereinName,mannschaftNummer);
    }


    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DsbMannschaftDO that = (DsbMannschaftDO) o;
        return id == that.id &&
                vereinId == that.vereinId &&
                benutzerId == that.benutzerId &&
                sortierung == that.sortierung &&
                mannschaftNummer == that.mannschaftNummer &&
                createdByUserId == that.createdByUserId &&
                lastModifiedByUserId == that.lastModifiedByUserId &&
                version == that.version &&
                Objects.equals(mannschaftNummer,that.mannschaftNummer) &&
                Objects.equals(nummer, that.nummer) &&
                Objects.equals(veranstaltungId, that.veranstaltungId) &&
                Objects.equals(lastModifiedAtUtc, that.lastModifiedAtUtc) &&
                Objects.equals(veranstaltungName, that.veranstaltungName) &&
                Objects.equals(wettkampfTag, that.wettkampfTag) &&
                Objects.equals(wettkampfOrtsname, that.wettkampfOrtsname) &&
                Objects.equals(vereinName, that.vereinName);
    }
}

