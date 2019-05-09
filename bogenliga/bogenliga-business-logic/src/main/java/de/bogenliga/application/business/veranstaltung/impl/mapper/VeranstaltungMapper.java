package de.bogenliga.application.business.veranstaltung.impl.mapper;

import de.bogenliga.application.business.liga.impl.entity.LigaBE;
import de.bogenliga.application.business.wettkampftyp.impl.entity.WettkampftypBE;
import de.bogenliga.application.common.component.mapping.ValueObjectMapper;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.function.Function;
import de.bogenliga.application.business.veranstaltung.api.types.VeranstaltungDO;
import de.bogenliga.application.business.veranstaltung.impl.entity.VeranstaltungBE;
import de.bogenliga.application.business.user.impl.entity.UserBE;
import de.bogenliga.application.business.wettkampftyp.impl.entity.WettkampftypBE;

import de.bogenliga.application.common.time.DateProvider;

/**
 * TODO [AL] class documentation
 *
 * @author Daniel Schott, daniel.schott@student.reutlingen-university.de
 */
public class VeranstaltungMapper implements ValueObjectMapper {


    /**
     * Converts a {@link VeranstaltungBE} to a {@link VeranstaltungDO}
     *
     */
    public static final VeranstaltungDO toVeranstaltungDO(VeranstaltungBE veranstaltungBE, UserBE userBE, WettkampftypBE wettkamptypBE, LigaBE ligaBE){

        OffsetDateTime createdAtUtc = DateProvider.convertTimestamp(veranstaltungBE.getCreatedAtUtc());
        OffsetDateTime lastModifiedAtUtc = DateProvider.convertTimestamp(veranstaltungBE.getLastModifiedAtUtc());

        VeranstaltungDO veranstaltungDO = new VeranstaltungDO(
                veranstaltungBE.getVeranstaltungID(),
                veranstaltungBE.getVeranstaltungWettkampftypID(),
                veranstaltungBE.getVeranstaltungName(),
                veranstaltungBE.getVeranstaltungSportJahr(),
                veranstaltungBE.getVeranstaltungMeldeDeadline(),
                veranstaltungBE.getVeranstaltungLigaleiterID(),
                veranstaltungBE.getVeranstaltungLigaID(),
                userBE.getUserEmail(),
                wettkamptypBE.getwettkampftypname(),
                ligaBE.getLigaName()
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
       veranstaltungBE.setVeranstaltungID(veranstaltungDO.getVeranstaltungID());
       veranstaltungBE.setVeranstaltungWettkampftypID(veranstaltungDO.getVeranstaltungWettkampftypID());
       veranstaltungBE.setVeranstaltungName(veranstaltungDO.getVeranstaltungName());
       veranstaltungBE.setVeranstaltungMeldeDeadline(veranstaltungDO.getVeranstaltungMeldeDeadline());
       veranstaltungBE.setVeranstaltungLigaleiterID(veranstaltungDO.getVeranstaltungLigaleiterID());
       veranstaltungBE.setVeranstaltungLigaID(veranstaltungDO.getVeranstaltungLigaID());
       veranstaltungBE.setVeranstaltungWettkampftypName(veranstaltungDO.getVeranstaltungWettkampftypName());

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
