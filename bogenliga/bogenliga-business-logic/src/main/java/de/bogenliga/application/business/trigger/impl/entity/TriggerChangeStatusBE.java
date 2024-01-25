package de.bogenliga.application.business.trigger.impl.entity;

import de.bogenliga.application.common.component.entity.BusinessEntity;
import de.bogenliga.application.common.component.entity.CommonBusinessEntity;

/**
 * I´m a business entity :  trigger change status.
 * The {@link CommonBusinessEntity} contains the technical parameter. Business entities commonly use these parameters to
 * control their lifecycle.
 *
 * @author Andre Lehnert, BettercallPaul gmbh
 */
public class TriggerChangeStatusBE extends CommonBusinessEntity implements BusinessEntity {
    private static final long serialVersionUID = 1885252555083901842L;

    private Long id;
    private String name;


    /**
     * Constructor
     */
    public TriggerChangeStatusBE() {
        // empty
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "TriggerChangeOperationBE{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
