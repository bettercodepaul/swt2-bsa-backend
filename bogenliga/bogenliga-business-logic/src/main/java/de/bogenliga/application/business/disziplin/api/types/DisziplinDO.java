package de.bogenliga.application.business.disziplin.api.types;

import java.time.OffsetDateTime;
import de.bogenliga.application.common.component.types.CommonDataObject;
import de.bogenliga.application.common.component.types.DataObject;

/**
 *
 * @author Marcel Neumann, marcel.neumann@student.reutlingen-university.de
 * @author Robin Mueller, robin.mueller@student.reutlingen-university.de
 */
public class DisziplinDO extends CommonDataObject implements DataObject {
    private Long disziplinID;
    private String disziplinName;

    public DisziplinDO(Long disziplinID)
    {
        this.disziplinID = disziplinID;
    }

    public DisziplinDO(Long disziplinID, String disziplinName)
    {
        this.disziplinID = disziplinID;
        this.disziplinName = disziplinName;
    }

    public DisziplinDO(Long disziplinID, String disziplinName, final OffsetDateTime createdAtUtc,
                       final Long createdByUserId, final OffsetDateTime lastModifiedUtc,
                       final Long lastModifiedByUserId, final Long version)
    {
        this.disziplinID = disziplinID;
        this.disziplinName = disziplinName;

        this.setCreatedAtUtc(createdAtUtc);
        this.setCreatedByUserId(createdByUserId);
        this.setLastModifiedAtUtc(lastModifiedUtc);
        this.setLastModifiedByUserId(lastModifiedByUserId);
        this.setVersion(version);
    }

    public Long getDisziplinID() {
        return disziplinID;
    }


    public void setDisziplinID(Long disziplinID) {
        this.disziplinID = disziplinID;
    }


    public String getDisziplinName() {
        return disziplinName;
    }


    public void setDisziplinName(String disziplinName) {
        this.disziplinName = disziplinName;
    }
}
