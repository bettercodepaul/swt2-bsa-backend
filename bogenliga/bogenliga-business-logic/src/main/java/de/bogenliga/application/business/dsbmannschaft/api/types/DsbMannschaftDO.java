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
    private String veranstaltung_name;
    private String wettkampf_tag;
    private String wettkampf_ortsname;
    private String verein_name;
    private Long mannschaft_nummer;


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
     * @param veranstaltung_name
     * @param wettkampfTag
     * @param wettkampf_ortsname
     * @param verein_name
     * @param mannschaft_nummer
     */
    public DsbMannschaftDO(final String veranstaltung_name, final String wettkampfTag, final String wettkampf_ortsname, final String verein_name, final long mannschaft_nummer) {
        this.veranstaltung_name = veranstaltung_name;
        this.wettkampf_tag = wettkampfTag;
        this.wettkampf_ortsname = wettkampf_ortsname;
        this.verein_name = verein_name;
        this.mannschaft_nummer = mannschaft_nummer;
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
    public String getVeranstaltung_name() {
        return veranstaltung_name;
    }

    public void setVeranstaltung_name(final String veranstaltung_name) {
        this.veranstaltung_name = veranstaltung_name;
    }

    public String getWettkampfTag() {
        return wettkampf_tag;
    }

    public void setWettkampfTag(final String wettkampfTag) {
        this.wettkampf_tag = wettkampfTag;
    }

    public String getWettkampf_ortsname() {
        return wettkampf_ortsname;
    }

    public void setWettkampf_ortsname(final String wettkampf_ortsname) {
        this.wettkampf_ortsname = wettkampf_ortsname;
    }

    public String getVerein_name() {
        return verein_name;
    }

    public void setVerein_name(final String verein_name) {
        this.verein_name = verein_name;
    }
    public Long getMannschaft_nummer() {
        return mannschaft_nummer;
    }
    public void setMannschaft_nummer(Long mannschaft_nummer) {
        this.mannschaft_nummer = mannschaft_nummer;
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, vereinId,nummer,benutzerId,veranstaltungId,sortierung,
                createdByUserId, lastModifiedAtUtc,
                lastModifiedByUserId, version,id, veranstaltung_name, wettkampf_tag, wettkampf_ortsname, verein_name,
                mannschaft_nummer);
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
                mannschaft_nummer == that.mannschaft_nummer &&
                createdByUserId == that.createdByUserId &&
                lastModifiedByUserId == that.lastModifiedByUserId &&
                version == that.version &&
                Objects.equals(mannschaft_nummer,that.mannschaft_nummer) &&
                Objects.equals(nummer, that.nummer) &&
                Objects.equals(veranstaltungId, that.veranstaltungId) &&
                Objects.equals(lastModifiedAtUtc, that.lastModifiedAtUtc) &&
                Objects.equals(veranstaltung_name, that.veranstaltung_name) &&
                Objects.equals(wettkampf_tag, that.wettkampf_tag) &&
                Objects.equals(wettkampf_ortsname, that.wettkampf_ortsname) &&
                Objects.equals(verein_name, that.verein_name);
    }
}

