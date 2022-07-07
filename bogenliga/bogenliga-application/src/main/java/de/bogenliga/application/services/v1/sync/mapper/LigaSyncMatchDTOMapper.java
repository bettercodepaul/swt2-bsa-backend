package de.bogenliga.application.services.v1.sync.mapper;

import de.bogenliga.application.business.match.api.types.LigamatchDO;
import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.common.service.mapping.DataTransferObjectMapper;
import de.bogenliga.application.services.v1.match.model.MatchDTO;
import de.bogenliga.application.services.v1.passe.model.PasseDTO;
import de.bogenliga.application.services.v1.sync.model.LigaSyncMatchDTO;
import java.util.List;
import java.util.function.Function;

/**
 * I map the {@link MatchDO} to {@link LigaSyncMatchDTO} objects
 * // only One-Way as Ligatabelle is read-only
 */
public class LigaSyncMatchDTOMapper implements DataTransferObjectMapper {

    /**
     * I map the {@link MatchDO} object to the {@link LigaSyncMatchDTO} object
     */
    public static final Function<MatchDO, LigaSyncMatchDTO> fromMatchDOtoDTO = LigaSyncMatchDTOMapper::apply;

    public static final Function<LigamatchDO, LigaSyncMatchDTO> toDTO = LigaSyncMatchDTOMapper::apply;

    /**
     * I map the {@link LigaSyncMatchDTO} object to the {@link MatchDTO} object
     */
    public static final Function<LigaSyncMatchDTO, MatchDTO> toMatchDTO = LigaSyncMatchDTOMapper::apply;


    private LigaSyncMatchDTOMapper() {
        // empty private constructor
    }

    public static LigaSyncMatchDTO apply(MatchDO matchDO) {

        final Long id = matchDO.getId();
        final Long version = matchDO.getVersion();
        final Long wettkampfId = matchDO.getWettkampfId();
        final Integer matchNr = Math.toIntExact(matchDO.getNr());
        final Integer matchScheibennummer = matchDO.getScheibenNummer()!=null ? Math.toIntExact(matchDO.getScheibenNummer()) : null;
        final Long matchpunkte = matchDO.getMatchpunkte()!=null ? matchDO.getMatchpunkte() : null;
        final Long satzpunkte = matchDO.getSatzpunkte()!=null ? matchDO.getSatzpunkte() : null;
        final Long mannschaftsId = matchDO.getMannschaftId();
        final String mannschaftName = null;
        final String nameGegner = null;
        final Integer scheibennummerGegner = null;
        final Long matchIdGegner = null;
        final Long naechsteMatchId = null;
        final Long naechsteNaechsteMatchNrMatchId = null;
        final Integer strafpunkteSatz1 = matchDO.getStrafPunkteSatz1()!=null ? Math.toIntExact(matchDO.getStrafPunkteSatz1()) : null;
        final Integer strafpunkteSatz2 = matchDO.getStrafPunkteSatz2()!=null ? Math.toIntExact(matchDO.getStrafPunkteSatz2()) : null;
        final Integer strafpunkteSatz3 = matchDO.getStrafPunkteSatz3()!=null ? Math.toIntExact(matchDO.getStrafPunkteSatz3()) : null;
        final Integer strafpunkteSatz4 = matchDO.getStrafPunkteSatz4()!=null ? Math.toIntExact(matchDO.getStrafPunkteSatz4()) : null;
        final Integer strafpunkteSatz5 = matchDO.getStrafPunkteSatz5()!=null ? Math.toIntExact(matchDO.getStrafPunkteSatz5()) : null;


        return new LigaSyncMatchDTO(id, version, wettkampfId, matchNr, matchScheibennummer, matchpunkte, satzpunkte, mannschaftsId,
                mannschaftName, nameGegner, scheibennummerGegner, matchIdGegner, naechsteMatchId,
                naechsteNaechsteMatchNrMatchId, strafpunkteSatz1, strafpunkteSatz2, strafpunkteSatz3, strafpunkteSatz4,
                strafpunkteSatz5);
    }

