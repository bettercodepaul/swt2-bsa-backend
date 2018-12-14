package de.bogenliga.application.business.Setzliste.impl.mapper;

import de.bogenliga.application.business.Setzliste.api.types.SetzlisteDO;
import de.bogenliga.application.business.Setzliste.impl.entity.SetzlisteBE;
import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import de.bogenliga.application.common.component.mapping.ValueObjectMapper;
import de.bogenliga.application.common.time.DateProvider;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.function.Function;

/**
 * I convert the dsbmitglied DataObjects and BusinessEntities.
 *
 */
public class SetzlisteMapper implements ValueObjectMapper {

    /**
     * Converts a {@link SetzlisteBE} to a {@link SetzlisteDO}
     *
     */
    public static final Function<SetzlisteBE, SetzlisteDO> toSetzlisteDO = be -> {

        final Integer ligatabelleTabellenplatz = be.getLigatabelleTabellenplatz();
        final String vereinName = be.getVereinName();
        final Integer mannschaftNummer = be.getMannschaftNummer();
        final String veranstaltungName = be.getVeranstaltungName();
        final Integer wettkampfTag = be.getWettkampfTag();
        final Date wettkampfDatum = be.getWettkampfDatum();
        final String wettkampfBeginn = be.getWettkampfBeginn();
        final String wettkampfOrt = be.getWettkampfOrt();

        // technical parameter
//        Long createdByUserId = be.getCreatedByUserId();
//        Long lastModifiedByUserId = be.getLastModifiedByUserId();
//        Long version = be.getVersion();

//        OffsetDateTime createdAtUtc = DateProvider.convertTimestamp(be.getCreatedAtUtc());
//        OffsetDateTime lastModifiedAtUtc = DateProvider.convertTimestamp(be.getLastModifiedAtUtc());

        return new SetzlisteDO(ligatabelleTabellenplatz, vereinName, mannschaftNummer, veranstaltungName, wettkampfTag,
                wettkampfDatum, wettkampfBeginn, wettkampfOrt);
    };

    /**
     * Converts a {@link SetzlisteDO} to a {@link SetzlisteBE}
     */
    public static final Function<SetzlisteDO, SetzlisteBE> toSetzlisteBE = setzlisteDO -> {

        SetzlisteBE setzlisteBE = new SetzlisteBE();
        setzlisteBE.setLigatabelleTabellenplatz(setzlisteDO.getLigatabelleTabellenplatz());
        setzlisteBE.setVereinName(setzlisteDO.getVereinName());
        setzlisteBE.setMannschaftNummer(setzlisteDO.getMannschaftNummer());
        setzlisteBE.setVeranstaltungName(setzlisteDO.getVeranstaltungName());
        setzlisteBE.setWettkampfTag(setzlisteDO.getWettkampfTag());
        setzlisteBE.setWettkampfDatum(setzlisteDO.getWettkampfDatum());
        setzlisteBE.setWettkampfBeginn(setzlisteDO.getWettkampfBeginn());
        setzlisteBE.setWettkampfOrt(setzlisteDO.getWettkampfOrt());

        return setzlisteBE;
    };


    /**
     * Private constructor
     */
    private SetzlisteMapper() {
        // empty private constructor
    }
}
