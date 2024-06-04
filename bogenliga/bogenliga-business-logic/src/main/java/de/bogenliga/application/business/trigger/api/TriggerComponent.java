package de.bogenliga.application.business.trigger.api;
import java.util.List;
import de.bogenliga.application.business.trigger.api.types.TriggerCountDO;
import de.bogenliga.application.common.component.ComponentFacade;
import de.bogenliga.application.business.trigger.api.types.TriggerDO;
/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public interface TriggerComponent extends ComponentFacade{
    /**
     * Return all class entries
     * @return list of all Trigger classes in the DB
     */
    List<TriggerDO> findAll();
    List<TriggerDO> findAllWithPages(String multiplicator,String pageLimit,String dateInterval);
    List<TriggerDO> findAllLimited();
    List<TriggerDO> findAllSuccessed(String multiplicator,String pageLimit,String dateInterval);
    List<TriggerDO> findAllErrors(String multiplicator,String pageLimit,String dateInterval);
    List<TriggerDO> findAllNews(String multiplicator,String pageLimit,String dateInterval);
    List<TriggerDO> findAllInProgress(String multiplicator,String pageLimit,String dateInterval);

    void deleteEntries(String status, String dateInterval);

    List<TriggerDO> findAllUnprocessed();
    TriggerCountDO findAllCount();
    TriggerCountDO findUnprocessedCount();
    TriggerCountDO findInProgressCount();


    TriggerDO create(TriggerDO triggerDO, Long currentUserId);

    TriggerDO update(TriggerDO triggerDO, Long currentUserId);
}

