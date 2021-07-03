package de.bogenliga.application.services.v1.schuetzenstatistik.model;

import de.bogenliga.application.common.service.types.DataTransferObject;

import java.util.Objects;

/**
 * I'm the data transfer object of the liga.
 * <p>
 * I define the payload for the external REST interface of the liga business entity.
 *
 * @author Giuseppe Ferrera, giuseppe.ferrera@student.reutlingen-university.de
 */
public class SchuetzenstatistikDTO implements DataTransferObject {

    /**
     * business parameter
     */


    private Long veranstaltungId;
    private String veranstaltungName;
    private Long wettkampfId;
    private int wettkampfTag;
    private Long mannschaftId;
    private int mannschaftNummer;
    private Long vereinId;
    private String vereinName;

    /**
     * Constructor with optional parameters
     *
     * @param veranstaltungId;
     * @param veranstaltungName;
     * @param wettkampfId;
     * @param wettkampfTag;
     * @param mannschaftId;
     * @param mannschaftNummer;
     * @param vereinId;
     * @param vereinName;

     */
    public SchuetzenstatistikDTO(
            Long veranstaltungId,
            String veranstaltungName,
            Long wettkampfId,
            int wettkampfTag,
            Long mannschaftId,
            int mannschaftNummer,
            Long vereinId,
            String vereinName
    ) {
        this.veranstaltungId=veranstaltungId;
        this.veranstaltungName = veranstaltungName;
        this.wettkampfId = wettkampfId;
        this.wettkampfTag = wettkampfTag;
        this.mannschaftId = mannschaftId;
        this.mannschaftNummer = mannschaftNummer;
        this.vereinId = vereinId;
        this.vereinName = vereinName;
    }


    public SchuetzenstatistikDTO() {
        // empty constructor
    }

    public Long getVeranstaltungId() {
        return veranstaltungId;
    }

    public void setVeranstaltungId(Long veranstaltungId) {
        this.veranstaltungId = veranstaltungId;
    }

    public String getVeranstaltungName() {
        return veranstaltungName;
    }

    public void setVeranstaltungName(String veranstaltungName) {
        this.veranstaltungName = veranstaltungName;
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

    public Long getMannschaftId() {
        return mannschaftId;
    }

    public void setMannschaftId(Long mannschaftId) {
        this.mannschaftId = mannschaftId;
    }

    public int getMannschaftNummer() {
        return mannschaftNummer;
    }

    public void setMannschaftNummer(int mannschaftNummer) {
        this.mannschaftNummer = mannschaftNummer;
    }

    public Long getVereinId() {
        return vereinId;
    }

    public void setVereinId(Long vereinId) {
        this.vereinId = vereinId;
    }

    public String getVereinName() {
        return vereinName;
    }

    public void setVereinName(String vereinName) {
        this.vereinName = vereinName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SchuetzenstatistikDTO that = (SchuetzenstatistikDTO) o;
        return wettkampfTag == that.wettkampfTag &&
                mannschaftNummer == that.mannschaftNummer &&
                veranstaltungId.equals(that.veranstaltungId) &&
                Objects.equals(veranstaltungName, that.veranstaltungName) &&
                wettkampfId.equals(that.wettkampfId) &&
                mannschaftId.equals(that.mannschaftId) &&
                vereinId.equals(that.vereinId) &&
                Objects.equals(vereinName, that.vereinName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(veranstaltungId, veranstaltungName, wettkampfId, wettkampfTag,
                mannschaftId, mannschaftNummer, vereinId, vereinName);
    }
}
