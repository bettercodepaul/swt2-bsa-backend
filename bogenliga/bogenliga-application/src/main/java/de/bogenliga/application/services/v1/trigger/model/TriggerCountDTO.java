package de.bogenliga.application.services.v1.trigger.model;

import de.bogenliga.application.common.service.types.DataTransferObject;


public class TriggerCountDTO implements DataTransferObject {
    private Long count;


    public TriggerCountDTO(Long count) {
        this.count = count;
    }


    public Long getCount() {
        return count;
    }


    public void setCount(Long count) {
        this.count = count;
    }
}
