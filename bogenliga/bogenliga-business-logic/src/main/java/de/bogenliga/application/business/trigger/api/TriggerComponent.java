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
}