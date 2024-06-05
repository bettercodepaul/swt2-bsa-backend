package de.bogenliga.application.business.trigger.impl.entity;

import org.springframework.beans.factory.annotation.Autowired;
import de.bogenliga.application.common.component.entity.BusinessEntity;
import de.bogenliga.application.common.component.entity.CommonBusinessEntity;

public class TriggerCountBE extends CommonBusinessEntity implements BusinessEntity {
    private Long count;

    public TriggerCountBE(){}

    public void setCount(Long count){
        this.count=count;
    }

    public Long getCount(){
        return count;
    }

    @Override
    public String toString(){
        return "TriggerCountBE{" +
                "count=" +  count +
                '}';
    }
}
