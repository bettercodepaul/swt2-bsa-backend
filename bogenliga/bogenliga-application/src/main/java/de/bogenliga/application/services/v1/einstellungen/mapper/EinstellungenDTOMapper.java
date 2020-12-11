package de.bogenliga.application.services.v1.einstellungen.mapper;

import java.sql.Date;
import java.util.function.Function;
import de.bogenliga.application.business.einstellungen.api.types.EinstellungenDO;
import de.bogenliga.application.common.service.mapping.DataTransferObjectMapper;
import de.bogenliga.application.services.v1.einstellungen.model.EinstellungenDTO;


/**
 * TODO [AL] class documentation
 *
 * @author Lars Bahnmüller, Lars_Herbert.Bahnmueller@Student.Reutlingen-University.DE
 */
public final class EinstellungenDTOMapper implements DataTransferObjectMapper {

    public static final Function<EinstellungenDO, EinstellungenDTO> toDTO = einstellungenDO -> {

        final String einstellungenId= einstellungenDO.getId();
        final String einstellungenValue = einstellungenDO.getValue();
        final String einstellungenKey = einstellungenDO.getKey();

        return new EinstellungenDTO(einstellungenId, einstellungenValue, einstellungenKey);

    };

    /**
     * I map the {@link EinstellungenDTO} object to the {@link EinstellungenDO} object
     */
    public static final Function<EinstellungenDTO, EinstellungenDO> toDO = dto -> {

        final String einstellungenId=dto.getId();
        final String einstellungenValue = dto.getValue();
        final String einstellungenKey = dto.getKey();

        return new EinstellungenDO(einstellungenId,einstellungenValue,einstellungenKey);

    };


    /**
     * Constructor
     */
    private EinstellungenDTOMapper() {
        // empty private constructor
    }

}
