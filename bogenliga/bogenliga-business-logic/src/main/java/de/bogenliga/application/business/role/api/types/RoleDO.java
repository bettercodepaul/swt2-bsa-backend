package de.bogenliga.application.business.role.api.types;

import de.bogenliga.application.common.component.types.CommonDataObject;
import de.bogenliga.application.common.component.types.DataObject;

import java.time.OffsetDateTime;
import java.util.Objects;

/**
 * Contains the values of the user business entity.
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class RoleDO extends CommonDataObject implements DataObject {
    private static final long serialVersionUID = 298357103627898987L;

    /**
     * business parameter
     */
    private Long id;
    private String roleName;


    /**
     * Constructor with optional parameters
     */
    public RoleDO() {
        // empty constructor
    }


    /**
     * Constructor with mandatory parameters
     */
    public RoleDO(final Long id, final String roleName, final OffsetDateTime createdAtUtc,
                  final Long createdByUserId, final OffsetDateTime lastModifiedAtUtc,
                  final Long lastModifiedByUserId, final Long version) {
        this.id = id;
        this.roleName = roleName;

        // set parameter from CommonDataObject
        this.createdAtUtc = createdAtUtc;
        this.createdByUserId = createdByUserId;
        this.lastModifiedAtUtc = lastModifiedAtUtc;
        this.lastModifiedByUserId = lastModifiedByUserId;
        this.version = version;
    }


    public Long getId() {
        return id;
    }


    public void setId(final Long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }


    public void setRoleName(final String roleName) {
        this.roleName = roleName;
    }


    @Override
    public int hashCode() {
        return Objects.hash(getId(), getRoleName(), getCreatedAtUtc(), getCreatedByUserId(), getLastModifiedAtUtc(),
                getLastModifiedByUserId(), getVersion());
    }


    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RoleDO)) {
            return false;
        }
        final RoleDO roleDO = (RoleDO) o;
        return getId().equals(roleDO.getId()) &&
                getVersion() == roleDO.getVersion() &&
                Objects.equals(getRoleName(), roleDO.getRoleName()) &&
                Objects.equals(getCreatedAtUtc(), roleDO.getCreatedAtUtc()) &&
                Objects.equals(getCreatedByUserId(), roleDO.getCreatedByUserId()) &&
                Objects.equals(getLastModifiedAtUtc(), roleDO.getLastModifiedAtUtc()) &&
                Objects.equals(getLastModifiedByUserId(), roleDO.getLastModifiedByUserId());
    }
}
