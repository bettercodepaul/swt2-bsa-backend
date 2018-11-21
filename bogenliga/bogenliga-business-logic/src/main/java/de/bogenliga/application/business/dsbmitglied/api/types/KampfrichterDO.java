package de.bogenliga.application.business.dsbmitglied.api.types;

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

    public KampfrichterDO(final Long userId){
        this.userId = userId;
        this. wettkampfId = 9999;
        this.leitend = false;
    }


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long id) {
        this.userId = id;
    }

    public long getWettkampfId() {
        return wettkampfId;
    }

    public void setWettkampfId(int wettkampfId) {
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
        return Objects.hash(userId, wettkampfId, leitend);
    }


    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final KampfrichterDO that = (KampfrichterDO) o;
        return userId == that.userId &&
                wettkampfId == that.wettkampfId &&
                leitend == that.leitend &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(wettkampfId, that.wettkampfId) &&
                Objects.equals(leitend, that.leitend);
    }
}