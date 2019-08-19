package de.bogenliga.application.services.v1.wettkampf.model;

import java.time.OffsetDateTime;
import java.sql.Date;
import de.bogenliga.application.common.service.types.DataTransferObject;
/**
 *  *I´m the data transfer object of the wettkampf.
 *
 *  *I define the payload for the external REST interface of the wettkampf business entity.
 *
 * @author Marvin Holm, Daniel Schott
 */
public class WettkampfDTO implements DataTransferObject {


    private Long id;
    private Long wettkampfVeranstaltungsId;
    private String wettkampfDatum;
    private String wettkampfOrt;
    private String wettkampfBeginn;
    private Long wettkampfTag;
    private Long wettkampfDisziplinId;
    private Long wettkampfTypId;
    private Long version;




    public WettkampfDTO(Long wettkampfId, Long wettkampfVeranstaltungsId, Date wettkampfDatum, String wettkampfOrt,
                        String wettkampfBeginn, Long wettkampfTag,
                        Long wettkampfDisziplinId, Long wettkampfTypId,
                        Long version){
        this.setId(wettkampfId);
        this.setwettkampfVeranstaltungsId(wettkampfVeranstaltungsId);
        this.setDatum(wettkampfDatum);
        this.setWettkampfOrt(wettkampfOrt);
        this.setWettkampfBeginn(wettkampfBeginn);
        this.setWettkampfTag(wettkampfTag);
        this.setWettkampfDisziplinId(wettkampfDisziplinId);
        this.setWettkampfTypId(wettkampfTypId);
        this.setVersion(version);


    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public Long getwettkampfVeranstaltungsId() {
        return wettkampfVeranstaltungsId;
    }


    public void setwettkampfVeranstaltungsId(Long wettkampfVeranstaltungsId) {
        this.wettkampfVeranstaltungsId = wettkampfVeranstaltungsId;
    }


    public Date getDatum() {
        return Date.valueOf(wettkampfDatum);
    }


    public void setDatum(Date datum) {
        this.wettkampfDatum = datum.toString();
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


    public Long getWettkampfTypId() {
        return wettkampfTypId;
    }


    public void setWettkampfTypId(Long wettkampfTypId) {
        this.wettkampfTypId = wettkampfTypId;
    }


    public Long getVersion() {
        return version;
    }


    public void setVersion(Long version) {
        this.version = version;
    }


    @Override
    public String toString() {
        return "WettkampfDTO{" +
                "id='" + this.id + '\'' +
                ", wettkampfVeranstaltungsId='" + this.wettkampfVeranstaltungsId + '\'' +
                ", wettkampfDatum='" + this.wettkampfDatum + '\'' +
                ", wettkampfOrt='" + this.wettkampfOrt + '\'' +
                ", wettkampfBeginn='" + this.wettkampfBeginn + '\'' +
                ", wettkampfTag='" + this.wettkampfTag + '\'' +
                ", wettkampfDisziplinId='" + this.wettkampfDisziplinId + '\'' +
                ", wettkampfTypId='" + this.wettkampfTypId + '\'' +
                ", version='" + this.version + '\'' +
                "}";
    }
}