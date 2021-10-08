package de.bogenliga.application.business.user.impl.mapper;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.function.Function;
import de.bogenliga.application.business.user.api.types.UserRoleDO;
import de.bogenliga.application.business.user.impl.entity.UserRoleBE;
import de.bogenliga.application.business.user.impl.entity.UserRoleExtBE;
import de.bogenliga.application.common.component.mapping.ValueObjectMapper;
import de.bogenliga.application.common.time.DateProvider;

/**
 * I convert the userRole DataObjects and BusinessEntities.
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/util/function/package-summary.html">
 * Oracle Function Package Overview</a>
 * @see <a href="https://www.baeldung.com/java-8-functional-interfaces">Functional Interfaces in Java 8</a>
 */
public class UserRoleMapper implements ValueObjectMapper {

    /**
     * Converts a {@link UserRoleBE} to a {@link UserRoleDO}
     */
    public static final Function<UserRoleExtBE, UserRoleDO> extToUserRoleDO = be -> {

        final Long id = be.getUserId();
        final String email = be.getUserEmail();
        final Long roleId = be.getRoleId();
        final String roleName = be.getRoleName();
        final boolean active = be.isActive();

        // technical parameter
        Long createdByUserId = be.getCreatedByUserId();
        Long lastModifiedByUserId = be.getLastModifiedByUserId();
        Long version = be.getVersion();

        OffsetDateTime createdAtUtc = DateProvider.convertTimestamp(be.getCreatedAtUtc());
        OffsetDateTime lastModifiedAtUtc = DateProvider.convertTimestamp(be.getLastModifiedAtUtc());

        return new UserRoleDO(id, email, active, roleId, roleName, createdAtUtc, createdByUserId, lastModifiedAtUtc, lastModifiedByUserId, version);
    };

    /**
     * Converts a {@link UserRoleBE} to a {@link UserRoleDO}
     */
    public static final Function<UserRoleBE, UserRoleDO> toUserRoleDO = be -> {

        final Long id = be.getUserId();
        final Long roleId = be.getRoleId();

        // technical parameter
        Long createdByUserId = be.getCreatedByUserId();
        Long lastModifiedByUserId = be.getLastModifiedByUserId();
        Long version = be.getVersion();

        OffsetDateTime createdAtUtc = DateProvider.convertTimestamp(be.getCreatedAtUtc());
        OffsetDateTime lastModifiedAtUtc = DateProvider.convertTimestamp(be.getLastModifiedAtUtc());

        return new UserRoleDO(id, roleId, createdAtUtc, createdByUserId, lastModifiedAtUtc, lastModifiedByUserId, version);
    };

    /**
     * Converts a {@link UserRoleDO} to a {@link UserRoleBE}
     */
    public static final Function<UserRoleDO, UserRoleBE> toUserRoleBE = vo -> {

        Timestamp createdAtUtcTimestamp = DateProvider.convertOffsetDateTime(vo.getCreatedAtUtc());
        Timestamp lastModifiedAtUtcTimestamp = DateProvider.convertOffsetDateTime(vo.getLastModifiedAtUtc());

        UserRoleBE userRoleBE = new UserRoleBE();
        userRoleBE.setUserId(vo.getId());
        userRoleBE.setRoleId(vo.getRoleId());

        userRoleBE.setCreatedAtUtc(createdAtUtcTimestamp);
        userRoleBE.setCreatedByUserId(vo.getCreatedByUserId());
        userRoleBE.setLastModifiedAtUtc(lastModifiedAtUtcTimestamp);
        userRoleBE.setLastModifiedByUserId(vo.getLastModifiedByUserId());
        userRoleBE.setVersion(vo.getVersion());

        return userRoleBE;
    };

    /**
     * Converts a {@link UserRoleDO} to a {@link UserRoleExtBE}
     */
    public static final Function<UserRoleDO, UserRoleExtBE> toUserRoleExtBE = vo -> {

        Timestamp createdAtUtcTimestamp = DateProvider.convertOffsetDateTime(vo.getCreatedAtUtc());
        Timestamp lastModifiedAtUtcTimestamp = DateProvider.convertOffsetDateTime(vo.getLastModifiedAtUtc());

        UserRoleExtBE userRoleExtBE = new UserRoleExtBE();
        userRoleExtBE.setUserId(vo.getId());
        userRoleExtBE.setRoleId(vo.getRoleId());
        userRoleExtBE.setUserEmail(vo.getEmail());
        userRoleExtBE.setRoleName(vo.getRoleName());
        userRoleExtBE.setActive(vo.isActive());

        userRoleExtBE.setCreatedAtUtc(createdAtUtcTimestamp);
        userRoleExtBE.setCreatedByUserId(vo.getCreatedByUserId());
        userRoleExtBE.setLastModifiedAtUtc(lastModifiedAtUtcTimestamp);
        userRoleExtBE.setLastModifiedByUserId(vo.getLastModifiedByUserId());
        userRoleExtBE.setVersion(vo.getVersion());

        return userRoleExtBE;
    };


    /**
     * Private constructor
     */
    private UserRoleMapper() {
        // empty private constructor
    }
}
