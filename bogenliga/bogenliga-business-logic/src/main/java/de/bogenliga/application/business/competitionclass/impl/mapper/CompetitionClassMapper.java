package de.bogenliga.application.business.competitionclass.impl.mapper;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.function.Function;
import de.bogenliga.application.business.competitionclass.api.types.CompetitionClassDO;
import de.bogenliga.application.business.competitionclass.impl.entity.CompetitionClassBE;
import de.bogenliga.application.common.component.mapping.ValueObjectMapper;
import de.bogenliga.application.common.time.DateProvider;

/**
 * I convert the klassen DataObjects and BusinessEntities.
 *
 * @author Giuseppe Ferrera, giuseppe.ferrera@student.reutlingen-university.de
 */
public class CompetitionClassMapper implements ValueObjectMapper {

    public static final Function<CompetitionClassBE, CompetitionClassDO> toCompetitionClassDO = competitionClassBE -> {
        final Long klasseId = competitionClassBE.getKlasseId();
        final String klasseName = competitionClassBE.getKlasseName();
        final Long klasseAlterMin = competitionClassBE.getKlasseAlterMin();
        final Long klasseAlterMax = competitionClassBE.getKlasseAlterMax();
        final Long klasseNr = competitionClassBE.getKlasseNr();

        // Technical Parameter
        final Long createdByUserId = competitionClassBE.getCreatedByUserId();
        final Long lastModifiedUserId = competitionClassBE.getLastModifiedByUserId();
        final Long version = competitionClassBE.getVersion();

        OffsetDateTime createdAtUtc = DateProvider.convertTimestamp(competitionClassBE.getCreatedAtUtc());
        OffsetDateTime lastModifiedUtc = DateProvider.convertTimestamp(competitionClassBE.getLastModifiedAtUtc());

        return new CompetitionClassDO(klasseId,klasseName,klasseAlterMin,klasseAlterMax,klasseNr,createdAtUtc,createdByUserId,lastModifiedUtc,version);
    };

    public static final Function<CompetitionClassDO, CompetitionClassBE> toCompetitionClassBE = competitionClassDO -> {

        Timestamp createdAtUtcTimestamp = DateProvider.convertOffsetDateTime(competitionClassDO.getCreatedAtUtc());
        Timestamp lastModifiedAtUtcTimestamp = DateProvider.convertOffsetDateTime(competitionClassDO.getLastModifiedAtUtc());

        CompetitionClassBE competitionClassBE = new CompetitionClassBE();
        competitionClassBE.setKlasseId(competitionClassDO.getId());
        competitionClassBE.setKlasseName(competitionClassDO.getKlasseName());
        competitionClassBE.setKlasseAlterMin(competitionClassDO.getKlasseAlterMin());
        competitionClassBE.setKlasseAlterMax(competitionClassDO.getKlasseAlterMax());
        competitionClassBE.setKlasseNr(competitionClassDO.getKlasseNr());

        competitionClassBE.setCreatedAtUtc(createdAtUtcTimestamp);
        competitionClassBE.setLastModifiedAtUtc(lastModifiedAtUtcTimestamp);
        competitionClassBE.setLastModifiedByUserId(competitionClassBE.getLastModifiedByUserId());
        competitionClassBE.setCreatedByUserId(competitionClassBE.getCreatedByUserId());
        competitionClassBE.setVersion(competitionClassBE.getVersion());

        return competitionClassBE;
    };
}