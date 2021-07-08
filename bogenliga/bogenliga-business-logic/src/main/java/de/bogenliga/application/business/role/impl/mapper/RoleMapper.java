package de.bogenliga.application.business.role.impl.mapper;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.function.Function;
import de.bogenliga.application.business.role.api.types.RoleDO;
import de.bogenliga.application.business.role.impl.entity.RoleBE;
import de.bogenliga.application.common.component.mapping.ValueObjectMapper;
import de.bogenliga.application.common.time.DateProvider;


/**
 * I convert the user DataObjects and BusinessEntities.
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/util/function/package-summary.html">
 * Oracle Function Package Overview</a>
 * @see <a href="https://www.baeldung.com/java-8-functional-interfaces">Functional Interfaces in Java 8</a>
 */
public class RoleMapper implements ValueObjectMapper {

    /**
     * Converts a {@link RoleBE} to a {@link RoleDO}
     */
    public static final Function<RoleBE, RoleDO> toRoleDO = be -> {

        final Long id = be.getRoleId();
        final String roleName = be.getRoleName();

        // technical parameter
        Long createdByUserId = be.getCreatedByUserId();
        Long lastModifiedByUserId = be.getLastModifiedByUserId();
        Long version = be.getVersion();

        OffsetDateTime createdAtUtc = DateProvider.convertTimestamp(be.getCreatedAtUtc());
        OffsetDateTime lastModifiedAtUtc = DateProvider.convertTimestamp(be.getLastModifiedAtUtc());

        return new RoleDO(id, roleName, createdAtUtc, createdByUserId, lastModifiedAtUtc, lastModifiedByUserId, version);
    };

    /**
     * Converts a {@link RoleDO} to a {@link RoleBE}
     */
    public static final Function<RoleDO, RoleBE> toUserBE = vo -> {

        Timestamp createdAtUtcTimestamp = DateProvider.convertOffsetDateTime(vo.getCreatedAtUtc());
        Timestamp lastModifiedAtUtcTimestamp = DateProvider.convertOffsetDateTime(vo.getLastModifiedAtUtc());

        RoleBE roleBE = new RoleBE();
        roleBE.setRoleId(vo.getId());
        roleBE.setRoleName(vo.getRoleName());

        roleBE.setCreatedAtUtc(createdAtUtcTimestamp);
        roleBE.setCreatedByUserId(vo.getCreatedByUserId());
        roleBE.setLastModifiedAtUtc(lastModifiedAtUtcTimestamp);
        roleBE.setLastModifiedByUserId(vo.getLastModifiedByUserId());
        roleBE.setVersion(vo.getVersion());

        return roleBE;
    };



    /**
     * Private constructor
     */
    private RoleMapper() {
        // empty private constructor
    }
}
