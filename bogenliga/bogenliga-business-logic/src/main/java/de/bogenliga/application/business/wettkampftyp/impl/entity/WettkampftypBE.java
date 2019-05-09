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
    private Long wettkampftypID;
    private String wettkampftypname;


    /**
     * Constructor
     */
    public WettkampftypBE() { }


    public Long getwettkampftypID() {
        return wettkampftypID;
    }


    public void setwettkampftypID(final Long wettkampftypID) {
        this.wettkampftypID = wettkampftypID;
    }


    public String getwettkampftypname() {
        return wettkampftypname;
    }


    public void setwettkampftypname(String name) {
        this.wettkampftypname = name;
    }


    @Override
    public String toString() {
        return "Wettkampftyp ID = " + getwettkampftypID() + "\n" +
                "Wettkampftyp Name  =  " + getwettkampftypname();
    }
}
