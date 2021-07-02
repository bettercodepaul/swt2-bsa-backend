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
    private String kampfrichterVorname;
    private String kampfrichterNachname;
    private String email;
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
     * @param kampfrichterNachname
     * @param email
     */
    public KampfrichterDO(final Long userId, String kampfrichterVorname, String kampfrichterNachname, String email, final Long wettkampfId, final boolean leitend){
        this.userId = userId;
        this.kampfrichterVorname=kampfrichterVorname;
        this.kampfrichterNachname=kampfrichterNachname;
        this.email=email;
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


    public String getKampfrichterVorname() {
        return kampfrichterVorname;
    }


    public void setKampfrichterVorname(String kampfrichterVorname) {
        this.kampfrichterVorname = kampfrichterVorname;
    }


    public String getKampfrichterNachname() {
        return kampfrichterNachname;
    }


    public void setKampfrichterNachname(String kampfrichterNachname) {
        this.kampfrichterNachname = kampfrichterNachname;
    }


    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
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
                that.getUserId()) && Objects.equals(getKampfrichterVorname(),
                that.getKampfrichterVorname()) && Objects.equals(getKampfrichterNachname(),
                that.getKampfrichterNachname()) && Objects.equals(getEmail(),
                that.getEmail()) && Objects.equals(getWettkampfId(), that.getWettkampfId());
    }


    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getKampfrichterVorname(), getKampfrichterNachname(), getEmail(),
                getWettkampfId(),
                isLeitend());
    }
}