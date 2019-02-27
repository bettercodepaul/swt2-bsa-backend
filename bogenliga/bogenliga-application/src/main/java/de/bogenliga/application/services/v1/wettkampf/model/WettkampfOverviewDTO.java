package de.bogenliga.application.services.v1.wettkampf.model;

import de.bogenliga.application.common.service.types.DataTransferObject;

import java.time.OffsetDateTime;

/**
 *  *I´m the data transfer object of the wettkampf.
 *
 *  *I define the payload for the external REST interface of the wettkampf business entity.
 *
 * @author Marvin Holm, Daniel Schott
 */
public class WettkampfOverviewDTO implements DataTransferObject {


    private Long id;
    private String datum;
    private String wettkampfOrt;
    private String wettkampfBeginn;
    private Long wettkampfTag;
    private Long wettkampfDisziplinId;
    private String disziplinName;

    public WettkampfOverviewDTO(Long wettkampfId, String datum, String wettkampfOrt,
                                String wettkampfBeginn, Long wettkampfTag,
                                Long wettkampfDisziplinId, String disziplinName) {
        this.setId(wettkampfId);
        this.setDatum(datum);
        this.setWettkampfOrt(wettkampfOrt);
        this.setWettkampfBeginn(wettkampfBeginn);
        this.setWettkampfTag(wettkampfTag);
        this.setWettkampfDisziplinId(wettkampfDisziplinId);
        this.setDisziplinName(disziplinName);
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public String getDatum() {
        return datum;
    }


    public void setDatum(String datum) {
        this.datum = datum;
    }


    public String getWettkampfOrt() {
        return wettkampfOrt;
    }


    public void setWettkampfOrt(String wettkampfOrt) {
        this.wettkampfOrt = wettkampfOrt;
    }


    public String getWettkampfBeginn() {
        return wettkampfBeginn;
    }


    public void setWettkampfBeginn(String wettkampfBeginn) {
        this.wettkampfBeginn = wettkampfBeginn;
    }


    public Long getWettkampfTag() {
        return wettkampfTag;
    }


    public void setWettkampfTag(Long wettkampfTag) {
        this.wettkampfTag = wettkampfTag;
    }


    public Long getWettkampfDisziplinId() {
        return wettkampfDisziplinId;
    }


    public void setWettkampfDisziplinId(Long wettkampfDisziplinId) {
        this.wettkampfDisziplinId = wettkampfDisziplinId;
    }

    public String getDisziplinName() {
        return disziplinName;
    }

    public void setDisziplinName(String disziplinName) {
        this.disziplinName = disziplinName;
    }
}

