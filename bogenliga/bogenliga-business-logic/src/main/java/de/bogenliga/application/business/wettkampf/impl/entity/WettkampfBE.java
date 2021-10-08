package de.bogenliga.application.business.wettkampf.impl.entity;

import java.sql.Date;
import de.bogenliga.application.common.component.entity.BusinessEntity;
import de.bogenliga.application.common.component.entity.CommonBusinessEntity;

/**
 * I represent the wettkampf business entity.
 * <p>
 * A wettkampf is a registered member of the DSB. The wettkampf is not necessarily a technical user of the system.
 * <p>
 * The {@link CommonBusinessEntity} contains the technical parameter. Business entities commonly use these parameters to
 * control their lifecycle.
 *
 * @see CommonBusinessEntity
 */
public class WettkampfBE extends CommonBusinessEntity implements BusinessEntity {

    private static final long serialVersionUID = 7307883175430867611L;
    private Long id;
    private Long veranstaltungsId;
    private Date datum;
    private String wettkampfStrasse;
    private String wettkampfPlz;
    private String wettkampfOrtsname;
    private String wettkampfOrtsinfo;
    private String wettkampfBeginn;
    private Long wettkampfTag;
    private Long wettkampfDisziplinId;
    private Long wettkampfTypId;
    private Long wettkampfAusrichter;


    /**
     * Constructor
     */
    public WettkampfBE() {
        // empty
    }


    public Long getId() {
        return id;
    }


    public void setId(final Long id) {
        this.id = id;
    }


    public Long getVeranstaltungsId() {
        return veranstaltungsId;
    }


    public void setVeranstaltungsId(final Long veranstaltungsId) {
        this.veranstaltungsId = veranstaltungsId;
    }


    public Date getDatum() {
        return datum;
    }


    public void setDatum(final Date datum) {
        this.datum = datum;
    }



    public String getWettkampfOrtsinfo() {
        return wettkampfOrtsinfo;
    }


    public void setWettkampfOrtsinfo(final String wettkampfOrtsinfo) {
        this.wettkampfOrtsinfo = wettkampfOrtsinfo;
    }


    public String getWettkampfOrtsname() {
        return wettkampfOrtsname;
    }


    public void setWettkampfOrtsname(final String wettkampfOrtsname) {
        this.wettkampfOrtsname = wettkampfOrtsname;
    }


    public String getWettkampfPlz() {
        return wettkampfPlz;
    }


    public void setWettkampfPlz(final String wettkampfPlz) {
        this.wettkampfPlz = wettkampfPlz;
    }


    public String getWettkampfStrasse() {
        return wettkampfStrasse;
    }


    public void setWettkampfStrasse(final String wettkampfStrasse) {
        this.wettkampfStrasse = wettkampfStrasse;
    }


    public String getWettkampfBeginn() {
        return wettkampfBeginn;
    }


    public void setWettkampfBeginn(final String wettkampfBeginn) {
        this.wettkampfBeginn = wettkampfBeginn;
    }


    public Long getWettkampfTag() {
        return wettkampfTag;
    }


    public void setWettkampfTag(final Long wettkampfTag) {
        this.wettkampfTag = wettkampfTag;
    }


    public Long getWettkampfDisziplinId() {
        return wettkampfDisziplinId;
    }


    public void setWettkampfDisziplinId(final Long wettkampfDisziplinId) {
        this.wettkampfDisziplinId = wettkampfDisziplinId;
    }


    public Long getWettkampfTypId() {
        return wettkampfTypId;
    }


    public void setWettkampfTypId(final Long wettkampfTypId) {
        this.wettkampfTypId = wettkampfTypId;
    }


    public Long getWettkampfAusrichter() {
        return wettkampfAusrichter;
    }


    public void setWettkampfAusrichter(Long wettkampfAusrichter) {
        this.wettkampfAusrichter = wettkampfAusrichter;
    }


    @Override
    public String toString() {
        return "WettkampfBE {\n" +
                "\tWettkampfId = " + getId() + ",\n" +
                "\tVeranstaltungsId =  " + getVeranstaltungsId() + ",\n" +
                "\tDatum = " + getDatum() + ",\n" +
                "\tWettkampfstrasse = " + getWettkampfStrasse() + ",\n" +
                "\tWettkampfplz = " + getWettkampfPlz() + ",\n" +
                "\tWettkampfortsname = " + getWettkampfOrtsname() + ",\n" +
                "\tWettkampfortsinfo = " + getWettkampfOrtsinfo() + ",\n" +
                "\tWettkampfbeginn = " + getWettkampfBeginn() + ",\n" +
                "\tWettkampftag = " + getWettkampfTag() + ",\n" +
                "\tWettkampfdiziplinId = " + getWettkampfDisziplinId() + ",\n" +
                "\tWettkampftypId = " + getWettkampfTypId() + ",\n" +
                "\tWettkampfAusrichter = " + getWettkampfAusrichter() + "\n" +
                "}";
    }


}
