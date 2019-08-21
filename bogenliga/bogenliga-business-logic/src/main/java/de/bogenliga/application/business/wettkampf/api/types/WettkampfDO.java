package de.bogenliga.application.business.wettkampf.api.types;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.sql.Date;
import de.bogenliga.application.common.component.types.CommonDataObject;
import de.bogenliga.application.common.component.types.DataObject;

/**
 * Contains the values of the wettkampf business entity.
 *
 * @author Daniel Schott
 */
public class WettkampfDO extends CommonDataObject implements DataObject {


    private static final long serialVersionUID = -3541537678685603149L;
    private Long id;
    private Long wettkampfVeranstaltungsId;
    private Date wettkampfDatum;
    private String wettkampfOrt;
    private String wettkampfBeginn;
    private Long wettkampfTag;
    private Long wettkampfDisziplinId;
    private Long wettkampfTypId;


    public WettkampfDO(final Long id, final Long veranstaltungsId, final Date datum, final String wettkampfOrt,
                       final String wettkampfBeginn, final Long wettkampfTag, final Long wettkampfDisziplinId,
                       final Long wettkampfTypId,
                       final OffsetDateTime createdAtUtc, final Long createdByUserId,
                       final Long version) {
        this.id = id;
        this.wettkampfVeranstaltungsId = veranstaltungsId;
        this.wettkampfDatum = datum;
        this.wettkampfOrt = wettkampfOrt;
        this.wettkampfBeginn = wettkampfBeginn;
        this.wettkampfTag = wettkampfTag;
        this.wettkampfDisziplinId = wettkampfDisziplinId;
        this.wettkampfTypId = wettkampfTypId;
        this.createdAtUtc = createdAtUtc;
        this.createdByUserId = createdByUserId;
        this.version = version;

    }


    /**
     * Constructor with optional parameters
     *
     * @param id
     * @param wettkampfVeranstaltungsId
     * @param wettkampfDatum
     * @param wettkampfOrt
     * @param wettkampfBeginn
     * @param wettkampfTag
     * @param wettkampfDisziplinId
     * @param wettkampfTypId
     * @param version
     */
    public WettkampfDO(final Long id, final Long wettkampfVeranstaltungsId, final Date wettkampfDatum, final String wettkampfOrt,
                       final String wettkampfBeginn, final Long wettkampfTag, final Long wettkampfDisziplinId,
                       final Long wettkampfTypId,
                       final Long version) {
        this.id = id;
        this.wettkampfVeranstaltungsId = wettkampfVeranstaltungsId;
        this.wettkampfDatum = wettkampfDatum;
        this.wettkampfOrt = wettkampfOrt;
        this.wettkampfBeginn = wettkampfBeginn;
        this.wettkampfTag = wettkampfTag;
        this.wettkampfDisziplinId = wettkampfDisziplinId;
        this.wettkampfTypId = wettkampfTypId;
        this.version = version;

    }


    /**
     * Constructor with id for deleting existing entries
     *
     * @param id
     */
    public WettkampfDO(final Long id) {
        this.id = id;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWettkampfVeranstaltungsId() {
        return wettkampfVeranstaltungsId;
    }

    public void setWettkampfVeranstaltungsId(Long wettkampfVeranstaltungsId) {
        this.wettkampfVeranstaltungsId = wettkampfVeranstaltungsId;
    }

    public Date getWettkampfDatum() {
        return wettkampfDatum;
    }

    public void setWettkampfDatum(Date wettkampfDatum) {
        this.wettkampfDatum = wettkampfDatum;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WettkampfDO that = (WettkampfDO) o;
        return id.equals(that.id) &&
                wettkampfVeranstaltungsId.equals(that.wettkampfVeranstaltungsId) &&
                Objects.equals(wettkampfDatum, that.wettkampfDatum) &&
                Objects.equals(wettkampfOrt, that.wettkampfOrt) &&
                Objects.equals(wettkampfBeginn, that.wettkampfBeginn) &&
                wettkampfTag.equals(that.wettkampfTag) &&
                Objects.equals(wettkampfDisziplinId, that.wettkampfDisziplinId) &&
                Objects.equals(wettkampfTypId, that.wettkampfTypId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, wettkampfVeranstaltungsId, wettkampfDatum, wettkampfOrt, wettkampfBeginn, wettkampfTag, wettkampfDisziplinId, wettkampfTypId);
    }
}
