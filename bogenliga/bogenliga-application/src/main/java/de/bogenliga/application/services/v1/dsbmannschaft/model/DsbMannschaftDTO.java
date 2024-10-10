package de.bogenliga.application.services.v1.dsbmannschaft.model;

import de.bogenliga.application.common.service.types.DataTransferObject;

/**
 * I´m the data transfer object of the dsbMannschaft.
 *
 * I define the payload for the external REST interface of the dsbMannschaft business entity.
 *
 * @author Kaywan Barzani
 * @see DataTransferObject
 */
public class DsbMannschaftDTO implements DataTransferObject {
    private static final long serialVersionUID = 8559563978424033907L;

    private Long id;
    private String name;
    private Long vereinId;
    private Long nummer;
    private Long benutzerId;
    private Long veranstaltungId;
    private Long sortierung;
    private Long sportjahr;
    private String veranstaltungName;
    private String wettkampfTag;
    private String wettkampfOrtsname;
    private String vereinName;
    private Long mannschaftNummer;


    /**
     * Constructors
     */
    public DsbMannschaftDTO() {
        // empty constructor
    }


    public DsbMannschaftDTO(Long id, String name, Long vereinId, Long nummer,
                            Long benutzerId, Long veranstaltungId, Long sortierung, Long sportjahr) {
        this.id = id;
        this.name = name;
        this.vereinId = vereinId;
        this.nummer = nummer;
        this.benutzerId=benutzerId;
        this.veranstaltungId=veranstaltungId;
        this.sortierung = sortierung;
        this.sportjahr = sportjahr;

    }
    public DsbMannschaftDTO(final String veranstaltungName,final String wettkampfTag, final String wettkampfOrtsname, final String vereinName,final long mannschaftNummer) {
        this.veranstaltungName = veranstaltungName;
        this.wettkampfTag = wettkampfTag;
        this.wettkampfOrtsname = wettkampfOrtsname;
        this.vereinName = vereinName;
        this.mannschaftNummer = mannschaftNummer;
    }
    public Long getId(){ return id; }

    public void setId(Long id){this.id=id;}


    public String getName(){ return name; }

    public void setName(final String name){this.name=name;}


    public Long getVereinId(){return vereinId;}

    public void setVereinId(Long vereinId){this.vereinId=vereinId;}


    public Long getNummer(){return nummer;}

    public void setNummer(Long nummer){this.nummer=nummer;}


    public Long getBenutzerId(){return benutzerId;}

    public void setBenutzerId(Long benutzerId){this.benutzerId=benutzerId;}


    public Long getVeranstaltungId(){return veranstaltungId;}

    public void setVeranstaltungId(Long veranstaltungId){this.veranstaltungId=veranstaltungId;}


    public Long getSortierung(){return sortierung;}

    public void setSortierung(Long sortierung){this.sortierung=sortierung;}

    public Long getSportjahr(){return sportjahr;}

    public void setSportjahr(Long sportjahr){this.sportjahr=sportjahr;}
    public String getWettkampfOrtsname() {
        return wettkampfOrtsname;
    }

    public void setWettkampfOrtsname(final String wettkampfOrtsname) {
        this.wettkampfOrtsname = wettkampfOrtsname;
    }
    public String getVeranstaltungName(){return this.veranstaltungName;}
    public void setVeranstaltungName(final String veranstaltungName){this.veranstaltungName = veranstaltungName;}
    public String getWettkampfTag(){return this.wettkampfTag;}
    public void setWettkampfTag(final String wettkampfTag){this.wettkampfTag = wettkampfTag;}
    public String getVereinName() {
        return vereinName;
    }

    public void setVereinName(final String vereinName) {
        this.vereinName = vereinName;
    }
    public void setMannschaftNummer(Long mannschaftNummer) {
        this.mannschaftNummer = mannschaftNummer;
    }
    public Long getMannschaftNummer() {
        return mannschaftNummer;
    }
}
