package de.bogenliga.application.business.trigger.impl.business;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.trigger.api.TriggerComponent;
import de.bogenliga.application.business.trigger.api.types.TriggerDO;
import de.bogenliga.application.business.trigger.impl.dao.TriggerDAO;
import de.bogenliga.application.business.trigger.impl.entity.TriggerBE;
import de.bogenliga.application.business.trigger.api.types.TriggerCountDO;
import de.bogenliga.application.business.trigger.impl.dao.TriggerCountDAO;
import de.bogenliga.application.business.trigger.impl.entity.TriggerCountBE;
import de.bogenliga.application.business.trigger.impl.mapper.TriggerMapper;
import de.bogenliga.application.common.validation.Preconditions;

/**
 * Implementation of {@link TriggerComponent}
 *
 * @author Maximilian Fronmueller
 */
@Component
public class TriggerComponentImpl implements TriggerComponent {
    public final TriggerDAO triggerDAO;
    public final TriggerCountDAO triggerCountDAO;

    @Autowired
    public TriggerComponentImpl (TriggerDAO triggerDAO, TriggerCountDAO triggerCountDAO){
        this.triggerDAO = triggerDAO;
        this.triggerCountDAO = triggerCountDAO;
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
    public List<TriggerDO> findAllWithPages(String multiplicator,String pageLimit,String dateInterval) {
        final List<TriggerBE> triggerBEList = triggerDAO.findAllWithPages(multiplicator,pageLimit,dateInterval);
        return triggerBEList.stream().map(TriggerMapper.toTriggerDO).collect(Collectors.toList());
    }
    @Override
    public List<TriggerDO> findAllSuccessed(String multiplicator,String pageLimit,String dateInterval) {
        final List<TriggerBE> triggerBEList = triggerDAO.findSuccessed(multiplicator,pageLimit,dateInterval);
        return triggerBEList.stream().map(TriggerMapper.toTriggerDO).collect(Collectors.toList());
    }
    @Override
    public List<TriggerDO> findAllErrors(String multiplicator,String pageLimit,String dateInterval) {
        final List<TriggerBE> triggerBEList = triggerDAO.findErrors(multiplicator,pageLimit,dateInterval);
        return triggerBEList.stream().map(TriggerMapper.toTriggerDO).collect(Collectors.toList());
    }
    @Override
    public List<TriggerDO> findAllInProgress(String multiplicator,String pageLimit,String dateInterval) {
        final List<TriggerBE> triggerBEList = triggerDAO.findInProgress(multiplicator,pageLimit,dateInterval);
        return triggerBEList.stream().map(TriggerMapper.toTriggerDO).collect(Collectors.toList());
    }
    @Override
    public List<TriggerDO> findAllNews(String multiplicator,String pageLimit,String dateInterval) {
        final List<TriggerBE> triggerBEList = triggerDAO.findNews(multiplicator,pageLimit,dateInterval);
        return triggerBEList.stream().map(TriggerMapper.toTriggerDO).collect(Collectors.toList());
    }
    @Override
    public void deleteEntries(String status, String dateInterval) {
        triggerDAO.deleteEntries(status,dateInterval);
    }
    @Override
    public List<TriggerDO> findAllUnprocessed() {
        final List<TriggerBE> triggerBEList = triggerDAO.findAllUnprocessed();
        return triggerBEList.stream().map(TriggerMapper.toTriggerDO).collect(Collectors.toList());
    }
    @Override
    public TriggerCountDO findAllCount(){
        final TriggerCountBE triggerBECount = triggerCountDAO.findAllCount();
        return TriggerMapper.toTriggerCountDO.apply(triggerBECount);
    }
    @Override
    public TriggerCountDO findUnprocessedCount(){
        final TriggerCountBE triggerBECount = triggerCountDAO.findUnprocessedCount();
        return TriggerMapper.toTriggerCountDO.apply(triggerBECount);
    }

    @Override
    public TriggerCountDO findInProgressCount(){
        final TriggerCountBE triggerBECount = triggerCountDAO.findInProgressCount();
        return TriggerMapper.toTriggerCountDO.apply(triggerBECount);
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
}
