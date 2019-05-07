package de.bogenliga.application.services.v1.wettkampf.model;

import java.time.OffsetDateTime;
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
    private Long veranstaltungsId;
    private String datum;
    private String wettkampfOrt;
    private String wettkampfBeginn;
    private Long wettkampfTag;
    private Long wettkampfDisziplinId;
    private Long wettkampfTypId;
    private OffsetDateTime createdAtUtc;
    private Long createdByUserId;
    private Long version;




    public WettkampfDTO(Long wettkampfId, Long veranstaltungsId, String datum, String wettkampfOrt,
                        String wettkampfBeginn, Long wettkampfTag,
                        Long wettkampfDisziplinId, Long wettkampfTypId,
                        Long createdByUserId, OffsetDateTime createdAtUtc, Long version){
        this.setId(wettkampfId);
        this.setVeranstaltungsId(veranstaltungsId);
        this.setDatum(datum);
        this.setWettkampfOrt(wettkampfOrt);
        this.setWettkampfBeginn(wettkampfBeginn);
        this.setWettkampfTag(wettkampfTag);
        this.setWettkampfDisziplinId(wettkampfDisziplinId);
        this.setWettkampfTypId(wettkampfTypId);
        this.setCreatedAtUtc(createdAtUtc);
        this.setCreatedByUserId(createdByUserId);
        this.setVersion(version);

    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public Long getVeranstaltungsId() {
        return veranstaltungsId;
    }


    public void setVeranstaltungsId(Long veranstaltungsId) {
        this.veranstaltungsId = veranstaltungsId;
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


    public Long getWettkampfTypId() {
        return wettkampfTypId;
    }


    public void setWettkampfTypId(Long wettkampfTypId) {
        this.wettkampfTypId = wettkampfTypId;
    }


    public OffsetDateTime getCreatedAtUtc() {
        return createdAtUtc;
    }


    public void setCreatedAtUtc(OffsetDateTime createdAtUtc) {
        this.createdAtUtc = createdAtUtc;
    }


    public Long getCreatedByUserId() {
        return createdByUserId;
    }


    public void setCreatedByUserId(Long createdByUserId) {
        this.createdByUserId = createdByUserId;
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
                ", veranstaltungId='" + this.veranstaltungsId + '\'' +
                ", datum='" + this.datum + '\'' +
                ", wettkampfOrt='" + this.wettkampfOrt + '\'' +
                ", wettkampfBeginn='" + this.wettkampfBeginn + '\'' +
                ", wettkampfTag='" + this.wettkampfTag + '\'' +
                ", wettkampfDisziplinId='" + this.wettkampfDisziplinId + '\'' +
                ", wettkampfTypId='" + this.wettkampfTypId + '\'' +
                ", createdAtUtc='" + this.createdAtUtc + '\'' +
                ", createdByUserId='" + this.createdByUserId + '\'' +
                ", version='" + this.version + '\'' +
                "}";
    }
}