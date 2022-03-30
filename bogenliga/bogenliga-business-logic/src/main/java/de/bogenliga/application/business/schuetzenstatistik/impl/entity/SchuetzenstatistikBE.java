package de.bogenliga.application.business.schuetzenstatistik.impl.entity;

import de.bogenliga.application.common.component.entity.BusinessEntity;
import de.bogenliga.application.common.component.entity.CommonBusinessEntity;

/**
 * I´m a composed business entity of the user and the permission business entity.
 *
 * The user permissions are resolved with a JOIN via the user roles, roles, role permissions and permissions.
 */
public class SchuetzenstatistikBE extends CommonBusinessEntity implements BusinessEntity {
    private static final long serialVersionUID = 5734330182051890903L;

    private Long veranstaltungId;
    private String veranstaltungName;
    private Long wettkampfId;
    private int wettkampfTag;
    private Long mannschaftId;
    private int mannschaftNummer;
    private Long vereinId;
    private String vereinName;
    private Long matchId;
    private int matchNr;
    private Long dsbMitgliedId;
    private String dsbMitgliedName;
    private int rueckenNummer;
    private float pfeilpunkteSchnitt;

    public SchuetzenstatistikBE() {
        // nothing is here
    }

    @Override
    public String toString() {
        return "SchuetzenstatistikBE{" +
                "veranstaltungId=" + veranstaltungId +
                ", veranstaltungName=" + veranstaltungId +
                ", wettkampfId='" + wettkampfId +
                ", wettkampfTag='" + wettkampfTag +
                ", mannschaftId='" + mannschaftId +
                ", mannschaftNummer='" + mannschaftNummer +
                ", vereinId='" + vereinId +
                ", vereinName='" + vereinName +
                ", matchId='" + matchId +
                ", matchNr='" + matchNr +
                ", dsbMitgliedId='" + dsbMitgliedId +
                ", dsbMitgliedName='" + dsbMitgliedName +
                ", rueckenNummer='" + rueckenNummer +
                ", pfeilpunkteSchnitt='" + pfeilpunkteSchnitt +
                '}';
    }

    public long getVeranstaltungId() {
        return veranstaltungId;
    }
    public void setVeranstaltungId(final long veranstaltungId) {
        this.veranstaltungId = veranstaltungId;
    }

    public String getVeranstaltungName() {
        return veranstaltungName;
    }
    public void setVeranstaltungName(final String veranstaltungName) {
        this.veranstaltungName = veranstaltungName;
    }

    public long getWettkampfId() {
        return wettkampfId;
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

    public long getMannschaftId() {
        return mannschaftId;
    }
    public void setMannschaftId(final long mannschaftId) {
        this.mannschaftId = mannschaftId;
    }

    public int getMannschaftNummer() {
        return mannschaftNummer;
    }
    public void setMannschaftNummer(final int mannschaftNummer) {
        this.mannschaftNummer = mannschaftNummer;
    }

    public long getVereinId() {
        return vereinId;
    }
    public void setVereinId(final long vereinId) {
        this.vereinId = vereinId;
    }

    public String getVereinName() {
        return vereinName;
    }
    public void setVereinName(final String vereinName) {
        this.vereinName = vereinName;
    }

    public Long getMatchId() {
        return matchId;
    }
    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    public int getMatchNr() {
        return matchNr;
    }
    public void setMatchNr(int matchNr) {
        this.matchNr = matchNr;
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

    public float getPfeilpunkteSchnitt() {
        return pfeilpunkteSchnitt;
    }
    public void setPfeilpunkteSchnitt(float pfeilpunkteSchnitt) {
        this.pfeilpunkteSchnitt = pfeilpunkteSchnitt;
    }
}
