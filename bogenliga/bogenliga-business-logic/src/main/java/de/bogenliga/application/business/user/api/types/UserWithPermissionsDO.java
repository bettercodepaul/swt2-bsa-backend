package de.bogenliga.application.business.user.api.types;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import de.bogenliga.application.common.component.types.DataObject;

/**
 * Contains the values of the user business entity and the user permissions.
 * <p>
 * Extends the {@link UserDO} with the list of permissions.
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class UserWithPermissionsDO extends UserDO implements DataObject {

    private static final long serialVersionUID = 2606336980265200366L;
    /**
     * business parameter
     */
    private List<String> permissions;


    /**
     * Constructor with optional parameters
     */
    public UserWithPermissionsDO() {
        // empty constructor
    }


    /**
     * Constructor with mandatory parameters
     */
    public UserWithPermissionsDO(final Long id, final String email, final OffsetDateTime createdAtUtc,
                                 final Long createdByUserId, final OffsetDateTime lastModifiedAtUtc,
                                 final Long lastModifiedByUserId, final Long version,
                                 final List<String> permissions) {
        setId(id);
        setEmail(email);
        this.permissions = permissions == null ? Collections.emptyList() : new ArrayList<>(permissions);

        // set parameter from CommonDataObject
        this.createdAtUtc = createdAtUtc;
        this.createdByUserId = createdByUserId;
        this.lastModifiedAtUtc = lastModifiedAtUtc;
        this.lastModifiedByUserId = lastModifiedByUserId;
        this.version = version;
    }


    /**
     * Copy constructor
     *
     * @param userDO      copy user
     * @param permissions list of permissions
     */
    public UserWithPermissionsDO(final UserDO userDO,
                                 final List<String> permissions) {
        super(userDO.getId(), userDO.getEmail(), userDO.getCreatedAtUtc(), userDO.getCreatedByUserId(),
                userDO.getLastModifiedAtUtc(), userDO.getLastModifiedByUserId(), userDO.getVersion());

        this.permissions = permissions == null ? Collections.emptyList() : new ArrayList<>(permissions);
    }


    @Override
    public int hashCode() {
        return Objects.hash(getId(), getEmail(), getPermissions(), getCreatedAtUtc(), getCreatedByUserId(),
                getLastModifiedAtUtc(),
                getLastModifiedByUserId(), getVersion());
    }


    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserWithPermissionsDO)) {
            return false;
        }
        final UserWithPermissionsDO userDO = (UserWithPermissionsDO) o;
        return getId() == userDO.getId() &&
                getVersion() == userDO.getVersion() &&
                Objects.equals(getEmail(), userDO.getEmail()) &&
                Objects.equals(getPermissions(), userDO.getPermissions()) &&
                Objects.equals(getCreatedAtUtc(), userDO.getCreatedAtUtc()) &&
                Objects.equals(getCreatedByUserId(), userDO.getCreatedByUserId()) &&
                Objects.equals(getLastModifiedAtUtc(), userDO.getLastModifiedAtUtc()) &&
                Objects.equals(getLastModifiedByUserId(), userDO.getLastModifiedByUserId());
    }


    public List<String> getPermissions() {
        return new ArrayList<>(permissions);
    }


    public void setPermissions(final List<String> permissions) {
        this.permissions = permissions;
    }
}
