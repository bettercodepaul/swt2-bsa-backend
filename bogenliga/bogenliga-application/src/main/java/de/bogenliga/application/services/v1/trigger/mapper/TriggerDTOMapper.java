package de.bogenliga.application.services.v1.trigger.mapper;

import java.time.OffsetDateTime;
import java.util.function.Function;
import de.bogenliga.application.business.trigger.api.types.TriggerChangeStatus;
import de.bogenliga.application.business.trigger.api.types.TriggerChangeOperation;
import de.bogenliga.application.business.trigger.api.types.TriggerCountDO;
import de.bogenliga.application.business.trigger.api.types.TriggerDO;
import de.bogenliga.application.common.service.mapping.DataTransferObjectMapper;
import de.bogenliga.application.services.v1.trigger.model.TriggerCountDTO;
import de.bogenliga.application.services.v1.trigger.model.TriggerDTO;

/**
 *
 *
 * @author Maximilian Fronmüller
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
        final OffsetDateTime createdAtUtc = triggerDO.getCreatedAt();
        final OffsetDateTime runAtUtc = triggerDO.getRunAtUtc();
        final OffsetDateTime lastModifiedAtUtc= triggerDO.getUpdatedAtUtc();



        return new TriggerDTO(id, kategorie, altsystemId, operation, status, nachricht, createdAtUtc, runAtUtc, lastModifiedAtUtc);
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
        final OffsetDateTime createdAtUtc = triggerDTO.getCreatedAtUtc();
        final OffsetDateTime runAtUtc = triggerDTO.getRunAtUtc();
        final OffsetDateTime lastModifiedAtUtc = triggerDTO.getlastModifiedAtUtc();

        return new TriggerDO(id, kategorie, altsystemId, operation, status, nachricht, createdAtUtc, runAtUtc, lastModifiedAtUtc);
    };
    public static final Function<TriggerCountDO, TriggerCountDTO> toCountDTO = triggerCountDO -> {
        final Long count = triggerCountDO.getCount();

        return new TriggerCountDTO(count);
    };

    public static final Function<TriggerCountDTO, TriggerCountDO> toCountDO = triggerCountDTO -> {
        final Long count = triggerCountDTO.getCount();

        return new TriggerCountDO(count);
    };

    private TriggerDTOMapper(){
        //empty Constructor
    }
}
