package de.bogenliga.application.services.v1.schuetzenstatistikwettkampf.model;

import java.util.Objects;
import de.bogenliga.application.common.service.types.DataTransferObject;

/**
 * data transfer object of schützenstatistikWettkampf
 *
 * @author Anna Baur
 */
public class SchuetzenstatistikWettkampfDTO implements DataTransferObject {

    /**
     * attributes of DTO
     */

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


    /**
     * The Constructor with optional parameters
     *
     * @param veranstaltungId;
     * @param wettkampfId;
     * @param wettkampfTag;
     * @param vereinId;
     * @param dsbMitgliedId;
     * @param dsbMitgliedName;
     * @param rueckenNummer;
     * @param wettkampftag1;
     * @param wettkampftag2;
     * @param wettkampftag3;
     * @param wettkampftag4;
     * @param wettkampftageSchnitt;
     *
     */

    public SchuetzenstatistikWettkampfDTO(
            Long veranstaltungId,
            Long wettkampfId,
            int wettkampfTag,
            Long vereinId,
            Long dsbMitgliedId,
            String dsbMitgliedName,
            int rueckenNummer,
            float wettkampftag1,
            float wettkampftag2,
            float wettkampftag3,
            float wettkampftag4,
            float wettkampftageSchnitt
    ) {
        this.veranstaltungId=veranstaltungId;
        this.wettkampfId = wettkampfId;
        this.wettkampfTag = wettkampfTag;
        this.vereinId = vereinId;
        this.dsbMitgliedId = dsbMitgliedId;
        this.dsbMitgliedName = dsbMitgliedName;
        this.rueckenNummer = rueckenNummer;
        this.wettkampftag1 = wettkampftag1;
        this.wettkampftag2 = wettkampftag2;
        this.wettkampftag3 = wettkampftag3;
        this.wettkampftag4 = wettkampftag4;
        this.wettkampftageSchnitt = wettkampftageSchnitt;
    }

    // Getters and Setters
    public Long getVeranstaltungId() {
        return veranstaltungId;
    }
    public void setVeranstaltungId(Long veranstaltungId) {
        this.veranstaltungId = veranstaltungId;
    }

    public Long getWettkampfId() {
        return wettkampfId;
    }
    public void setWettkampfId(Long wettkampfId) {
        this.wettkampfId = wettkampfId;
    }

    public int getWettkampfTag() {
        return wettkampfTag;
    }
    public void setWettkampfTag(int wettkampfTag) {
        this.wettkampfTag = wettkampfTag;
    }

    public Long getVereinId() {
        return vereinId;
    }
    public void setVereinId(Long vereinId) {
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

    public int getRueckenNummer() {
        return rueckenNummer;
    }

    public float getWettkampftag1() {return wettkampftag1;}

    public float getWettkampftag2() {return wettkampftag2;}

    public float getWettkampftag3() {return wettkampftag3;}

    public float getWettkampftag4() {return wettkampftag4;}

    public float getWettkampftageSchnitt() {return wettkampftageSchnitt;}


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SchuetzenstatistikWettkampfDTO that = (SchuetzenstatistikWettkampfDTO)  o;
        return wettkampfTag == that.wettkampfTag &&
                veranstaltungId.equals(that.veranstaltungId) &&
                wettkampfId.equals(that.wettkampfId) &&
                vereinId.equals(that.vereinId) &&
                dsbMitgliedId.equals(that.dsbMitgliedId) &&
                Objects.equals(dsbMitgliedName, that.dsbMitgliedName) &&
                rueckenNummer == that.rueckenNummer &&
                wettkampftag1 == that.wettkampftag1 &&
                wettkampftag2 == that.wettkampftag2 &&
                wettkampftag3 == that.wettkampftag3 &&
                wettkampftag4 == that.wettkampftag4 &&
                wettkampftageSchnitt == that.wettkampftageSchnitt;
    }

    @Override
    public int hashCode() {
        return Objects.hash(veranstaltungId, wettkampfId, wettkampfTag, vereinId,
                dsbMitgliedId, dsbMitgliedName, rueckenNummer, wettkampftag1, wettkampftag2,
                wettkampftag3,wettkampftag4, wettkampftageSchnitt);
    }
}