    public static LigaSyncMatchDTO apply(LigamatchDO ligamatchDO) {

        final Long id = ligamatchDO.getId();
        final Long version = ligamatchDO.getVersion();
        final Long wettkampfId = ligamatchDO.getWettkampfId();
        final Integer matchNr = Math.toIntExact(ligamatchDO.getMatchNr());
        final Integer matchScheibennummer = Math.toIntExact(ligamatchDO.getMatchScheibennummer());
        final Long matchpunkte = ligamatchDO.getMatchpunkte()!=null ? ligamatchDO.getMatchpunkte() : null;
        final Long satzpunkte = ligamatchDO.getSatzpunkte()!=null ? ligamatchDO.getSatzpunkte() : null;
        final Long mannschaftsId = ligamatchDO.getMannschaftId();
        final String mannschaftName = ligamatchDO.getMannschaftName();
        final String nameGegner = ligamatchDO.getNameGegner();
        final Integer scheibennummerGegner = ligamatchDO.getScheibennummerGegner()!=null ? Math.toIntExact(ligamatchDO.getScheibennummerGegner()) : null;
        final Long matchIdGegner = ligamatchDO.getMatchIdGegner();
        final Long naechsteMatchId = ligamatchDO.getNaechsteMatchId();
        final Long naechsteNaechsteMatchNrMatchId = ligamatchDO.getNaechsteNaechsteMatchNrMatchId();
        final Integer strafpunkteSatz1 = ligamatchDO.getStrafpunkteSatz1()!=null ? Math.toIntExact(ligamatchDO.getStrafpunkteSatz1()) : null;
        final Integer strafpunkteSatz2 = ligamatchDO.getStrafpunkteSatz2()!=null ? Math.toIntExact(ligamatchDO.getStrafpunkteSatz2()) : null;
        final Integer strafpunkteSatz3 = ligamatchDO.getStrafpunkteSatz3()!=null ? Math.toIntExact(ligamatchDO.getStrafpunkteSatz3()) : null;
        final Integer strafpunkteSatz4 = ligamatchDO.getStrafpunkteSatz4()!=null ? Math.toIntExact(ligamatchDO.getStrafpunkteSatz4()) : null;
        final Integer strafpunkteSatz5 = ligamatchDO.getStrafpunkteSatz5()!=null ? Math.toIntExact(ligamatchDO.getStrafpunkteSatz5()) : null;


        return new LigaSyncMatchDTO(id, version, wettkampfId, matchNr, matchScheibennummer, matchpunkte, satzpunkte, mannschaftsId,
                mannschaftName, nameGegner, scheibennummerGegner, matchIdGegner, naechsteMatchId,
                naechsteNaechsteMatchNrMatchId, strafpunkteSatz1, strafpunkteSatz2, strafpunkteSatz3, strafpunkteSatz4,
                strafpunkteSatz5);
    }

    public static MatchDTO apply(LigaSyncMatchDTO ligaSyncMatchDTO) {

        final Long nr = ligaSyncMatchDTO.getMatchNr().longValue();
        final Long id = ligaSyncMatchDTO.getId();
        final Long version = ligaSyncMatchDTO.getVersion();
        final Long wettkampfId = ligaSyncMatchDTO.getWettkampfId();
        final Long mannschaftId = ligaSyncMatchDTO.getMannschaftId();

        final Long begegnung = null;
        final Long scheibenNummer = ligaSyncMatchDTO.getMatchScheibennummer().longValue();
        final Long matchpunkte = null;
        final Long satzpunkte = null;

        final Long strafPunkteSatz1 = ligaSyncMatchDTO.getStrafpunkteSatz1() != null ? ligaSyncMatchDTO.getStrafpunkteSatz1().longValue() : null;
        final Long strafPunkteSatz2 = ligaSyncMatchDTO.getStrafpunkteSatz2() != null ? ligaSyncMatchDTO.getStrafpunkteSatz2().longValue() : null;
        final Long strafPunkteSatz3 = ligaSyncMatchDTO.getStrafpunkteSatz3() != null ? ligaSyncMatchDTO.getStrafpunkteSatz3().longValue() : null;
        final Long strafPunkteSatz4 = ligaSyncMatchDTO.getStrafpunkteSatz4() != null ? ligaSyncMatchDTO.getStrafpunkteSatz4().longValue() : null;
        final Long strafPunkteSatz5 = ligaSyncMatchDTO.getStrafpunkteSatz5() != null ? ligaSyncMatchDTO.getStrafpunkteSatz5().longValue() : null;

        // used to transport related passe objects to the frontend or to save them from the
        // table form (schusszettel) to the database
        final List<PasseDTO> passen = null;

        return new MatchDTO(id, nr, version, wettkampfId, mannschaftId, begegnung, scheibenNummer, matchpunkte, satzpunkte, passen,
                strafPunkteSatz1, strafPunkteSatz2, strafPunkteSatz3, strafPunkteSatz4, strafPunkteSatz5);
    }
}