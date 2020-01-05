package de.bogenliga.application.business.user.api.types;

import de.bogenliga.application.common.component.types.CommonDataObject;
import de.bogenliga.application.common.component.types.DataObject;

import java.time.OffsetDateTime;
import java.util.Objects;

/**
 * Contains the values of the user business entity.
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class UserRoleDO extends CommonDataObject implements DataObject {
    private static final long serialVersionUID = 298357103627898987L;

    /**
     * business parameter
     */
    private Long id;
    private String email;
    private Long roleId;
    private String roleName;
    private boolean active;


    /**
     * Constructor with optional parameters
     */
    public UserRoleDO() {
        // empty constructor
    }

    /**
     * Constructor with minimum  parameters
     */
    public UserRoleDO(final Long id, final Long roleId,
                      final OffsetDateTime createdAtUtc,
                      final Long createdByUserId, final OffsetDateTime lastModifiedAtUtc,
                      final Long lastModifiedByUserId, final Long version) {
        this.id = id;
        this.email = null;
        this.roleId = roleId;
        this.roleName = null;
        this.active = true;

        // set parameter from CommonDataObject
        this.createdAtUtc = createdAtUtc;
        this.createdByUserId = createdByUserId;
        this.lastModifiedAtUtc = lastModifiedAtUtc;
        this.lastModifiedByUserId = lastModifiedByUserId;
        this.version = version;
    }

    /**
     * Constructor with business  parameters
     */
    public UserRoleDO(final Long id, final String email, final boolean active, final Long roleId, final String roleName){
        this.id = id;
        this.email = email;
        this.roleId = roleId;
        this.roleName = roleName;
        this.active = active;
    }

    /**
     * Constructor with mandatory parameters
     */
    public UserRoleDO(final Long id, final String email, final boolean active, final Long roleId, final String roleName,
                      final OffsetDateTime createdAtUtc,
                      final Long createdByUserId, final OffsetDateTime lastModifiedAtUtc,
                      final Long lastModifiedByUserId, final Long version) {
        this.id = id;
        this.email = email;
        this.roleId = roleId;
        this.roleName = roleName;
        this.active = active;

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

    public String getEmail() {
        return email;
    }
    public void setEmail(final String email) {
        this.email = email;
    }

    public Long getRoleId() {
        return roleId;
    }
    public void setRoleId(final Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }
    public void setRoleName(final String roleName) {
        this.roleName = roleName;
    }

    public boolean isActive () {return this.active;}
    public void setActive (boolean active) {this.active = active;}


    @Override
    public int hashCode() {
        return Objects.hash(getId(), getEmail(), getRoleId(), getRoleName(), getCreatedAtUtc(),
                getCreatedByUserId(), getLastModifiedAtUtc(), getLastModifiedByUserId(), getVersion(), isActive());
    }


    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserRoleDO)) {
            return false;
        }
        final UserRoleDO userRoleDO = (UserRoleDO) o;
        return getId().equals(userRoleDO.getId()) &&
                getVersion() == userRoleDO.getVersion() &&
                Objects.equals(getEmail(), userRoleDO.getEmail()) &&
                getRoleId().equals(userRoleDO.getRoleId()) &&
                Objects.equals(getRoleName(), userRoleDO.getRoleName()) &&
                Objects.equals(getCreatedAtUtc(), userRoleDO.getCreatedAtUtc()) &&
                Objects.equals(getCreatedByUserId(), userRoleDO.getCreatedByUserId()) &&
                Objects.equals(getLastModifiedAtUtc(), userRoleDO.getLastModifiedAtUtc()) &&
                Objects.equals(getLastModifiedByUserId(), userRoleDO.getLastModifiedByUserId()) &&
                Objects.equals(isActive(), userRoleDO.isActive());
    }
}
