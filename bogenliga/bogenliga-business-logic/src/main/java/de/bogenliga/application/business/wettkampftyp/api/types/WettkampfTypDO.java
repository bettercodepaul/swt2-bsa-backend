package de.bogenliga.application.business.wettkampftyp.api.types;

import java.time.OffsetDateTime;
import de.bogenliga.application.common.component.types.CommonDataObject;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class WettkampfTypDO extends CommonDataObject {
    private Long id;
    private String name;


    public WettkampfTypDO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public WettkampfTypDO(Long id, String name,final OffsetDateTime createdAtUtc,
                          final Long createdByUserId, final OffsetDateTime lastModifiedUtc,
                          final Long lastModifiedByUserId, final Long version) {
        this.id = id;
        this.name = name;
        this.createdByUserId = createdByUserId;
        this.createdAtUtc = createdAtUtc;
        this.lastModifiedAtUtc = lastModifiedUtc;
        this.version = version;
        this.lastModifiedByUserId = lastModifiedByUserId;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }
}
