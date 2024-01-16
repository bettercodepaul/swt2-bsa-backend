package de.bogenliga.application.business.trigger.impl.mapper;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.function.Function;
import de.bogenliga.application.business.trigger.api.types.MigrationChangeState;
import de.bogenliga.application.business.trigger.api.types.MigrationChangeType;
import de.bogenliga.application.business.trigger.api.types.TriggerDO;
import de.bogenliga.application.business.trigger.impl.entity.TriggerBE;
import de.bogenliga.application.common.component.mapping.ValueObjectMapper;
import de.bogenliga.application.common.time.DateProvider;

/**
 * I convert the region DataObjects and BusinessEntities
 *
 * @author Maximilian Fronmueller, maximilian.fronmueller@student.reutlingen-university.de
 */
public class TriggerMapper implements ValueObjectMapper {
    private TriggerMapper(){
        //empty constructor
    }
    public static final Function<TriggerBE, TriggerDO> toTriggerDO = triggerBE -> {
        final Long id = triggerBE.getId();
        final String kategorie = triggerBE.getKategorie();
        final Long altsystemId = triggerBE.getAltsystemId();
        final MigrationChangeType operation = triggerBE.getChangeOperation();
        final MigrationChangeState status = triggerBE.getChangeStatus();
        final String nachricht = triggerBE.getNachricht();

        OffsetDateTime createdAtUtc = DateProvider.convertTimestamp(triggerBE.getCreatedAtUtc());

        return new TriggerDO(id, kategorie, altsystemId, operation, status, nachricht, createdAtUtc);
    };
    public static final Function<TriggerDO, TriggerBE> toTriggerBE = triggerDO -> {
        Timestamp createdAtUtcTimestamp = DateProvider.convertOffsetDateTime(triggerDO.getCreatedAtUtc());

        TriggerBE triggerBE = new TriggerBE();
        triggerBE.setId(triggerDO.getId());
        triggerBE.setKategorie(triggerDO.getKategorie());
        triggerBE.setAltsystemId(triggerDO.getAltsystemId());
        triggerBE.setChangeOperation(triggerDO.getOperation());
        triggerBE.setChangeStatus(triggerDO.getStatus());
        triggerBE.setNachricht(triggerDO.getNachricht());

        triggerBE.setCreatedAtUtc(createdAtUtcTimestamp);


        return triggerBE;
    };
}
