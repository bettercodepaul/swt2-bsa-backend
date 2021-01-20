package de.bogenliga.application.services.v1.wettkampf.model;

import java.sql.Date;
import de.bogenliga.application.common.service.types.DataTransferObject;

/**
 * *I´m the data transfer object of the wettkampf.
 * <p>
 * *I define the payload for the external REST interface of the wettkampf business entity.
 *
 * @author Marvin Holm, Daniel Schott
 */
public class WettkampfDTO implements DataTransferObject {


    private Long id;
    private Long wettkampfVeranstaltungsId;
    private String wettkampfDatum;
    private String wettkampfStrasse;
    private String wettkampfPlz;
    private String wettkampfOrtsname;
    private String wettkampfOrtsinfo;
    private String wettkampfBeginn;
    private Long wettkampfTag;
    private Long wettkampfDisziplinId;
    private Long wettkampfTypId;
    private Long version;
    private Long wettkampfAusrichter;


    public WettkampfDTO(Long wettkampfId, Long wettkampfVeranstaltungsId, Date wettkampfDatum,
                        String wettkampfStrasse,
                        String wettkampfPlz, String wettkampfOrtsname, String wettkampfOrtsinfo,
                        String wettkampfBeginn, Long wettkampfTag,
                        Long wettkampfDisziplinId, Long wettkampfTypId,
                        Long version, Long wettkampfAusrichter) {

        this.setId(wettkampfId);
        this.setwettkampfVeranstaltungsId(wettkampfVeranstaltungsId);
        this.setDatum(wettkampfDatum);
        this.setWettkampfStrasse(wettkampfStrasse);
        this.setWettkampfPlz(wettkampfPlz);
        this.setWettkampfOrtsname(wettkampfOrtsname);
        this.setWettkampfOrtsinfo(wettkampfOrtsinfo);
        this.setWettkampfBeginn(wettkampfBeginn);
        this.setWettkampfTag(wettkampfTag);
        this.setWettkampfDisziplinId(wettkampfDisziplinId);
        this.setWettkampfTypId(wettkampfTypId);
        this.setVersion(version);
        this.setWettkampfAusrichter(wettkampfAusrichter);

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


    public String getWettkampfStrasse() {
        return wettkampfStrasse;
    }


    public void setWettkampfStrasse(String wettkampfStrasse) {
        this.wettkampfStrasse = wettkampfStrasse;
    }


    public String getWettkampfPlz() {
        return wettkampfPlz;
    }


    public void setWettkampfPlz(String wettkampfPlz) {
        this.wettkampfPlz = wettkampfPlz;
    }


    public String getWettkampfOrtsname() {
        return wettkampfOrtsname;
    }


    public void setWettkampfOrtsname(String wettkampfOrtsname) {
        this.wettkampfOrtsname = wettkampfOrtsname;
    }


    public String getWettkampfOrtsinfo() {
        return wettkampfOrtsinfo;
    }


    public void setWettkampfOrtsinfo(String wettkampfOrtsinfo) {
        this.wettkampfOrtsinfo = wettkampfOrtsinfo;
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


    public Long getWettkampfAusrichter() {
        return wettkampfAusrichter;
    }


    public void setWettkampfAusrichter(Long wettkampfAusrichter) {
        this.wettkampfAusrichter = wettkampfAusrichter;
    }


    @Override
    public String toString() {
        return "WettkampfDTO{" +
                "id='" + this.id + '\'' +
                ", wettkampfVeranstaltungsId='" + this.wettkampfVeranstaltungsId + '\'' +
                ", wettkampfDatum='" + this.wettkampfDatum + '\'' +
                ", wettkampfStrasse='"+ this.wettkampfStrasse + '\'' +
                ", wettkampfPlz='"+ this.wettkampfPlz + '\'' +
                ", wettkampfOrtsname='"+ this.wettkampfOrtsname + '\'' +
                ", wettkampfOrtsinfo='"+ this.wettkampfOrtsinfo + '\'' +
                ", wettkampfBeginn='" + this.wettkampfBeginn + '\'' +
                ", wettkampfTag='" + this.wettkampfTag + '\'' +
                ", wettkampfDisziplinId='" + this.wettkampfDisziplinId + '\'' +
                ", wettkampfTypId='" + this.wettkampfTypId + '\'' +
                ", version='" + this.version + '\'' +
                ", wettkampfAusrichter='" + this.wettkampfAusrichter +
                "}";
    }


}