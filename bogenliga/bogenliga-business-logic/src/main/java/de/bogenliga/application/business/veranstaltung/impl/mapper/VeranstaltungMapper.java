package de.bogenliga.application.business.veranstaltung.impl.mapper;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.function.Function;
import de.bogenliga.application.business.veranstaltung.api.types.VeranstaltungDO;
import de.bogenliga.application.business.veranstaltung.impl.entity.VeranstaltungBE;
import de.bogenliga.application.business.veranstaltung.impl.entity.VeranstaltungBEext;
import de.bogenliga.application.business.veranstaltung.impl.entity.VeranstaltungPhase;
import de.bogenliga.application.common.component.mapping.ValueObjectMapper;
import de.bogenliga.application.common.time.DateProvider;

/**
 * @author Daniel Schott, daniel.schott@student.reutlingen-university.de
 */
public class VeranstaltungMapper implements ValueObjectMapper {


    /**
     * mapps a Veranstaltung Data Object into a Veranstaltung Business Entity
     */
    public static final Function<VeranstaltungDO, VeranstaltungBE> toVeranstaltungBE = veranstaltungDO -> {

        VeranstaltungPhase veranstaltungPhase = new VeranstaltungPhase();

        Timestamp createdAtUtcTimestamp = DateProvider.convertOffsetDateTime(veranstaltungDO.getCreatedAtUtc());
        Timestamp lastModifiedAtUtcTimestamp = DateProvider.convertOffsetDateTime(
                veranstaltungDO.getLastModifiedAtUtc());

        VeranstaltungBE veranstaltungBE = new VeranstaltungBE();
        veranstaltungBE.setVeranstaltungId(veranstaltungDO.getVeranstaltungID());
        veranstaltungBE.setVeranstaltungWettkampftypId(veranstaltungDO.getVeranstaltungWettkampftypID());
        veranstaltungBE.setVeranstaltungName(veranstaltungDO.getVeranstaltungName());
        veranstaltungBE.setVeranstaltungMeldedeadline(veranstaltungDO.getVeranstaltungMeldeDeadline());
        veranstaltungBE.setVeranstaltungLigaleiterId(veranstaltungDO.getVeranstaltungLigaleiterID());
        veranstaltungBE.setVeranstaltungLigaId(veranstaltungDO.getVeranstaltungLigaID());
        veranstaltungBE.setVeranstaltungSportjahr(veranstaltungDO.getVeranstaltungSportJahr());

        /* the phase in veranstaltungBE is from type Integer and the phase of tempVeranstaltungDO is from type String.
         *  The phase will convert from String to Integer, because the phase is stored in the database as Integer,
         *  but in the dialogs of the frontend it should show the phase as text.
         **/
        veranstaltungBE.setVeranstaltungPhase(veranstaltungPhase.getPhaseFromStringToInt(
                veranstaltungDO.getVeranstaltungPhase()));

        veranstaltungBE.setVeranstaltungGroesse(veranstaltungDO.getVeranstaltungGroesse());

        veranstaltungBE.setCreatedAtUtc(createdAtUtcTimestamp);
        veranstaltungBE.setCreatedByUserId(veranstaltungDO.getCreatedByUserId());
        veranstaltungBE.setLastModifiedAtUtc(lastModifiedAtUtcTimestamp);
        veranstaltungBE.setLastModifiedByUserId(veranstaltungDO.getLastModifiedByUserId());
        veranstaltungBE.setVersion(veranstaltungDO.getVersion());

        return veranstaltungBE;
    };


    private VeranstaltungMapper() {
        //empty Constructor
    }


    /**
     * Converts a {@link VeranstaltungBE} to a {@link VeranstaltungDO}
     */
    public static VeranstaltungDO toVeranstaltungDO(VeranstaltungBE veranstaltungBE, String userEmail,
                                                          String wettkamptypName, String ligaName) {

        VeranstaltungPhase veranstaltungPhase = new VeranstaltungPhase();

        OffsetDateTime createdAtUtc = DateProvider.convertTimestamp(veranstaltungBE.getCreatedAtUtc());
        OffsetDateTime lastModifiedAtUtc = DateProvider.convertTimestamp(veranstaltungBE.getLastModifiedAtUtc());

        VeranstaltungDO veranstaltungDO = new VeranstaltungDO(
                veranstaltungBE.getVeranstaltungId(),
                veranstaltungBE.getVeranstaltungWettkampftypId(),
                veranstaltungBE.getVeranstaltungName(),
                veranstaltungBE.getVeranstaltungSportjahr(),
                veranstaltungBE.getVeranstaltungMeldedeadline(),
                veranstaltungBE.getVeranstaltungLigaleiterId(),
                veranstaltungBE.getVeranstaltungLigaId(),
                userEmail,
                wettkamptypName,
                ligaName,
                String.valueOf(veranstaltungBE.getVeranstaltungPhase()), //temporary setting with number  as string
                veranstaltungBE.getVeranstaltungGroesse()
        );
        veranstaltungDO.setVeranstaltungPhase(veranstaltungPhase.getPhaseAsString(veranstaltungBE.getVeranstaltungPhase()));

        veranstaltungDO.setCreatedAtUtc(createdAtUtc);
        veranstaltungDO.setLastModifiedAtUtc(lastModifiedAtUtc);
        return veranstaltungDO;

    }

    public static VeranstaltungDO toVeranstaltungDOext(VeranstaltungBEext be) {

        VeranstaltungPhase veranstaltungPhase = new VeranstaltungPhase();

        OffsetDateTime createdAtUtc = DateProvider.convertTimestamp(be.getCreatedAtUtc());
        OffsetDateTime lastModifiedAtUtc = DateProvider.convertTimestamp(be.getLastModifiedAtUtc());

        VeranstaltungDO veranstaltungDO = new VeranstaltungDO(
                be.getVeranstaltungId(),
                be.getVeranstaltungWettkampftypId(),
                be.getVeranstaltungName(),
                be.getVeranstaltungSportjahr(),
                be.getVeranstaltungMeldedeadline(),
                be.getVeranstaltungLigaleiterId(),
                be.getVeranstaltungLigaId(),
                be.getLigaLeiterEmail(),
                be.getwettkampftypname(),
                be.getLigaName(),
                String.valueOf(be.getVeranstaltungPhase()), //temporary setting with number  as string
                be.getVeranstaltungGroesse()
        );
        veranstaltungDO.setVeranstaltungPhase(veranstaltungPhase.getPhaseAsString(be.getVeranstaltungPhase()));
        veranstaltungDO.setCreatedAtUtc(createdAtUtc);
        veranstaltungDO.setLastModifiedAtUtc(lastModifiedAtUtc);
        return veranstaltungDO;
    }

}