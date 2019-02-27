package de.bogenliga.application.business.wettkampf.impl.entity;

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
public class WettkampfOverviewBE extends CommonBusinessEntity implements BusinessEntity {

    private static final long serialVersionUID = 7307883175430867611L;
    private Long id;
    private String ligaName;
    private String datum;
    private String wettkampfOrt;
    private String wettkampfBeginn;
    private Long wettkampfTag;
    private Long disziplinID;
    private String disziplinName;


    /**
     * Constructor
     */
    public WettkampfOverviewBE() {
        // empty
    }


    public Long getId() {
        return id;
    }

    public String getLigaName() {
        return ligaName;
    }

    public String getDatum() {
        return datum;
    }

    public String getWettkampfOrt() {
        return wettkampfOrt;
    }

    public String getWettkampfBeginn() {
        return wettkampfBeginn;
    }

    public Long getWettkampfTag() {
        return wettkampfTag;
    }

    public Long getDisziplinID() {
        return disziplinID;
    }

    public String getDisziplinName() {
        return disziplinName;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public void setLigaName(String ligaName) {
        this.ligaName = ligaName;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public void setWettkampfOrt(String wettkampfOrt) {
        this.wettkampfOrt = wettkampfOrt;
    }

    public void setWettkampfBeginn(String wettkampfBeginn) {
        this.wettkampfBeginn = wettkampfBeginn;
    }

    public void setWettkampfTag(Long wettkampfTag) {
        this.wettkampfTag = wettkampfTag;
    }

    public void setDisziplinID(Long disziplinID) {
        this.disziplinID = disziplinID;
    }

    public void setDisziplinName(String disziplinName) {
        this.disziplinName = disziplinName;
    }


    @Override
    public String toString() {
        return "Wettkampf ID = " + getId() + "\n" +
                "Liga Name = " + getLigaName() + "\n" +
                "Datum = " + getDatum() + "\n" +
                "Wettkampf Ort = " + getWettkampfOrt() + "\n" +
                "Wettkampfbeginn = " + getWettkampfBeginn() + "\n" +
                "Wettkampftag = " + getWettkampfTag() + "\n" +
                "DisziplinID = " + getDisziplinID() + "\n" +
                "Disziplin = " + getDisziplinName() + "\n";
    }
}
