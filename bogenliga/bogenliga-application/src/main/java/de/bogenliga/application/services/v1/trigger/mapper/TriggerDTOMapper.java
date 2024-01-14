package de.bogenliga.application.services.v1.trigger.mapper;

import java.time.OffsetDateTime;
import java.util.function.Function;
import de.bogenliga.application.business.trigger.api.types.TriggerDO;
import de.bogenliga.application.common.service.mapping.DataTransferObjectMapper;
import de.bogenliga.application.services.v1.trigger.model.TriggerDTO;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */

/**
 * Mapping the {@link TriggerDO} to the {@link TriggerDTO} object
 */
public class TriggerDTOMapper implements DataTransferObjectMapper {
    public static final Function<TriggerDO, TriggerDTO> toDTO = triggerDO -> {
        final Long id = triggerDO.getId();
        final Long version = triggerDO.getVersion();
        final String kategorie = triggerDO.getKategorie();
        final Long altsystemId = triggerDO.getAltsystemId();
        final Long operation = triggerDO.getOperation();
        final Long status = triggerDO.getStatus();
        final String nachricht = triggerDO.getNachricht();
        final OffsetDateTime createdAtUtc = triggerDO.getCreatedAtUtc();



        return new TriggerDTO(id, version, kategorie, altsystemId, operation, status, nachricht, createdAtUtc);
    };

    /**
     * Mapping the {@link TriggerDTO} to the {@link TriggerDO} object
     */

    public static final Function<TriggerDTO, TriggerDO> toDO = triggerDTO -> {
        final Long id = triggerDTO.getId();
        final Long version = triggerDTO.getVersion();
        final String kategorie = triggerDTO.getKategorie();
        final Long altsystemId = triggerDTO.getAltsystemId();
        final Long operation = triggerDTO.getOperation();
        final Long status = triggerDTO.getStatus();
        final String nachricht = triggerDTO.getNachricht();
        final OffsetDateTime createdAtUtc = triggerDTO.getCreatedAtUtc();

        return new TriggerDO(id, version, kategorie, altsystemId, operation, status, nachricht, createdAtUtc);
    };

    private TriggerDTOMapper(){
        //empty Constructor
    }
}
