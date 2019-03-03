package de.bogenliga.application.business.kampfrichter.impl.entity;

import de.bogenliga.application.common.component.entity.BusinessEntity;
import de.bogenliga.application.common.component.entity.CommonBusinessEntity;

/**
 * I represent the kampfrichter business entity.
 * <p>
 * A kampfrichter is a registered member of the DSB. The kampfrichter is not necessarily a technical user of the system.
 * <p>
 * The {@link CommonBusinessEntity} contains the technical parameter. Business entities commonly use these parameters to
 * control their lifecycle.
 *
 * @see CommonBusinessEntity
 */
public class KampfrichterBE extends CommonBusinessEntity implements BusinessEntity {
    private static final long serialVersionUID = -76389969048178948L;
    private Long kampfrichterUserId;
    private Long kampfrichterWettkampfId;
    private Boolean kampfrichterLeitend;


    public KampfrichterBE(){
        // empty constructor
    }


    public static Long getSerialVersionUID() {
        return serialVersionUID;
    }


    public Long getKampfrichterUserId() {
        return kampfrichterUserId;
    }

    public void setKampfrichterUserId(Long kampfrichterUserId) {
        this.kampfrichterUserId = kampfrichterUserId;
    }

    public Long getKampfrichterWettkampfId() {
        return kampfrichterWettkampfId;
    }

    public void setKampfrichterWettkampfId(Long kampfrichterWettkampfId) {
        this.kampfrichterWettkampfId = kampfrichterWettkampfId;
    }

    public Boolean isKampfrichterLeitend() {
        return kampfrichterLeitend;
    }

    public void setKampfrichterLeitend(Boolean kampfrichterLeitend) {
        this.kampfrichterLeitend = kampfrichterLeitend;
    }


    @Override
    public String toString() {
        return "KampfrichterBE{" +
                "kampfrichterUserId=" + kampfrichterUserId +
                ", kampfrichterWettkampfId=" + kampfrichterWettkampfId + '\'' +
                ", kampfrichterLeitend=" + kampfrichterLeitend +
                '}';
    }
}
