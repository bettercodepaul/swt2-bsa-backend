package de.bogenliga.application.business.trigger.impl.business;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import de.bogenliga.application.business.trigger.api.TriggerComponent;
import de.bogenliga.application.business.trigger.api.types.TriggerDO;
import de.bogenliga.application.business.trigger.impl.dao.TriggerDAO;
import de.bogenliga.application.business.trigger.impl.entity.TriggerBE;
import de.bogenliga.application.business.trigger.impl.mapper.TriggerMapper;

/**
 * Implementation of {@link TriggerComponent}
 *
 * @author Maximilian Fronmueller
 */
//TODO: PRECONDITIONS
public class TriggerComponentImpl implements TriggerComponent {
    public final TriggerDAO triggerDAO;

    //@Autowired how?
    public TriggerComponentImpl (TriggerDAO triggerDAO){
        this.triggerDAO = triggerDAO;
    }
    @Override
    public List<TriggerDO> findAll() {
        final List<TriggerBE> triggerBEList = triggerDAO.findAll();
        return triggerBEList.stream().map(TriggerMapper.toTriggerDO).collect(Collectors.toList());
    }
}
