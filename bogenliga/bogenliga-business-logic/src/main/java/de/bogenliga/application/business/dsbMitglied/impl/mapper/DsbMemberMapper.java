package de.bogenliga.application.business.dsbMember.impl.mapper;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import de.bogenliga.application.business.dsbMember.api.types.DsbMemberDO;
import de.bogenliga.application.business.dsbMember.impl.entity.DsbMemberBE;
import de.bogenliga.application.common.component.mapping.ValueObjectMapper;
import de.bogenliga.application.common.time.DateProvider;

/**
 * I convert the dsbMember DataObjects and BusinessEntities.
 *
 */
public class DsbMemberMapper implements ValueObjectMapper {

    /**
     * Converts a {@link DsbMemberBE} to a {@link DsbMemberDO}
     */
    public static final Function<DsbMemberBE, DsbMemberDO> toDsbMemberDO = be -> {

        final long id = be.getDsbMemberId();
        final String forename = be.getDsbMemberForename();
        final String surname = be.getDsbMemberSurname();
        final String birthdate = be.getDsbMemberBirthdate();
        final String nationality = be.getDsbMemberNationality();
        final String memberNumber = be.getDsbMemberMemberNumber();
        final long clubId = be.getDsbMemberClubId();
        final long userId = be.getDsbMemberUserId();

        // technical parameter
        long createdByUserId = be.getCreatedByUserId();
        long lastModifiedByUserId = be.getLastModifiedByUserId();
        long version = be.getVersion();

        OffsetDateTime createdAtUtc = DateProvider.convertTimestamp(be.getCreatedAtUtc());
        OffsetDateTime lastModifiedAtUtc = DateProvider.convertTimestamp(be.getLastModifiedAtUtc());

        return new DsbMemberDO(id, forename, surname, birthdate, nationality, memberNumber, clubId, userId,
                createdAtUtc, createdByUserId, lastModifiedAtUtc, lastModifiedByUserId, version);
    };

    /**
     * Converts a {@link DsbMemberDO} to a {@link DsbMemberBE}
     */
    public static final Function<DsbMemberDO, DsbMemberBE> toDsbMemberBE = vo -> {

        Timestamp createdAtUtcTimestamp = DateProvider.convertOffsetDateTime(vo.getCreatedAtUtc());
        Timestamp lastModifiedAtUtcTimestamp = DateProvider.convertOffsetDateTime(vo.getLastModifiedAtUtc());

        DsbMemberBE dsbMemberBE = new DsbMemberBE();
        dsbMemberBE.setDsbMemberId(vo.getId());
        dsbMemberBE.setDsbMemberForename(vo.getForename());
        dsbMemberBE.setDsbMemberSurname(vo.getSurname());
        dsbMemberBE.setDsbMemberBirthdate(vo.getBirthdate());
        dsbMemberBE.setDsbMemberNationality(vo.getNationality());
        dsbMemberBE.setDsbMemberMemberNumber(vo.getMemberNumber());
        dsbMemberBE.setDsbMemberClubId(vo.getClubId());
        dsbMemberBE.setDsbMemberUserId(vo.getUserId());

        dsbMemberBE.setCreatedAtUtc(createdAtUtcTimestamp);
        dsbMemberBE.setCreatedByUserId(vo.getCreatedByUserId());
        dsbMemberBE.setLastModifiedAtUtc(lastModifiedAtUtcTimestamp);
        dsbMemberBE.setLastModifiedByUserId(vo.getLastModifiedByUserId());
        dsbMemberBE.setVersion(vo.getVersion());

        return dsbMemberBE;
    };


    /**
     * Private constructor
     */
    private DsbMemberMapper() {
        // empty private constructor
    }
}
