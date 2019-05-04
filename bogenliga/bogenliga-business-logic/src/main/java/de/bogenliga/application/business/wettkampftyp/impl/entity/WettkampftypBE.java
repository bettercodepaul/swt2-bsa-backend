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
    private String wettkampftypName;




    /**
     * Constructor
     */
    public WettkampftypBE() {
        // empty
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }


    public Long getWettkampftypID() {
        return wettkampftypID;
    }


    public void setWettkampftypID(Long wettkampftypID) {
        this.wettkampftypID = wettkampftypID;
    }


    public String getWettkampftypName() {
        return wettkampftypName;
    }


    public void setWettkampftypName(String wettkampftypName) {
        this.wettkampftypName = wettkampftypName;
    }


    @Override
    public String toString() {
        return "Wettkampftyp ID = " + getWettkampftypID() + "\n" +
                "Wettkampftyp Name =  " + getWettkampftypName() + "\n";
    }
}
