package de.bogenliga.application.services.v1.einstellungen.mapper;

import java.sql.Date;
import java.util.function.Function;
import de.bogenliga.application.common.service.mapping.DataTransferObjectMapper;
import de.bogenliga.application.services.v1.einstellungen.model.EinstellungenDTO;


/**
 * TODO [AL] class documentation
 *
 * @author Lars Bahnmüller, Lars_Herbert.Bahnmueller@Student.Reutlingen-University.DE
 */
public final class EinstellungenDTOMapper implements DataTransferObjectMapper {

    public static final Function<EinstellungenDO, EinstellungenDTO> toDTO = EinstellungenDO -> {


    };

    /**
     * I map the {@link EinstellungenDTO} object to the {@link EinstellungenDO} object
     */
    public static final Function<EinstellungenDTO, EinstellungenDO> toDO = dto -> {


    };

}
