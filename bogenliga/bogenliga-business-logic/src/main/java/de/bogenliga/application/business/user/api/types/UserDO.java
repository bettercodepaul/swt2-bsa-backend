package de.bogenliga.application.business.user.api.types;

import java.time.OffsetDateTime;
import java.util.Objects;
import de.bogenliga.application.common.component.types.CommonDataObject;
import de.bogenliga.application.common.component.types.DataObject;

/**
 * Contains the values of the user business entity.
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class UserDO extends CommonDataObject implements DataObject {
    private static final long serialVersionUID = 298357103627898987L;

    /**
     * business parameter
     */
    private Long id;
    private String email;


    /**
     * Constructor with optional parameters
     */
    public UserDO() {
        // empty constructor
    }


    /**
     * Constructor with mandatory parameters
     */
    public UserDO(final Long id, final String email, final OffsetDateTime createdAtUtc,
                  final Long createdByUserId, final OffsetDateTime lastModifiedAtUtc,
                  final Long lastModifiedByUserId, final Long version) {
        this.id = id;
        this.email = email;

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


    @Override
    public int hashCode() {
        return Objects.hash(getId(), getEmail(), getCreatedAtUtc(), getCreatedByUserId(), getLastModifiedAtUtc(),
                getLastModifiedByUserId(), getVersion());
    }


    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserDO)) {
            return false;
        }
        final UserDO userDO = (UserDO) o;
        return getId().equals(userDO.getId()) &&
                getVersion() == userDO.getVersion() &&
                Objects.equals(getEmail(), userDO.getEmail()) &&
                Objects.equals(getCreatedAtUtc(), userDO.getCreatedAtUtc()) &&
                Objects.equals(getCreatedByUserId(), userDO.getCreatedByUserId()) &&
                Objects.equals(getLastModifiedAtUtc(), userDO.getLastModifiedAtUtc()) &&
                Objects.equals(getLastModifiedByUserId(), userDO.getLastModifiedByUserId());
    }
}
