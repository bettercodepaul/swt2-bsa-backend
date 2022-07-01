package de.bogenliga.application.services.v1.sync.model;

import java.sql.Date;
import java.util.Objects;
import de.bogenliga.application.common.service.types.DataTransferObject;

/**
 * I'm the data transfer object of the Wettkampf with offline token
 * separate from WettkampfDTO to be able to change it independently
 * @author Jonas Sigloch, SWT SoSe 2022
 */
public class WettkampfExtDTO implements DataTransferObject {

    // field names fit attributes in offline-wettkampf.interface.ts
    private Long id;
    private Long veranstaltungsId;
    private String datum;
    private String strasse;
    private String plz;
    private String ortsname;
    private String ortsinfo;
    private String beginn;
    private Long tag;
    private Long disziplinId;
    private Long wettkampfTypId;
    private Long version;
    private Long ausrichter;
    private String offlineToken;


    public WettkampfExtDTO(Long wettkampfId, Long wettkampfVeranstaltungsId, Date wettkampfDatum,
                           String wettkampfStrasse,
                           String wettkampfPlz, String wettkampfOrtsname, String wettkampfOrtsinfo,
                           String wettkampfBeginn, Long wettkampfTag,
                           Long wettkampfDisziplinId, Long wettkampfTypId,
                           Long version, Long wettkampfAusrichter, String offlineToken) {

        this.setId(wettkampfId);
        this.setVeranstaltungsId(wettkampfVeranstaltungsId);
        this.setDatum(wettkampfDatum);
        this.setStrasse(wettkampfStrasse);
        this.setPlz(wettkampfPlz);
        this.setOrtsname(wettkampfOrtsname);
        this.setOrtsinfo(wettkampfOrtsinfo);
        this.setBeginn(wettkampfBeginn);
        this.setTag(wettkampfTag);
        this.setDisziplinId(wettkampfDisziplinId);
        this.setWettkampfTypId(wettkampfTypId);
        this.setVersion(version);
        this.setAusrichter(wettkampfAusrichter);
        this.setOfflineToken(offlineToken);
    }


    /**
     * default constructor
     */
    public WettkampfExtDTO() {
        // empty
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

    public void setVeranstaltungsId(Long wettkampfVeranstaltungsId) {
        this.veranstaltungsId = wettkampfVeranstaltungsId;
    }

    public Date getDatum() {
        return Date.valueOf(datum);
    }

    public void setDatum(Date wettkampfDatum) {
        this.datum = wettkampfDatum.toString();
    }

    public String getStrasse() {
        return strasse;
    }

    public void setStrasse(String wettkampfStrasse) {
        this.strasse = wettkampfStrasse;
    }

    public String getPlz() {
        return plz;
    }

    public void setPlz(String wettkampfPlz) {
        this.plz = wettkampfPlz;
    }

    public String getOrtsname() {
        return ortsname;
    }

    public void setOrtsname(String wettkampfOrtsname) {
        this.ortsname = wettkampfOrtsname;
    }

    public String getOrtsinfo() {
        return ortsinfo;
    }

    public void setOrtsinfo(String wettkampfOrtsinfo) {
        this.ortsinfo = wettkampfOrtsinfo;
    }

    public String getBeginn() {
        return beginn;
    }

    public void setBeginn(String wettkampfBeginn) {
        this.beginn = wettkampfBeginn;
    }

    public Long getTag() {
        return tag;
    }

    public void setTag(Long wettkampfTag) {
        this.tag = wettkampfTag;
    }

    public Long getDisziplinId() {
        return disziplinId;
    }

    public void setDisziplinId(Long wettkampfDisziplinId) {
        this.disziplinId = wettkampfDisziplinId;
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

    public Long getAusrichter() {
        return ausrichter;
    }

    public void setAusrichter(Long wettkampfAusrichter) {
        this.ausrichter = wettkampfAusrichter;
    }

    public String getOfflineToken() {
        return offlineToken;
    }

    public void setOfflineToken(String offlineToken) {
        this.offlineToken = offlineToken;
    }


    @Override
    public String toString() {
        return "WettkampfExtDTO{" +
                "id='" + this.id + '\'' +
                ", wettkampfVeranstaltungsId='" + this.veranstaltungsId + '\'' +
                ", wettkampfDatum='" + this.datum + '\'' +
                ", wettkampfStrasse='"+ this.strasse + '\'' +
                ", wettkampfPlz='"+ this.plz + '\'' +
                ", wettkampfOrtsname='"+ this.ortsname + '\'' +
                ", wettkampfOrtsinfo='"+ this.ortsinfo + '\'' +
                ", wettkampfBeginn='" + this.beginn + '\'' +
                ", wettkampfTag='" + this.tag + '\'' +
                ", wettkampfDisziplinId='" + this.disziplinId + '\'' +
                ", wettkampfTypId='" + this.wettkampfTypId + '\'' +
                ", version='" + this.version + '\'' +
                ", wettkampfAusrichter='" + this.ausrichter + '\'' +
                ", offlineToken=" + this.offlineToken +
                "}";
    }
}
