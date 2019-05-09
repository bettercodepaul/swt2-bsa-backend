package de.bogenliga.application.business.match.impl.mapper;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.function.Function;
import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.business.match.impl.entity.MatchBE;
import de.bogenliga.application.common.component.mapping.ValueObjectMapper;
import de.bogenliga.application.common.time.DateProvider;

/**
 * @author Dominik Halle, HSRT MKI SS19 - SWT2
 */
public class MatchMapper implements ValueObjectMapper {

    /**
     * Empty hidden constructor to prevent instantiation
     */
    private MatchMapper () {}

    public static final Function<MatchBE, MatchDO> toMatchDO = matchBE -> {
        OffsetDateTime createdAtUtc = DateProvider.convertTimestamp(matchBE.getCreatedAtUtc());
        OffsetDateTime lastModifiedUtc = DateProvider.convertTimestamp(matchBE.getLastModifiedAtUtc());

        return new MatchDO(
                matchBE.getId(),
                matchBE.getNr(),
                matchBE.getWettkampfId(),
                matchBE.getMannschaftId(),
                matchBE.getBegegnung(),
                matchBE.getScheibenNummer(),
                matchBE.getMatchpunkte(),
                matchBE.getSatzpunkte(),

                createdAtUtc,
                matchBE.getCreatedByUserId(),
                lastModifiedUtc,
                matchBE.getLastModifiedByUserId(),
                matchBE.getVersion()
        );
    };


    public static final Function<MatchDO, MatchBE> toMatchBE = matchDO -> {

        Timestamp createdAtUtcTimestamp = DateProvider.convertOffsetDateTime(matchDO.getCreatedAtUtc());
        Timestamp lastModifiedAtUtcTimestamp = DateProvider.convertOffsetDateTime(matchDO.getLastModifiedAtUtc());

        MatchBE matchBE = new MatchBE();

        matchBE.setId(matchDO.getId());
        matchBE.setNr(matchDO.getNr());
        matchBE.setMannschaftId(matchDO.getMannschaftId());
        matchBE.setWettkampfId(matchDO.getWettkampfId());
        matchBE.setScheibenNummer(matchDO.getScheibenNummer());
        matchBE.setBegegnung(matchDO.getBegegnung());
        matchBE.setMatchpunkte(matchDO.getMatchpunkte());
        matchBE.setSatzpunkte(matchDO.getSatzpunkte());

        matchBE.setCreatedAtUtc(createdAtUtcTimestamp);
        matchBE.setCreatedByUserId(matchDO.getCreatedByUserId());
        matchBE.setLastModifiedAtUtc(lastModifiedAtUtcTimestamp);
        matchBE.setLastModifiedByUserId(matchDO.getLastModifiedByUserId());
        matchBE.setVersion(matchDO.getVersion());

        return matchBE;
    };

}
