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
    private String wettkampfOrt;
    private String wettkampfBeginn;
    private Long wettkampfTag;
    private Long wettkampfDisziplinId;
    private Long wettkampfTypId;


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


    public String getWettkampfOrt() {
        return wettkampfOrt;
    }


    public void setWettkampfOrt(final String wettkampfOrt) {
        this.wettkampfOrt = wettkampfOrt;
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


    @Override
    public String toString() {
        return "WettkampfId = " + getId() + "\n" +
                "VeranstaltungsId =  " + getVeranstaltungsId() + "\n" +
                "Datum = " + getDatum() + "\n" +
                "Wettkampf Ort = " + getWettkampfOrt() + "\n" +
                "Wettkampfbeginn = " + getWettkampfBeginn() + "\n" +
                "Wettkampftag = " + getWettkampfTag() + "\n" +
                "WettkampfdiziplinId = " + getWettkampfDisziplinId() + "\n" +
                "WettkampftypId = " + getWettkampfTypId();
    }
}
