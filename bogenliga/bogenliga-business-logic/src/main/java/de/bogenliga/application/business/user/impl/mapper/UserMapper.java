package de.bogenliga.application.business.user.impl.mapper;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import de.bogenliga.application.business.user.api.types.UserDO;
import de.bogenliga.application.business.user.api.types.UserWithPermissionsDO;
import de.bogenliga.application.business.user.impl.entity.UserBE;
import de.bogenliga.application.common.component.mapping.ValueObjectMapper;
import de.bogenliga.application.common.time.DateProvider;

public class UserMapper implements ValueObjectMapper {
    public static final Function<UserBE, UserDO> toUserDO = be -> {

        final long id = be.getUserId();
        final String email = be.getUserEmail();

        // technical parameter
        long createdByUserId = be.getCreatedByUserId();
        long lastModifiedByUserId = be.getLastModifiedByUserId();
        long version = be.getVersion();

        OffsetDateTime createdAtUtc = DateProvider.convertTimestamp(be.getCreatedAtUtc());
        OffsetDateTime lastModifiedAtUtc = DateProvider.convertTimestamp(be.getLastModifiedAtUtc());

        return new UserDO(id, email, createdAtUtc, createdByUserId, lastModifiedAtUtc, lastModifiedByUserId, version);
    };

    public static final BiFunction<UserBE, List<String>, UserWithPermissionsDO> toUserWithPermissionsDO =
            (be, permissions) -> {
                UserDO userDO = toUserDO.apply(be);

                return new UserWithPermissionsDO(userDO, permissions);
            };

    public static final Function<UserDO, UserBE> toUserBE = vo -> {

        Timestamp createdAtUtcTimestamp = DateProvider.convertOffsetDateTime(vo.getCreatedAtUtc());
        Timestamp lastModifiedAtUtcTimestamp = DateProvider.convertOffsetDateTime(vo.getLastModifiedAtUtc());

        UserBE userBE = new UserBE();
        userBE.setUserId(vo.getId());
        userBE.setUserEmail(vo.getEmail());

        userBE.setCreatedAtUtc(createdAtUtcTimestamp);
        userBE.setCreatedByUserId(vo.getCreatedByUserId());
        userBE.setLastModifiedAtUtc(lastModifiedAtUtcTimestamp);
        userBE.setLastModifiedByUserId(vo.getLastModifiedByUserId());
        userBE.setVersion(vo.getVersion());

        return userBE;
    };


    /**
     * Private constructor
     */
    private UserMapper() {
        // empty private constructor
    }
}
