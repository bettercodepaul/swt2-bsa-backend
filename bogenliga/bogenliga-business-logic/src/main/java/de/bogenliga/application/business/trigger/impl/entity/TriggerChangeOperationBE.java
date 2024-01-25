package de.bogenliga.application.business.trigger.impl.entity;

import de.bogenliga.application.common.component.entity.BusinessEntity;
import de.bogenliga.application.common.component.entity.CommonBusinessEntity;

/**
 * I´m a business entity :  trigger change operation.
 * The {@link CommonBusinessEntity} contains the technical parameter. Business entities commonly use these parameters to
 * control their lifecycle.
 *
 * @author Andre Lehnert, BettercallPaul gmbh
 */
public class TriggerChangeOperationBE extends CommonBusinessEntity implements BusinessEntity {
    private static final long serialVersionUID = 8501874422525432266L;

    private Long id;
    private String name;


    /**
     * Constructor
     */
    public TriggerChangeOperationBE() {
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
