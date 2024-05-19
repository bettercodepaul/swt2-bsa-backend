package de.bogenliga.application.business.trigger.impl.business;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.tabletsession.impl.entity.TabletSessionBE;
import de.bogenliga.application.business.tabletsession.impl.mapper.TabletSessionMapper;
import de.bogenliga.application.business.trigger.api.TriggerComponent;
import de.bogenliga.application.business.trigger.api.types.TriggerDO;
import de.bogenliga.application.business.trigger.impl.dao.TriggerDAO;
import de.bogenliga.application.business.trigger.impl.entity.TriggerBE;
import de.bogenliga.application.business.trigger.impl.mapper.TriggerMapper;
import de.bogenliga.application.common.validation.Preconditions;

/**
 * Implementation of {@link TriggerComponent}
 *
 * @author Maximilian Fronmueller
 */
@Component
public abstract class TriggerComponentImpl implements TriggerComponent {
    public final TriggerDAO triggerDAO;

    @Autowired
    public TriggerComponentImpl (TriggerDAO triggerDAO){
        this.triggerDAO = triggerDAO;
    }
    @Override
    public List<TriggerDO> findAll() {
        final List<TriggerBE> triggerBEList = triggerDAO.findAll();
        return triggerBEList.stream().map(TriggerMapper.toTriggerDO).collect(Collectors.toList());
    }


    @Override
    public List<TriggerDO> findAllLimited() {
        final List<TriggerBE> triggerBEList = triggerDAO.findAllLimited();
        return triggerBEList.stream().map(TriggerMapper.toTriggerDO).collect(Collectors.toList());
    }


    @Override
    public List<TriggerDO> findAllUnprocessed() {
        final List<TriggerBE> triggerBEList = triggerDAO.findAllUnprocessed();
        return triggerBEList.stream().map(TriggerMapper.toTriggerDO).collect(Collectors.toList());
    }

    @Override
    public TriggerDO create(TriggerDO triggerDO, Long currentUserId) {
        Preconditions.checkNotNull(currentUserId, "User ID must not be null");
        Preconditions.checkNotNull(triggerDO, "TriggerDO must not be null");

        final TriggerBE triggerBE = TriggerMapper.toTriggerBE.apply(triggerDO);
        TriggerBE tab2BE = triggerDAO.create(triggerBE, currentUserId);
        return TriggerMapper.toTriggerDO.apply(tab2BE);
    }

    @Override
    public TriggerDO update(TriggerDO triggerDO, Long currentUserId) {
        Preconditions.checkNotNull(currentUserId, "User ID must not be null");
        Preconditions.checkNotNull(triggerDO, "TriggerDO must not be null");

        final TriggerBE triggerBE = TriggerMapper.toTriggerBE.apply(triggerDO);
        TriggerBE tab2BE = triggerDAO.update(triggerBE, currentUserId);
        return TriggerMapper.toTriggerDO.apply(tab2BE);
    }

    @Override
    public void deleteEntries(String status, String dateInterval) {
        Preconditions.checkNotNull(status, "Status must not be null");
        Preconditions.checkNotNull(dateInterval, "Date interval must not be null");

        triggerDAO.deleteEntries(status, dateInterval);
    }
}
