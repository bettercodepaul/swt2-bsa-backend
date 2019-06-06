package de.bogenliga.application.business.sportjahr.impl.entity;

import de.bogenliga.application.common.component.entity.BusinessEntity;
import de.bogenliga.application.common.component.entity.CommonBusinessEntity;

/**
 * I represent the sportjahr business entity.
 * <p>
 * A sportjahr is a registered member of the DSB. The sportjahr is not necessarily a technical user of the system.
 * <p>
 * The {@link CommonBusinessEntity} contains the technical parameter. Business entities commonly use these parameters to
 * control their lifecycle.
 *
 * @author Marcel Schneider
 * @see CommonBusinessEntity
 */
public class SportjahrBE extends CommonBusinessEntity implements BusinessEntity {

    private static final long serialVersionUID = 7307883175430867611L;
    private Long sportjahrId;
    private String sportjahrName;


    /**
     * Constructor
     */
    public SportjahrBE() {
        //empty constructor
    }


    public Long getSportjahrId() {
        return sportjahrId;
    }


    public void setSportjahrId(final Long sportjahrId) {
        this.sportjahrId = sportjahrId;
    }


    public String getSportjahrName() {
        return sportjahrName;
    }


    public void setSportjahrName(String name) {
        this.sportjahrName = name;
    }


    @Override
    public String toString() {
        return "Sportjahr ID = " + getSportjahrId() + "\n" +
                "Sportjahr Name  =  " + getSportjahrName();
    }
}
