package de.bogenliga.application.business.trigger.impl.mapper;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.function.Function;
import de.bogenliga.application.business.trigger.api.types.TriggerChangeStatus;
import de.bogenliga.application.business.trigger.api.types.TriggerChangeOperation;
import de.bogenliga.application.business.trigger.api.types.TriggerDO;
import de.bogenliga.application.business.trigger.impl.entity.TriggerBE;
import de.bogenliga.application.business.trigger.api.types.TriggerCountDO;
import de.bogenliga.application.business.trigger.impl.entity.TriggerCountBE;
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
        final TriggerChangeOperation operation = triggerBE.getChangeOperation();
        final TriggerChangeStatus status = triggerBE.getChangeStatus();
        final String nachricht = triggerBE.getNachricht();

        OffsetDateTime createdAtUtc = DateProvider.convertTimestamp(triggerBE.getCreatedAtUtc());
        OffsetDateTime runAtUtc = DateProvider.convertTimestamp(triggerBE.getRunAtUtc());
        OffsetDateTime lastModifiedAtUtc = DateProvider.convertTimestamp(triggerBE.getLastModifiedAtUtc());


        TriggerDO vo = new TriggerDO(id, kategorie, altsystemId, operation, status, nachricht, createdAtUtc,
                lastModifiedAtUtc);

        vo.setCreatedAtUtc(createdAtUtc);
        vo.setCreatedByUserId(triggerBE.getCreatedByUserId());
        vo.setLastModifiedAtUtc(lastModifiedAtUtc);
        vo.setLastModifiedByUserId(triggerBE.getLastModifiedByUserId());
        vo.setVersion(triggerBE.getVersion());

        return vo;
    };
    public static final Function<TriggerDO, TriggerBE> toTriggerBE = triggerDO -> {
        Timestamp runAtUtcTimestamp = DateProvider.convertOffsetDateTime(triggerDO.getRunAtUtc());
        Timestamp createdAtUtcTimestamp = DateProvider.convertOffsetDateTime(triggerDO.getCreatedAtUtc());

        TriggerBE triggerBE = new TriggerBE();
        triggerBE.setId(triggerDO.getId());
        triggerBE.setKategorie(triggerDO.getKategorie());
        triggerBE.setAltsystemId(triggerDO.getAltsystemId());
        triggerBE.setChangeOperation(triggerDO.getOperation());
        triggerBE.setChangeStatus(triggerDO.getStatus());
        triggerBE.setNachricht(triggerDO.getNachricht());

        triggerBE.setCreatedAtUtc(createdAtUtcTimestamp);
        triggerBE.setRunAtUtc(runAtUtcTimestamp);


        return triggerBE;
    };
    public static final Function<TriggerCountDO, TriggerCountBE> toTriggerCountBE = triggerCountDO -> {
        TriggerCountBE triggerCountBE = new TriggerCountBE();
        triggerCountBE.setCount(triggerCountDO.getCount());
        return triggerCountBE;

    };

    public static final Function<TriggerCountBE, TriggerCountDO> toTriggerCountDO = triggerCountBE -> {
        final Long count = triggerCountBE.getCount();

        TriggerCountDO vo = new TriggerCountDO(count);

        return vo;
    };
}
