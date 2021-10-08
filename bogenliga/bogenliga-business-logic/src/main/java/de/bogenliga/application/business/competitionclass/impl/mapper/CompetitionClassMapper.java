package de.bogenliga.application.business.competitionclass.impl.mapper;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.Calendar;
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

    private CompetitionClassMapper() {
        // empty constructor
    }

    public static final Function<CompetitionClassBE, CompetitionClassDO> toCompetitionClassDO = competitionClassBE -> {
        int year = Calendar.getInstance().get(Calendar.YEAR);

        final Long klasseId = competitionClassBE.getKlasseId();
        final String klasseName = competitionClassBE.getKlasseName();
        final Long klasseJahrgangMin = year - competitionClassBE.getKlasseAlterMin();
        final Long klasseJahrgangMax = year - competitionClassBE.getKlasseAlterMax();
        final Long klasseNr = competitionClassBE.getKlasseNr();

        // Technical Parameter
        final Long createdByUserId = competitionClassBE.getCreatedByUserId();
        final Long lastModifiedUserId = competitionClassBE.getLastModifiedByUserId();
        final Long version = competitionClassBE.getVersion();

        OffsetDateTime createdAtUtc = DateProvider.convertTimestamp(competitionClassBE.getCreatedAtUtc());
        OffsetDateTime lastModifiedUtc = DateProvider.convertTimestamp(competitionClassBE.getLastModifiedAtUtc());

        return new CompetitionClassDO(klasseId,klasseName,klasseJahrgangMin,klasseJahrgangMax,klasseNr,createdAtUtc,createdByUserId,lastModifiedUtc, lastModifiedUserId, version);
    };

    public static final Function<CompetitionClassDO, CompetitionClassBE> toCompetitionClassBE = competitionClassDO -> {

        Timestamp createdAtUtcTimestamp = DateProvider.convertOffsetDateTime(competitionClassDO.getCreatedAtUtc());
        Timestamp lastModifiedAtUtcTimestamp = DateProvider.convertOffsetDateTime(competitionClassDO.getLastModifiedAtUtc());

        CompetitionClassBE competitionClassBE = new CompetitionClassBE();
        competitionClassBE.setKlasseId(competitionClassDO.getId());
        competitionClassBE.setKlasseName(competitionClassDO.getKlasseName());
        competitionClassBE.setKlasseAlterMin(competitionClassDO.getKlasseJahrgangMin());
        competitionClassBE.setKlasseAlterMax(competitionClassDO.getKlasseJahrgangMax());
        competitionClassBE.setKlasseNr(competitionClassDO.getKlasseNr());

        competitionClassBE.setCreatedAtUtc(createdAtUtcTimestamp);
        competitionClassBE.setLastModifiedAtUtc(lastModifiedAtUtcTimestamp);
        competitionClassBE.setLastModifiedByUserId(competitionClassBE.getLastModifiedByUserId());
        competitionClassBE.setCreatedByUserId(competitionClassBE.getCreatedByUserId());
        competitionClassBE.setVersion(competitionClassBE.getVersion());

        // Sets the Age based on the Year
        int year = Calendar.getInstance().get(Calendar.YEAR);
        competitionClassBE.setKlasseAlterMin(year - competitionClassDO.getKlasseJahrgangMin());
        competitionClassBE.setKlasseAlterMax(year - competitionClassDO.getKlasseJahrgangMax());

        return competitionClassBE;
    };
}
