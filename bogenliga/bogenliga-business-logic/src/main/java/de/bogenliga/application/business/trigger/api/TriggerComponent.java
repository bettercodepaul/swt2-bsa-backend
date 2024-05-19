package de.bogenliga.application.business.trigger.api;
import java.util.List;
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
    List<TriggerDO> findAllLimited();

    List<TriggerDO> findAllUnprocessed();

    TriggerDO create(TriggerDO triggerDO, Long currentUserId);

    TriggerDO update(TriggerDO triggerDO, Long currentUserId);

    List<TriggerDO> findAllWithPages(String offsetMultiplicator, String queryPageLimit, String dateInterval);

    List<TriggerDO> findAllSuccessed(String offsetMultiplicator, String queryPageLimit, String dateInterval);

    List<TriggerDO> findAllErrors(String offsetMultiplicator, String queryPageLimit, String dateInterval);

    List<TriggerDO> findAllInProgress(String offsetMultiplicator, String queryPageLimit, String dateInterval);

    List<TriggerDO> findAllNews(String offsetMultiplicator, String queryPageLimit, String dateInterval);

    void deleteEntries(String status, String dateInterval);
}
