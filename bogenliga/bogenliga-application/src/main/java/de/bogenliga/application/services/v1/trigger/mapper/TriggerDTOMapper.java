package de.bogenliga.application.services.v1.trigger.mapper;

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
        final String timestamp = triggerDO.getTimestamp();
        final String description = triggerDO.getDescription();
        final String status = triggerDO.getStatus();



        return new TriggerDTO(id, version, timestamp, description, status);
    };

    /**
     * Mapping the {@link TriggerDTO} to the {@link TriggerDO} object
     */

    public static final Function<TriggerDTO, TriggerDO> toDO = triggerDTO -> {
        final Long id = triggerDTO.getId();
        final Long version = triggerDTO.getVersion();
        final String timestamp = triggerDTO.getTimestamp();
        final String description = triggerDTO.getDescription();
        final String status = triggerDTO.getStatus();

        return new TriggerDO(id, version, timestamp, description, status);
    };

    private TriggerDTOMapper(){
        //empty Constructor
    }
}
