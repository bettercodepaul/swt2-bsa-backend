package de.bogenliga.application.business.wettkampftyp.impl.entity;

import de.bogenliga.application.common.component.entity.BusinessEntity;
import de.bogenliga.application.common.component.entity.CommonBusinessEntity;

/**
 * I represent the wettkampf business entity.
 * <p>
 * A wettkampf is a registered member of the DSB. The wettkampf is not necessarily a technical user of the system.
 * <p>
 * The {@link CommonBusinessEntity} contains the technical parameter. Business entities commonly use these parameters to
 * control their lifecycle.
 *
 * @see CommonBusinessEntity
 */
public class WettkampftypBE extends CommonBusinessEntity implements BusinessEntity {

    private static final long serialVersionUID = 7307883175430867611L;
    private Long id;
    private String name;


    /**
     * Constructor
     */
    public WettkampftypBE(/*final Long id, final String name*/) {
        //this.id = id;
        //his.name = name;
    }


    public Long getId() {
        return id;
    }


    public void setId(final Long id) {
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
        return "Wettkampftyp ID = " + getId() + "\n" +
                "Wettkampftyp Name  =  " + getName();
    }
}
