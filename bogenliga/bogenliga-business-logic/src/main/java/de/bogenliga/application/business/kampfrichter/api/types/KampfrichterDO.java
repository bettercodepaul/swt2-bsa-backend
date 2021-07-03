package de.bogenliga.application.business.kampfrichter.api.types;

import java.time.OffsetDateTime;
import java.util.Objects;
import de.bogenliga.application.common.component.types.CommonDataObject;
import de.bogenliga.application.common.component.types.DataObject;

/**
 * Contains the values of the kampfrichter business entity.
 *
 * @author Rahul Pöse
 */
public class KampfrichterDO extends CommonDataObject implements DataObject {
    private static final long serialVersionUID = 8559563978424033907L;

    /**
     * business parameter
     */
    private Long userId;
    private String vorname;
    private String nachname;
    private String kampfrichterEmail;
    private Long wettkampfId;
    private boolean leitend;

    /**
     * Constructor with optional parameters
     * @param userId
     * @param wettkampfId
     * @param leitend
     * @param createdAtUtc
     * @param createdByUserId
     * @param lastModifiedAtUtc
     * @param lastModifiedByUserId
     * @param version
     */
    public KampfrichterDO(final Long userId, final Long wettkampfId, final boolean leitend,
                          final OffsetDateTime createdAtUtc,
                          final Long createdByUserId, final OffsetDateTime lastModifiedAtUtc,
                          final Long lastModifiedByUserId, final Long version){
        this.userId = userId;
        this.wettkampfId = wettkampfId;
        this.leitend = leitend;
        this.createdAtUtc = createdAtUtc;
        this.createdByUserId = createdByUserId;
        this.lastModifiedAtUtc = lastModifiedAtUtc;
        this.lastModifiedByUserId = lastModifiedByUserId;
        this.version = version;
    }

    /**
     * Constructor with mandatory parameters
     * @param userId
     * @param wettkampfId
     * @param leitend
     * @param createdAtUtc
     * @param createdByUserId
     * @param version
     */
    public KampfrichterDO(final Long userId, final Long wettkampfId, final boolean leitend,
                          final OffsetDateTime createdAtUtc,
                          final Long createdByUserId, final Long version) {
        this.userId = userId;
        this.wettkampfId = wettkampfId;
        this.leitend = leitend;
        this.createdAtUtc = createdAtUtc;
        this.createdByUserId = createdByUserId;
        this.version = version;
    }

    /**
     * Constructor with optional parameters
     * @param userId
     * @param wettkampfId
     * @param leitend
     */
    public KampfrichterDO(final Long userId, final Long wettkampfId, final boolean leitend){
        this.userId = userId;
        this.wettkampfId = wettkampfId;
        this.leitend = leitend;
    }

    /**
     * Constructor with optional parameters
     * @param userId
     */
    public KampfrichterDO(final Long userId){
        this.userId = userId;
        this.leitend = false;
    }
    /**
     * Constructor with optional parameters
     * @param userId
     * @param wettkampfId
     * @param leitend
     * @param kampfrichterVorname
     * @param nachname
     * @param kampfrichterEmail
     */
    public KampfrichterDO(final Long userId, String kampfrichterVorname, String nachname, String kampfrichterEmail, final Long wettkampfId, final boolean leitend){
        this.userId = userId;
        this.vorname =kampfrichterVorname;
        this.nachname = nachname;
        this.kampfrichterEmail = kampfrichterEmail;
        this.wettkampfId = wettkampfId;
        this.leitend = leitend;
    }


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getWettkampfId() {
        return wettkampfId;
    }

    public void setWettkampfId(Long wettkampfId) {
        this.wettkampfId = wettkampfId;
    }

    public boolean isLeitend() {
        return leitend;
    }

    public void setLeitend(boolean leitend) {
        this.leitend = leitend;
    }


    public String getVorname() {
        return vorname;
    }


    public void setVorname(String vorname) {
        this.vorname = vorname;
    }


    public String getNachname() {
        return nachname;
    }


    public void setNachname(String nachname) {
        this.nachname = nachname;
    }


    public String getKampfrichterEmail() {
        return kampfrichterEmail;
    }


    public void setKampfrichterEmail(String kampfrichterEmail) {
        this.kampfrichterEmail = kampfrichterEmail;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof KampfrichterDO)) {
            return false;
        }
        KampfrichterDO that = (KampfrichterDO) o;
        return isLeitend() == that.isLeitend() && Objects.equals(getUserId(),
                that.getUserId()) && Objects.equals(getVorname(),
                that.getVorname()) && Objects.equals(getNachname(),
                that.getNachname()) && Objects.equals(getKampfrichterEmail(),
                that.getKampfrichterEmail()) && Objects.equals(getWettkampfId(), that.getWettkampfId());
    }


    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getVorname(), getNachname(), getKampfrichterEmail(),
                getWettkampfId(),
                isLeitend());
    }
}