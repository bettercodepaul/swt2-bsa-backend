package de.bogenliga.application.business.mannschaftsmitglied.impl.mapper;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.function.Function;
import de.bogenliga.application.business.mannschaftsmitglied.api.types.MannschaftsmitgliedDO;
import de.bogenliga.application.business.mannschaftsmitglied.impl.entity.MannschaftsmitgliedBE;
import de.bogenliga.application.business.mannschaftsmitglied.impl.entity.MannschaftsmitgliedExtendedBE;
import de.bogenliga.application.common.component.mapping.ValueObjectMapper;
import de.bogenliga.application.common.time.DateProvider;


/**
 * @author Philip Dengler; Edited by: Jonas Müller, Felix Maute
 * To ensure that MannschaftsmitgliedDOs that get passed down to the Data-Access-Layer don't have attributes which do not exist
 * in the database table, but MannschaftsmitgliedDOs that get passed up to the Business-Layer can still have these attributes,
 * we separated the DO into MannschaftsmitgliedBE and MannschaftsmitgliedExtendedBE which are mapped to the DO here.
 */
public class MannschaftsmitgliedMapper implements ValueObjectMapper {

    public static final Function<MannschaftsmitgliedExtendedBE, MannschaftsmitgliedDO> toMannschaftsmitgliedDO = beExtended -> {
        final Long id = beExtended.getId();
        final Long mannschaftId = beExtended.getMannschaftId();
        final Long mitgliedId = beExtended.getDsbMitgliedId();
        final Integer mitgliedEingesetzt = beExtended.getDsbMitgliedEingesetzt();

        final String mitgliedVorname = beExtended.getDsbMitgliedVorname();
        final String mitgliedNachname = beExtended.getDsbMitgliedNachname();
        final Long rueckennummer = beExtended.getRueckennummer();

        // technical parameter
        Long createdByUserId = beExtended.getCreatedByUserId();
        Long lastModifiedByUserId = beExtended.getLastModifiedByUserId();
        Long version = beExtended.getVersion();

        OffsetDateTime createdAtUtc = DateProvider.convertTimestamp(beExtended.getCreatedAtUtc());
        OffsetDateTime lastModifiedAtUtc = DateProvider.convertTimestamp(beExtended.getLastModifiedAtUtc());

        return new MannschaftsmitgliedDO(id, mannschaftId, mitgliedId, mitgliedEingesetzt, mitgliedVorname,
                mitgliedNachname,  createdAtUtc, createdByUserId, lastModifiedAtUtc, lastModifiedByUserId, version, rueckennummer);
    };


    public static final Function<MannschaftsmitgliedDO, MannschaftsmitgliedBE> toMannschaftsmitgliedBE = mannschaftsmitgliedDO -> {

        Timestamp createdAtUtcTimestamp = DateProvider.convertOffsetDateTime(mannschaftsmitgliedDO.getCreatedAtUtc());
        Timestamp lastModifiedAtUtcTimestamp = DateProvider.convertOffsetDateTime(
                mannschaftsmitgliedDO.getLastModifiedAtUtc());

        MannschaftsmitgliedBE mannschaftsmitgliedBE = new MannschaftsmitgliedBE();

        mannschaftsmitgliedBE.setId(mannschaftsmitgliedDO.getId());
        mannschaftsmitgliedBE.setMannschaftId(mannschaftsmitgliedDO.getMannschaftId());
        mannschaftsmitgliedBE.setDsbMitgliedId(mannschaftsmitgliedDO.getDsbMitgliedId());
        mannschaftsmitgliedBE.setDsbMitgliedEingesetzt(mannschaftsmitgliedDO.getDsbMitgliedEingesetzt());

        mannschaftsmitgliedBE.setCreatedAtUtc(createdAtUtcTimestamp);
        mannschaftsmitgliedBE.setCreatedByUserId(mannschaftsmitgliedDO.getCreatedByUserId());
        mannschaftsmitgliedBE.setLastModifiedAtUtc(lastModifiedAtUtcTimestamp);
        mannschaftsmitgliedBE.setLastModifiedByUserId(mannschaftsmitgliedDO.getLastModifiedByUserId());
        mannschaftsmitgliedBE.setVersion(mannschaftsmitgliedDO.getVersion());
        mannschaftsmitgliedBE.setRueckennummer(mannschaftsmitgliedDO.getRueckennummer());

        return mannschaftsmitgliedBE;
    };


    /**
     * Private constructor
     */
    private MannschaftsmitgliedMapper() {
    }
}
