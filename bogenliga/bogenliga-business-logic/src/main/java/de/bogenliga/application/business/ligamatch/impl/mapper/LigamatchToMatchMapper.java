package de.bogenliga.application.business.ligamatch.impl.mapper;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.function.Function;
import de.bogenliga.application.business.ligamatch.impl.entity.LigamatchBE;
import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.business.match.impl.entity.MatchBE;
import de.bogenliga.application.common.component.mapping.ValueObjectMapper;
import de.bogenliga.application.common.time.DateProvider;

/**
 * I map the ligamatchBE to a MatchDO in order to be able to use the known structure and functions of MatchDO
 */
public class LigamatchToMatchMapper implements ValueObjectMapper {

    private LigamatchToMatchMapper(){};


    public static final Function<LigamatchBE, MatchDO> LigamatchToMatchDO = ligamatchBE -> {
        OffsetDateTime createdAtUtc = DateProvider.convertTimestamp(ligamatchBE.getCreatedAtUtc());
        OffsetDateTime lastModifiedUtc = DateProvider.convertTimestamp(ligamatchBE.getLastModifiedAtUtc());

        return new MatchDO(
                ligamatchBE.getMatchId(),
                ligamatchBE.getMatchNr(),
                ligamatchBE.getWettkampfId(),
                ligamatchBE.getMannschaftId(),
                ligamatchBE.getBegegnung(),
                ligamatchBE.getScheibennummer(),
                null,
                null,
                ligamatchBE.getStrafpunkteSatz1(),
                ligamatchBE.getStrafpunkteSatz2(),
                ligamatchBE.getStrafpunkteSatz3(),
                ligamatchBE.getStrafpunkteSatz4(),
                ligamatchBE.getStrafpunkteSatz5(),
                createdAtUtc,
                ligamatchBE.getCreatedByUserId(),
                lastModifiedUtc,
                ligamatchBE.getLastModifiedByUserId(),
                ligamatchBE.getVersion()
        );


    };

//    public static final Function<MatchDO, LigamatchBE> toLigamatchBE = matchDO -> {
//
//        Timestamp createdAtUtcTimestamp = DateProvider.convertOffsetDateTime(matchDO.getCreatedAtUtc());
//        Timestamp lastModifiedAtUtcTimestamp = DateProvider.convertOffsetDateTime(matchDO.getLastModifiedAtUtc());
//
//        LigamatchBE ligamatchBE = new LigamatchBE();
//
//        ligamatchBE.setMatchId(matchDO.getId());
//        ligamatchBE.setMatchNr(matchDO.getNr());
//        ligamatchBE.setMannschaftId(matchDO.getMannschaftId());
//        ligamatchBE.setWettkampfId(matchDO.getWettkampfId());
//        ligamatchBE.setScheibennummer(matchDO.getScheibenNummer());
//        ligamatchBE.setBegegnung(matchDO.getBegegnung());
//        ligamatchBE.setStrafpunkteSatz1(matchDO.getStrafPunkteSatz1());
//        ligamatchBE.setStrafpunkteSatz2(matchDO.getStrafPunkteSatz2());
//        ligamatchBE.setStrafpunkteSatz3(matchDO.getStrafPunkteSatz3());
//        ligamatchBE.setStrafpunkteSatz4(matchDO.getStrafPunkteSatz4());
//        ligamatchBE.setStrafpunkteSatz5(matchDO.getStrafPunkteSatz5());
//
//        ligamatchBE.setCreatedAtUtc(createdAtUtcTimestamp);
//        ligamatchBE.setCreatedByUserId(matchDO.getCreatedByUserId());
//        ligamatchBE.setLastModifiedAtUtc(lastModifiedAtUtcTimestamp);
//        ligamatchBE.setLastModifiedByUserId(matchDO.getLastModifiedByUserId());
//        ligamatchBE.setVersion(matchDO.getVersion());
//
//        return ligamatchBE;
//    };


}
