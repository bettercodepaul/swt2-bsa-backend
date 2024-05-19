package de.bogenliga.application.business.trigger.api.types;

import de.bogenliga.application.common.component.types.CommonDataObject;
import de.bogenliga.application.common.component.types.DataObject;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class TriggerCountDO extends CommonDataObject implements DataObject {
    private Long count;


    public TriggerCountDO(Long count) {
        this.count = count;
    }


    public Long getCount() {
        return count;
    }


    public void setCount(Long count) {
        this.count = count;
    }
}
