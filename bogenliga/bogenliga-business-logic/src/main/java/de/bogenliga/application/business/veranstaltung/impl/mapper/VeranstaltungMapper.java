package de.bogenliga.application.business.veranstaltung.impl.mapper;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.function.Function;
import de.bogenliga.application.business.liga.api.types.LigaDO;
import de.bogenliga.application.business.user.api.types.UserDO;
import de.bogenliga.application.business.veranstaltung.api.types.VeranstaltungDO;
import de.bogenliga.application.business.veranstaltung.impl.entity.VeranstaltungBE;
import de.bogenliga.application.business.wettkampftyp.api.types.WettkampfTypDO;
import de.bogenliga.application.common.component.mapping.ValueObjectMapper;
import de.bogenliga.application.common.time.DateProvider;

/**
 * @author Daniel Schott, daniel.schott@student.reutlingen-university.de
 */
public class VeranstaltungMapper implements ValueObjectMapper {


    /**
     * Converts a {@link VeranstaltungBE} to a {@link VeranstaltungDO}
     *
     */
    public static final VeranstaltungDO toVeranstaltungDO(VeranstaltungBE veranstaltungBE, UserDO userDO, WettkampfTypDO wettkamptypDO, LigaDO ligaDO){

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
                userDO.getEmail(),
                wettkamptypDO.getName(),
                ligaDO.getName(),
                veranstaltungBE.getVeranstaltungPhase()
        );
        veranstaltungDO.setCreatedAtUtc(createdAtUtc);
        veranstaltungDO.setLastModifiedAtUtc(lastModifiedAtUtc);
        return veranstaltungDO;

    }


    /**
     * mapps a Veranstaltung Data Object into a Veranstaltung Business Entity
     */
    public static final Function<VeranstaltungDO, VeranstaltungBE> toVeranstaltungBE= veranstaltungDO -> {

        Timestamp createdAtUtcTimestamp = DateProvider.convertOffsetDateTime(veranstaltungDO.getCreatedAtUtc());
        Timestamp lastModifiedAtUtcTimestamp = DateProvider.convertOffsetDateTime(veranstaltungDO.getLastModifiedAtUtc());

       VeranstaltungBE veranstaltungBE = new VeranstaltungBE();
       veranstaltungBE.setVeranstaltungId(veranstaltungDO.getVeranstaltungID());
       veranstaltungBE.setVeranstaltungWettkampftypId(veranstaltungDO.getVeranstaltungWettkampftypID());
       veranstaltungBE.setVeranstaltungName(veranstaltungDO.getVeranstaltungName());
       veranstaltungBE.setVeranstaltungMeldedeadline(veranstaltungDO.getVeranstaltungMeldeDeadline());
       veranstaltungBE.setVeranstaltungLigaleiterId(veranstaltungDO.getVeranstaltungLigaleiterID());
       veranstaltungBE.setVeranstaltungLigaId(veranstaltungDO.getVeranstaltungLigaID());
       veranstaltungBE.setVeranstaltungSportjahr(veranstaltungDO.getVeranstaltungSportJahr());
       veranstaltungBE.setVeranstaltungPhase(veranstaltungDO.getVeranstaltungPhase());

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
