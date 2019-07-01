package de.bogenliga.application.business.tabletsession.api.types;

import java.time.OffsetDateTime;
import de.bogenliga.application.common.component.types.CommonDataObject;
import de.bogenliga.application.common.component.types.DataObject;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class TabletSessionDO extends CommonDataObject implements DataObject {
    private Long wettkampfId;
    private Long scheibennummer;

    public TabletSessionDO(Long wettkampfId, Long scheibennummer) {
        this.wettkampfId = wettkampfId;
        this.scheibennummer = scheibennummer;
    }

    public TabletSessionDO(Long wettkampfId, Long scheibennummer, final OffsetDateTime createdAtUtc,
                           final Long createdByUserId, final OffsetDateTime lastModifiedAtUtc,
                           final Long lastModifiedByUserId, final Long version) {
        this.wettkampfId = wettkampfId;
        this.scheibennummer = scheibennummer;

        // set parameter from CommonDataObject
        this.createdAtUtc = createdAtUtc;
        this.createdByUserId = createdByUserId;
        this.lastModifiedAtUtc = lastModifiedAtUtc;
        this.lastModifiedByUserId = lastModifiedByUserId;
        this.version = version;
    }


    public Long getWettkampfId() {
        return wettkampfId;
    }


    public void setWettkampfId(Long wettkampfId) {
        this.wettkampfId = wettkampfId;
    }


    public Long getScheibennummer() {
        return scheibennummer;
    }


    public void setScheibennummer(Long scheibennummer) {
        this.scheibennummer = scheibennummer;
    }
}
