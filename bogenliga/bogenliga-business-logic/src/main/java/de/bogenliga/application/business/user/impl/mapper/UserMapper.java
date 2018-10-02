package de.bogenliga.application.business.user.impl.mapper;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.function.Function;
import de.bogenliga.application.business.user.api.types.UserDO;
import de.bogenliga.application.business.user.impl.entity.UserBE;
import de.bogenliga.application.common.component.mapping.ValueObjectMapper;

public class UserMapper implements ValueObjectMapper {
    public static final Function<UserBE, UserDO> toVO = be -> {

        final long id = be.getUserId();
        final String email = be.getUserEmail();

        // technical parameter
        Timestamp createdAtUtcTimestamp = be.getCreatedAtUtc();
        long createdByUserId = be.getCreatedByUserId();
        Timestamp lastModifiedAtUtcTimestamp = be.getLastModifiedAtUtc();
        long lastModifiedByUserId = be.getLastModifiedByUserId();
        long version = be.getVersion();

        OffsetDateTime createdAtUtc = createdAtUtcTimestamp == null ? null :
                OffsetDateTime.ofInstant(Instant.ofEpochMilli(createdAtUtcTimestamp.getTime()), ZoneId.of("UTC"));
        OffsetDateTime lastModifiedAtUtc = lastModifiedAtUtcTimestamp == null ? null :
                OffsetDateTime.ofInstant(Instant.ofEpochMilli(lastModifiedAtUtcTimestamp.getTime()), ZoneId.of("UTC"));

        return new UserDO(id, email, createdAtUtc, createdByUserId, lastModifiedAtUtc, lastModifiedByUserId, version);
    };

    public static final Function<UserDO, UserBE> toBE = vo -> {

        OffsetDateTime createdAtUtc = vo.getCreatedAtUtc();
        OffsetDateTime lastModifiedAtUtc = vo.getLastModifiedAtUtc();
        Timestamp createdAtUtcTimestamp = Timestamp.valueOf(
                createdAtUtc.atZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime());
        Timestamp lastModifiedAtUtcTimestamp = Timestamp.valueOf(
                lastModifiedAtUtc.atZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime());

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
