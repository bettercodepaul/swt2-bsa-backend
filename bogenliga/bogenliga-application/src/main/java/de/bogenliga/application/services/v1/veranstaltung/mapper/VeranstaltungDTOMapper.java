package de.bogenliga.application.services.v1.veranstaltung.mapper;

import java.sql.Date;
import java.util.function.Function;
import de.bogenliga.application.business.veranstaltung.api.types.VeranstaltungDO;
import de.bogenliga.application.business.vereine.api.types.VereinDO;
import de.bogenliga.application.common.service.mapping.DataTransferObjectMapper;
import de.bogenliga.application.services.v1.veranstaltung.model.VeranstaltungDTO;

/**
 * I map the {@link VereinDO} and {@link VeranstaltungDTO} objects
 *
 * @author Marvin Holm
 */
public class VeranstaltungDTOMapper implements DataTransferObjectMapper {

    /**
     * I map the {@link VeranstaltungDO} to the {@link VeranstaltungDTO} object
     */

    public static final Function<VeranstaltungDO, VeranstaltungDTO> toDTO = veranstaltungDO -> {
        final Long veranstaltungId = veranstaltungDO.getVeranstaltungID();
        final Long veranstaltungWettkampfTypId = veranstaltungDO.getVeranstaltungWettkampftypID();
        final String veranstaltungName = veranstaltungDO.getVeranstaltungName();
        final Long veranstaltungSportjahr = veranstaltungDO.getVeranstaltungSportJahr();
        final Date veranstaltungMeldeDeadline = veranstaltungDO.getVeranstaltungMeldeDeadline();
        final Long veranstaltungLigaleiterId = veranstaltungDO.getVeranstaltungLigaleiterID();

        return new VeranstaltungDTO(veranstaltungId, veranstaltungWettkampfTypId, veranstaltungName, veranstaltungSportjahr,
                veranstaltungMeldeDeadline, veranstaltungLigaleiterId);
    };

    /**
     * Constructor
     */
    private VeranstaltungDTOMapper(){
        //empty
    }
}
