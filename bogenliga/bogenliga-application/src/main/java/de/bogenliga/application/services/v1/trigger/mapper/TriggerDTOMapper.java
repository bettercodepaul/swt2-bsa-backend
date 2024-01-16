package de.bogenliga.application.services.v1.trigger.mapper;

import java.time.OffsetDateTime;
import java.util.function.Function;
import de.bogenliga.application.business.trigger.api.types.TriggerChangeStatus;
import de.bogenliga.application.business.trigger.api.types.TriggerChangeOperation;
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
        final String kategorie = triggerDO.getKategorie();
        final Long altsystemId = triggerDO.getAltsystemId();
        final TriggerChangeOperation operation = triggerDO.getOperation();
        final TriggerChangeStatus status = triggerDO.getStatus();
        final String nachricht = triggerDO.getNachricht();
        final OffsetDateTime runAtUtc = triggerDO.getRunAtUtc();



        return new TriggerDTO(id, kategorie, altsystemId, operation, status, nachricht, runAtUtc);
    };

    /**
     * Mapping the {@link TriggerDTO} to the {@link TriggerDO} object
     */

    public static final Function<TriggerDTO, TriggerDO> toDO = triggerDTO -> {
        final Long id = triggerDTO.getId();
        final String kategorie = triggerDTO.getKategorie();
        final Long altsystemId = triggerDTO.getAltsystemId();
        final TriggerChangeOperation operation = triggerDTO.getOperation();
        final TriggerChangeStatus status = triggerDTO.getStatus();
        final String nachricht = triggerDTO.getNachricht();
        final OffsetDateTime runAtUtc = triggerDTO.getRunAtUtc();

        return new TriggerDO(id, kategorie, altsystemId, operation, status, nachricht, runAtUtc);
    };

    private TriggerDTOMapper(){
        //empty Constructor
    }
}
