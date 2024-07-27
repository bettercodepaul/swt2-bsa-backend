package de.bogenliga.application.business.dsbmannschaft.impl.entity;

import de.bogenliga.application.common.component.entity.BusinessEntity;
import de.bogenliga.application.common.component.entity.CommonBusinessEntity;

public class DsbMannschaftBE extends CommonBusinessEntity implements BusinessEntity {

    private static final long serialVersionUID = -6431886856322437597L;
    private Long id;
    private Long vereinId;
    private Long nummer;
    private Long veranstaltungId;
    private Long benutzerId;
    private Long sortierung;
    private String veranstaltungName;
    private String wettkampfTag;
    private String wettkampfOrtsname;
    private String vereinName;
    private Long mannschaftNummer;


    public DsbMannschaftBE() {/*empty constructor*/}
    /**
     * Constructor with mandatory parameters
     * @param veranstaltungName
     * @param wettkampfTag
     * @param wettkampfOrtsname
     * @param vereinName
     * @param nummer
     */
    public DsbMannschaftBE(final String veranstaltungName,final String wettkampfTag, final String wettkampfOrtsname, final String vereinName, final long nummer) {
        this.veranstaltungName = veranstaltungName;
        this.wettkampfTag = wettkampfTag;
        this.wettkampfOrtsname = wettkampfOrtsname;
        this.vereinName = vereinName;
        this.mannschaftNummer = nummer;
    }

    public Long getId() {
        return id;
    }


    public void setId(final Long id) {
        this.id = id;
    }


    public Long getVereinId() {
        return vereinId;
    }


    public void setVereinId(final Long vereinId) {
        this.vereinId = vereinId;
    }


    public Long getNummer() {
        return nummer;
    }


    public void setNummer(final Long nummer) {
        this.nummer = nummer;
    }


    public Long getVeranstaltungId() {
        return veranstaltungId;
    }


    public void setVeranstaltungId(final Long veranstaltungId) {
        this.veranstaltungId = veranstaltungId;
    }


    public Long getBenutzerId() {
        return benutzerId;
    }


    public void setBenutzerId(final Long benutzerId) {
        this.benutzerId = benutzerId;
    }


    public Long getSortierung() {
        return sortierung;
    }


    public void setSortierung(final Long sortierung) {
        this.sortierung = sortierung;
    }

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
    public String toString() {
        return "DsbMannschaftBE{" +
                "mannschaftId=" + id +
                ", mannschaftVereinId='" + vereinId + '\'' +
                ", mannschaftNummer='" + nummer + '\'' +
                ", mannschaftVeranstaltungId='" + veranstaltungId + '\'' +
                ", mannschaftBenutzerId='" + benutzerId + '\'' +
                ", mannschaftSortierung='" + sortierung + '\'' +
                '}';
    }
}
