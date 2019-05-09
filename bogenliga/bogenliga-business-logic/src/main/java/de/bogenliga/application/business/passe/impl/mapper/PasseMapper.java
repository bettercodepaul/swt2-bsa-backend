package de.bogenliga.application.business.passe.impl.mapper;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.function.Function;
import de.bogenliga.application.business.passe.api.types.PasseDO;
import de.bogenliga.application.business.passe.impl.entity.PasseBE;
import de.bogenliga.application.common.component.mapping.ValueObjectMapper;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.time.DateProvider;

/**
 * TODO [AL] class documentation
 *
 * @author Kay Scheerer
 */
public class PasseMapper implements ValueObjectMapper {

    private PasseMapper() {
    }


    public static final Function<PasseBE, PasseDO> toPasseDO = passeBE -> {
        OffsetDateTime createdAtUtc = DateProvider.convertTimestamp(passeBE.getCreatedAtUtc());
        OffsetDateTime lastModifiedUtc = DateProvider.convertTimestamp(passeBE.getLastModifiedAtUtc());
        if (passeBE == null) {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    "Entity could not be found with the given IDs");
        }
        return new PasseDO(
                passeBE.getId(),
                passeBE.getPasseMannschaftId(),
                passeBE.getPasseWettkampfId(),
                passeBE.getPasseMatchNr(),
                passeBE.getPasseMatchId(),
                passeBE.getPasseLfdnr(),
                passeBE.getPasseDsbMitgliedId(),
                passeBE.getPfeil1(),
                passeBE.getPfeil2(),
                passeBE.getPfeil3(),
                passeBE.getPfeil4(),
                passeBE.getPfeil5(),
                passeBE.getPfeil6(),
                createdAtUtc,
                passeBE.getCreatedByUserId(),
                lastModifiedUtc,
                passeBE.getLastModifiedByUserId(),
                passeBE.getVersion());
    };


    public static final Function<PasseDO, PasseBE> toPasseBE = passeDO -> {
        if (passeDO == null) {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    "Entity could not be found with the given IDs");
        }
        Timestamp createdAtUtcTimestamp = DateProvider.convertOffsetDateTime(passeDO.getCreatedAtUtc());
        Timestamp lastModifiedAtUtcTimestamp = DateProvider.convertOffsetDateTime(passeDO.getLastModifiedAtUtc());

        PasseBE passeBE = new PasseBE();
        passeBE.setId(passeDO.getId());
        passeBE.setPasseMannschaftId(passeDO.getPasseMannschaftId());
        passeBE.setPasseWettkampfId(passeDO.getPasseWettkampfId());
        passeBE.setPasseMatchNr(passeDO.getPasseMatchNr());
        passeBE.setPasseMatchId(passeDO.getPasseMatchId());
        passeBE.setPasseLfdnr(passeDO.getPasseLfdnr());
        passeBE.setPasseDsbMitgliedId(passeDO.getPasseDsbMitgliedId());
        passeBE.setPfeil1(passeDO.getPfeil1());
        passeBE.setPfeil2(passeDO.getPfeil2());
        passeBE.setPfeil3(passeDO.getPfeil3());
        passeBE.setPfeil4(passeDO.getPfeil4());
        passeBE.setPfeil5(passeDO.getPfeil5());
        passeBE.setPfeil6(passeDO.getPfeil6());
        passeBE.setCreatedAtUtc(createdAtUtcTimestamp);
        passeBE.setCreatedByUserId(passeDO.getCreatedByUserId());
        passeBE.setLastModifiedAtUtc(lastModifiedAtUtcTimestamp);
        passeBE.setLastModifiedByUserId(passeDO.getLastModifiedByUserId());
        passeBE.setVersion(passeDO.getVersion());

        return passeBE;
    };
}
