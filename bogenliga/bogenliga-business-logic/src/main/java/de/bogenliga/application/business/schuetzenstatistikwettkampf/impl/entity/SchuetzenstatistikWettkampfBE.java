package de.bogenliga.application.business.schuetzenstatistikwettkampf.impl.entity;

import de.bogenliga.application.common.component.entity.BusinessEntity;
import de.bogenliga.application.common.component.entity.CommonBusinessEntity;

/**
 *
 * @author Anna Baur
 */
public class SchuetzenstatistikWettkampfBE extends CommonBusinessEntity implements BusinessEntity {
    private static final long serialVersionUID = 5734330182051890903L;

    private Long veranstaltungId;
    private Long wettkampfId;
    private int wettkampfTag;
    private Long vereinId;
    private Long dsbMitgliedId;
    private String dsbMitgliedName;
    private int rueckenNummer;
    private float wettkampftag1;
    private float wettkampftag2;
    private float wettkampftag3;
    private float wettkampftag4;
    private float wettkampftageSchnitt;

    @Override
    public String toString() {
        return "SchuetzenstatistikBE{" +
                "veranstaltungId=" + veranstaltungId +
                ", wettkampfId='" + wettkampfId +
                ", wettkampfTag='" + wettkampfTag +
                ", vereinId='" + vereinId +
                ", dsbMitgliedId='" + dsbMitgliedId +
                ", dsbMitgliedName='" + dsbMitgliedName +
                ", rueckenNummer='" + rueckenNummer +
                ", wettkampftag1='" + wettkampftag1 +
                ", wettkampftag2='" + wettkampftag2 +
                ", wettkampftag3='" + wettkampftag3 +
                ", wettkampftag4='" + wettkampftag4 +
                ", wettkampftageSchnitt='" + wettkampftageSchnitt +
                '}';
    }


    public long getVeranstaltungId() {
        return veranstaltungId;
    }
    public void setVeranstaltungId(final long veranstaltungId) {
        this.veranstaltungId = veranstaltungId;
    }

    public long getWettkampfId() {
        //bei der Schützenstatistik über mehr als einen Wettkampf gibt es keine gültige WettkampfID
        if (wettkampfId == null) {
            this.wettkampfTag = 0;
            return 0;
        }
        else return wettkampfId;
    }
    public void setWettkampfId(final long wettkampfId) {
        this.wettkampfId = wettkampfId;
    }

    public int getWettkampfTag() {
        return wettkampfTag;
    }
    public void setWettkampfTag(final int wettkampfTag) {
        this.wettkampfTag = wettkampfTag;
    }

    public long getVereinId() {
        return vereinId;
    }
    public void setVereinId(final long vereinId) {
        this.vereinId = vereinId;
    }

    public Long getDsbMitgliedId() {
        return dsbMitgliedId;
    }
    public void setDsbMitgliedId(Long dsbMitgliedId) {
        this.dsbMitgliedId = dsbMitgliedId;
    }

    public String getDsbMitgliedName() {
        return dsbMitgliedName;
    }
    public void setDsbMitgliedName(String dsbMitgliedName) {
        this.dsbMitgliedName = dsbMitgliedName;
    }

    public int getRueckenNummer() {
        return rueckenNummer;
    }
    public void setRueckenNummer(int rueckenNummer) {
        this.rueckenNummer = rueckenNummer;
    }

    public float getWettkampftag1() {return wettkampftag1;}
    public void setWettkampftag1(float wettkampftag1) {this.wettkampftag1 = wettkampftag1;}

    public float getWettkampftag2() {return wettkampftag2;}
    public void setWettkampftag2(float wettkampftag2) {this.wettkampftag2 = wettkampftag2;}

    public float getWettkampftag3() {return wettkampftag3;}
    public void setWettkampftag3(float wettkampftag3) {this.wettkampftag3 = wettkampftag3;}

    public float getWettkampftag4() {return wettkampftag4;}
    public void setWettkampftag4(float wettkampftag4) {this.wettkampftag4 = wettkampftag4;}

    public float getWettkampftageSchnitt() {return wettkampftageSchnitt;}
    public void setWettkampftageSchnitt(float wettkampftageSchnitt) {this.wettkampftageSchnitt = wettkampftageSchnitt;}


}
