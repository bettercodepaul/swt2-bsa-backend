package de.bogenliga.application.business.veranstaltung.impl.mapper;

import de.bogenliga.application.common.component.mapping.ValueObjectMapper;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.function.Function;
import de.bogenliga.application.business.veranstaltung.api.types.VeranstaltungDO;
import de.bogenliga.application.business.veranstaltung.impl.entity.VeranstaltungBE;

import de.bogenliga.application.common.time.DateProvider;

/**
 * TODO [AL] class documentation
 *
 * @author Daniel Schott, daniel.schott@student.reutlingen-university.de
 */
public class VeranstaltungMapper implements ValueObjectMapper {

    /**
     * Mapps a Veranstaltung Business Entity to a Veranstaltung Data Object
     */
    public static final Function<VeranstaltungBE, VeranstaltungDO> toVeranstaltungDO= be -> {

        final Long id = be.getVeranstaltungID();
        final Long wettkampfTypID = be.getVeranstaltungWettkampftypID();
        final String name= be.getVeranstaltungName();
        final Long sportJahr = be.getVeranstaltungSportJahr();
        final Date meldeDeadline = be.getVeranstaltungMeldeDeadline();
        final Long ligaleiterID = be.getVeranstaltungLigaleiterID();

        // technical parameter
        Long createdByUserId = be.getCreatedByUserId();
        Long lastModifiedByUserId = be.getLastModifiedByUserId();
        Long version = be.getVersion();

        OffsetDateTime createdAtUtc = DateProvider.convertTimestamp(be.getCreatedAtUtc());
        OffsetDateTime lastModifiedAtUtc = DateProvider.convertTimestamp(be.getLastModifiedAtUtc());

        return new VeranstaltungDO(id, wettkampfTypID, name, sportJahr, meldeDeadline, ligaleiterID,
                createdAtUtc, createdByUserId, lastModifiedAtUtc, lastModifiedByUserId, version);
    };


    /**
     * mapps a Veranstaltung Data Object into a Veranstaltung Business Entity
     */
    public static final Function<VeranstaltungDO, VeranstaltungBE> toVeranstaltungBE= veranstaltungDO -> {

        Timestamp createdAtUtcTimestamp = DateProvider.convertOffsetDateTime(veranstaltungDO.getCreatedAtUtc());
        Timestamp lastModifiedAtUtcTimestamp = DateProvider.convertOffsetDateTime(veranstaltungDO.getLastModifiedAtUtc());

       VeranstaltungBE veranstaltungBE = new VeranstaltungBE();
       veranstaltungBE.setVeranstaltungID(veranstaltungDO.getVeranstaltungID());
       veranstaltungBE.setVeranstaltungWettkampftypID(veranstaltungDO.getVeranstaltungWettkampftypID());
       veranstaltungBE.setVeranstaltungName(veranstaltungDO.getVeranstaltungName());
       veranstaltungBE.setVeranstaltungMeldeDeadline(veranstaltungDO.getVeranstaltungMeldeDeadline());
       veranstaltungBE.setVeranstaltungLigaleiterID(veranstaltungDO.getVeranstaltungLigaleiterID());

       veranstaltungBE.setCreatedAtUtc(createdAtUtcTimestamp);
       veranstaltungBE.setCreatedByUserId(veranstaltungDO.getCreatedByUserId());
       veranstaltungBE.setLastModifiedAtUtc(lastModifiedAtUtcTimestamp);
       veranstaltungBE.setLastModifiedByUserId(veranstaltungDO.getLastModifiedByUserId());
       veranstaltungBE.setVersion(veranstaltungDO.getVersion());

        return veranstaltungBE;
    };


    private VeranstaltungMapper(){
        //empty Constructor
    }
}
