package de.bogenliga.application.business.ligamatch.impl.mapper;

import java.time.OffsetDateTime;
import java.util.function.Function;
import de.bogenliga.application.business.ligamatch.impl.entity.LigamatchBE;
import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.business.match.impl.entity.MatchBE;
import de.bogenliga.application.common.component.mapping.ValueObjectMapper;
import de.bogenliga.application.common.time.DateProvider;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
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


}
