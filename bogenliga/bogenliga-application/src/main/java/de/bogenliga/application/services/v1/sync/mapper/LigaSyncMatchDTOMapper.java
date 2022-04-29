package de.bogenliga.application.services.v1.sync.mapper;

import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.common.service.mapping.DataTransferObjectMapper;
import de.bogenliga.application.services.v1.sync.model.LigaSyncMatchDTO;
import java.util.function.Function;

/**
 * I map the {@link MatchDO} to {@link LigaSyncMatchDTO} objects
 * // only One-Way as Ligatabelle is read-only
 */
public class LigaSyncMatchDTOMapper implements DataTransferObjectMapper {

    /**
     * I map the {@link MatchDO} object to the {@link LigaSyncMatchDTO} object
     */
    public static final Function<MatchDO, LigaSyncMatchDTO> toDTO = LigaSyncMatchDTOMapper::apply;

    private LigaSyncMatchDTOMapper() {
        // empty private constructor
    }

    private static LigaSyncMatchDTO apply(MatchDO matchDO) {

        final Long id = matchDO.getId();
        final Long version = matchDO.getVersion();
        final Long wettkampfId = matchDO.getWettkampfId();
        final Integer matchNr = Math.toIntExact(matchDO.getNr());
        final Integer matchScheibennummer = Math.toIntExact(matchDO.getScheibenNummer());
        final Long mannschaftsId = matchDO.getMannschaftId();
        final String mannschaftName = null;
        final String nameGegner = null;
        final Integer scheibennummerGegner = null;
        final Long matchIdGegner = null;
        final Long naechsteMatchId = null;
        final Long naechsteNaechsteMatchNrMatchId = null;
        final Integer strafpunkteSatz1 = Math.toIntExact(matchDO.getStrafPunkteSatz1());
        final Integer strafpunkteSatz2 = Math.toIntExact(matchDO.getStrafPunkteSatz2());
        final Integer strafpunkteSatz3 = Math.toIntExact(matchDO.getStrafPunkteSatz3());
        final Integer strafpunkteSatz4 = Math.toIntExact(matchDO.getStrafPunkteSatz4());
        final Integer strafpunkteSatz5 = Math.toIntExact(matchDO.getStrafPunkteSatz5());

        return new LigaSyncMatchDTO(id, version, wettkampfId, matchNr, matchScheibennummer, mannschaftsId,
                mannschaftName, nameGegner, scheibennummerGegner, matchIdGegner, naechsteMatchId,
                naechsteNaechsteMatchNrMatchId, strafpunkteSatz1, strafpunkteSatz2, strafpunkteSatz3, strafpunkteSatz4,
                strafpunkteSatz5);
    }
}