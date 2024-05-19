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
    List<TriggerDO> findAllLimited();

    List<TriggerDO> findAllUnprocessed();
    TriggerCountDO findAllCount();
    TriggerCountDO findUnprocessedCount();


    TriggerDO create(TriggerDO triggerDO, Long currentUserId);

    TriggerDO update(TriggerDO triggerDO, Long currentUserId);
}

