package de.bogenliga.application.business.einstellungen.impl.entity;

import de.bogenliga.application.common.component.entity.BusinessEntity;
import de.bogenliga.application.common.component.entity.CommonBusinessEntity;

/**
 * TODO [AL] class documentation
 *
 * @author Fabio Care, fabio_silas.care@student.reutlingen-university.de
 */
public class EinstellungenBE extends CommonBusinessEntity implements BusinessEntity {
    private static final long serialVersionUID = -76389969048178948L;




    private String einstellungenId;
    private String einstellungenKey;
    private String einstellungenValue;



    public void setEinstellungenId(String einstellungenId) {
        this.einstellungenId = einstellungenId;
    }


    public String getEinstellungenId() {
        return einstellungenId;
    }




    public EinstellungenBE() {
        //empty constructor
    }

    @Override
    public String toString() {
        return "EinstellungenBE{" +
                ", key=" + einstellungenKey + '\'' +
                ", value=" + einstellungenValue + '\'' +
                ", id="+ einstellungenId+ '\''+
                "}";
    }

    public String getEinstellungenKey() {
        return einstellungenKey;
    }

    public void setEinstellungenKey(final String einstellungenKey) {
        this.einstellungenKey = einstellungenKey;
    }

    public String getEinstellungenValue() {
        return einstellungenValue;
    }

    public void setEinstellungenValue(final String einstellungenValue) {
        this.einstellungenValue = einstellungenValue;
    }




}
