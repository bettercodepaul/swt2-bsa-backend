package de.bogenliga.application.business.kampfrichter.api.types;

import java.time.OffsetDateTime;
import java.util.Objects;
import de.bogenliga.application.common.component.types.CommonDataObject;
import de.bogenliga.application.common.component.types.DataObject;

/**
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class KampfrichterDO extends CommonDataObject implements DataObject {
    private static final long serialVersionUID = 8559563978424033907L;

    /**
     * business parameter
     */
    private Long userId;
    private long wettkampfId;
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
    public KampfrichterDO(final Long userId, final long wettkampfId, final boolean leitend,
                          final OffsetDateTime createdAtUtc,
                          final Long createdByUserId, final OffsetDateTime lastModifiedAtUtc,
                          final Long lastModifiedByUserId, final Long version){
        this.userId = userId;
        this. wettkampfId = wettkampfId;
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
    public KampfrichterDO(final Long userId, final long wettkampfId, final boolean leitend,
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
    public KampfrichterDO(final Long userId, final long wettkampfId, final boolean leitend){
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


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public long getWettkampfId() {
        return wettkampfId;
    }

    public void setWettkampfId(long wettkampfId) {
        this.wettkampfId = wettkampfId;
    }

    public boolean isLeitend() {
        return leitend;
    }

    public void setLeitend(boolean leitend) {
        this.leitend = leitend;
    }


    @Override
    public int hashCode() {
        return Objects.hash(userId, wettkampfId, leitend,
                createdByUserId, lastModifiedAtUtc,
                lastModifiedByUserId, version);
    }


    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final de.bogenliga.application.business.kampfrichter.api.types.KampfrichterDO that = (de.bogenliga.application.business.kampfrichter.api.types.KampfrichterDO) o;
        return userId == that.userId &&
                wettkampfId == that.wettkampfId &&
                leitend == that.leitend &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(wettkampfId, that.wettkampfId) &&
                Objects.equals(leitend, that.leitend);
    }
}