package de.bogenliga.application.business.wettkampf.api.types;

import de.bogenliga.application.common.component.types.CommonDataObject;
import de.bogenliga.application.common.component.types.DataObject;

import java.sql.Date;
import java.util.Objects;

public class WettkampfOverviewDO extends CommonDataObject implements DataObject {
    private static final long serialVersionUID = 298357103627898987L;

    /**
     * business parameter
     */
    private Long id;
    private String ligaName;
    private Long veranstaltungsId;
    private String datum;
    private String wettkampfOrt;
    private String wettkampfBeginn;
    private Long wettkampfTag;
    private Long disziplinID;
    private String disziplinName;

    /**
     * Constructor with optional parameters
     */
    public WettkampfOverviewDO(final Long id, final String datum, final String wettkampfOrt,
                       final String wettkampfBeginn, final Long wettkampfTag, final Long wettkampfDisziplinId,
                       final String disziplinName) {
        this.id = id;
        this.datum = datum;
        this.wettkampfOrt = wettkampfOrt;
        this.wettkampfBeginn = wettkampfBeginn;
        this.wettkampfTag = wettkampfTag;
        this.disziplinID = wettkampfDisziplinId;
        this.disziplinName = disziplinName;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WettkampfOverviewDO that = (WettkampfOverviewDO) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(ligaName, that.ligaName) &&
                Objects.equals(datum, that.datum) &&
                Objects.equals(wettkampfOrt, that.wettkampfOrt) &&
                Objects.equals(wettkampfBeginn, that.wettkampfBeginn) &&
                Objects.equals(wettkampfTag, that.wettkampfTag) &&
                Objects.equals(disziplinID, that.disziplinID) &&
                Objects.equals(disziplinName, that.disziplinName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ligaName, datum, wettkampfOrt, wettkampfBeginn, wettkampfTag, disziplinID, disziplinName);
    }
}
