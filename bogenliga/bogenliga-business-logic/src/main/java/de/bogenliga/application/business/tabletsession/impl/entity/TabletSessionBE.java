package de.bogenliga.application.business.tabletsession.impl.entity;

import de.bogenliga.application.common.component.entity.BusinessEntity;
import de.bogenliga.application.common.component.entity.CommonBusinessEntity;

/**
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class TabletSessionBE extends CommonBusinessEntity implements BusinessEntity {
    private Long wettkampfId;
    private Long scheibennummer;


    public Long getWettkampfId() {
        return wettkampfId;
    }


    public void setWettkampfId(Long wettkampfId) {
        this.wettkampfId = wettkampfId;
    }


    public Long getScheibennummer() {
        return scheibennummer;
    }


    public void setScheibennummer(Long scheibennummer) {
        this.scheibennummer = scheibennummer;
    }

}
