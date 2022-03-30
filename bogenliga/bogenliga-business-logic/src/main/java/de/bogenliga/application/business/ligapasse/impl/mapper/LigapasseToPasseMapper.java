package de.bogenliga.application.business.ligapasse.impl.mapper;

import java.time.OffsetDateTime;
import java.util.function.Function;
import de.bogenliga.application.business.ligapasse.impl.entity.LigapasseBE;
import de.bogenliga.application.business.passe.api.types.PasseDO;
import de.bogenliga.application.common.component.mapping.ValueObjectMapper;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.time.DateProvider;

/**
 * I map the ligapasseBE to a PasseDO in order to be able to use the known structure and functions of passeDO
 */
public class LigapasseToPasseMapper implements ValueObjectMapper {

    private LigapasseToPasseMapper(){};

    public static final Function<LigapasseBE, PasseDO> ligapasseToPasseDO = ligapasseBE -> {
        OffsetDateTime createdAtUtc = DateProvider.convertTimestamp(ligapasseBE.getCreatedAtUtc());
        OffsetDateTime lastModifiedUtc = DateProvider.convertTimestamp(ligapasseBE.getLastModifiedAtUtc());
        if (ligapasseBE == null) {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    "Entity could not be found with the given IDs");
        }
        return new PasseDO(
                ligapasseBE.getPasseId(),
                ligapasseBE.getPasseMannschaftId(),
                ligapasseBE.getWettkampfId(),
                ligapasseBE.getPasseMatchNr(),
                ligapasseBE.getMatchId(),
                ligapasseBE.getPasseLfdnr(),
                ligapasseBE.getDsbMitgliedId(),
                ligapasseBE.getPasseRingzahlPfeil1(),
                ligapasseBE.getPasseRingzahlPfeil2(),
                null,
                null,
                null,
                null,
                createdAtUtc,
                ligapasseBE.getCreatedByUserId(),
                lastModifiedUtc,
                ligapasseBE.getLastModifiedByUserId(),
                ligapasseBE.getVersion());
    };


}
