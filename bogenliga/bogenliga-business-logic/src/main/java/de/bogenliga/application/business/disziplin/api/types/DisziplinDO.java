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
    private Long id;
    private String disziplinName;

    public DisziplinDO(){}

    public DisziplinDO(Long id)
    {
        this.id = id;
    }

    public DisziplinDO(Long id, String disziplinName)
    {
        this.id = id;
        this.disziplinName = disziplinName;
    }

    public DisziplinDO(Long id, String disziplinName, final OffsetDateTime createdAtUtc,
                       final Long createdByUserId, final OffsetDateTime lastModifiedUtc,
                       final Long lastModifiedByUserId, final Long version)
    {
        this.id = id;
        this.disziplinName = disziplinName;

        this.setCreatedAtUtc(createdAtUtc);
        this.setCreatedByUserId(createdByUserId);
        this.setLastModifiedAtUtc(lastModifiedUtc);
        this.setLastModifiedByUserId(lastModifiedByUserId);
        this.setVersion(version);
    }

    public Long getDisziplinId() {
        return this.id;
    }


    public void setDisziplinId(Long id) {
        this.id = id;
    }


    public String getDisziplinName() {
        return disziplinName;
    }


    public void setDisziplinName(String disziplinName) {
        this.disziplinName = disziplinName;
    }
}
